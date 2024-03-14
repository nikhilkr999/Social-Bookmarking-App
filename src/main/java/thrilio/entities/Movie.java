package thrilio.entities;

import java.util.Arrays;

import thrilio.constants.MovieGenre;

public class Movie extends Bookmark {
	private int releaseYear;
	private String[] cast;
	private String[] directors;
	private MovieGenre genre;
	private double imdbRating;
	
	public int getReleaseYear() {
		return releaseYear;
	}
	public void setReleaseYear(int releaseYear) {
		this.releaseYear = releaseYear;
	}
	public String[] getCast() {
		return cast;
	}
	public void setCast(String[] cast) {
		this.cast = cast;
	}
	public String[] getDirectors() {
		return directors;
	}
	public void setDirectors(String[] directors) {
		this.directors = directors;
	}
	public MovieGenre getGenre() {
		return genre;
	}
	public void setGenre(MovieGenre genre2) {
		this.genre = genre2;
	}
	public double getImdbRating() {
		return imdbRating;
	}
	public void setImdbRating(double imdbRating) {
		this.imdbRating = imdbRating;
	}
	
	
	@Override
	public boolean isKidFriendlyEligible() {
		
		if(genre.equals(MovieGenre.THRILLERS) || genre.equals(MovieGenre.HORROR)) {
			return false;
		}
		return true;
	}
	@Override
	public String toString() {
		return "Movie [releaseYear=" + releaseYear + ", cast=" + Arrays.toString(cast) + ", directors="
				+ Arrays.toString(directors) + ", genre=" + genre + ", imdbRating=" + imdbRating + "]";
	}
	
}