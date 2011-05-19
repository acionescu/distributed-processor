/*******************************************************************************
 * Copyright 2011 Adrian Cristian Ionescu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ro.zg.distributed.framework;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.jgroups.Address;
import org.jgroups.ChannelException;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.Receiver;
import org.jgroups.View;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.log.Log;
import ro.zg.util.logging.Logger;
import ro.zg.util.logging.MasterLogManager;
import ro.zg.util.processing.Destination;
import ro.zg.util.processing.GenericSource;
import ro.zg.util.processing.RunnableProcessor;

/**
 * 
 * @author adi A processing node implementation the properties file should contain the following keys: group.name - the
 *         name of the group service.mandatory.<service name> - the name of the mandatory service(can be more)
 *         service.mandatory.<service name>.count - the number of instances service.<service name> - the name of
 *         specific services service.<service name>.count - number of instances for the service service.<service
 *         name>.class - class name for the service
 * 
 */
public class ProcessingNode implements Receiver, Destination<Task, TaskProcessingResponse> {
    private static final Logger logger = MasterLogManager.getLogger("ProcessingNode");
    public static String DISTRIBUTED_PROCESSOR_PROPERTIES_FILE = "node.properties";
    private static final String GROUP_NAME = "group.name";
    private static final String SERVICE_PREFIX = "service.";
    private static final String CLASS_SUFIX = ".class";
    private static final String COUNT_SUFIX = ".count";
    private Properties props;
    private String groupName;

    private ClassLoader resourcesLoader;

    private Map<DistributedServiceDescription, DistributedService> localServices = new Hashtable<DistributedServiceDescription, DistributedService>();

    private Map<DistributedServiceDescription, List<Address>> globalServices = new Hashtable<DistributedServiceDescription, List<Address>>();
    /**
     * the tasks that were sent to be processed and expecting the result
     */
    private Map<Long, ProcessingResponseReceiver> pendingRequests = new Hashtable<Long, ProcessingResponseReceiver>();

    private Map<Long, BroadcastTaskProcessingResponse> pendigBroadcastRequests = new Hashtable<Long, BroadcastTaskProcessingResponse>();
    /**
     * Holds the tasks that are currently processed by this node
     */
    private Map<Task, Address> pendingResponses = new Hashtable<Task, Address>();
    /**
     * holds the tasks to be processed on this node
     */
    private Map<DistributedServiceDescription, GenericSource<Task>> servicesQueues = new HashMap<DistributedServiceDescription, GenericSource<Task>>();
    private JChannel channel;
    private List<Address> members = new ArrayList<Address>();
    private Long nextTaskId = 1L;
    private int maxConcurentTasksToProcess = 100;
    /**
     * Used to hold the index of the address of the next node to process a task for a certain service </br> the key
     * identifies the service
     * 
     */
    private Map<DistributedServiceDescription, Integer> addressesIndexes = new HashMap<DistributedServiceDescription, Integer>();

    /**
     * Thread executor that spawns a new thread for each task that is processed on this node
     */
    private Executor executor;

    private boolean started = false;

    public ProcessingNode() {
	localServices = new HashMap<DistributedServiceDescription, DistributedService>();
	loadProperties();
	executor = Executors.newCachedThreadPool();
    }

    public ProcessingNode(String groupName) {
	this();
	if (groupName != null) {
	    this.groupName = groupName;
	}
    }

    /**
     * loads the properties and stores them in {@link #props}
     */
    private void loadProperties() {
	// TODO: load properties - if not found try to get the file from the system properties
	props = new Properties();
	try {
	    props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(
		    DISTRIBUTED_PROCESSOR_PROPERTIES_FILE));
	} catch (Exception e) {
	    Log.warn(this, "Could not load props file " + DISTRIBUTED_PROCESSOR_PROPERTIES_FILE);
	}

