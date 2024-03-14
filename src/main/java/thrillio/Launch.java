package thrillio;

import java.util.ArrayList;
import java.util.List;

import thrilio.bgjobs.WebpageDownloaderTask;
import thrilio.entities.Bookmark;
import thrilio.entities.User;
import thrilio.manager.BookmarkManager;
import thrilio.manager.UserManager;

public class Launch {
	private static List<User> users = new ArrayList<>();
	private static List<List<Bookmark>> bookmarks = new ArrayList<>();
	
	private static void loadData() {
		System.out.println("1. Loading data...");
		DataStore.loadData();
		
		bookmarks = BookmarkManager.getInstance().getBookmark();
		users = UserManager.getInstance().getUser();
		
		System.out.println("2. Data printing..");
		printUserData();
		printBookmarkData();
	}
	
	private static void printUserData() {
		for(User user: users) {
			System.out.println(user);
		}
		
	}
	
	private static void printBookmarkData() {
		for(List<Bookmark> bookmarkList : bookmarks) {
			for(Bookmark bookmark : bookmarkList) {
				System.out.println(bookmark);
			}
		}
		
	}
	private static void start() {
		// TODO Auto-generated method stub
		//System.out.println("2. Bookmarking");
		for(User users: users) {
			View.browse(users, bookmarks);
		}
		
	}
	public static void main(String[] args) {
		loadData();
		start();
		//background jobs
		//runDownloaderJob();
	}
	private static void runDownloaderJob() {
		WebpageDownloaderTask task = new WebpageDownloaderTask(true);
		(new Thread(task)).start();
	}
	

}
