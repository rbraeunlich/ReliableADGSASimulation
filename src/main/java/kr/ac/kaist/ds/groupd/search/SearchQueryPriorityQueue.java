
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

    /*
     * add element and check all element usin self comparator
     * (non-Javadoc)
     * @see java.util.PriorityQueue#offer(java.lang.Object)
     */
    @Override
    public boolean offer(E e) {
        if (this.contains(e))
            return false;

        if (this.size() >= fixedSizeOfQueue) {

            E element = remove();

            if (null != element)
                if (this.comparator().compare(element, e) == -1)
                    return super.offer(e);

            return false;
        }

        return super.offer(e);
    }

}
