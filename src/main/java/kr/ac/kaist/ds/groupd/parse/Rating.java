package kr.ac.kaist.ds.groupd.parse;

public class Rating {

	private final long userId;

	private final Movie movie;

	private final int rating;

	private final long timestamp;

	public Rating(long userId, Movie movie, int rating, long timestamp) {
		super();
		this.userId = userId;
		this.movie = movie;
		this.rating = rating;
		this.timestamp = timestamp;
	}

	public long getUserId() {
		return userId;
	}

	public Movie getMovie() {
		return movie;
	}

	public int getRating() {
		return rating;
	}

	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((movie == null) ? 0 : movie.hashCode());
		result = prime * result + rating;
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		result = prime * result + (int) (userId ^ (userId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rating other = (Rating) obj;
		if (movie == null) {
			if (other.movie != null)
				return false;
		} else if (!movie.equals(other.movie))
			return false;
		if (rating != other.rating)
			return false;
		if (timestamp != other.timestamp)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}


}
