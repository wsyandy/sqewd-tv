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
package com.sqewd.tv.services.search;

/**
 * @author subhagho
 * 
 */
public class FeedDefinition {
	private String feedUrl;

	private String key;

	private String name;

	private String cetegory;

	/**
	 * @return the feedUrl
	 */
	public String getFeedUrl() {
		return feedUrl;
	}

	/**
	 * @param feedUrl
	 *            the feedUrl to set
	 */
	public void setFeedUrl(final String feedUrl) {
		this.feedUrl = feedUrl;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(final String key) {
		this.key = key;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the desription
	 */
	public String getCategory() {
		return cetegory;
	}

	/**
	 * @param desription
	 *            the desription to set
	 */
	public void setCategory(final String category) {
		this.cetegory = category;
	}

}
