package kr.ac.kaist.ds.groupd.topology;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;

import kr.ac.kaist.ds.groupd.parse.Genre;
import kr.ac.kaist.ds.groupd.parse.Movie;
import kr.ac.kaist.ds.groupd.parse.Rating;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.SparseVector;
import org.junit.BeforeClass;
import org.junit.Test;

import peersim.config.Configuration;

public class InterestInitializerTest {

	@BeforeClass
	public static void setUpBeforeClass() {
		Properties prop = new Properties();
		prop.put("foo.protocol", "bar");
		prop.put("protocol.bar", "barprot");
		Configuration.setConfig(prop);
	}

	@Test
	public void createInterestVectorForOneRating() {
		Movie m = new Movie(1L, "Foo", Collections.singleton(Genre.Action));
		Rating r = new Rating(1L, m, 5, new Date());
		InterestInitializer initializer = new InterestInitializer("foo");
		SparseVector<Real> vector = initializer
				.createInterestVector(Collections.singletonList(r));
		for (int i = 0; i < vector.getDimension(); i++) {
			if (i == 0) {
				assertThat(vector.get(i).intValue(), is(5));
			} else {
				assertThat(vector.get(i).intValue(), is(0));
			}

		}
	}

	@Test
	public void createInterestVectorForTwoDistinctRatings() {
		Movie m = new Movie(1L, "Foo", Collections.singleton(Genre.Action));
		Movie m2 = new Movie(2L, "Foo", Collections.singleton(Genre.Adventure));
		Rating r = new Rating(1L, m, 5, new Date());
		Rating r2 = new Rating(1L, m2, 3, new Date());
		InterestInitializer initializer = new InterestInitializer("foo");
		SparseVector<Real> vector = initializer.createInterestVector(Arrays
				.asList(r, r2));
		for (int i = 0; i < vector.getDimension(); i++) {
			if (i == 0) {
				assertThat(vector.get(i).intValue(), is(5));
			} else if (i == 1) {
				assertThat(vector.get(i).intValue(), is(3));
			} else {
				assertThat(vector.get(i).intValue(), is(0));
			}

		}
	}

	@Test
	public void createInterestVectorForThreeOverlappingRatings() {
		Movie m = new Movie(1L, "Foo", Arrays.asList(Genre.Action,
				Genre.Adventure));
		Movie m2 = new Movie(2L, "Foo", Arrays.asList(Genre.Adventure,
				Genre.Animation));
		Movie m3 = new Movie(3L, "Foo", Arrays.asList(Genre.Animation,
				Genre.Childrens));
		Rating r = new Rating(1L, m, 5, new Date());
		Rating r2 = new Rating(1L, m2, 3, new Date());
		Rating r3 = new Rating(1L, m3, 1, new Date());
		InterestInitializer initializer = new InterestInitializer("foo");
		SparseVector<Real> vector = initializer.createInterestVector(Arrays
				.asList(r, r2, r3));
		for (int i = 0; i < vector.getDimension(); i++) {
			if (i == 0) {
				assertThat(vector.get(i).intValue(), is(5));
			} else if (i == 1) {
				assertThat(vector.get(i).intValue(), is(4));
			} else if (i == 2) {
				assertThat(vector.get(i).intValue(), is(2));
			} else if (i == 3) {
				assertThat(vector.get(i).intValue(), is(1));
			} else {
				assertThat(vector.get(i).intValue(), is(0));
			}

		}
	}
}
