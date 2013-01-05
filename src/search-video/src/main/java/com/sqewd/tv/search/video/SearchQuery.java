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

import java.util.ArrayList;
import java.util.List;

/**
 * @author subhagho
 * 
 */
public class SearchQuery {
	public static class QueryPart {
		private String part;
		private String query;

		public QueryPart(final String part, final String query) {
			this.part = part;
			this.query = query;
		}

		public QueryPart() {

		}

		/**
		 * @return the part
		 */
		public String getPart() {
			return part;
		}

		/**
		 * @param part
		 *            the part to set
		 */
		public void setPart(final String part) {
			this.part = part;
		}

		/**
		 * @return the query
		 */
		public String getQuery() {
			return query;
		}

		/**
		 * @param query
		 *            the query to set
		 */
		public void setQuery(final String query) {
			this.query = query;
		}
	}

	public static final String _QUERY_PART_TEXT_ = "text";

	private List<QueryPart> parts = new ArrayList<SearchQuery.QueryPart>();

	private VideoSortColumn sort = VideoSortColumn.Relevance;

	public SearchQuery(final String q) {
		QueryPart qp = new QueryPart(_QUERY_PART_TEXT_, q);
		parts.add(qp);
	}

	public void add(final QueryPart qp) {
		parts.add(qp);
	}

	public void add(final String part, final String query) {
		QueryPart qp = new QueryPart(part, query);
		parts.add(qp);
	}

	public List<QueryPart> getQuery() {
		return parts;
	}

	/**
	 * @return the sort
	 */
	public VideoSortColumn getSort() {
		return sort;
	}

	/**
	 * @param sort
	 *            the sort to set
	 */
	public void setSort(final VideoSortColumn sort) {
		this.sort = sort;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer("QUERY[");
		if (parts != null && parts.size() > 0) {
			for (QueryPart qp : parts) {
				buff.append("{").append(qp.getPart()).append(":")
						.append(qp.getQuery()).append("}");
			}
		}
		buff.append("]");
		return buff.toString();
	}

}
