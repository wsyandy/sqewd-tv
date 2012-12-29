package com.sqewd.tv.home;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

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

	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	private final Handler updateHandler = new Handler();

	private Thread runner = null;

	private ClockHandler clockUpdate = null;

	private List<Runnable> updaters = new ArrayList<Runnable>();

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			HomeEnv.create(this);

			setContentView(R.layout.activity_home_screen);

			final View contentView = findViewById(R.id.lay_panelContent);

			// Set up an instance of SystemUiHider to control the system UI for
			// this activity.
			mSystemUiHider = SystemUiHider.getInstance(this, contentView,
					HIDER_FLAGS);
			mSystemUiHider.setup();
			mSystemUiHider
					.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
						// Cached values.
						int mShortAnimTime;

						@Override
						@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
						public void onVisibilityChange(final boolean visible) {
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
								// If the ViewPropertyAnimator API is available
								// (Honeycomb MR2 and later), use it to animate
								// the
								// in-layout UI controls at the bottom of the
								// screen.
								if (mShortAnimTime == 0) {
									mShortAnimTime = getResources()
											.getInteger(
													android.R.integer.config_shortAnimTime);
								}

							} else {
								// If the ViewPropertyAnimator APIs aren't
								// available, simply show or hide the in-layout
								// UI
								// controls.

							}

							if (visible && AUTO_HIDE) {
								// Schedule a hide().
								delayedHide(AUTO_HIDE_DELAY_MILLIS);
							}
						}
					});

			// Set up the user interaction to manually show or hide the system
			// UI.
			contentView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View view) {
					if (TOGGLE_ON_CLICK) {
						mSystemUiHider.hide();
					} else {
						// mSystemUiHider.show();
					}
				}
			});

			// Setup the search text control
			final AutoCompleteTextView scvSearch = (AutoCompleteTextView) findViewById(R.id.ac_NotSearchText);

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

			clockUpdate = new ClockHandler(
					(TextView) findViewById(R.id.txt_NotDate));

			updaters.add(clockUpdate);

			if (runner == null) {
				runner = new Thread(this);
				runner.start();
			}
		} catch (Exception e) {
			Log.e(ACTIVITY_SERVICE, e.getLocalizedMessage());
			LogHelper.stacktrace(e);
		}
	}

	@Override
	protected void onPostCreate(final Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(final View view, final MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(final int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
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
