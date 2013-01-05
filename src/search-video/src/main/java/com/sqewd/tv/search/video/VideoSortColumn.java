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
public enum VideoSortColumn {
	/**
	 * Sort returned results by Relevance.
	 */
	Relevance,
	/**
	 * Sort returned results by Viewer rating.
	 */
	Rating,
	/**
	 * Sort returned results by Total Views.
	 */
	Views,
	/**
	 * Sort returned results by Date.
	 */
	Date;
}
