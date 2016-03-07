package web;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.*;

import java.text.SimpleDateFormat;

public class QuizManager {
	private Statement stmt;
	
	public QuizManager(Statement stmt) {
		this.stmt = stmt;
	}
	
	public synchronized int createQuiz(String quizTitle, String quizDescription, String quizCategory, String creatorName, 
							String dateCreated, boolean isRandom, boolean isOnePage, boolean isImmediate,
							boolean isPracticeMode, int numQuestions) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM quizzes");
			int quizNumber = 1;
			if (rs.isBeforeFirst()) {
				rs.last();
				String lastQuizNum = rs.getString("quizId");
				quizNumber = Integer.parseInt(lastQuizNum) + 1;
			}
			//String qry = "INSERT INTO quizzes VALUES (" + quizNumber + ", \"" + quizTitle + "\", \"" + quizDescription + "\", \"" + quizCategory + "\", \"" + creatorName
			//+ "\", \"" + dateCreated + "\", '" + isRandom + "', '" + isOnePage + "', '" + isPracticeMode + "'," + numQuestions + ")";
			String qry = "INSERT INTO quizzes VALUES (" + quizNumber + ", \"" + quizTitle + "\", \"" + quizDescription + "\", \"" + quizCategory + "\", \"" + creatorName
			+ "\", NULL, '" + isRandom + "', '" + isOnePage + "', '" + isImmediate + "', '" + isPracticeMode + "'," + numQuestions + ")";
			System.out.println(qry);
			stmt.executeUpdate(qry);
			return quizNumber;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	public synchronized int addQuestion(int quizNumber, String questionType, String text) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM questions");
			int questionNumber = 1;
			if (rs.isBeforeFirst()) {
				rs.last();
				String lastQuesNum = rs.getString("questionId");
				questionNumber = Integer.parseInt(lastQuesNum) + 1;
			}
			String qry = "INSERT INTO questions VALUES (" + questionNumber + ", " + quizNumber + ", \"" + questionType + "\", \"" + text + "\")";
			System.out.println(qry);
			stmt.executeUpdate(qry);
			return questionNumber;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	public synchronized int addUnorderedQuestion(int quizNumber, String questionType, String text, int numSlots) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM questions");
			int questionNumber = 1;
			if (rs.isBeforeFirst()) {
				rs.last();
				String lastQuesNum = rs.getString("questionId");
				questionNumber = Integer.parseInt(lastQuesNum) + 1;
			}
			String qry = "INSERT INTO questions VALUES (" + questionNumber + ", " + quizNumber + ", \"" + questionType + "\", \"" + text + "\")";
			System.out.println(qry);
			stmt.executeUpdate(qry);
			rs = stmt.executeQuery("SELECT * FROM numberSlots");
			rs.last();
			String lastkeyNum = rs.getString("keyId");
			int keyNumber = Integer.parseInt(lastkeyNum) + 1;
			String qrySlot = "INSERT INTO numberSlots VALUES (" + keyNumber + ", " + questionNumber + ", " + numSlots + ")";
			System.out.println(qrySlot);
			stmt.executeUpdate(qrySlot);
			return questionNumber;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	public synchronized void addMultiChoiceAnswer(int questionNumber, String[] ansArray, boolean[] correctArray) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM answerOptions");
			int keyNum = 1;
			if (rs.isBeforeFirst()) {
				rs.last();
				String lastkeyNum = rs.getString("keyId");
				keyNum = Integer.parseInt(lastkeyNum) + 1;
			}
			for (int i = 0; i < ansArray.length; i++) {
				String qry = "INSERT INTO answerOptions VALUES (" + keyNum + ", " + questionNumber + ", \"" + ansArray[i] + "\", '" + correctArray[i] + "')";
				System.out.println(qry);
				stmt.executeUpdate(qry);
				keyNum++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void addMatchingAnswer(int questionNumber, String[] choiceArray, String[] answerArray) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM matchingOptions");
			int keyNum = 1;
			if (rs.isBeforeFirst()) {
				rs.last();
				String lastkeyNum = rs.getString("keyId");
				keyNum = Integer.parseInt(lastkeyNum) + 1;
			}
			for (int i = 0; i < choiceArray.length; i++) {
				String qry = "INSERT INTO matchingOptions VALUES (" + keyNum + ", " + questionNumber + ", \"" + choiceArray[i] + "\", '" + answerArray[i] + "')";
				System.out.println(qry);
				stmt.executeUpdate(qry);
				keyNum++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void addAnswer(int questionNumber, String[] ansArray) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM answers");
			int keyNum = 1;
			if (rs.isBeforeFirst()) {
				rs.last();
				String lastkeyNum = rs.getString("keyId");
				keyNum = Integer.parseInt(lastkeyNum) + 1;
			}
			for (int i = 0; i < ansArray.length; i++) {
				String qry = "INSERT INTO answers VALUES (" + keyNum + ", " + questionNumber + ", \"" + ansArray[i] + "\")";
				System.out.println(qry);
				stmt.executeUpdate(qry);
				keyNum++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateQuizCreation(int quizNumber) {
		try {		
			String timeStamp = ClockTimeStamp.getTimeStamp();
			String qry = "UPDATE quizzes SET dateCreated = \"" + timeStamp + "\" WHERE quizId = " + quizNumber;
			System.out.println(qry);
			stmt.executeUpdate(qry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ResultSet getQuizList() {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM quizzes WHERE dateCreated IS NOT NULL");
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public ResultSet getQuizListbyUser(String username) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM quizzes WHERE creatorUsername=\"" + username + "\"");
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public ResultSet getQuizListbyCategory(String category) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM quizzes WHERE lower(category)=\"" + category.toLowerCase() + "\"");
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public Map<String, Set<String>> getAllQuizCategory() {
		Map<String, Set<String>> mp = new HashMap<String, Set<String>>();
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM quizzes");
			while (rs.next()) {
				String categoryName = rs.getString("category");
				String quizid = rs.getString("quizId");
				if (!mp.containsKey(categoryName.toLowerCase())) {
					mp.put(categoryName.toLowerCase(), new HashSet<String>());
				}
				Set<String> s = mp.get(categoryName.toLowerCase());
				s.add(quizid);
				mp.put(categoryName.toLowerCase(), s);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mp;
	}
	
	public ResultSet getQuiz(int quizNumber) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM quizzes WHERE quizId = " + quizNumber);
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public ResultSet getQuestion(int quizNumber) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM questions WHERE quizId = " + quizNumber);
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public ResultSet getAnswerSlot(int questionNumber) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM numberSlots WHERE questionId = " + questionNumber);
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public ResultSet getAnswer(int questionNumber) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM answers WHERE questionId = " + questionNumber);
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public ResultSet getAnswerOption(int questionNumber) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM answerOptions WHERE questionId = " + questionNumber);
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public ResultSet getMatchingOption(int questionNumber) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM matchingOptions WHERE questionId = " + questionNumber);
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public String clearQuizHistory(int quizNumber) {
		String timeStamp = ClockTimeStamp.getTimeStamp();
		String returnStatus = "(" + timeStamp + ") Quiz Records hvae been successfully cleared"; 
		
		try {
			
			if (!quizExist(quizNumber)) {
				returnStatus = "(" + timeStamp + ") The quiz you entered does not exist!";
				return returnStatus;
			}
			
			stmt.executeUpdate("DELETE FROM quizRecords WHERE quizId = " + quizNumber + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnStatus;
	}
	
	public boolean quizExist(int quizNumber) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM quizzes WHERE quizId = " + quizNumber + ";" );
			return rs.isBeforeFirst();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/* Should not reach here. */
		return false;
	}
	
	private void removeQuestion(int questionNumber) {
		try {
			
			stmt.executeUpdate("DELETE FROM answers WHERE questionId = " + questionNumber + ";");
			stmt.executeUpdate("DELETE FROM answerOptions WHERE questionId = " + questionNumber + ";");
			stmt.executeUpdate("DELETE FROM matchingOptions WHERE questionId = " + questionNumber + ";");
			stmt.executeUpdate("DELETE FROM numberSlots WHERE questionId = " + questionNumber + ";");
			
			
			/* Delete question. */
			stmt.executeUpdate("DELETE FROM questions WHERE questionId = " + questionNumber + ";");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String removeQuiz(int quizNumber) {
		String timeStamp = ClockTimeStamp.getTimeStamp();
		String returnStatus = "(" + timeStamp + ") Quiz has been successfully removed.";
		
		/* Check quiz existence. */
		if (!quizExist(quizNumber)) {
			returnStatus = "(" + timeStamp + ") The quiz you entered does not exist!";
			return returnStatus;
		}
		
		try {
			/* Delete challenges of the quiz. */
			stmt.executeUpdate("DELETE FROM challenges WHERE quizId = " + quizNumber + ";");
			
			/* Delete playing record. */
			clearQuizHistory(quizNumber);
			
			/* Delete questions of that quiz. */
			ResultSet rs = stmt.executeQuery("SELECT questionId from questions WHERE quizId = " + quizNumber + ";");
			List<Integer> questions = new LinkedList<Integer>();
			
			while (rs.next()) {
				questions.add(rs.getInt("questionId"));
			}
			
			/* Remove each question. */
			for (int questionId : questions) {
				removeQuestion(questionId);
			}
			
			/* Remove review and rating of quiz. */
			stmt.executeUpdate("DELETE FROM ratingNreviews WHERE quizId = " + quizNumber + ";");
			
			stmt.executeUpdate("DELETE FROM quizzes WHERE quizId = " +  quizNumber + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return returnStatus;
		
	}
	
	public void addUserQuizRecord(String username, int quizId, int totalTime, int score) {
		String timeStamp = ClockTimeStamp.getTimeStamp();
		try {
			stmt.executeUpdate("INSERT INTO quizRecords VALUES (\"" + username + "\"," + quizId + ",\"" + timeStamp + "\"," + totalTime + "," + score + ");");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getNumQuizzes() {
		int num = -1;
		try {
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM quizzes;");
			rs.next();
			num = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return num;
	}
	
	public int  getNumQuizRecords() {
		int num = -1;
		
		try {
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM quizRecords;");
			rs.next();
			num = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return num;
	}
	
	public ResultSet getPopularQuizzes(int numRecords) {
		ResultSet rs = null;
		
		try {
			rs = stmt.executeQuery("SELECT quizId, title, COUNT(quizId) FROM quizRecords NATURAL JOIN quizzes GROUP BY quizId ORDER BY COUNT(quizId) DESC LIMIT " + numRecords + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return rs;
	}
	
	public ResultSet getRecentlyCreatedQuizzes(int numRecords) {
		ResultSet rs = null;
		
		try {
			rs = stmt.executeQuery("SELECT quizId, dateCreated, title FROM quizzes WHERE dateCreated IS NOT NULL ORDER BY dateCreated DESC LIMIT " + numRecords + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}
	
	public ResultSet getUserRecentActivities(String username, int numRecords) {
		ResultSet rs = null;
		
		try {
			rs = stmt.executeQuery("SELECT * FROM quizzes NATURAL JOIN quizRecords WHERE username = \"" + username + "\" LIMIT " + numRecords + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}
	
	public ResultSet getRecentlyCreatedQuizzes(String username, int numRecords) {
		ResultSet rs = null;
		
		try {
			rs = stmt.executeQuery("SELECT quizId, dateCreated, title FROM quizzes WHERE creatorUsername = \"" + username + "\" AND dateCreated IS NOT NULL ORDER BY dateCreated DESC LIMIT " + numRecords + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}
	
	public ResultSet getUserQuizPerformance(String username, int quizId, int numRecords) {
		ResultSet rs = null;
		
		try {
			String qry = "SELECT time, duration, score FROM quizRecords WHERE username = \"" + username + "\" AND quizId = " + quizId + " ORDER BY time DESC LIMIT " + numRecords + ";";
			System.out.println("qry = " + qry);
			rs = stmt.executeQuery(qry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}
	
	public ResultSet getHighestPerformers(int quizId, int numRecords) {
		ResultSet rs = null;
		
		try {
			String qry = "SELECT username, time, duration, score FROM quizRecords WHERE quizId = " + quizId + " ORDER BY score DESC, duration LIMIT " + numRecords + ";";
			System.out.println("qry = " + qry);
			rs = stmt.executeQuery(qry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}

	public ResultSet getHighestPerformers(int quizId, int minutes, int numRecords) {
		ResultSet rs = null;
		String referenceTime = ClockTimeStamp.getTimeStampMinusMin(minutes);
		try {
			String qry = "SELECT username, time, duration, score FROM quizRecords WHERE quizId = " + quizId + " AND time >= \"" + referenceTime + "\" ORDER BY score DESC, duration LIMIT " + numRecords + ";";
			System.out.println("qry = " + qry);
			rs = stmt.executeQuery(qry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}
	
	public ResultSet getPerformers(int quizId, int minutes, int numRecords) {
		ResultSet rs = null;
		String referenceTime = ClockTimeStamp.getTimeStampMinusMin(minutes);
		try {
			String qry = "SELECT username, time, duration, score FROM quizRecords WHERE quizId = " + quizId + " AND time >= \"" + referenceTime + "\" ORDER BY time DESC LIMIT " + numRecords + ";";
			System.out.println("qry = " + qry);
			rs = stmt.executeQuery(qry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}
	
	public synchronized void addRatingReview(int quizNumber, String username, int userRating, String reviewText) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM ratingNreviews");
			int keyNumber = 1;
			if (rs.isBeforeFirst()) {
				rs.last();
				String lastkeyNum = rs.getString("keyId");
				keyNumber = Integer.parseInt(lastkeyNum) + 1;
			}
			String qry = "INSERT INTO ratingNreviews VALUES (" + keyNumber + ", " + quizNumber + ", \"" + username + "\", " + userRating + ", \"" + reviewText + "\")";
			System.out.println(qry);
			stmt.executeUpdate(qry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ResultSet getQuizRatingReview(int quizNumber) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM ratingNreviews WHERE quizId = " + quizNumber);
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public synchronized void addQuizTag(int quizNumber, String tagText) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM quizTags");
			int keyNumber = 1;
			if (rs.isBeforeFirst()) {
				rs.last();
				String lastkeyNum = rs.getString("keyId");
				keyNumber = Integer.parseInt(lastkeyNum) + 1;
			}
			String qry = "INSERT INTO quizTags VALUES (" + keyNumber + ", " + quizNumber + ", \"" + tagText.toLowerCase() + "\")";
			System.out.println(qry);
			stmt.executeUpdate(qry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ResultSet getQuizTags(int quizNumber) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM quizTags WHERE quizId = " + quizNumber);
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public Map<String, Set<String>> getAllQuizTags() {
		Map<String, Set<String>> mp = new HashMap<String, Set<String>>();
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM quizTags");
			while (rs.next()) {
				String tagName = rs.getString("tagName");
				String quizid = rs.getString("quizId");
				if (!mp.containsKey(tagName.toLowerCase())) {
					mp.put(tagName.toLowerCase(), new HashSet<String>());
				}
				Set<String> s = mp.get(tagName.toLowerCase());
				s.add(quizid);
				mp.put(tagName.toLowerCase(), s);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mp;
	}
	
	public Set<String> getAllQuizMatchingTags(String tag) {
		Set<String> allTagList = new HashSet<String>();
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM quizTags WHERE tagName = \"" + tag.toLowerCase() + "\"");
			while (rs.next()) {
				String quizid = rs.getString("quizId");
				allTagList.add(quizid);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allTagList;
	}
	
}
