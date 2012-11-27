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
package com.sqewd.sqewdtv.widgets;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.sqewd.sqewdtv.AbstractWidgetProcess;
import com.sqewd.sqewdtv.ConfigurationException;
import com.sqewd.sqewdtv.ObjectState;
import com.sqewd.sqewdtv.utils.LogUtils;
import com.sqewd.sqewdtv.utils.XMLUtils;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * @author subhagho
 * 
 */
public class WeatherWidgetProcess extends AbstractWidgetProcess {
	private static final class Constants {
		public static final String _CONFIG_RSS_URL_ = "rss.feed.url";
		public static final String _CONFIG_REFRESH_TIME_ = "refresh.interval";
	}

	private static final Logger log = LoggerFactory
			.getLogger(WeatherWidgetProcess.class);

	private String rssUrl;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.sqewdtv.AbstractWidgetProcess#init(org.apache.commons.configuration
	 * .XMLConfiguration)
	 */
	@Override
	public void init(final Element parent) throws Exception {
		try {
			HashMap<String, Object> params = XMLUtils.parseParams(parent);
			rssUrl = (String) params.get(Constants._CONFIG_RSS_URL_);
			if (rssUrl == null || rssUrl.isEmpty())
				throw new ConfigurationException("Missing parameter. [name="
						+ Constants._CONFIG_RSS_URL_ + "]");
			if (params.containsKey(Constants._CONFIG_REFRESH_TIME_)) {
				String value = (String) params
						.get(Constants._CONFIG_REFRESH_TIME_);
				if (value != null && !value.isEmpty()) {
					runInterval = Long.parseLong(value);
				}
			}
			state = ObjectState.Available;
		} catch (Exception e) {
			LogUtils.stacktrace(log, e);
			state = ObjectState.Exception;
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.sqewdtv.AbstractWidgetProcess#run()
	 */
	@Override
	public void run() throws Exception {
		if (state == ObjectState.Executing)
			return;

		if (state != ObjectState.Available)
			throw new Exception("Invalid Object state. [state=" + state.name()
					+ "]");
		state = ObjectState.Executing;
		try {
			URL url = new URL(rssUrl);
			XmlReader reader = new XmlReader(url);
			try {
				SyndFeed feed = new SyndFeedInput().build(reader);
				for (Iterator<SyndEntry> i = feed.getEntries().iterator(); i
						.hasNext();) {
					SyndEntry entry = i.next();
				}
			} finally {
				if (reader != null) {
					reader.close();
				}
			}
			state = ObjectState.Available;
		} catch (Exception e) {
			LogUtils.stacktrace(log, e);
			state = ObjectState.Exception;
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.sqewdtv.AbstractWidgetProcess#stop()
	 */
	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.sqewdtv.AbstractWidgetProcess#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
