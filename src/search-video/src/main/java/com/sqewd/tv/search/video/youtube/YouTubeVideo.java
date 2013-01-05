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
package com.sqewd.tv.search.video.youtube;

import com.sqewd.tv.search.video.Video;

/**
 * @author subhagho
 * 
 */
public class YouTubeVideo extends Video {
	private String thumbUrlMedium;

	private String thumbUrlHigh;

	private String channelId;

	/**
	 * @return the thumbUrlMedium
	 */
	public String getThumbUrlMedium() {
		return thumbUrlMedium;
	}

	/**
	 * @param thumbUrlMedium
	 *            the thumbUrlMedium to set
	 */
	public void setThumbUrlMedium(final String thumbUrlMedium) {
		this.thumbUrlMedium = thumbUrlMedium;
	}

	/**
	 * @return the thumbUrlHigh
	 */
	public String getThumbUrlHigh() {
		return thumbUrlHigh;
	}

	/**
	 * @param thumbUrlHigh
	 *            the thumbUrlHigh to set
	 */
	public void setThumbUrlHigh(final String thumbUrlHigh) {
		this.thumbUrlHigh = thumbUrlHigh;
	}

	/**
	 * @return the channelId
	 */
	public String getChannelId() {
		return channelId;
	}

	/**
	 * @param channelId
	 *            the channelId to set
	 */
	public void setChannelId(final String channelId) {
		this.channelId = channelId;
	}

}
