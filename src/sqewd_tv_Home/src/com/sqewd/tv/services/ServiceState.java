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
 * 
 * Jan 2, 2013
 * 
 */
package com.sqewd.tv.services;

/**
 * @author subhagho
 * 
 */
public enum ServiceState {
	/**
	 * Service has been initialized properly.
	 */
	Initialized,
	/**
	 * Service is running.
	 */
	Running,
	/**
	 * Service has been stopped.
	 */
	Stopped,
	/**
	 * Service has raised an exception.
	 */
	Exception,
	/**
	 * Service state is unknown.
	 */
	Unknown;
}
