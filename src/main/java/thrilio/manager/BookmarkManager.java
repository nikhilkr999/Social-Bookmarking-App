package thrilio.manager;

import java.util.Collection;
import java.util.List;

import thrilio.constants.BookGenre;
import thrilio.constants.KidFriendlyStatus;
import thrilio.constants.MovieGenre;
import thrilio.dao.BookmarkDao;
import thrilio.entities.Book;
import thrilio.entities.Bookmark;
import thrilio.entities.Movie;
import thrilio.entities.User;
import thrilio.entities.UserBookmark;
import thrilio.entities.Weblink;

public class BookmarkManager {
	private static BookmarkManager instance = new BookmarkManager();
	private static BookmarkDao dao = new BookmarkDao();
	
	private BookmarkManager(){}
	
	public static BookmarkManager getInstance() {
		return instance;
	}
	public Movie createMovie(long id, String title,String profileUrl, int releaseYear,String[] cast,
			String[] directors, MovieGenre genre , double imdbRating) {
		Movie movie = new Movie();
		movie.setId(id);
		movie.setTitle(title);
		movie.setProfileUrl(profileUrl);
		movie.setReleaseYear(releaseYear);
		movie.setCast(cast);
		movie.setDirectors(directors);
		movie.setGenre(genre);
		movie.setImdbRating(imdbRating);
		
		return movie;
	}
	
	public Book createBook(long id, String title, String imageUrl, int publicationYear, String publisher,
			String[] authors, BookGenre genre, double amazonRating) {
		Book book = new Book();
		book.setId(id);
		book.setTitle(title);
		book.setImageUrl(imageUrl);
		book.setPublicationYear(publicationYear);
		book.setPublisher(publisher);
		book.setAuthors(authors);
		book.setGenre(genre);
		book.setAmazonRating(amazonRating);
		
		return book;
	}
	
	public Weblink createWebLink(long id, String title, String url, String host) {
		Weblink weblink = new Weblink();
		weblink.setId(id);
		weblink.setTitle(title);
		weblink.setUrl(url);
		weblink.setHost(host);
		
		return weblink;
	}
	
	public List<List<Bookmark>> getBookmark(){
		return dao.getBookmarks();
	}
	
	public void saveUserBookmark(User user, Bookmark bookmark) {

		UserBookmark userBookmark = new UserBookmark();

		userBookmark.setUser(user);

		userBookmark.setBookmark(bookmark); 
		
		
		dao.saveUserBookmark(userBookmark);
	}
		

	public void setKidFriendlyStatus(User user, KidFriendlyStatus kidFriendlyStatus, Bookmark bookmark) {
		bookmark.setKidFriendlyStatus(kidFriendlyStatus);
		bookmark.setKidFriendlyMarkBy(user);
		
		dao.updateKidFriendlyStatus(bookmark);
		System.out.println("kids- friendly status: " + kidFriendlyStatus +", Marked by" + user.getEmail()+ "," + bookmark);
		
	}

	public void share(User user, Bookmark bookmark) {
		bookmark.setSharedBY(user);
		
		System.out.println("Data to be Shared : ");
		if(bookmark instanceof Book ) {
			System.out.println( ((Book)bookmark).getItemData());
		}else if(bookmark instanceof Weblink) {
			System.out.println(((Weblink)bookmark).getItemData());
		}
		dao.sharedByInfo(bookmark);
	}

	public Collection<Bookmark> getBooks(boolean isBookmarked, long id) {

		return dao.getBooks(isBookmarked,id);
	}

	public Bookmark getBook(long bid) {
		// TODO Auto-generated method stub
		return dao.getBook(bid);
	}
	
	
}
