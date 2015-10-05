package kr.ac.kaist.ds.groupd.parse;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Movie {

	private final long id;

	private final String title;

	private final Set<Genre> genre;

	public Movie(long id, String title, Collection<Genre> genre) {
		super();
		this.id = id;
		this.title = title;
		this.genre = new HashSet<>(genre);
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Set<Genre> getGenres() {
		return genre;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((genre == null) ? 0 : genre.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Movie other = (Movie) obj;
		if (genre == null) {
			if (other.genre != null)
				return false;
		} else if (!genre.equals(other.genre))
			return false;
		if (id != other.id)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

}
