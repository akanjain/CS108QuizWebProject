package web;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserDataManager {
	private Statement stmt;
	private AccountManager accountManager;
	
	public UserDataManager(Statement stmt, AccountManager accountManager) {
		this.stmt = stmt;
		this.accountManager = accountManager;
	}
	
	public ResultSet getUserMessages(String username) {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT DISTINCT * FROM messages WHERE fromUser = \"" + username + "\" OR toUser = \"" + username + "\";");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return rs;
	}
	
	public String sendMessage(String fromUser, String toUser, String message) {
		String timeStamp = getTimeStamp();
		String returnStatus = "(" + timeStamp + ") Message successfully sent.";
		
		try {
			/* Check if the receiver exist. */
			if (!accountManager.accountExist(toUser)) {
				return "(" + timeStamp + ") The user " + toUser + " does not exist.";
			}
			

			
			/* Send message. */
			stmt.executeUpdate("INSERT INTO messages VALUES (\"" + fromUser + "\",\"" + toUser + "\",\"" + timeStamp + "\",\"" + message + "\");");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			returnStatus = ("(" + timeStamp + ") An DB error occurred: " + e.toString() );
			e.printStackTrace();
		}
		
		return returnStatus;
	}
	
	public ResultSet getUserFriendRequests(String username) {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT DISTINCT * FROM friendRequests WHERE fromUser = \"" + username + "\" OR toUser = \"" + username + "\";");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return rs;
	}
	
	/* Promote user to administrator. */
	public String promoteUser(String username) {
		String timeStamp = getTimeStamp();
		String returnStatus = "(" + timeStamp + ")  " + username + " has been promoted to administrator.";
		
		if (!accountManager.accountExist(username)) {
			return "(" + timeStamp + "), The user " + username + " does not exist.";
		}
		
		if (isAdministrator(username)) {
			return "(" + timeStamp + "), The user " + username + " already is an administrator";
		}
		
		try {
			
			stmt.executeUpdate("UPDATE accounts SET isAdministrator=\"true\" WHERE username = \"" + username + "\";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			returnStatus = ("(" + timeStamp + ") An DB error occurred: " + e.toString() );
			e.printStackTrace();
		}
		
		return returnStatus;
		
	}

	public String sendFriendRequest(String fromUser, String toUser, String message) {
		String timeStamp = getTimeStamp();
		String returnStatus = "(" + timeStamp + ") Request successfully sent to user" + toUser + ".";
		
		try {
			/* Check if the receiver exist. */
			if (!accountManager.accountExist(toUser)) {
				return "(" + timeStamp + "), The user " + toUser + " does not exist.";
			}
			
			/* Prevent user from sending request to himself. */
			if (fromUser.equals(toUser)) {
				return "(" + timeStamp + ") You cannot send a friend request to yourself.";
			}
			
			/* Check if user sent this request before. */
			ResultSet rs = stmt.executeQuery("SELECT * FROM friendRequests WHERE fromUser = \"" + fromUser + "\" AND toUser = \"" + toUser + "\";");
			if (rs.isBeforeFirst()) {
				return "(" + timeStamp + ") You have already sent a request to " + toUser + ".";
			}
			
			/* Check if receiver already sent you a request. */
			rs = stmt.executeQuery("SELECT * FROM friendRequests WHERE fromUser = \"" + toUser + "\" AND toUser = \"" + fromUser + "\";");
			if (rs.isBeforeFirst()) {
				return "(" + timeStamp + ")" + toUser + " already sent you a request.";
			}	
			
			/* Check if friendship already exist. */
			if (areFriends(fromUser, toUser)) {
				return "(" + timeStamp + ") " + toUser + " and you are already firends.";
			}
					
			/* Insert friendRequest. */
			stmt.executeUpdate("INSERT INTO friendRequests VALUES (\"" + fromUser + "\",\"" + toUser + "\",\"" + timeStamp + "\",\"" + message + "\");");
		} catch (SQLException e) {
			returnStatus = ("(" + timeStamp + ") An DB error occurred: " + e.toString() );
			e.printStackTrace();
		}
		
		return returnStatus;
	}
	
	public String acceptFriendRequest(String fromUser, String toUser) {
		String timeStamp = getTimeStamp();
		String returnStatus = "(" + timeStamp + ") You and " + fromUser + " are now friends.";
		
		try {
			/* Delete friendRequest. */
			stmt.executeUpdate("DELETE FROM friendRequests WHERE fromUser = \"" + fromUser + "\" AND toUser = \"" + toUser + "\";");
			
			/* Insert mutual friendship. */
			stmt.executeUpdate("INSERT INTO friends VALUES (\"" + fromUser + "\",\"" + toUser + "\");");
			stmt.executeUpdate("INSERT INTO friends VALUES (\"" + toUser + "\",\"" + fromUser + "\");");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnStatus;		
	}
	
	public ResultSet searchUsers(String str) {
		ResultSet rs = null;
		try {
			System.out.println("SELECT * FROM accounts WHERE username LIKE \"%" + str + "%\";");
			rs = stmt.executeQuery("SELECT * FROM accounts WHERE username LIKE \"%" + str + "%\";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
		
	}
	
	/* Returns true if user1 and user2 are friends. */
	public boolean areFriends(String user1, String user2) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM friends WHERE fromUser = \"" + user1 + "\" AND toUser = \"" + user2 + "\";");
			return rs.isBeforeFirst();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/* Should not reach here. */
		assert false;
		return false;
	}
	
	/* Returns true is provided user is administrator. */
	public boolean isAdministrator(String username) {
		
		/* Return false if account does not exist. */
		if (!accountManager.accountExist(username)) { return false; }
		try {
			System.out.println("SELECT * FROM accounts WHERE username = \"" + username + "\";");
			ResultSet rs = stmt.executeQuery("SELECT * FROM accounts WHERE username = \"" + username + "\";");
			rs.next();
			return rs.getString("isAdministrator").equals("true");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/* Should not reach here. */
		assert false;
		return false;
	}
	private String getTimeStamp() {
		return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
	}

	public String createAnnouncement(String username, String announcement) {
		String timeStamp = getTimeStamp();
		String returnStatus = "(" + timeStamp + ") Announcement has been successfully created";
		
		try {
			stmt.executeUpdate("INSERT INTO announcements VALUES (\"" + username + "\",\"" + timeStamp + "\",\"" + announcement + "\");");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			returnStatus = ("(" + timeStamp + ") An DB error occurred: " + e.toString() );
			e.printStackTrace();
		}
		
		return returnStatus;
	}
	
	public ResultSet getAnnouncements() {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM announcements ORDER BY time DESC;");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	public enum Achievements {
		CREATE_QUIZ,
		TAKE_QUIZ,
		HIGH_SCORE,
		PRACTICE_MODE 
	};
	
	// TODO: tests this function. 
	public void updateUserAchievements (Achievements ach, String username, String quizId, int score) {
		ResultSet rs;
		
		try {
			switch (ach) {
				case CREATE_QUIZ:
					rs = stmt.executeQuery("SELECT COUNT(*) FROM quizzes WHERE creatorUsername = \"" + username + "\";");
					int numCreated = rs.getInt(1);
					if (numCreated == 1) { 
						stmt.executeUpdate("INSERT INTO achievements VALUES (\"" + username + "\"," + "\"Amateur Author\");");
					} else if (numCreated == 5) {
						stmt.executeUpdate("INSERT INTO achievements VALUES (\"" + username + "\"," + "\"Prolific Author\");");
					} else if (numCreated == 10) {
						stmt.executeUpdate("INSERT INTO achievements VALUES (\"" + username + "\"," + "\"Prodigious Author\");");
					}				
					break;					
				case TAKE_QUIZ:
					rs = stmt.executeQuery("SELECT COUNT(*) FROM quizRecords WHERE username = \"" + username + "\";");
					int numPlayed = rs.getInt(1);
					if (numPlayed == 10) {
						stmt.executeUpdate("INSERT INTO achievements VALUES (\"" + username + "\"," + "\"Quiz Machine\");");
					}			
					break;
				case HIGH_SCORE:
					rs = stmt.executeQuery("SELECT score FROM quizRecords WHERE quizId = \"" + quizId + "\" ORDER BY score DESC LIMIT 1;");
					int maxScore = rs.getInt(1);
					if (score > maxScore) {
						stmt.executeUpdate("INSERT INTO achievements VALUES (\"" + username + "\"," + "\"I am the Greatest\");");
					}
					break;
				case PRACTICE_MODE:
					rs = stmt.executeQuery("SELECT * FROM achievements WHERE username = \"" + username + "\" AND achievements = \"Practice Makes Perfect\";");
					if (!rs.isBeforeFirst()) {
						stmt.executeUpdate("INSERT INTO achievements VALUES (\"" + username + "\"," + "\"Practice Makes Perfect\");");
					}
					break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public ResultSet getUserHistory (String username) {
		 ResultSet rs = null;
		 
		 try {
			 System.out.println("SELECT * FROM quizRecords NATURAL JOIN quizzes WHERE username = \"" + username + "\" ORDER BY time DESC");
			rs = stmt.executeQuery("SELECT * FROM quizRecords NATURAL JOIN quizzes WHERE username = \"" + username + "\" ORDER BY time DESC");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
}


	

