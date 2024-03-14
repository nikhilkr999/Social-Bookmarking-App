package thrilio.entities;

import thrilio.constants.KidFriendlyStatus;

public abstract class Bookmark {
	private long id;
	private String title;
	private String profileUrl;
	private KidFriendlyStatus kidFriendlyStatus = KidFriendlyStatus.UNKNOWN;
	private User kidFriendlyMarkBy;
	private User sharedBY;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getProfileUrl() {
		return profileUrl;
	}
	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}
	
	public abstract boolean isKidFriendlyEligible();
	public User getKidFriendlyMarkBy() {
		return kidFriendlyMarkBy;
	}
	public void setKidFriendlyMarkBy(User kidFriendlyMarkBy) {
		this.kidFriendlyMarkBy = kidFriendlyMarkBy;
	}
	public User getSharedBY() {
		return sharedBY;
	}
	public void setSharedBY(User sharedBY) {
		this.sharedBY = sharedBY;
	}
	public KidFriendlyStatus getKidFriendlyStatus() {
		return kidFriendlyStatus;
	}
	public void setKidFriendlyStatus(KidFriendlyStatus kidFriendlyStatus) {
		this.kidFriendlyStatus = kidFriendlyStatus;
	}
	
	
}
