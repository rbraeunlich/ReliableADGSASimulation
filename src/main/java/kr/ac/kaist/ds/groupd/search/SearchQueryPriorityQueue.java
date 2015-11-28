
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
    public boolean offer(E e) {
        
        if(this.contains(e))
            return false;
        
        if (this.size() >= fixedSizeOfQueue) {
            if (null != remove())
                return super.offer(e);
            
            return false;
        }
        
        return super.offer(e);
    }

}
