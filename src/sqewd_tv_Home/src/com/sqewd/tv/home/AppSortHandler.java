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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;

/**
 * @author subhagho
 * 
 */
public class AppSortHandler {
	public static class SortByName implements Comparator<ResolveInfo> {
		public void sort(final List<ResolveInfo> apps) {
			Collections.sort(apps, this);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(final ResolveInfo lhs, final ResolveInfo rhs) {
			String ls = lhs.loadLabel(
					HomeContext.get().getHomeInstance().getPackageManager())
					.toString();
			String rs = rhs.loadLabel(
					HomeContext.get().getHomeInstance().getPackageManager())
					.toString();
			return ls.compareTo(rs);
		}

	}

	public static class SortByDate implements Comparator<ResolveInfo> {
		public void sort(final List<ResolveInfo> apps) {
			Collections.sort(apps, this);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(final ResolveInfo lhs, final ResolveInfo rhs) {
			try {
				long ldt = HomeContext.get().getHomeInstance()
						.getPackageManager()
						.getPackageInfo(lhs.activityInfo.packageName, 0).firstInstallTime;
				long rdt = HomeContext.get().getHomeInstance()
						.getPackageManager()
						.getPackageInfo(rhs.activityInfo.packageName, 0).firstInstallTime;
				return (int) (ldt - rdt);
			} catch (NameNotFoundException e) {
				return 0;
			}
		}

	}

	public static class SortByUsage implements Comparator<ResolveInfo> {
		public void sort(final List<ResolveInfo> apps) {
			Collections.sort(apps, this);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(final ResolveInfo lhs, final ResolveInfo rhs) {
			try {
				long ldt = HomeContext.get().getHomeInstance()
						.getPackageManager()
						.getPackageInfo(lhs.activityInfo.packageName, 0).firstInstallTime;
				long rdt = HomeContext.get().getHomeInstance()
						.getPackageManager()
						.getPackageInfo(rhs.activityInfo.packageName, 0).firstInstallTime;
				return (int) (ldt - rdt);
			} catch (NameNotFoundException e) {
				return 0;
			}
		}

	}
}
