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
 * Dec 29, 2012
 * 
 */
package com.sqewd.tv.home;

import android.app.Activity;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;

/**
 * @author subhagho
 * 
 */
public class HomeEnv {
	private Point displaySize;

	private DisplayType displayType;

	private void init(final Activity parent) throws Exception {
		Display display = parent.getWindowManager().getDefaultDisplay();
		if (display != null) {
			display.getSize(displaySize);

			if (displaySize.x >= 1080) {
				displayType = DisplayType.TV1080p;
			} else if (displaySize.x >= 720) {
				displayType = DisplayType.TV720p;
			} else {
				displayType = DisplayType.Unsupported;
				throw new Exception("Display size [" + displaySize.y + "x"
						+ displaySize.x + "] not supported.");
			}
			Log.d("DISPLAY TYPE", displayType.name());
			Log.d("DISPLAY SIZE", displaySize.toString());
		} else
			throw new Exception("Error getting display handle.");
	}

	public Point getDisplaySize() {
		return displaySize;
	}

	public DisplayType getDisplayType() {
		return displayType;
	}

	public static final HomeEnv _env_ = new HomeEnv();

	public static final void create(final Activity parent) throws Exception {
		_env_.init(parent);
	}

	public static final HomeEnv get() {
		return _env_;
	}
}
