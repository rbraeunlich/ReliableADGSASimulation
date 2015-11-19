
package kr.ac.kaist.ds.groupd.topology;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

<<<<<<< HEAD
import kr.ac.kaist.ds.groupd.information.ManagerGroups;
=======
>>>>>>> 938bd0ce7dc7193a4ca301d51340b209d52b4448
import kr.ac.kaist.ds.groupd.parse.Genre;
import kr.ac.kaist.ds.groupd.parse.Movie;
import kr.ac.kaist.ds.groupd.parse.MovieParser;
import kr.ac.kaist.ds.groupd.parse.Rating;
import kr.ac.kaist.ds.groupd.parse.RatingParser;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

/**
 * Initialzer class that parses the movie and ratings files and assigns each
 * node in the network it's interest vector, derived from the movie ratings. If
 * there are more nodes in the network than users in the dataset, then the nodes
 * that are too many will get an empty vector.
 */
public class InterestInitializer implements Control {

<<<<<<< HEAD
    private static final String PAR_PROT = "protocol";

    private static ManagerGroups MG = new ManagerGroups(Network.size(), ManagerGroups._BLANCE_MODE);

    private final int pid;

    public InterestInitializer(String prefix) {
        this.pid = Configuration.getPid(prefix + "." + PAR_PROT);
    }

    @Override
    public boolean execute() {
        System.out.println("Start");

        Collection<Movie> movies = parseMovies();
        Collection<Rating> ratings = parseRatings(movies);
        Map<Long, List<Rating>> ratingsByUserId = ratings.stream().collect(
                Collectors.groupingBy(Rating::getUserId));

        System.out.println("finish initializing each node's states");

        for (int i = 0; i < Network.size(); i++) {
            List<Rating> filteredRatings = ratingsByUserId.get(Long.valueOf(i + 1));
            double[] interestVector = createInterestVector(filteredRatings);
            InterestProtocol protocol = (InterestProtocol)Network.get(i).getProtocol(pid);
            protocol.setInterestVector(interestVector);
        }
        return false;

    }

    private Collection<Rating> parseRatings(Collection<Movie> movies) {
        URL ratingsFile = this.getClass().getClassLoader()
                .getResource("kr/ac/kaist/ds/groupd/topology/ratings.dat");
        Collection<Rating> ratings = new RatingParser(movies, ratingsFile).parseRatings();
        return ratings;
    }

    private Collection<Movie> parseMovies() {
        URL moviesFile = this.getClass().getClassLoader()
                .getResource("kr/ac/kaist/ds/groupd/topology/movies.dat");
        Collection<Movie> movies = new MovieParser(moviesFile).parseMovies();
        return movies;
    }

    /**
     * Creates a vector based on the ratings in the Movielens dataset. For every
     * gerne the user's average rating is calculated and placed in the vector.
     * The position inside the vector is determined by the ordinal inside
     * {@link Genre}.
     * 
     * @param filteredRatings
     * @return
     */
    protected double[] createInterestVector(List<Rating> filteredRatings) {
        List<Genre> genreList = Arrays.asList(Genre.values());
        double[] ratings = new double[genreList.size()];
        double[] madeRatingsPerGenre = new double[genreList.size()];
        // FIXME I am unsure if averaging is the best way
        for (Rating rating : filteredRatings) {
            Set<Genre> genres = rating.getMovie().getGenres();
            for (Genre genre : genres) {
                ratings[genre.ordinal()] = ratings[genre.ordinal()] + rating.getRating();
                madeRatingsPerGenre[genre.ordinal()] = madeRatingsPerGenre[genre.ordinal()] + 1.0;
            }
        }
        double[] interestVector = new double[genreList.size()];
        for (int i = 0; i < interestVector.length; i++) {
            if (madeRatingsPerGenre[i] == 0) {
                continue;
            }
            interestVector[i] = ratings[i] / madeRatingsPerGenre[i];
        }
        return interestVector;
    }

    static ManagerGroups getManagerGroups() {
        return InterestInitializer.MG;
    }
=======
	private static final String PAR_PROT = "protocol";

	private final int pid;

	public InterestInitializer(String prefix) {
		this.pid = Configuration.getPid(prefix + "." + PAR_PROT);
	}

	@Override
	public boolean execute() {
		Collection<Movie> movies = parseMovies();
		Collection<Rating> ratings = parseRatings(movies);
		Map<Long, List<Rating>> ratingsByUserId = ratings.stream().collect(Collectors.groupingBy(Rating::getUserId));
		for (int i = 0; i < Network.size(); i++) {
			List<Rating> filteredRatings = ratingsByUserId.get(Long.valueOf(i + 1));
			double[] interestVector = createInterestVector(filteredRatings);
			InterestProtocol protocol = (InterestProtocol) Network.get(i)
					.getProtocol(pid);
			protocol.setInterestVector(interestVector);
		}
		return false;
	}

	private Collection<Rating> parseRatings(Collection<Movie> movies) {
		URL ratingsFile = this.getClass().getClassLoader()
				.getResource("kr/ac/kaist/ds/groupd/topology/ratings.dat");
		Collection<Rating> ratings = new RatingParser(movies, ratingsFile)
				.parseRatings();
		return ratings;
	}

	private Collection<Movie> parseMovies() {
		URL moviesFile = this.getClass().getClassLoader()
				.getResource("kr/ac/kaist/ds/groupd/topology/movies.dat");
		Collection<Movie> movies = new MovieParser(moviesFile).parseMovies();
		return movies;
	}

	/**
	 * Creates a vector based on the ratings in the Movielens dataset.
	 * For every gerne the user's average rating is calculated and placed in the vector.
	 * The position inside the vector is determined by the ordinal inside {@link Genre}.
	 * @param filteredRatings
	 * @return
	 */
	protected double[] createInterestVector(
			List<Rating> filteredRatings) {
		List<Genre> genreList = Arrays.asList(Genre.values());
		double[] ratings = new double[genreList.size()];
		double[] madeRatingsPerGenre = new double[genreList.size()];
		// FIXME I am unsure if averaging is the best way
		for (Rating rating : filteredRatings) {
			Set<Genre> genres = rating.getMovie().getGenres();
			for (Genre genre : genres) {
				ratings[genre.ordinal()] = ratings[genre.ordinal()] + rating.getRating();
				madeRatingsPerGenre[genre.ordinal()] = madeRatingsPerGenre[genre.ordinal()] + 1.0;
			}
		}
		double[] interestVector = new double[genreList.size()];
		for(int i = 0; i < interestVector.length; i++){
			if(madeRatingsPerGenre[i] == 0){
				continue;
			}
			interestVector[i] = ratings[i] / madeRatingsPerGenre[i];
		}
		return interestVector;
	}
>>>>>>> 938bd0ce7dc7193a4ca301d51340b209d52b4448

}
