/**
 * Copyright 2012 Subho Ghosh (subho dot ghosh at outlook dot com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sqewd.sqewdtv;

/**
 * Enumeration to be used to depict states of an Object handle.
 * 
 * @author subhagho
 * 
 */
public enum ObjectState {
	/**
	 * Object State Unknown.
	 */
	Unknown,
	/**
	 * Object Initialized.
	 */
	Initialized,
	/**
	 * Object Available for use.
	 */
	Available,
	/**
	 * Object is in the process of executing a task.
	 */
	Executing,
	/**
	 * Object Disposed.
	 */
	Disposed,
	/**
	 * Object raised exception and terminated.
	 */
	Exception;
}
