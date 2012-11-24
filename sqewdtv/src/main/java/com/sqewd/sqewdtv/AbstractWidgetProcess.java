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

import org.apache.commons.configuration.XMLConfiguration;

/**
 * Class represents an abstract widget base. All widgets should inherit from
 * this class.
 * 
 * @author subhagho
 * 
 */
public abstract class AbstractWidgetProcess {
	protected String name;

	protected ObjectState state;

	protected AbstractWidgetProcess(String name) {
		this.name = name;
		this.state = ObjectState.Unknown;
	}

	/**
	 * @return the state
	 */
	public ObjectState getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(ObjectState state) {
		this.state = state;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Initialize the Widget.
	 * 
	 * @param config
	 *            - Configuration handle to initialize the widget with.
	 * @throws Exception
	 */
	public abstract void init(XMLConfiguration config) throws Exception;

	/**
	 * Run the widget process.
	 * 
	 * @throws Exception
	 */
	public abstract void run() throws Exception;

	/**
	 * Stop the widget process, if running.
	 */
	public abstract void stop();

	/**
	 * Dispose the widget.
	 */
	public abstract void dispose();
}
