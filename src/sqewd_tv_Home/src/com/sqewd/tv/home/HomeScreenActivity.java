package com.sqewd.tv.home;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.sqewd.tv.home.util.LogHelper;
import com.sqewd.tv.home.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class HomeScreenActivity extends Activity implements Runnable {
	private static final long _DEFAULT_SLEEP_INTERVAL_ = 500;

	private final Handler updateHandler = new Handler();

	private Thread runner = null;

	private ClockHandler clockUpdate = null;

	private List<Runnable> updaters = new ArrayList<Runnable>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPostCreate(android.os.Bundle)
	 */
	@Override
	protected void onPostCreate(final Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		addHomeButtonEventHandlers();

		LinearLayout contentPanel = (LinearLayout) findViewById(R.id.lay_panelContent);
		HomeContext.get().setContentPanel(contentPanel);

	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {

		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		try {
			HomeContext.create(this);

			setContentView(R.layout.activity_home_screen_ldpi);

			// Setup the search text control
			final AutoCompleteTextView scvSearch = (AutoCompleteTextView) findViewById(R.id.ac_HeaderSearchText);

			scvSearch
					.setOnFocusChangeListener(new View.OnFocusChangeListener() {
						final Resources res = getResources();
						final String defaultTxt = res
								.getString(R.string.txt_AC_Search);

						@Override
						public void onFocusChange(final View v,
								final boolean hasFocus) {
							String txt = scvSearch.getText().toString().trim();
							if (hasFocus) {
								if (txt.compareTo(defaultTxt) == 0) {
									txt = "";
								}
								scvSearch.setText(txt);
								scvSearch.setEms(20);
								int tc = res
										.getColor(R.color.txt_SearchFocusedText);
								scvSearch.setTextColor(tc);
							} else {
								if (txt.length() == 0) {
									txt = defaultTxt;
								}
								scvSearch.setEms(10);
								int tc = res
										.getColor(R.color.txt_SearchDefaultText);
								scvSearch.setTextColor(tc);
								scvSearch.setText(txt);
							}
						}
					});
			scvSearch.setOnEditorActionListener(new OnEditorActionListener() {

				@Override
				public boolean onEditorAction(final TextView v,
						final int actionId, final KeyEvent event) {
					if (event != null
							&& event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
						HomeContext
								.get()
								.getSearchContext()
								.setSearchString(scvSearch.getText().toString());
						return true;
					}
					return false;
				}
			});

			HomeContext.get().getSearchContext().setTextBox(scvSearch);

			clockUpdate = new ClockHandler(
					(TextView) findViewById(R.id.txt_NotDate));

			updaters.add(clockUpdate);

			if (runner == null) {
				runner = new Thread(this);
				runner.start();
			}

		} catch (Exception e) {
			Log.e("sqewdTv", e.getLocalizedMessage());
			LogHelper.stacktrace(e);
		}
	}

	private void addHomeButtonEventHandlers() {
		Button btnSearch = (Button) findViewById(R.id.btn_HeaderSearch);
		btnSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View v) {
				AutoCompleteTextView acv = (AutoCompleteTextView) findViewById(R.id.ac_HeaderSearchText);

				HomeContext.get().getSearchContext()
						.setSearchString(acv.getText().toString());
			}
		});

		ImageButton v_btnApps = (ImageButton) findViewById(R.id.btn_Home_Apps);
		TextView v_txtApss = (TextView) findViewById(R.id.dbtn_Home_Apps);

		LinearLayout l_headerPanel = (LinearLayout) findViewById(R.id.home_HeaderInfoPanel);

		new AppsActionHandler(v_btnApps, v_txtApss, l_headerPanel);

		ImageButton v_btnGames = (ImageButton) findViewById(R.id.btn_Home_Games);
		TextView v_txtGames = (TextView) findViewById(R.id.dbtn_Home_Games);

		new GamesActionHandler(v_btnGames, v_txtGames, l_headerPanel);
	}

	public void addUpdater(final Runnable r) {
		synchronized (updaters) {
			updaters.add(r);
		}
	}

	public void removeUpdater(final Runnable r) {
		synchronized (updaters) {
			int index = -1;
			for (int ii = 0; ii < updaters.size(); ii++) {
				Runnable rr = updaters.get(ii);
				if (rr.equals(r)) {
					index = ii;
					break;
				}
			}
			if (index >= 0) {
				updaters.remove(index);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (runner != null) {
			try {
				Thread.sleep(_DEFAULT_SLEEP_INTERVAL_);
			} catch (InterruptedException e) {
				return;
			}
			if (updaters != null && updaters.size() > 0) {
				for (Runnable r : updaters) {
					updateHandler.post(r);
				}
			}
		}
	}
}
