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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sqewd.tv.utils.KeyValuePair;

/**
 * @author subhagho
 * 
 */
public class Properties {

	private final HashMap<String, String> properties = new HashMap<String, String>();

	public void init(final InputStream configstr) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				configstr));
		while (true) {
			String line = reader.readLine();
			if (line == null) {
				break;
			}
			if (line.isEmpty()) {
				continue;
			}
			line = line.trim();
			if (line.startsWith("#")) {
				continue;
			}
			String[] data = line.split("=");
			if (data.length >= 2) {
				String key = data[0].trim();
				if (key != null && !key.isEmpty()) {
					String value = data[1].trim();
					properties.put(key, value);
				}
			}
		}
	}

	public void init(final File cf) throws Exception {
		if (!cf.exists())
			throw new Exception("Cannot find specified properties file. ["
					+ cf.getAbsolutePath() + "]");
		FileInputStream fis = new FileInputStream(cf);
		try {
			init(fis);
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
	}

	public final String getProperty(final String key) {
		if (properties.containsKey(key))
			return properties.get(key);
		return null;
	}

	public final List<KeyValuePair<String, String>> search(final String prefix) {
		List<KeyValuePair<String, String>> values = new ArrayList<KeyValuePair<String, String>>();
		for (String key : properties.keySet()) {
			if (key.startsWith(prefix)) {
				KeyValuePair<String, String> value = new KeyValuePair<String, String>();
				value.setKey(key);
				value.setValue(properties.get(key));
			}
		}
		return values;
	}
}
