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
 * Dec 31, 2012
 * 
 */
package com.sqewd.tv.home;

import java.util.List;

import android.content.res.Resources;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

/**
 * @author subhagho
 * 
 */
public class SearchContext {
	private String searchString;

	private String defaultSearchText = null;

	private AutoCompleteTextView textBox;

	/**
	 * @return the textBox
	 */
	public AutoCompleteTextView getTextBox() {
		return textBox;
	}

	/**
	 * @param textBox
	 *            the textBox to set
	 */
	public void setTextBox(final AutoCompleteTextView textBox) {
		this.textBox = textBox;
	}

	public void addAutoCompleteList(final List<String> values) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(HomeContext
				.get().getHomeInstance(),
				android.R.layout.simple_dropdown_item_1line, values);
		textBox.setAdapter(adapter);
	}

	public void clearAutoCompleteList() {
		textBox.setAdapter(null);
	}

	/**
	 * @return the seachString
	 */
	public String getSearchString() {
		return searchString;
	}

	/**
	 * @param seachString
	 *            the seachString to set
	 */
	public void setSearchString(final String searchString) {
		if (defaultSearchText == null) {
			Resources res = HomeContext.get().getHomeInstance().getResources();
			defaultSearchText = res.getString(R.string.txt_AC_Search);
		}
		if (searchString.compareTo(defaultSearchText) == 0
				|| searchString.isEmpty()) {
			boolean relayout = this.searchString != null;
			this.searchString = null;
			if (relayout) {
				HomeContext.get().getCurrentButtonHandler().search(true);
			}
		} else {
			this.searchString = searchString.toLowerCase();
			if (HomeContext.get().getCurrentButtonHandler() != null) {
				HomeContext.get().getCurrentButtonHandler().search(true);
			}
		}
	}

	public boolean isSearchSet() {
		if (searchString != null && !searchString.isEmpty())
			return true;
		return false;
	}
}
