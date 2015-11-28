
package kr.ac.kaist.ds.groupd.search;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Queue;

import org.junit.Test;

public class SearchQueryPriorityQueueTest {

    @Test
    public void testAddHigherPriorityElementToFullQueue() {
        Queue<Integer> q = new SearchQueryPriorityQueue<>(1, (i1, i2) -> i1.compareTo(i2));
        q.offer(1);
        boolean offer = q.offer(2);
        assertThat(offer, is(true));
    }

    @Test
    public void testAddLowerPriorityElementToFullQueue() {
        Queue<Integer> q = new SearchQueryPriorityQueue<>(1, (i1, i2) -> i1.compareTo(i2));
        q.offer(2);
        boolean offer = q.offer(1);
        assertThat(offer, is(false));
    }
}
