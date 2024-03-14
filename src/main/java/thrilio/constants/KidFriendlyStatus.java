package thrilio.constants;

public enum KidFriendlyStatus {
	 APPROVED("approved"),
	 REJECTED("rejected"),
	 UNKNOWN("unlnown");
	 
	 private KidFriendlyStatus(String name) {
		 this.name = name;
	 }
	
	 private String name;
	 
	 public String getName() {
		 return name;
	 }
}
