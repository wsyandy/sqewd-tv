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
 * Dec 30, 2012
 * 
 */
package com.sqewd.tv.home;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * @author subhagho
 * 
 */
public class PaginatorWidget {

	private LinearLayout parent;

	private int count;

	private int baseId;

	private int currentPage = -1;

	private PaginatorCallback callback;

	private List<RadioButton> pages;

	public PaginatorWidget(final LinearLayout parent, final int count,
			final PaginatorCallback callback) {
		this.parent = parent;
		this.count = count;
		this.callback = callback;

		parent.removeAllViewsInLayout();

		baseId = getNextPageIdBase();

		create();
	}

	private void create() {
		if (count < 1)
			return;

		pages = new ArrayList<RadioButton>();

		parent.setGravity(Gravity.CENTER);

		RadioGroup rg = new RadioGroup(HomeContext.get().getHomeInstance());
		rg.setLayoutParams(new RadioGroup.LayoutParams(
				RadioGroup.LayoutParams.WRAP_CONTENT,
				RadioGroup.LayoutParams.WRAP_CONTENT));
		rg.setOrientation(LinearLayout.HORIZONTAL);

		for (int ii = 0; ii < count; ii++) {
			RadioButton rb = new RadioButton(HomeContext.get()
					.getHomeInstance());
			rb.setId(baseId + ii);
			rb.setText("");
			LinearLayout.LayoutParams lp = new RadioGroup.LayoutParams(
					RadioGroup.LayoutParams.WRAP_CONTENT,
					RadioGroup.LayoutParams.WRAP_CONTENT);
			rb.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(final View v) {
					RadioButton trb = (RadioButton) v;
					int page = trb.getId() - baseId;

					currentPage = page;

					callback.page(page);
				}
			});

			pages.add(rb);

			rg.addView(rb, lp);
		}

		parent.addView(rg);

	}

	public void dispose() {
		parent.removeAllViewsInLayout();
	}

	public int getCount() {
		return count;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(final int page) {
		currentPage = page;
	}

	public void gotoPage(final int page) {
		if (page >= 0 && page < pages.size()) {
			pages.get(page).performClick();
		}
	}

	@SuppressLint("UseValueOf")
	private static Integer _page_id_base_ = new Integer(901000);

	private static int getNextPageIdBase() {
		synchronized (_page_id_base_) {
			int value = _page_id_base_;
			_page_id_base_ += 1000;
			return value;
		}
	}
}
