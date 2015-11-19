package kr.ac.kaist.ds.groupd.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

public class MovieParser {

	private final URL fileUrl;

	public MovieParser(URL fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Collection<Movie> parseMovies() {
		Collection<Movie> movies = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(new File(
				fileUrl.toURI())));) {
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] splitted = line.split("::");
				String genres = splitted[2].replaceAll("'", "").replaceAll("-",
						"");
				Collection<Genre> collGenres = new ArrayList<>();
				for (String genre : genres.split("\\|")) {
					collGenres.add(Genre.valueOf(genre));
				}
				long movieId = Long.parseLong(splitted[0]);
				String title = splitted[1];
				Movie mov = new Movie(movieId, title, collGenres);
				movies.add(mov);
			}
			return movies;
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
}
