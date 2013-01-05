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

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;

import com.sqewd.tv.storage.StorageManager;

/**
 * @author subhagho
 * 
 */
public class HomeContext {
	public static final String _SHARED_PREFERENCE_KEY_ = "com.sqewd.tv.home";

	public static final String _DIRECTORY_TEMP_ = "tmp";

	private Point displaySize;

	private DisplayType displayType;

	private HomeScreenActivity home = null;

	private AbstractOnClickHandler buttonHandler = null;

	private LinearLayout contentPanel;

	private SharedPreferences preferences = null;

	private SearchContext searchContext = new SearchContext();

	private DisplayMetrics displayMetrics;

	private void init(final HomeScreenActivity parent) throws Exception {
		home = parent;

		preferences = parent.getSharedPreferences(_SHARED_PREFERENCE_KEY_,
				Context.MODE_PRIVATE);
		displayMetrics = parent.getResources().getDisplayMetrics();

		Display display = parent.getWindowManager().getDefaultDisplay();
		displaySize = new Point();
		if (display != null) {
			display.getSize(displaySize);
		} else
			throw new Exception("Error getting display handle.");

		switch (displayMetrics.densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
			displayType = DisplayType.LDPI;
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			displayType = DisplayType.MDPI;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			displayType = DisplayType.HDPI;
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			displayType = DisplayType.XHDPI;
			break;
		case DisplayMetrics.DENSITY_XXHIGH:
			displayType = DisplayType.XXHDPI;
			break;
		case DisplayMetrics.DENSITY_TV:
			displayType = DisplayType.TVDPI;
			break;
		default:
			throw new Exception("Undefined Screen Pixel Density.");
		}

		StorageManager.create(parent);
		StorageManager.get().directory(_DIRECTORY_TEMP_);

		Log.i("HOME_CONTEXT", "Using storage directory ["
				+ StorageManager.get().directory(null) + "]...");
	}

	public void setCurrentButtonHandler(final AbstractOnClickHandler handler) {
		if (buttonHandler != null) {
			buttonHandler.dispose();
		}
		buttonHandler = handler;
	}

	public AbstractOnClickHandler getCurrentButtonHandler() {
		return buttonHandler;
	}

	public Point getDisplaySize() {
		return displaySize;
	}

	public DisplayType getDisplayType() {
		return displayType;
	}

	public HomeScreenActivity getHomeInstance() {
		return home;
	}

	public LinearLayout getContentPanel() {
		return contentPanel;
	}

	public void setContentPanel(final LinearLayout panel) {
		contentPanel = panel;
	}

	public View lookupControl(final int id) {
		return home.findViewById(id);
	}

	public SharedPreferences getPrefeneces() {
		return preferences;
	}

	public String getPreferenceKey(final String value) {
		return _SHARED_PREFERENCE_KEY_ + "." + value;
	}

	public SearchContext getSearchContext() {
		return searchContext;
	}

	private static final HomeContext _ctx_ = new HomeContext();

	public static final void create(final HomeScreenActivity parent)
			throws Exception {
		_ctx_.init(parent);
	}

	public static final HomeContext get() {
		return _ctx_;
	}
}
