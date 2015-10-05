package kr.ac.kaist.ds.groupd.parse;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.Collection;

import org.junit.Test;

public class MovieParserTest {

	@Test
	public void testParse() {
		URL movies = this.getClass().getClassLoader().getResource("kr/ac/kaist/ds/groupd/parse/movies.dat");
		MovieParser parser = new MovieParser(movies);
		Collection<Movie> parseMovies = parser.parseMovies();
		assertThat(parseMovies.isEmpty(), is(false));
		assertThat(parseMovies.size(), is(3));
		for (Movie movie : parseMovies) {
			if (movie.getTitle().equals("Movie 1 (1995)")) {
				assertThat(movie.getId(), is(1L));
				for(Genre g : movie.getGenres()){
					assertThat(g, is(either(equalTo(Genre.Animation)).or(equalTo(Genre.Childrens)).or(equalTo(Genre.Comedy))));
				}
			} else if (movie.getTitle().equals("Movie 2 (1995)")) {
				assertThat(movie.getId(), is(2L));
				for(Genre g : movie.getGenres()){
					assertThat(g, is(either(equalTo(Genre.Adventure)).or(equalTo(Genre.Childrens)).or(equalTo(Genre.Fantasy))));
				}

			} else if (movie.getTitle().equals("Movie 3 (1995)")) {
				assertThat(movie.getId(), is(3L));
				for(Genre g : movie.getGenres()){
					assertThat(g, is(Genre.FilmNoir));
				}
			} else {
				fail();
			}
		}
	}

}
