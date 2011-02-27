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
/** 
 * The response received after a task was submitted. This does not contain the processing response
 * but only the information weather the task was successfully submitted or not.
 * @author adi
 *
 */
public class SubmitTaskResponse {
	private boolean isSuccessfull;
	private Exception error;
	
	
	public boolean isSuccessfull() {
		return isSuccessfull;
	}
	public Exception getError() {
		return error;
	}
	public void setSuccessfull(boolean isSuccessfull) {
		this.isSuccessfull = isSuccessfull;
	}
	public void setError(Exception error) {
		this.error = error;
	}
	
	

}
