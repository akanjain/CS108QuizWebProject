package web;

import java.io.PrintStream;
import java.sql.ResultSet;
import web.ClockTimeStamp;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
			rs = stmt.executeQuery("SELECT DISTINCT * FROM messages WHERE fromUser = \"" + username + "\" OR toUser = \"" + username + "\" ORDER by time DESC;");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return rs;
	}
	
	public int getUserNumNewMessages(String username) {
		int num = -1;
		try {
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM messages WHERE toUser = \"" + username + "\" AND viewed = \"false\";");
			rs.next();
			num = rs.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return num;
	}
	
	public void markUserAllMessagesViewed(String username) {
		try {
			stmt.executeUpdate("UPDATE messages SET viewed = \"true\" WHERE toUser = \"" + username + "\" AND viewed = \"false\";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String sendMessage(String fromUser, String toUser, String message) {
		String timeStamp = ClockTimeStamp.getTimeStamp();
		String returnStatus = "(" + timeStamp + ") Message successfully sent.";
		
		try {
			/* Check if the receiver exist. */
			if (!accountManager.accountExist(toUser)) {
				return "(" + timeStamp + ") The user " + toUser + " does not exist.";
			}
					
			/* Send message. */
			stmt.executeUpdate("INSERT INTO messages VALUES (\"" + fromUser + "\",\"" + toUser + "\",\"" + timeStamp + "\",\"" + message + "\",\"false\");");
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
		
	public int getUserNumNewFriendRequests(String username) {
		int num = -1;
		try {
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM friendRequests WHERE toUser = \"" + username + "\" AND viewed = \"false\";");
			rs.next();
			num = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return num;
	}
	
	public void markUserAllFriendRequestsViewed(String username) {
		try {
			stmt.executeUpdate("UPDATE friendRequests SET viewed = \"true\" WHERE toUser = \"" + username + "\" AND viewed = \"false\";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String sendFriendRequest(String fromUser, String toUser, String message) {
		String timeStamp = ClockTimeStamp.getTimeStamp();
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
			stmt.executeUpdate("INSERT INTO friendRequests VALUES (\"" + fromUser + "\",\"" + toUser + "\",\"" + message + "\",\"false\");");
		} catch (SQLException e) {
			returnStatus = ("(" + timeStamp + ") An DB error occurred: " + e.toString() );
			e.printStackTrace();
		}
		
		return returnStatus;
	}
	
	public String acceptFriendRequest(String fromUser, String toUser) {
		String timeStamp = ClockTimeStamp.getTimeStamp();
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
		return false;
	}
	
	/* Send a challenge, from fromUser to toUser on quizId, assuming all arguments are correct. */
	public String sendChallenge(String fromUser, String toUser, int quizNumber) {
		String timeStamp = ClockTimeStamp.getTimeStamp();
		String returnStatus = "(" + timeStamp + ") Your challenge has been sent.";
		
		try {
			stmt.executeUpdate("INSERT INTO challenges VALUES (\"" + fromUser + "\",\"" + toUser + "\",\"" + quizNumber + "\",\"false\");");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			returnStatus = ("(" + timeStamp + ") An DB error occurred: " + e.toString() );
			e.printStackTrace();
		}
		
		return returnStatus;
	}
	
	public int getUserNumNewChallenges(String username) {
		int num = -1;	

		try {
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM challenges WHERE toUser = \"" + username + "\" AND viewed=\"false\";");
			rs.next();
			num = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return num;
	}
	
	public void markUserAllChallengesViewed(String username) {
		try {
			stmt.executeUpdate("UPDATE challenges SET viewed = \"true\" WHERE toUser = \"" + username + "\" AND viewed = \"false\";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Challenge> getUserChallengeStrings (String username) {
		List<Challenge> challenges = new LinkedList<Challenge>();
			
		try {
			ResultSet rs = getUserChallenges(username);
			while(rs.next()) {
				challenges.add(new Challenge(rs.getString("fromUser"), rs.getString("toUser"), rs.getInt("quizId")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return challenges;
	}
	
	
	public void removeChallenge(String fromUser, String toUser, int quizId) {
		try {
			stmt.executeUpdate("DELETE FROM challenges WHERE fromUser = \"" + fromUser + "\" AND toUser = \"" + toUser + "\" AND quizId = " + quizId + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public class Challenge {
		
		private String fromUser;
		private String toUser;
		private int quizId;
		
		public Challenge (String fromUser, String toUser, int quizId) {
			this.fromUser = fromUser;
			this.toUser = toUser;
			this.quizId = quizId;
		}
		
		public String getFromUser() {
			return fromUser;
		}
		
		public String getToUser() {
			return toUser;
		}
		
		public int getQuizId() {
			return quizId;
		}
		
		public int getBestScore() {
			return getUserBestScoreByQuiz(fromUser, quizId);
		}
		
	}
	private ResultSet getUserChallenges(String username) {
		ResultSet rs = null;
		
		try {
			rs = stmt.executeQuery("SELECT * FROM challenges WHERE toUser = \"" + username + "\";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}
	
	
	/* Promote user to administrator. */
	public String promoteUser(String username) {
		String timeStamp = ClockTimeStamp.getTimeStamp();
		String returnStatus = "(" + timeStamp + ")  " + username + " has been promoted to administrator.";
		
		if (!accountManager.accountExist(username)) {
			return "(" + timeStamp + ") The user " + username + " does not exist.";
		}
		
		if (isAdministrator(username)) {
			return "(" + timeStamp + ") The user " + username + " already is an administrator";
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
		return false;
	}

	public String createAnnouncement(String username, String announcement) {
		String timeStamp = ClockTimeStamp.getTimeStamp();
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
		PRACTICE_MODE 
	};
	
	// TODO: tests this function. 
	public void updateUserAchievements (Achievements ach, String username, int quizId, int score) {
		String timeStamp = ClockTimeStamp.getTimeStamp();
		ResultSet rs;
		
		try {
			switch (ach) {
				case CREATE_QUIZ:
					rs = stmt.executeQuery("SELECT COUNT(*) FROM quizzes WHERE creatorUsername = \"" + username + "\";");
					rs.next();
					int numCreated = rs.getInt(1);
					if (numCreated == 1) {
						if (!userAchievementExist(username, "Amateur Author")) {
							stmt.executeUpdate("INSERT INTO achievements VALUES (\"" + username + "\"," + "\"Amateur Author\",\"" + timeStamp + "\");");
						}			
					} else if (numCreated == 5) {
						if (!userAchievementExist(username, "Prolific Author")) {
							stmt.executeUpdate("INSERT INTO achievements VALUES (\"" + username + "\"," + "\"Prolific Author\",\"" + timeStamp + "\");");
						}		
					} else if (numCreated == 10) {
						if (!userAchievementExist(username, "Prodigious Autho")) {
							stmt.executeUpdate("INSERT INTO achievements VALUES (\"" + username + "\"," + "\"Prodigious Author\",\"" + timeStamp + "\");");
						}				
					}				
					break;					
				case TAKE_QUIZ:
					
					/* Check number of quiz a user took. */
					rs = stmt.executeQuery("SELECT COUNT(*) FROM quizRecords WHERE username = \"" + username + "\";");
					rs.next();
					int numPlayed = rs.getInt(1);
					if (numPlayed == 10 && !userAchievementExist(username, "Quiz Machine")) {
						stmt.executeUpdate("INSERT INTO achievements VALUES (\"" + username + "\"," + "\"Quiz Machine\",\"" + timeStamp + "\");");
					}			

					/* Check if user get the highest score. */
					/* Add only when user get highest score on a quiz and does not already have this achievements. */
					if (!userAchievementExist(username, "I am the Greatest")) {
						rs = stmt.executeQuery("SELECT score FROM quizRecords WHERE quizId = \"" + quizId + "\" ORDER BY score DESC LIMIT 1;");
						if (!rs.isBeforeFirst()) {
							stmt.executeUpdate("INSERT INTO achievements VALUES (\"" + username + "\"," + "\"I am the Greatest\",\"" + timeStamp + "\");");
						} else {
							rs.next();
							int maxScore = rs.getInt(1);
							if (score == maxScore) {
								stmt.executeUpdate("INSERT INTO achievements VALUES (\"" + username + "\"," + "\"I am the Greatest\",\"" + timeStamp + "\");");
							}
						}
						
					}					
					break;
				case PRACTICE_MODE:
					if (!userAchievementExist(username, "Practice Makes Perfect")) {
						stmt.executeUpdate("INSERT INTO achievements VALUES (\"" + username + "\"," + "\"Practice Makes Perfect\",\"" + timeStamp + "\");");
					}
					break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	// TODO: Test this function
	public ResultSet getUserHistory (String username) {
		 ResultSet rs = null;
		 
		 try {
			rs = stmt.executeQuery("SELECT * FROM quizRecords NATURAL JOIN quizzes WHERE username = \"" + username + "\" ORDER BY time DESC;");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	public ResultSet getUserFriends (String username) {
		ResultSet rs = null;
		
		try {
			rs = stmt.executeQuery("SELECT * FROM friends WHERE fromUser = \"" + username + "\";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}
	
	
	public ResultSet getUserAchievements(String username) {
		ResultSet rs = null;
		 
		 try {
			rs = stmt.executeQuery("SELECT * FROM achievements WHERE username = \"" + username + "\";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
		
	}
	
	public int getUserBestScoreByQuiz(String username, int quizId) {
		 int bestScore = -1;
		
		try {
			ResultSet rs = stmt.executeQuery("SELECT MAX(score) FROM quizRecords WHERE username = \"" + username + "\" AND quizId = \"" + quizId + "\";");
			rs.next();
			bestScore = rs.getInt("MAX(score)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bestScore;
	}
	
	public int getNumUsers() {
		int num = -1;
		
		try {
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM accounts;");
			rs.next();
			num = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return num;
	}
	
	public boolean isUserAccountValid(String username) {
		return accountManager.accountExist(username);
	}
	
	public String removeUser(String username) {
		String returnStatus = "The user has been successfully removed";
		
		if (!accountManager.accountExist(username)) {
			return "This account does not exist!";
		}
		
		try {
			
			/* Delete User Message. */
			stmt.executeUpdate("DELETE FROM messages WHERE fromUser = \"" + username + "\" OR toUser = \"" + username + "\";");
		
			/* Delete Challenges. */
			stmt.executeUpdate("DELETE FROM challenges WHERE fromUser = \"" + username + "\" OR toUser = \"" + username + "\";");
			
			/* Delete friend relationship. */
			stmt.executeUpdate("DELETE FROM friends WHERE fromUser = \"" + username + "\" OR toUser = \"" + username + "\";");
			
			/* Delete friend request. */
			stmt.executeUpdate("DELETE FROM friendRequests WHERE fromUser = \"" + username + "\" OR toUser = \"" + username + "\";");
			
			/* Delete achievements. */
			stmt.executeUpdate("DELETE FROM achievements WHERE username = \"" + username + "\";");
			
			/* Delete announcements. */
			stmt.executeUpdate("DELETE FROM announcements WHERE username = \"" + username + "\";");
			
			/* Delete playing records. */
			stmt.executeUpdate("DELETE FROM quizRecords WHERE username = \"" + username + "\";");
			
			/* Delete acccounts. */
			stmt.executeUpdate("DELETE FROM accounts WHERE username = \"" + username + "\";");
			stmt.executeUpdate("INSERT INTO deletedAccounts VALUES (\"" + username + "\");");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnStatus;
	}
	
	public class FriendActivity implements Comparable<FriendActivity>{
		private String time;
		private String HTMLActivity;
		
		public FriendActivity (String time, String HTMLActivity) {
			this.time = time;
			this.HTMLActivity = HTMLActivity;
		}
		
		public String getTimeStamp() {
			return time;
		}
		
		public void setTimeStamp(String time) {
			this.time = time;
		}
		
		public String getHTMLActivity() {
			return HTMLActivity;
		}
		
		public void setHTMLActivvity(String HTMLActivity) {
			this.HTMLActivity = HTMLActivity;
		}

		@Override
		public int compareTo(FriendActivity other) {
			return other.getTimeStamp().compareTo(this.getTimeStamp());
		}
		
		
	}
	public List<String> getUserFriendsRecentActivities(String username, int num) {
		ResultSet rs = null;
		List<FriendActivity> friendActivities = new LinkedList<FriendActivity>();
		
		try {
			System.out.println("SELECT * FROM achievements WHERE username IN (SELECT fromUser FROM friends WHERE toUser = \"" + username + "\") LIMIT " + num + ";");
			System.out.println("SELECT * FROM quizzes WHERE creatorUsername IN (SELECT fromUser FROM friends WHERE toUser = \"" + username + "\") LIMIT " + num + ";");
			System.out.println("SELECT * FROM quizRecords NATURAL JOIN quizzes WHERE username IN (SELECT fromUser FROM friends WHERE toUser = \"" + username + "\") LIMIT " + num + ";");
			rs = stmt.executeQuery("SELECT * FROM achievements WHERE username IN (SELECT fromUser FROM friends WHERE toUser = \"" + username + "\") LIMIT " + num + ";");
			while (rs.next()) {
				String timeStamp = rs.getString("time");
				String friendName = rs.getString("username");
				String html = "<a href=\"userpage.jsp?username=" + friendName + "\">" + friendName + "</a> acquired achievement \"" + rs.getString("achievement") + "\"";
				FriendActivity fa = new FriendActivity(timeStamp, html);
				friendActivities.add(fa);
			}
			
			rs = stmt.executeQuery("SELECT * FROM quizzes WHERE creatorUsername IN (SELECT fromUser FROM friends WHERE toUser = \"" + username + "\") LIMIT " + num + ";");
			while (rs.next()) {
				String timeStamp = rs.getString("dateCreated");
				String friendName = rs.getString("creatorUsername");
				String html = "<a href=\"userpage.jsp?username=" + friendName + "\">" + friendName + "</a> created quiz <a href=\"QuizPage.jsp?id=" + rs.getString("quizId") + "\">" + rs.getString("title") + "</a>"; 
				FriendActivity fa = new FriendActivity(timeStamp, html);
				friendActivities.add(fa);
			}	
			
			rs = stmt.executeQuery("SELECT * FROM quizRecords NATURAL JOIN quizzes WHERE username IN (SELECT fromUser FROM friends WHERE toUser = \"" + username + "\") LIMIT " + num + ";");
			while (rs.next()) {
				String timeStamp = rs.getString("time");
				String friendName = rs.getString("username");
				String html = "<a href=\"userpage.jsp?username=" + friendName + "\">" + friendName + "</a> played quiz <a href=\"QuizPage.jsp?id=" + rs.getString("quizId") + "\">" + rs.getString("title") + "</a> and got " + rs.getString("score") + " point(s)";
				FriendActivity fa = new FriendActivity(timeStamp, html);
				friendActivities.add(fa);
			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Collections.sort(friendActivities);
		
		if (friendActivities.size() > num) {
			friendActivities.subList(num, friendActivities.size()).clear();
		}
		
		
		List<String> result = new LinkedList<String>();
		for (FriendActivity fa : friendActivities) {
			result.add(fa.getTimeStamp() + "," + fa.getHTMLActivity());
		}

		return result;		
	}
	
	private boolean userAchievementExist(String username, String achievement) {
		ResultSet rs;
		try {
			rs = stmt.executeQuery("SELECT * FROM achievements WHERE username = \"" + username + "\" AND achievement = \"" + achievement + "\";");
			return rs.isBeforeFirst();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	

}


	

