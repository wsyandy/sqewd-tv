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
 * Jan 3, 2013
 * 
 */
package com.sqewd.tv.search.video;

/**
 * @author subhagho
 * 
 */
public class Video {
	protected String id;

	protected String title;

	protected String description;

	protected String publishedAt;

	protected String url;

	protected String thumbUrl;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return the publishedAt
	 */
	public String getPublishedAt() {
		return publishedAt;
	}

	/**
	 * @param publishedAt
	 *            the publishedAt to set
	 */
	public void setPublishedAt(final String publishedAt) {
		this.publishedAt = publishedAt;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(final String url) {
		this.url = url;
	}

	/**
	 * @return the thumbUrl
	 */
	public String getThumbUrl() {
		return thumbUrl;
	}

	/**
	 * @param thumbUrl
	 *            the thumbUrl to set
	 */
	public void setThumbUrl(final String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}

}
