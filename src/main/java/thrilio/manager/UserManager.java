package thrilio.manager;

import java.util.List;

import thrilio.constants.Gender;
import thrilio.constants.UserType;
import thrilio.dao.UserDao;
import thrilio.entities.User;
import thrilio.util.StringUtil;

public class UserManager {
	// creating instance variable static so that it could be accessible from static
	// getInsance method
	private static UserManager instance = new UserManager();
	private static UserDao dao = new UserDao();

	// creating Singleton
	private UserManager() {
	} // clients of userManager cannot create instance of UserManager
		// singleton, single object has to be created within this class

	public static UserManager getInstance() {
		// static bcz no one could create instance of UserManager
		// Return single instance of UserManager
		// It provides global point of access
		return instance;
	}

	// metod for instantiating user
	public User createUser(long id, String email, String password, String firstName, String lastName, Gender gender,
			UserType userType) {
		User user = new User();
		user.setId(id);
		user.setEmail(email);
		user.setPassword(password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setGender(gender);
		user.setUserType(userType);

		return user;
	}

	public List<User> getUser() {
		return dao.getUsers();
	}

	public User getUser(long userId) {
		// TODO Auto-generated method stub
		return dao.getUser(userId);
	}

	public long authenticate(String email, String password) {
		// TODO Auto-generated method stub
		return dao.authenticate(email, StringUtil.encodePassword(password));
	}
	

}
