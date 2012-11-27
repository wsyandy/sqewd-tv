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

import java.net.URL;
import java.util.Iterator;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * @author subhagho
 * 
 */
public class RomeFeedExample {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		try {
			URL url = new URL(
					"http://rss.accuweather.com/rss/liveweather_rss.asp?locCode=10017");
			XmlReader reader = null;

			try {

				reader = new XmlReader(url);
				SyndFeed feed = new SyndFeedInput().build(reader);

				for (Iterator i = feed.getEntries().iterator(); i.hasNext();) {
					SyndEntry entry = (SyndEntry) i.next();
					System.out.println(entry.getTitle());
				}
			} finally {
				if (reader != null) {
					reader.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
