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

import java.io.InputStream;
import java.util.List;

/**
 * @author subhagho
 * 
 */
public interface VideoSearchClient {
	/**
	 * Initialize the Video Search Client.
	 * 
	 * @param config
	 *            - InputStream to the configuration file.
	 * 
	 * @throws Exception
	 */
	public void init(InputStream config) throws Exception;

	/**
	 * Dispose the Search Client.
	 */
	public void dispose();

	/**
	 * Execute a Video Search based on the passed client.
	 * 
	 * @param query
	 *            - Search Query.
	 * @param size
	 *            - Result Set size to limit to.
	 * @return
	 * @throws Exception
	 */
	public List<Video> search(SearchQuery query, int size) throws Exception;

	/**
	 * Get videos for a particular channel.
	 * 
	 * @param channelId
	 *            - Channel Id
	 * @param sort
	 *            - Sort result by
	 * @param size
	 *            - Result Set size to limit to.
	 * @return
	 * @throws Exception
	 */
	public List<Video> listChannel(int channelId, VideoSortColumn sort, int size)
			throws Exception;
}
