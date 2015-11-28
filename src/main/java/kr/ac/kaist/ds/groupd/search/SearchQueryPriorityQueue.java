
package kr.ac.kaist.ds.groupd.search;

import java.util.Comparator;
import java.util.PriorityQueue;

public class SearchQueryPriorityQueue<E> extends PriorityQueue<E> {
    private static final long serialVersionUID = 1L;

    private final int fixedSizeOfQueue;

    public SearchQueryPriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
        super(initialCapacity, comparator);
        this.fixedSizeOfQueue = initialCapacity;
    }

    @Override
    public boolean add(E e) {
        if (this.size() >= fixedSizeOfQueue) {
            if (null != remove())
                return super.add(e);
            return false;
        }
        return super.add(e);
    }
}
