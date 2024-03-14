package thrillio;

import java.util.List;

import thrilio.constants.KidFriendlyStatus;
import thrilio.constants.UserType;
import thrilio.controllers.BookmarkController;
import thrilio.entities.Bookmark;
import thrilio.entities.User;
import thrilio.partner.Shareable;
class View {
	public static void browse(User user, List<List<Bookmark>> bookmarks) {
		System.out.println("\n" + user.getEmail() + "is browsing items...");
		int bookmarkCount=0;
		
		for(List<Bookmark> bookmarkList : bookmarks) {
			
			for(Bookmark bookmark : bookmarkList) {
				//bookmarking..
				
					boolean isBookmarked = getBookmarkDecision(bookmark);
					if( isBookmarked) {
						bookmarkCount++;
						
						BookmarkController.getInstance().saveUserBookmark(user,bookmark);
						
						System.out.println(bookmark);
					}
				
				
				if(user.getUserType().equals(UserType.EDITOR)
						|| user.getUserType().equals(UserType.CHIEF_EDITOR)){
					//Mark as Kid Friendly
					if(bookmark.isKidFriendlyEligible() 
							&& bookmark.getKidFriendlyStatus().equals(KidFriendlyStatus.UNKNOWN)) {
					   KidFriendlyStatus kidFriendlyStatus = getKidFriendlyStatusDecision(bookmark);
						if(!kidFriendlyStatus.equals(KidFriendlyStatus.UNKNOWN)) {
							BookmarkController.getInstance().setKidFriendlytStatus(user, kidFriendlyStatus, bookmark);
						}
					}
					
					//Sharing
					if(bookmark.getKidFriendlyStatus().equals(KidFriendlyStatus.APPROVED)
							&& bookmark instanceof Shareable){
						boolean isShared = getShareDecision();
						if(isShared) {
							BookmarkController.getInstance().share(user, bookmark);
						}
					}
				}
			}
		}
	}
	//below methods simulate user input .After IO wetake input via console.
	private static boolean getShareDecision() {
		return Math.random() < 0.5 ? true : false;
	}
	private static KidFriendlyStatus getKidFriendlyStatusDecision(Bookmark bookmark) {
		// TODO Auto-generated method stub
		 double randomVal = Math.random();
	     
		    return randomVal < 0.4 ? KidFriendlyStatus.APPROVED :
		        (randomVal >= 0.4 && randomVal < 0.8) ? KidFriendlyStatus.REJECTED :
		            KidFriendlyStatus.UNKNOWN;
	}
	private static boolean getBookmarkDecision(Bookmark bookmark) {
			
		return Math.random() < 0.5 ? true : false;
	}
	
}
