package thrilio.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import thrilio.constants.BookGenre;
import thrilio.entities.Book;
import thrilio.entities.Bookmark;
import thrilio.entities.Movie;
import thrilio.entities.UserBookmark;
import thrilio.entities.Weblink;
import thrilio.manager.BookmarkManager;
import thrillio.DataStore;

public class BookmarkDao {
	
	public List<List<Bookmark>> getBookmarks(){
		return DataStore.getBookmarks();
	}

	public void saveUserBookmark(UserBookmark userBookmark) {
		//DataStore.add(userBookmark);
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrilio?useSSL=true", "root", "Nikhil@9192");
				Statement stmt = conn.createStatement();) {
			if(userBookmark.getBookmark() instanceof Book) {
				saveUserBook(userBookmark, stmt);
			}else if(userBookmark.getBookmark() instanceof Movie) {
				saveUserMovie(userBookmark, stmt);
			}else {
				saveUserWeblink(userBookmark, stmt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void saveUserWeblink(UserBookmark userBookmark, Statement stmt) throws SQLException {
		// TODO Auto-generated method stub
		String query = " insert into User_Weblink (user_id, weblink_id) values(" +
				userBookmark.getUser().getId()+ "," + userBookmark.getBookmark().getId() + ")";
		stmt.executeUpdate(query);
	}

	private void saveUserMovie(UserBookmark userBookmark, Statement stmt) throws SQLException {
		// TODO Auto-generated method stub
		String query = " insert into User_Movie (user_id, movie_id) values(" +
				userBookmark.getUser().getId()+ "," + userBookmark.getBookmark().getId() + ")";
		stmt.executeUpdate(query);
	}

	private void saveUserBook(UserBookmark userBookmark, Statement stmt) throws SQLException {
		// TODO Auto-generated method stub
		String query = " insert into User_Book (user_id, book_id) values(" +
				userBookmark.getUser().getId()+ ", " + userBookmark.getBookmark().getId()+")";
		stmt.executeUpdate(query);
	}

	public List<Weblink> getAllWeblinks(){
		List<Weblink> result = new ArrayList<>();
		List<List<Bookmark>> bookmarks = DataStore.getBookmarks();
		List<Bookmark> allWeblinks = bookmarks.get(0);
		
		for(Bookmark bookmark : allWeblinks) {
			result.add((Weblink)bookmark);
		}
		return result;
	}
	
	public List<Weblink> getWeblinks(Weblink.DownloadStatus downloadStatus){
		List<Weblink> result = new ArrayList<>();
		List<Weblink> allWeblinks = getAllWeblinks();
		
		for(Weblink weblink : allWeblinks) {
			if(weblink.getDownloadStatus().equals(downloadStatus)) {
				result.add(weblink);
			}
		}
		
		return result;
	}

	public void updateKidFriendlyStatus(Bookmark bookmark) {
		// TODO Auto-generated method stub
		int kidFriendlyStatus = bookmark.getKidFriendlyStatus().ordinal();
		long userId = bookmark.getKidFriendlyMarkBy().getId();
		
		String tableToUpdate = "Book";
		if(bookmark instanceof Movie) {
			tableToUpdate = "Movie";
		}
		else if(bookmark instanceof Weblink) {
			tableToUpdate = "Weblink";
		}
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrilio?useSSL=true", "root", "Nikhil@9192");
				Statement stmt = conn.createStatement();) {
			String query = "update" + tableToUpdate +" set kid_friendly_status = " +kidFriendlyStatus + ", kid_friendly_marked_by = "+ userId  +"where id = " + bookmark.getId();            
					System.out.println("query (update kidFrinedlystatus): "+ query);
			stmt.executeUpdate(query);
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void sharedByInfo(Bookmark bookmark) {
		long userId = bookmark.getSharedBY().getId();
		String tableToUpdate = "Book";
		 
		if(bookmark instanceof Weblink) {
			tableToUpdate = "Weblink";
			
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrilio?useSSL=true", "root", "Nikhil@9192");
				Statement stmt = conn.createStatement();) {
			String query = "update" + tableToUpdate +" set shared_by = " + userId  + " where id = " + bookmark.getId();            
					System.out.println("query (update kidFrinedlystatus): "+ query);
			stmt.executeUpdate(query);
		
		} catch (SQLException e) {
			e.printStackTrace();
			}
		
		}
	}

	public Collection<Bookmark> getBooks(boolean isBookmarked, long userId) {
		
		Collection<Bookmark> result = new ArrayList<>();
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			//new com.mysql.jdbc.Driver(); 
			            // OR
			//System.setProperty("jdbc.drivers", "com.mysql.cj.jdbc.Driver");
		
		                // OR java.sql.DriverManager
		    //DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrilio?useSSL=true", "root", "Nikhil@9192");
				Statement stmt = conn.createStatement();) {			
			
			String query = "";
			if (!isBookmarked) {
				query = "Select b.id, title, image_url, publication_year, GROUP_CONCAT(a.name SEPARATOR ',') AS authors, book_genre_id, " +
									"amazon_rating from Book b, Author a, Book_Author ba where b.id = ba.book_id and ba.author_id = a.id and " + 
									"b.id NOT IN (select ub.book_id from User u, User_Book ub where u.id = " + userId +
									" and u.id = ub.user_id) group by b.id";				
			} else {
				query = "Select b.id, title, image_url, publication_year, GROUP_CONCAT(a.name SEPARATOR ',') AS authors, book_genre_id, " +
						"amazon_rating from Book b, Author a, Book_Author ba where b.id = ba.book_id and ba.author_id = a.id and " + 
						"b.id IN (select ub.book_id from User u, User_Book ub where u.id = " + userId +
						" and u.id = ub.user_id) group by b.id";
			}
			
			ResultSet rs = stmt.executeQuery(query);				
			
	    	while (rs.next()) {
	    		long id = rs.getLong("id");
				String title = rs.getString("title");
				String imageUrl = rs.getString("image_url");
				int publicationYear = rs.getInt("publication_year");
				//String publisher = rs.getString("name");		
				String[] authors = rs.getString("authors").split(",");			
				int genre_id = rs.getInt("book_genre_id");
				BookGenre genre = BookGenre.values()[genre_id];
				double amazonRating = rs.getDouble("amazon_rating");
				
				System.out.println("id: " + id + ", title: " + title + ", publication year: " + publicationYear + ", authors: " + String.join(", ", authors) + ", genre: " + genre + ", amazonRating: " + amazonRating);
	    		
	    		Bookmark bookmark = BookmarkManager.getInstance().createBook(id, title, imageUrl, publicationYear, null, authors, genre, amazonRating/*, values[7]*/);
	    		result.add(bookmark); 
	    	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public Bookmark getBook(long bookId) {
		Book book = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrilio?useSSL=true", "root", "Nikhil@9192");
				Statement stmt = conn.createStatement();) {
			String query = "Select b.id, title, image_url, publication_year, p.name, GROUP_CONCAT(a.name SEPARATOR ',') AS authors, book_genre_id, amazon_rating, created_date"
					+ " from Book b, Publisher p, Author a, Book_Author ba "
					+ "where b.id = " + bookId + " and b.publisher_id = p.id and b.id = ba.book_id and ba.author_id = a.id group by b.id";
	    	ResultSet rs = stmt.executeQuery(query);
			
	    	while (rs.next()) {
	    		long id = rs.getLong("id");
				String title = rs.getString("title");
				String imageUrl = rs.getString("image_url");
				int publicationYear = rs.getInt("publication_year");
				String publisher = rs.getString("name");		
				String[] authors = rs.getString("authors").split(",");			
				int genre_id = rs.getInt("book_genre_id");
				BookGenre genre = BookGenre.values()[genre_id];
				double amazonRating = rs.getDouble("amazon_rating");
				
				System.out.println("id: " + id + ", title: " + title + ", publication year: " + publicationYear + ", publisher: " + publisher + ", authors: " + String.join(", ", authors) + ", genre: " + genre + ", amazonRating: " + amazonRating);
	    		
	    		book = BookmarkManager.getInstance().createBook(id, title, imageUrl, publicationYear, publisher, authors, genre, amazonRating/*, values[7]*/);
	    	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return book;
	}
}
