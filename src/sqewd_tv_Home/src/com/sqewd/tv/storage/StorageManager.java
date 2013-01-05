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
package com.sqewd.tv.storage;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.util.Log;

/**
 * @author subhagho
 * 
 */
public class StorageManager {
	private Object _lock = new Object();

	private boolean useExternal = false;

	private File currentUsedDirectory = null;

	private BroadcastReceiver mExternalStorageReceiver;

	private Context ctx = null;

	private void init(final Context ctx) {
		this.ctx = ctx;

		startWatchingExternalStorage();
	}

	private void checkFileSystem() {
		synchronized (_lock) {
			if (useExternal) {
				currentUsedDirectory = ctx.getExternalFilesDir(null);
			} else {
				currentUsedDirectory = ctx.getFilesDir();
			}
		}
	}

	public File file(final String path, final String filename) {
		if (path == null || path.isEmpty())
			return new File(currentUsedDirectory, filename);
		else {
			File dir = new File(currentUsedDirectory, path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			return new File(dir, filename);
		}
	}

	public void clear(final File dir) {
		if (!dir.isDirectory())
			return;

		File[] content = dir.listFiles();
		if (content != null && content.length > 0) {
			for (File c : content) {
				if (c.isDirectory()) {
					clear(c);
				}
				c.delete();
			}
		}
	}

	public File directory(final String path) {
		if (path == null || path.isEmpty())
			return currentUsedDirectory;
		File dir = new File(currentUsedDirectory, path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	private void updateExternalStorageState() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			useExternal = true;
		} else {
			useExternal = false;
		}
		checkFileSystem();
	}

	public void startWatchingExternalStorage() {
		mExternalStorageReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(final Context context, final Intent intent) {
				Log.i("test", "Storage: " + intent.getData());
				updateExternalStorageState();
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		filter.addAction(Intent.ACTION_MEDIA_REMOVED);
		ctx.registerReceiver(mExternalStorageReceiver, filter);
		updateExternalStorageState();
	}

	public void stopWatchingExternalStorage() {
		ctx.unregisterReceiver(mExternalStorageReceiver);
	}

	private static final StorageManager _sm_ = new StorageManager();

	public static final void create(final Context ctx) {
		_sm_.init(ctx);
	}

	public static final StorageManager get() {
		return _sm_;
	}
}
