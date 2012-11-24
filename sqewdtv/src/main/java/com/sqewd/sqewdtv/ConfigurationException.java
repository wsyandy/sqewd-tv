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
 * @author subhagho
 * 
 */
public class ConfigurationException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8269145345736321412L;
	private static final String _PREFIX_ = "Invalid Configuration : ";

	public ConfigurationException(final String mesg) {
		super(_PREFIX_ + mesg);
	}

	public ConfigurationException(final String mesg, final Throwable inner) {
		super(_PREFIX_ + mesg, inner);
	}
}
