package kr.ac.kaist.ds.groupd.parse;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;

public class RatingParserTest {

	@Test
	public void testParse() {
		URL movies = this.getClass().getClassLoader().getResource("kr/ac/kaist/ds/groupd/parse/movies.dat");
		MovieParser movieParser = new MovieParser(movies);
		Collection<Movie> parseMovies = movieParser.parseMovies();
		URL ratings = this.getClass().getClassLoader().getResource("kr/ac/kaist/ds/groupd/parse/ratings.dat");
		RatingParser ratingParser = new RatingParser(parseMovies, ratings);
		Collection<Rating> parseRatings = ratingParser.parseRatings();
		assertThat(parseRatings.isEmpty(), is(false));
		assertThat(parseRatings.size(), is(3));
		for (Rating rating : parseRatings) {
			if (rating.getUserId() == 1L) {
				assertThat(rating.getMovie().getId(), is(1L));
				assertThat(rating.getRating(), is(5));
				assertThat(rating.getTimestamp(), is(equalTo(new Date(978600760L))));
			} else if (rating.getUserId() == 2L) {
				assertThat(rating.getMovie().getId(), is(2L));
				assertThat(rating.getRating(), is(3));
				assertThat(rating.getTimestamp(), is(equalTo(new Date(978402109))));
			} else if (rating.getUserId() == 3) {
				assertThat(rating.getMovie().getId(), is(3L));
				assertThat(rating.getRating(), is(3));
				assertThat(rating.getTimestamp(), is(equalTo(new Date(978501968))));
			} else {
				fail();
			}
		}
	}
}
