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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.widget.TextView;

/**
 * @author subhagho
 * 
 */
public class ClockHandler implements Runnable {
	public static final String _DEFAULT_DATE_FORMAT_ = "MMMMM, dd yyyy HH:mm:ss";

	private TextView view;
	private DateFormat df = new SimpleDateFormat(_DEFAULT_DATE_FORMAT_,
			Locale.getDefault());

	public ClockHandler(final TextView view) {
		this.view = view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		Date cd = Calendar.getInstance().getTime();
		String dtx = df.format(cd);

		view.setText(dtx);
	}

}
