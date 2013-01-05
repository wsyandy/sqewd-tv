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
 * Jan 3, 2013
 * 
 */
package com.sqewd.tv.search.video;

/**
 * @author subhagho
 * 
 */
public class CategoryConstants {
	private static final String[] CATEGORIES = { "Animation", "Automotive",
			"Beauty & Fashion", "Celebrities & Gossip", "Comedy",
			"Lifestyle & Health", "Family", "Films & Entertainment", "Blogs",
			"TV", "Gaming", "Music", "News & Politics", "Science & Education",
			"Sports", "Technology" };

	public static final int CATEGORY_ANIMATION = 1;
	public static final int CATEGORY_AUTOMOTIVE = 2;
	public static final int CATEGORY_FASHION = 3;
	public static final int CATEGORY_CELEBRITIES = 4;
	public static final int CATEGORY_COMEDY = 5;
	public static final int CATEGORY_LIFESTYLE = 6;
	public static final int CATEGORY_FAMILY = 7;
	public static final int CATEGORY_FILMS = 8;
	public static final int CATEGORY_BLOGS = 9;
	public static final int CATEGORY_TV = 10;
	public static final int CATEGORY_GAMING = 11;
	public static final int CATEGORY_Music = 12;
	public static final int CATEGORY_NEWS = 13;
	public static final int CATEGORY_EDUCATION = 14;
	public static final int CATEGORY_SPORTS = 15;
	public static final int CATEGORY_TECH = 16;

	public static final String getCategoryName(final int id) {
		return CATEGORIES[id - 1];
	}
}
