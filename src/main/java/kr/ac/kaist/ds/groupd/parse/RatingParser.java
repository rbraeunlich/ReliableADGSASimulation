package kr.ac.kaist.ds.groupd.parse;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class RatingParser {

	// map for speedup of lookup
	private final Map<Long, Movie> movies;

	private final URL ratingFile;

	public RatingParser(Collection<Movie> movies, URL ratingFile) {
		super();
		this.movies = movies.stream().collect(Collectors.toMap(Movie::getId, m -> m));
		this.ratingFile = ratingFile;
	}

	public Collection<Rating> parseRatings() {
		Collection<Rating> ratings = new ArrayList<>();
		try (Scanner sc = new Scanner(ratingFile.openStream());) {
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] splitted = line.split("::");
				long userId = Long.parseLong(splitted[0]);
				long movieId = Long.parseLong(splitted[1]);
				int rating = Integer.parseInt(splitted[2]);
				long timestamp = Long.parseLong(splitted[3]);
				Rating rat = new Rating(userId, movies.get(movieId), rating, new Date(timestamp));
				ratings.add(rat);
			}
			return ratings;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
