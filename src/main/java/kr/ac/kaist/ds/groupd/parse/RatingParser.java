package kr.ac.kaist.ds.groupd.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class RatingParser {

	// map for speedup of lookup
	private final Map<Long, Movie> movies;

	private final URL ratingFile;

	public RatingParser(Collection<Movie> movies, URL ratingFile) {
		super();
		this.movies = movies.stream().collect(
				Collectors.toMap(Movie::getId, m -> m));
		this.ratingFile = ratingFile;
	}

	public Collection<Rating> parseRatings() {
		Collection<Rating> ratings = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(new File(
				ratingFile.toURI())));) {
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] splitted = line.split("::");
				long userId = Long.parseLong(splitted[0]);
				long movieId = Long.parseLong(splitted[1]);
				int rating = Integer.parseInt(splitted[2]);
				long timestamp = Long.parseLong(splitted[3]);
				Rating rat = new Rating(userId, movies.get(movieId), rating,
						timestamp);
				ratings.add(rat);
			}
			return ratings;
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
}
