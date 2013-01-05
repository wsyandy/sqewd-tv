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

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import com.sqewd.tv.search.video.Properties;
import com.sqewd.tv.search.video.SearchQuery;
import com.sqewd.tv.search.video.SearchQuery.QueryPart;
import com.sqewd.tv.utils.KeyValuePair;

/**
 * @author subhagho
 * 
 */
public class YouTubeContext {
	public static final class Const {
		public static final String CHANNEL_ID = "channelId";
		public static final String PUBLISHED_AFTER = "publishedAfter";
		public static final String PUBLISHED_BEFORE = "publishedBefore";
		public static final String RELATED_TO = "relatedToVideoId";
		public static final String TOPIC_ID = "topicId";
		public static final String TYPE = "type";
		public static final String VIDEO_CAPTION = "videoCaption";
		public static final String VIDEO_CATEGORY = "videoCategoryId";
		public static final String VIDEO_DEFINITION = "videoDefinition";
		public static final String VIDEO_DIMENSION = "videoDimension";
	}

	public static final String _CONFIG_REST_URL_ = "youtube.rest.url";

	public static final String _CONFIG_ACCESS_KEY_ = "youtube.access.key";

	public static final String _CONFIG_CATEGORY_PREFIX_ = "CATEGORY.";

	private Properties config = new Properties();

	private String baseUrl;

	private String accessKey;

	private HashMap<Integer, String> categoryIds = new HashMap<Integer, String>();

	private void init(final InputStream cf) throws Exception {
		config.init(cf);

		baseUrl = config.getProperty(_CONFIG_REST_URL_);
		if (baseUrl == null || baseUrl.isEmpty())
			throw new Exception(
					"CONFIG_ERROR: Missing configuration parameter. [key="
							+ _CONFIG_REST_URL_ + "]");

		if (!baseUrl.startsWith("http")) {
			baseUrl = "https://" + baseUrl;
		}

		accessKey = config.getProperty(_CONFIG_ACCESS_KEY_);
		if (accessKey == null || accessKey.isEmpty())
			throw new Exception(
					"CONFIG_ERROR: Missing configuration parameter. [key="
							+ _CONFIG_ACCESS_KEY_ + "]");

		List<KeyValuePair<String, String>> channels = config
				.search(_CONFIG_CATEGORY_PREFIX_);
		if (channels != null && !channels.isEmpty()) {
			for (KeyValuePair<String, String> kvp : channels) {
				String key = kvp.getKey();
				String[] kp = key.split("\\.");
				if (kp != null && kp.length == 2) {
					Integer id = Integer.parseInt(kp[0]);
					String value = kp[1];

					categoryIds.put(id, value);
				}
			}
		}
	}

	/**
	 * @return the config
	 */
	public Properties getConfig() {
		return config;
	}

	/**
	 * @return the baseUrl
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * @return the accessKey
	 */
	public String getAccessKey() {
		return accessKey;
	}

	public String getCategoryId(final int id) {
		if (categoryIds.containsKey(id))
			return categoryIds.get(id);
		return null;
	}

	public String createQueryUrl(final SearchQuery query, final int size)
			throws Exception {
		StringBuffer ub = new StringBuffer();

		// Add the Url
		ub.append(baseUrl).append("/search?");

		// Add Access Key
		ub.append("key=").append(accessKey);

		// Add the snippet part
		ub.append("&part=snippet");

		// Add the max results attribute
		ub.append("&maxResults=").append(size);

		List<QueryPart> qps = query.getQuery();
		if (qps != null && !qps.isEmpty()) {
			for (QueryPart qp : qps) {
				if (qp.getPart().compareToIgnoreCase(
						SearchQuery._QUERY_PART_TEXT_) == 0) {
					ub.append("&q=").append(qp.getQuery());
				} else {
					ub.append("&").append(qp.getPart()).append("=")
							.append(qp.getQuery());
				}
			}
		}
		return ub.toString();
	}

	public String createVideoInfoUrl(final String videoid) throws Exception {
		StringBuffer ub = new StringBuffer();

		// Add the Url
		ub.append(baseUrl).append("/videos?");

		// Add Access Key
		ub.append("key=").append(accessKey);

		// Add the snippet part
		ub.append("&part=snippet");

		ub.append("&id=").append(videoid);

		return ub.toString();
	}

	private static final YouTubeContext _ctx_ = new YouTubeContext();

	public static final void create(final InputStream cf) throws Exception {
		_ctx_.init(cf);
	}

	public static final YouTubeContext get() {
		return _ctx_;
	}
}
