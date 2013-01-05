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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.IBinder;
import android.util.Log;

import com.sqewd.tv.home.util.LogHelper;
import com.sqewd.tv.services.ServiceState;
import com.sqewd.tv.services.SqewdService;
import com.sqewd.tv.storage.StorageManager;

/**
 * @author subhagho
 * 
 */
public class WebVideoFeedService extends Service implements SqewdService {
	public static final String _DATA_DIRECTORY_ = "/services/search/video";

	private static final String _FEED_DEFINITION_FILE_ = "webvideofeeds.lst";

	private static final String _LOGTAG_ = "WEB_VIDEO_FEED_SEARCH";

	private List<FeedDefinition> feeds = new ArrayList<FeedDefinition>();

	private ServiceState state = ServiceState.Unknown;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		try {
			load();
			Log.i(_LOGTAG_, "Service created normally...");
		} catch (Exception e) {
			state = ServiceState.Exception;
			Log.e(_LOGTAG_, e.getLocalizedMessage());
			LogHelper.stacktrace(e);
		}
	}

	private void load() throws Exception {
		AssetManager am = getAssets();

		InputStream is = am.open(_FEED_DEFINITION_FILE_);

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

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

			String[] data = line.split(";");
			if (data.length >= 3) {
				String key = data[0].trim();
				String name = data[1].trim();
				String url = data[2].trim();
				String category = null;

				if (key == null || key.isEmpty()) {
					Log.w(_LOGTAG_, "Error processing line [" + line + "]");
					continue;
				}
				if (name == null || name.isEmpty()) {
					name = key;
				}

				if (url == null || url.isEmpty()) {
					Log.w(_LOGTAG_, "Error processing line [" + line + "]");
					continue;
				}

				if (data.length >= 4) {
					category = data[4].trim();
				}

				FeedDefinition fd = new FeedDefinition();
				fd.setKey(key);
				fd.setName(name);
				fd.setFeedUrl(url);
				fd.setCategory(category);

				feeds.add(fd);

			} else {
				Log.w(_LOGTAG_, "Error processing line [" + line + "]");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		Log.i(_LOGTAG_, "Service destroyed...");
		feeds.clear();
		state = ServiceState.Unknown;
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(final Intent intent, final int flags,
			final int startId) {
		Log.i(_LOGTAG_, "Service command starting...");
		checkDataStorage();

		Log.i(_LOGTAG_, "Service command finished...");
		return Service.START_STICKY;
	}

	private void checkDataStorage() {
		StorageManager.get().directory(_DATA_DIRECTORY_);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(final Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.tv.services.SqewdService#getState()
	 */
	@Override
	public ServiceState getState() {
		return state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.tv.services.SqewdService#name()
	 */
	@Override
	public String name() {
		return getClass().getCanonicalName();
	}

}
