package kr.ac.kaist.ds.groupd.search;

import java.util.ArrayList;
import java.util.Comparator;

public class SpecialSearchQueryPriorityQueue extends
		SearchQueryPriorityQueue<SearchQuery> {

	private static final long serialVersionUID = 1L;

	private final int fixedSizeOfQueue;

	public SpecialSearchQueryPriorityQueue(int initialCapacity,
			Comparator<? super SearchQuery> comparator) {
		super(initialCapacity, comparator);
		this.fixedSizeOfQueue = initialCapacity;
	}

	@Override
	public boolean offer(SearchQuery searchQuery) {

		if (this.size() >= fixedSizeOfQueue) {
			SearchQuery element = remove();
			if (this.comparator().compare(element, searchQuery) == -1) {
				if (searchQuery.isBackward()) {
					deleteUnnecessarySearchQuery(searchQuery);
				}
				return super.offer(searchQuery);
			}
			return false;
		}

		return super.offer(searchQuery);
	}

	/**
	 * Delete the {@link SearchQuery}s that are still forward searching because
	 * this query already found the destination.
	 */
	private void deleteUnnecessarySearchQuery(SearchQuery searchQuery) {
		for (SearchQuery q : new ArrayList<SearchQuery>(this)) {
			if (searchQuery.getDestination() == q.getDestination()
					&& q.isBackward() == false
					&& searchQuery.getSource() == q.getSource()) {
				this.remove(q);
			}
		}

	}

}
