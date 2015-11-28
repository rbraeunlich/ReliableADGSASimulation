
package kr.ac.kaist.ds.groupd.search;

import java.util.Comparator;

public class SpecialSearchQueryPriorityQueue extends SearchQueryPriorityQueue<SearchQuery> {

    private static final long serialVersionUID = 1L;

    private final int fixedSizeOfQueue;

    public SpecialSearchQueryPriorityQueue(int initialCapacity,
            Comparator<? super SearchQuery> comparator) {
        super(initialCapacity, comparator);
        this.fixedSizeOfQueue = initialCapacity;
    }

    @Override
    public boolean offer(SearchQuery searchQuery) {

        deleteUnnecessarySearchQuery(searchQuery);

        if (this.size() >= fixedSizeOfQueue) {
            SearchQuery element = remove();
            if (this.comparator().compare(element, searchQuery) == -1)
                return super.offer(searchQuery);

            return false;
        }

        return super.offer(searchQuery);
    }

    private void deleteUnnecessarySearchQuery(SearchQuery searchQuery) {
        for (int i = this.size(); i > 0; i--) {
            if (searchQuery.getDestination() == element().getDestination()
                    && element().isBackward() == false
                    && searchQuery.getSource() == element().getSource()) {
                this.remove();
            }
        }

    }

}
