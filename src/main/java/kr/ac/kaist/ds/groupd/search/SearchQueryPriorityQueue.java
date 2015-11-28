
package kr.ac.kaist.ds.groupd.search;

import java.util.Comparator;
import java.util.PriorityQueue;

public class SearchQueryPriorityQueue<E> extends PriorityQueue<E> {
    private static final long serialVersionUID = 1L;

    private final int fixedSizeOfQueue;

    private Comparator<? super E> searchQueryPriorityQueueComparetor;

    public SearchQueryPriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
        super(initialCapacity, comparator);
        this.fixedSizeOfQueue = initialCapacity;
        this.searchQueryPriorityQueueComparetor = comparator;
    }

    @SuppressWarnings("unchecked")
    public E getTail() {
        Object[] queue = this.toArray();
        E tail = (E)queue[0];
        Comparator<? super E> comparator = this.comparator();
        if (comparator != null)
            for (int i = 1; i < this.size(); i++) {
                if (comparator.compare(tail, (E)queue[i]) < 0)
                    tail = (E)queue[i];
            }
        else
            for (int j = 1; j < this.size(); j++)
                if (((Comparable)tail).compareTo(((Comparable)queue[j])) < 0)
                    tail = (E)queue[j];
        return tail;

    }

    public E removeTail() {
        E tail = this.getTail();

        if (this.remove(tail))
            return tail;
        return null;
    }

    @Override
    public boolean add(E e) {
        if (this.size() >= fixedSizeOfQueue) {
            if (null != this.removeTail())
                return super.add(e);
            return false;
        }
        return super.add(e);
    }

    public void setTheSearchQueryPriorityQueueComparetor(Comparator<E> cmpt) {
        this.searchQueryPriorityQueueComparetor = cmpt;
    }

    @Override
    public int size() {
        return super.size();
    }

}