	for (Map.Entry<?, ?> entry : props.entrySet()) {
	    String key = (String) entry.getKey();
	    if (key.startsWith(SERVICE_PREFIX)) {
		String restKey = key.substring(SERVICE_PREFIX.length());
		if (restKey.indexOf(".") == -1) {
		    String serviceName = (String) entry.getValue();
		    String classKey = key + CLASS_SUFIX;
		    String countKey = key + COUNT_SUFIX;
		    String serviceClassName = props.getProperty(classKey);
		    String countValue = props.getProperty(countKey);

		}
	    }
	}
	groupName = props.getProperty(GROUP_NAME);
    }

    /**
     * Add a service to this node
     * 
     * @param service
     */
    public void addService(DistributedService service) {
	localServices.put(service.getServiceDescription(), service);
	Log.info(this, "Added service " + service.getServiceDescription());
	advertise();
    }

    /**
     * Connects to the specified group
     * 
     * @throws ChannelException
     */
    public void connect() throws ChannelException {
	if(groupName == null) {
	    throw new IllegalStateException("The groupName cannot be null");
	}
	if (channel == null) {
	    channel = new JChannel();
	} else if (channel.isConnected()) {
	    throw new RuntimeException("Channel is already connected");
	}
	logger.debug("Starting node with groupName '" + groupName + "'");
	// String groupName = groupName;
	channel.setReceiver(this);
	channel.connect(groupName);
	channel.getState(null, 0);
	logger.info("Node successfuly connected on group " + groupName);
    }

    public boolean isConnected() {
	if (channel == null) {
	    return false;
	}
	return channel.isConnected();
    }

    public synchronized void start() throws Exception {
	if (started) {
	    return;
	}
	if (!isConnected()) {
	    connect();
	}
	startServices();
	advertise();
	started = true;
    }

    private void startServices() throws ContextAwareException {
	logger.debug("Starting services");
	for (DistributedService ds : localServices.values()) {
	    ds.start();
	}
    }

    public void advertise() {
	if (channel == null || !channel.isConnected()) {
	    return;
	}

	Message msg = new Message();
	msg.setObject(localServices.keySet().toArray(new DistributedServiceDescription[0]));
	try {
	    sendMessage(msg);
	} catch (Exception e) {
	    Log.error(this, "Error sending advertise message", e);
	}
    }

    protected void sendMessage(Message msg) throws Exception {
	if (channel == null || !channel.isConnected()) {
	    throw new Exception("The channel is null or not connected");
	}
	channel.send(msg);
    }

    public SubmitTaskResponse submitTask(Task task, ProcessingResponseReceiver receiver) {
	synchronized (this) {
	    task.setTaskId(nextTaskId++);
	}
	if (task.isBroadcastTask()) {
	    return submitBroadcastTask(task, receiver);
	} else {
	    return submitSimpleTask(task, receiver);
	}
    }

    private SubmitTaskResponse submitSimpleTask(Task task, ProcessingResponseReceiver receiver) {
	DistributedServiceDescription sd = task.getTargetService();
	SubmitTaskResponse resp = new SubmitTaskResponse();
	Address adr = getProcessingAddressForService(sd);
	if (adr != null) {
	    Message msg = new Message();
	    msg.setDest(adr);
	    long taskId = task.getTaskId();

	    msg.setObject(task);
	    try {
		pendingRequests.put(taskId, receiver);
		sendMessage(msg);
		resp.setSuccessfull(true);
	    } catch (Exception e) {
		resp.setError(e);
		resp.setSuccessfull(false);
	    }
	} else {
	    resp.setSuccessfull(false);
	    resp.setError(new Exception("No address registered for service " + sd));
	}

	return resp;
    }

    private SubmitTaskResponse submitBroadcastTask(Task task, ProcessingResponseReceiver receiver) {
	DistributedServiceDescription sd = task.getTargetService();
	SubmitTaskResponse resp = new SubmitTaskResponse();
	resp.setSuccessfull(true);
	List<Address> servAddresses = globalServices.get(sd);
	if (servAddresses != null) {
	    long taskId = task.getTaskId();
	    BroadcastTaskProcessingResponse btpr = new BroadcastTaskProcessingResponse(taskId, servAddresses.size());
	    pendingRequests.put(taskId, receiver);
	    pendigBroadcastRequests.put(taskId, btpr);
	    /* submit the task to every node that has the service active */
	    for (Address adr : servAddresses) {
		Message msg = new Message();
		msg.setDest(adr);
		msg.setObject(task);
		try {
		    sendMessage(msg);
		} catch (Exception e) {
		    resp.setError(e);
		    resp.setSuccessfull(false);
		}
	    }
	} else {
	    resp.setSuccessfull(false);
	    resp.setError(new Exception("No address registered for service " + sd));
	}
	return resp;
    }

    private Address getProcessingAddressForService(DistributedServiceDescription sd) {
	List<Address> servAddresses = globalServices.get(sd);
	int size;
	if (servAddresses != null && (size = servAddresses.size()) > 0) {
	    int index = 0;
	    Integer indexObj = addressesIndexes.get(sd);
	    if (indexObj != null) {
		index = indexObj;
	    }
	    addressesIndexes.put(sd, (index + 1) % size);
	    return servAddresses.get(index);
	}
	return null;
    }

    /**
     * Checks if any members handle mandatory services.. if not then this node will start the mandatory services
     */
    private void checkMandatoryServices() {

    }

    /**
     * Update the members list
     * 
     * @param oldMembers
     * @param newMembers
     */
    private void updateMemberAndServices(List<Address> oldMembers, List<Address> newMembers) {
	// clean up : remove old members
	for (Address oldMember : oldMembers) {
	    if (!newMembers.contains(oldMember)) {
		// remove the address from the services list

		for (List<Address> list : globalServices.values()) {

		    for (int i = 0; i < list.size(); i++) {
			Address addr = list.get(i);
			if (addr.toString().equals(oldMember.toString())) {
			    list.remove(i);
			    break;
			}
		    }
		    // list = newList;
		}
	    }
	}
	members = newMembers;
	Log.info(this, "Members updated: " + members);
    }

    private void addServiceToGlobalServices(DistributedServiceDescription desc, Address adr) {
	List<Address> addresses = globalServices.get(desc);
	if (addresses == null) {
	    addresses = new Vector<Address>();
	    globalServices.put(desc, addresses);
	}
	if (!addresses.contains(adr)) {
	    addresses.add(adr);
	}
    }

    /**
     * Process the received messages
     * 
     * @param obj
     * @throws Exception
     */
    private void processMessage(Message msg) throws Exception {
	Object obj = msg.getObject();
	// advertise messages
	if (obj instanceof DistributedServiceDescription[]) {
	    DistributedServiceDescription[] servDesc = (DistributedServiceDescription[]) obj;
	    Log.info(this, "Receivd advertise message: " + Arrays.asList(servDesc));
	    for (DistributedServiceDescription desc : servDesc) {
		addServiceToGlobalServices(desc, msg.getSrc());
	    }
	    Log.info(this, "New state after advertise: " + globalServices);
	}
	// task messages
	else if (obj instanceof Task) {
	    Task task = (Task) obj;
	    // Log.info(this, "Received task for" + task.getTargetService());
	    processTask(task, msg.getSrc());

	}
	// responses received after a task was completed
	else if (obj instanceof TaskProcessingResponse) {
	    TaskProcessingResponse resp = (TaskProcessingResponse) obj;
	    long taskId = resp.getTaskId();
	    BroadcastTaskProcessingResponse btpr = pendigBroadcastRequests.get(taskId);
	    if (btpr != null) {
		btpr.addNewResponse(resp);
		if (btpr.allResponsesReceived()) {
		    resp = btpr;
		} else {
		    /* wait for the all the responses */
		    return;
		}
	    }

	    ProcessingResponseReceiver receiver = pendingRequests.remove(resp.getTaskId());
	    if (receiver == null) {
		throw new Exception("No receiver found for taskId " + resp.getTaskId());
	    }
	    receiver.receiveProcessingResponse(resp);
	}
	// unknown
	else {
	    Log.error(this, "Received unknown message " + obj);
	}
    }

    private void processTask(Task task, Address client) {
	DistributedServiceDescription sd = task.getTargetService();
	DistributedService service = localServices.get(sd);
	if (service != null) {
	    GenericSource<Task> sourceQueue = addTaskToProcessingQueue(sd, task);
	    pendingResponses.put(task, client);
	    LocalTaskProcessor p = new LocalTaskProcessor(service);
	    RunnableProcessor<Task, TaskProcessingResponse> r = new RunnableProcessor<Task, TaskProcessingResponse>(p,
		    sourceQueue, this);
	    executor.execute(r);
	}
    }

    public int getNumberOfInstancesForService(DistributedServiceDescription dsc) {
	List<Address> services = globalServices.get(dsc);
	if (services == null) {
	    return 0;
	}
	return services.size();
    }

    // Receiver implementation

    /*
     * (non-Javadoc)
     * 
     * @see org.jgroups.MessageListener#getState()
     */
    public byte[] getState() {
	try {
	    return org.jgroups.util.Util.objectToByteBuffer(globalServices);
	} catch (Exception e) {
	    Log.error(this, "Error when getState called", e);
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jgroups.MessageListener#receive(org.jgroups.Message)
     */
    public void receive(Message msg) {
	try {
	    processMessage(msg);
	} catch (Exception e) {
	    e.printStackTrace();
	    Log.error(this.getClass(), "Error receiving message " + msg, e);
	}
    }

    public void setState(byte[] state) {
	try {
	    globalServices = (Map<DistributedServiceDescription, List<Address>>) org.jgroups.util.Util
		    .objectFromByteBuffer(state);
	    Log.info(this, "State set to: " + globalServices);
	} catch (Exception e) {
	    Log.error(this, "Error setting state", e);
	}
    }

    public void block() {
	// TODO Auto-generated method stub

    }

    public void suspect(Address suspected_mbr) {
	// TODO Auto-generated method stub

    }

    public void viewAccepted(View new_view) {
	Address localAddress = channel.getLocalAddress();

	List<Address> addresses = new_view.getMembers();
	updateMemberAndServices(members, addresses);
	checkMandatoryServices();
    }

    public String getGroupName() {
	return groupName;
    }

    /**
     * @return the localServices
     */
    public Map<DistributedServiceDescription, DistributedService> getLocalServices() {
	return localServices;
    }

    /**
     * @param localServices
     *            the localServices to set
     */
    public void setLocalServices(Map<DistributedServiceDescription, DistributedService> localServices) {
	this.localServices = localServices;
    }

    public void setGroupName(String groupName) {
	this.groupName = groupName;
    }

    public void addError(Task input, Exception e) {
	Log.error(this, "Error processing task " + input.getTaskId(), e);
	TaskProcessingResponse response = new TaskProcessingResponse(input.getTaskId(), null, e);
	sendResponse(input, response);
    }

    public void addOutput(Task input, TaskProcessingResponse output) {
	sendResponse(input, output);
    }

    /**
     * Sends the response back to the client after the task was processed on the local node
     * 
     * @param input
     * @param output
     */
    private void sendResponse(Task input, TaskProcessingResponse output) {
	Message respMessage = new Message();
	/* get the client address */
	Address clientAddress = pendingResponses.remove(input);
	respMessage.setDest(clientAddress);
	respMessage.setObject(new TaskProcessingResponse(output.getTaskId(), output.getResult(), output.getException(),
		this.getLocalNodeAddress()));
	try {
	    sendMessage(respMessage);
	} catch (Exception e) {
	    Log.error(this, "Error sending response for task " + input.getTaskId(), e);
	}
    }

    private GenericSource<Task> addTaskToProcessingQueue(DistributedServiceDescription sd, Task task) {
	GenericSource<Task> source = servicesQueues.get(sd);
	if (source == null) {
	    source = new GenericSource<Task>();
	    servicesQueues.put(sd, source);
	}
	source.addInput(task);
	return source;
    }

    /**
     * @return the resourcesLoader
     */
    public ClassLoader getResourcesLoader() {
	return resourcesLoader;
    }

    /**
     * @param resourcesLoader
     *            the resourcesLoader to set
     */
    public void setResourcesLoader(ClassLoader resourcesLoader) {
	this.resourcesLoader = resourcesLoader;
    }

    public Address getLocalNodeAddress() {
	return channel.getLocalAddress();
    }

}
