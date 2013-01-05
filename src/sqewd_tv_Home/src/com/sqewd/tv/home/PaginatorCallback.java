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

/**
 * @author subhagho
 * 
 */
public interface PaginatorCallback {
	/**
	 * Callback to execute when a page is selected;
	 * 
	 * @param offset
	 *            - Page Number
	 */
	public void page(int offset);

	/**
	 * Go to next page.
	 * 
	 */
	public void next();

	/**
	 * Go to previous page.
	 * 
	 */
	public void previous();

	/**
	 * Go to last page.
	 * 
	 */
	public void last();

	/**
	 * Go to first page.
	 */
	public void first();
}
