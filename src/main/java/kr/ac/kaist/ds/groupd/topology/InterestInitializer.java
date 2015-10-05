package kr.ac.kaist.ds.groupd.topology;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.SparseVector;

import javolution.util.Index;
import kr.ac.kaist.ds.groupd.parse.Genre;
import kr.ac.kaist.ds.groupd.parse.Movie;
import kr.ac.kaist.ds.groupd.parse.MovieParser;
import kr.ac.kaist.ds.groupd.parse.Rating;
import kr.ac.kaist.ds.groupd.parse.RatingParser;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

public class InterestInitializer implements Control {

	private static final String PAR_PROT = "protocol";

	private final int pid;

	public InterestInitializer(String prefix) {
		this.pid = Configuration.getPid(prefix + "." + PAR_PROT);
	}

	@Override
	public boolean execute() {
		URL ratingsFile = this.getClass().getClassLoader().getResource("kr/ac/kaist/ds/groupd/topology/ratings.dat");
		URL moviesFile = this.getClass().getClassLoader().getResource("kr/ac/kaist/ds/groupd/topology/movies.dat");
		Collection<Movie> movies = new MovieParser(moviesFile).parseMovies();
		Collection<Rating> ratings = new RatingParser(movies, ratingsFile).parseRatings();
		for(int i = 0; i < Network.size(); i++){
			int j = i;
			List<Rating> filteredRatings = ratings.stream().filter(r -> r.getUserId() == j).collect(Collectors.toList());
			SparseVector<Real> interestVector = createInterestVector(filteredRatings);
			InterestProtocol protocol = (InterestProtocol) Network.get(i).getProtocol(pid);
			protocol.setInterestVector(interestVector);
		}
		return false;
	}

	private SparseVector<Real> createInterestVector(List<Rating> filteredRatings) {
		List<Genre> genreList = new ArrayList<Genre>(Arrays.asList(Genre.values()));
		Map<Index, Real> ratings = genreList.stream().collect(Collectors.toMap(g -> Index.valueOf(g.ordinal()), g -> Real.ZERO));
		//FIXME I am unsure if averaging is the best way
		for (Rating rating : filteredRatings) {
			Set<Genre> genres = rating.getMovie().getGenres();
			for (Genre genre : genres) {
				Real r = ratings.get(Index.valueOf(genre.ordinal()));
				ratings.put(Index.valueOf(genre.ordinal()), r.plus(Real.valueOf(rating.getRating())));
			}
		}
		//FIXME the average is wrong, I should only average for the actual genres seen
		//I divide by all ratings, but if he only once saw comedy for example it will be 5/100, which is wrong
		ratings.replaceAll((k, v) -> v.divide(filteredRatings.size()));
		SparseVector<Real> vector = SparseVector.valueOf(Genre.values().length, Real.ZERO, ratings);
		return vector;
	}

}
