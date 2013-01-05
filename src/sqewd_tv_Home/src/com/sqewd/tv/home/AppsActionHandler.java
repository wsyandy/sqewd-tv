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

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author subhagho
 * 
 */
public class AppsActionHandler extends AbstractOnClickHandler implements
		PaginatorCallback {
	public static final String _PREF_APP_SORT_ = "apps.sort";

	public static final int _CELL_SIZE_APP_ICON_ = 100;

	private static final int _SIZE_APP_ICON_ = 60;

	private static final int _H_PADDING_ = 40;

	private static final int _V_PADDING_ = 10;

	private PaginatorWidget paginator;

	private LinearLayout headerPanel;

	private LinearLayout paginatorPanel;

	private LinearLayout contentPanel;

	private ImageButton btn;

	private TextView view;

	private View selectLine;

	private List<ResolveInfo> allapps = null;

	private List<ResolveInfo> apps = null;

	private AppsSortPreference sort = AppsSortPreference.ByName;

	/**
	 * @param sources
	 */
	public AppsActionHandler(final ImageButton btn, final TextView view,
			final LinearLayout headerPanel) {
		super(new View[] { btn, view });
		this.headerPanel = headerPanel;
		this.btn = btn;
		this.view = view;

		String key = HomeContext.get().getPreferenceKey(_PREF_APP_SORT_);
		if (HomeContext.get().getPrefeneces().contains(key)) {
			String value = HomeContext.get().getPrefeneces()
					.getString(key, sort.name());
			sort = AppsSortPreference.valueOf(value);
		} else {
			sort = AppsSortPreference.ByName;
			HomeContext.get().getPrefeneces().edit()
					.putString(key, sort.name()).commit();
		}

		selectLine = HomeContext.get().lookupControl(R.id.dbtn_Home_AppsSelect);
	}

	private void setup() {
		int count = 0;

		Intent appint = new Intent(Intent.ACTION_MAIN, null);
		appint.addCategory(Intent.CATEGORY_LAUNCHER);

		allapps = HomeContext.get().getHomeInstance().getPackageManager()
				.queryIntentActivities(appint, 0);

		apps = allapps;

		List<String> names = new ArrayList<String>();
		for (ResolveInfo ri : allapps) {
			String name = ri
					.loadLabel(
							HomeContext.get().getHomeInstance()
									.getPackageManager()).toString()
					.toLowerCase();
			names.add(name);
		}
		HomeContext.get().getSearchContext().addAutoCompleteList(names);

		handleSort();

		search(false);

		LinearLayout panel = HomeContext.get().getContentPanel();
		panel.removeAllViewsInLayout();

		panel.setOrientation(LinearLayout.VERTICAL);

		headerPanel.removeAllViewsInLayout();

		LayoutInflater li = HomeContext.get().getHomeInstance()
				.getLayoutInflater();
		li.inflate(R.layout.apps_display_ldpi, panel);

		paginatorPanel = (LinearLayout) panel.findViewById(R.id.pager_Panel);
		contentPanel = (LinearLayout) panel.findViewById(R.id.appsInfo_Panel);

		int w = panel.getWidth();
		int h = panel.getHeight();

		int xcount = w / (_CELL_SIZE_APP_ICON_ + _H_PADDING_);
		int ycount = h / (_CELL_SIZE_APP_ICON_ + _V_PADDING_);

		int pcount = xcount * ycount;

		count = (int) Math.ceil((double) apps.size() / pcount);

		createHeaderPanels();

		paginator = new PaginatorWidget(paginatorPanel, count, this);

	}

	private void createHeaderPanels() {

		headerPanel.setWeightSum(100f);
		headerPanel.setGravity(Gravity.RIGHT | Gravity.CENTER);

		LinearLayout ly = new LinearLayout(HomeContext.get().getHomeInstance());
		ly.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT, 5f));
		ly.setOrientation(LinearLayout.HORIZONTAL);
		ly.setGravity(Gravity.CENTER | Gravity.CENTER);
		headerPanel.addView(ly);

		Spinner prefs = new Spinner(HomeContext.get().getHomeInstance());
		prefs.setLayoutParams(new LinearLayout.LayoutParams(30, 30));
		prefs.setBackground(HomeContext.get().getHomeInstance().getResources()
				.getDrawable(R.drawable.btn_settings));

		ArrayAdapter<AppsSortPreference> adapter = new ArrayAdapter<AppsSortPreference>(
				HomeContext.get().getHomeInstance(),
				android.R.layout.simple_spinner_item,
				AppsSortPreference.values());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		prefs.setAdapter(adapter);
		prefs.setOnItemSelectedListener(new PrefSelectListener());
		ly.addView(prefs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(final View v) {
		HomeContext.get().setCurrentButtonHandler(this);

		if (selectLine != null) {
			selectLine.setVisibility(View.VISIBLE);
		}

		setup();

		paginator.gotoPage(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.tv.home.AbstractOnClickHandler#dispose()
	 */
	@Override
	public void dispose() {
		if (paginator != null) {
			paginator.dispose();
		}
		paginator = null;

		if (selectLine != null) {
			selectLine.setVisibility(View.INVISIBLE);
		}

		HomeContext.get().getSearchContext().clearAutoCompleteList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.tv.home.PaginatorCallback#page(int)
	 */
	@Override
	public void page(final int offset) {
		contentPanel.removeAllViewsInLayout();

		contentPanel.setOrientation(LinearLayout.VERTICAL);

		LinearLayout panel = HomeContext.get().getContentPanel();

		// Work around, because the height/width returned by the contentPanel is
		// 0
		int w = panel.getWidth();
		int h = panel.getHeight();

		int xcount = w / (_CELL_SIZE_APP_ICON_ + _H_PADDING_);
		int ycount = h / (_CELL_SIZE_APP_ICON_ + _V_PADDING_);

		int start = offset * xcount * ycount;

		for (int ii = 0; ii < ycount; ii++) {
			int ystart = start + ii * xcount;
			LinearLayout ll = createRowLayout(contentPanel);
			for (int jj = 0; jj < xcount; jj++) {
				int index = jj + ystart;
				if (index >= apps.size()) {
					break;
				}
				ResolveInfo ri = apps.get(index);
				LinearLayout lc = createItemLayout(ll);
				createItem(ri, lc);
				ll.addView(lc);
			}
			contentPanel.addView(ll);
		}

	}

	private void createItem(final ResolveInfo item, final LinearLayout parent) {
		AppInfoHandler handler = new AppInfoHandler(item);

		Button btn = new Button(HomeContext.get().getHomeInstance());
		LayoutParams lp = new LinearLayout.LayoutParams(_SIZE_APP_ICON_,
				_SIZE_APP_ICON_);
		btn.setBackground(item.loadIcon(HomeContext.get().getHomeInstance()
				.getPackageManager()));
		btn.setOnClickListener(handler);
		parent.addView(btn, lp);

		Resources res = HomeContext.get().getHomeInstance().getResources();

		View sep = new View(HomeContext.get().getHomeInstance());
		lp = new LinearLayout.LayoutParams(_SIZE_APP_ICON_, 2);
		sep.setBackgroundColor(res.getColor(R.color.line_homeControl));
		parent.addView(sep, lp);

		TextView txt = new TextView(HomeContext.get().getHomeInstance());
		lp = new LinearLayout.LayoutParams(_SIZE_APP_ICON_, 20);
		txt.setTextColor(res.getColor(R.color.txt_homeBtn));
		txt.setText(item.loadLabel(HomeContext.get().getHomeInstance()
				.getPackageManager()));
		txt.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		txt.setOnClickListener(handler);
		parent.addView(txt, lp);
	}

	private LinearLayout createItemLayout(final LinearLayout parent) {
		LinearLayout ly = new LinearLayout(HomeContext.get().getHomeInstance());
		ly.setLayoutParams(new LinearLayout.LayoutParams(_CELL_SIZE_APP_ICON_
				+ _H_PADDING_, LinearLayout.LayoutParams.MATCH_PARENT));
		ly.setOrientation(LinearLayout.VERTICAL);
		ly.setPadding(20, 2, 20, 2);
		// ly.setOnClickListener(new AppInfoHandler(app));

		return ly;
	}

	private LinearLayout createRowLayout(final LinearLayout parent) {
		LinearLayout ly = new LinearLayout(HomeContext.get().getHomeInstance());
		ly.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, _CELL_SIZE_APP_ICON_
						+ _V_PADDING_));
		ly.setOrientation(LinearLayout.HORIZONTAL);

		return ly;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.tv.home.PaginatorCallback#next()
	 */
	@Override
	public void next() {
		int page = paginator.getCurrentPage();
		if (page >= paginator.getCount()) {
			page = 0;
		}
		paginator.setCurrentPage(page);
		paginator.gotoPage(page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.tv.home.PaginatorCallback#previous()
	 */
	@Override
	public void previous() {
		int page = paginator.getCurrentPage();
		if (page <= 0) {
			page = paginator.getCount();
		}
		paginator.setCurrentPage(page);
		paginator.gotoPage(page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.tv.home.PaginatorCallback#last()
	 */
	@Override
	public void last() {
		paginator.gotoPage(paginator.getCount());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.tv.home.PaginatorCallback#first()
	 */
	@Override
	public void first() {
		paginator.gotoPage(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.tv.home.AbstractOnClickHandler#search()
	 */
	@Override
	public void search(final boolean relayout) {
		SearchContext sc = HomeContext.get().getSearchContext();
		if (sc.isSearchSet()) {
			String ss = sc.getSearchString();

			apps = new ArrayList<ResolveInfo>();
			for (ResolveInfo ri : allapps) {
				String name = ri
						.loadLabel(
								HomeContext.get().getHomeInstance()
										.getPackageManager()).toString()
						.toLowerCase();
				if (name.indexOf(ss) >= 0) {
					apps.add(ri);
				}
			}
			if (relayout) {
				paginator.gotoPage(0);
			}
		} else if (apps.size() != allapps.size()) {
			apps = allapps;
			if (relayout) {
				paginator.gotoPage(0);
			}
		}
	}

	private class AppInfoHandler implements View.OnClickListener {
		private ResolveInfo app;

		public AppInfoHandler(final ResolveInfo app) {
			this.app = app;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(final View v) {
			Intent intent = new Intent(Intent.ACTION_MAIN, null);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			String pkg = app.activityInfo.packageName;
			String name = app.activityInfo.name;
			intent.setComponent(new ComponentName(pkg, name));

			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			HomeContext.get().getHomeInstance().startActivity(intent);
		}

	}

	private void handleSort() {
		switch (sort) {
		case ByName:
			new AppSortHandler.SortByName().sort(apps);
			break;
		case ByDate:
			new AppSortHandler.SortByDate().sort(apps);
			break;
		case ByUsage:
			new AppSortHandler.SortByUsage().sort(apps);
			break;
		default:
			break;
		}
	}

	private class PrefSelectListener implements OnItemSelectedListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.widget.AdapterView.OnItemSelectedListener#onItemSelected(
		 * android.widget.AdapterView, android.view.View, int, long)
		 */
		@Override
		public void onItemSelected(final AdapterView<?> arg0, final View view,
				final int pos, final long id) {
			AppsSortPreference sp = (AppsSortPreference) arg0
					.getItemAtPosition(pos);
			if (sp == sort)
				return;

			Toast.makeText(arg0.getContext(), sp.name(), Toast.LENGTH_SHORT)
					.show();
			sort = sp;

			String key = HomeContext.get().getPreferenceKey(_PREF_APP_SORT_);
			HomeContext.get().getPrefeneces().edit()
					.putString(key, sort.name()).commit();

			handleSort();

			paginator.gotoPage(0);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.widget.AdapterView.OnItemSelectedListener#onNothingSelected
		 * (android.widget.AdapterView)
		 */
		@Override
		public void onNothingSelected(final AdapterView<?> arg0) {
			// Do nothing...

		}

	}

	public static enum AppsSortPreference {
		ByName, ByUsage, ByDate;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return getDisplayText(this);
		}

		public static String getDisplayText(final AppsSortPreference value) {
			Resources res = HomeContext.get().getHomeInstance().getResources();

			switch (value) {
			case ByUsage:
				return res
						.getString(R.string.com_sqewd_tv_home_AppsSortPreference_ByUsage);
			case ByDate:
				return res
						.getString(R.string.com_sqewd_tv_home_AppsSortPreference_ByDate);
			case ByName:
				return res
						.getString(R.string.com_sqewd_tv_home_AppsSortPreference_ByName);
			}

			return "Unknown";
		}

		public static String[] getDisplayTexts() {
			String[] values = new String[AppsSortPreference.values().length];
			for (int ii = 0; ii < AppsSortPreference.values().length; ii++) {
				values[ii] = AppsSortPreference
						.getDisplayText(AppsSortPreference.values()[ii]);
			}
			return values;
		}
	}

}
