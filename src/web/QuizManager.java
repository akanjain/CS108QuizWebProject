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
	
	public Map<String, List<String>> getAllQuizByCategory() {
		Map<String, List<String>> mp = new HashMap<String, List<String>>();
		try {
			String qry = "SELECT * FROM quizzes ORDER BY dateCreated DESC";
			System.out.println(qry);
			ResultSet rs = stmt.executeQuery(qry);
			while (rs.next()) {
				String categoryName = rs.getString("category");
				String quizid = rs.getString("quizId");
				String title = rs.getString("title");
				String dateCreated = rs.getString("dateCreated");
				if (!mp.containsKey(categoryName.toLowerCase())) {
					mp.put(categoryName.toLowerCase(), new ArrayList<String>());
				}
				List<String> s = mp.get(categoryName.toLowerCase());
				s.add(quizid + "," + title + "," + dateCreated);
				mp.put(categoryName.toLowerCase(), s);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mp;
	}
	
	public Map<String, List<String>> getQuizByCategory(String category) {
		List<String> allQuizList = new ArrayList<String>();
		Map<String, List<String>> mp = new HashMap<String, List<String>>();
		try {
			String qry = "SELECT * FROM quizzes WHERE lower(category)=\"" + category.toLowerCase() + "\" ORDER BY dateCreated DESC";
			System.out.println(qry);
			ResultSet rs = stmt.executeQuery(qry);
			while (rs.next()) {
				String quizid = rs.getString("quizId");
				String title = rs.getString("title");
				String dateCreated = rs.getString("dateCreated");
				allQuizList.add(quizid + "," + title + "," + dateCreated);
			}
			mp.put(category.toLowerCase(), allQuizList);
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
	
	public Map<String, String> getAllUserQuizRecords(int quizId) {
		Map<String, String> userQuizRecord = new TreeMap<String, String>();
		
		try {
			String qry = "SELECT username, time, duration, score FROM quizRecords WHERE quizId = " + quizId + ";";
			System.out.println("qry = " + qry);
			ResultSet rs = stmt.executeQuery(qry);
			Map<String, Integer> countMap = new HashMap<String, Integer>();
			Map<String, String> bestScore = new HashMap<String, String>();
			Map<String, String> worstScore = new HashMap<String, String>();
			while (rs.next()) {
				String username = rs.getString("username");
				String duration = rs.getString("duration");
				String score = rs.getString("score");
				if (!countMap.containsKey(username)) {
					countMap.put(username, 1);
					bestScore.put(username, score + "," + duration);
					worstScore.put(username, score + "," + duration);
				} else {
					countMap.put(username, countMap.get(username)+1);
					String bScore = bestScore.get(username);
					String wScore = worstScore.get(username);
					String[] goodScore = bScore.split(",");
					String[] badScore = wScore.split(",");
					int score1 = Integer.parseInt(goodScore[0]);
					int dur1 = Integer.parseInt(goodScore[1]);
					int score2 = Integer.parseInt(badScore[0]);
					int dur2 = Integer.parseInt(badScore[1]);
					int scorenew = Integer.parseInt(score);
					int durnew = Integer.parseInt(duration);
					if (scorenew > score1) {
						bestScore.put(username, scorenew + "," + durnew);
					} else if (scorenew == score1) {
						if (durnew < dur1) {
							bestScore.put(username, scorenew + "," + durnew);
						}
					}
					if (scorenew < score2) {
						worstScore.put(username, scorenew + "," + durnew);
					} else if (scorenew == score2) {
						if (durnew > dur2) {
							worstScore.put(username, scorenew + "," + durnew);
						}
					}
				}
			}
			for (String user : countMap.keySet()) {
				userQuizRecord.put(user, countMap.get(user) + "_" + bestScore.get(user) + "_" + worstScore.get(user));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return userQuizRecord;
	}
	
	public String getUserQuizRecord(int quizId, String username) {

		String bestScore = "";
		try {
			String qry = "SELECT time, duration, score FROM quizRecords WHERE quizId = " + quizId + " AND username=\"" + username + "\" ORDER BY score DESC, duration;";
			System.out.println("qry = " + qry);
			ResultSet rs = stmt.executeQuery(qry);
			int countMap = 0;
			if (rs.next()) {
				String duration = rs.getString("duration");
				String score = rs.getString("score");
				bestScore = score + "," + duration;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bestScore;
	}
	
	public String getQuizTopScore(int quizId) {

		String bestScore = "";
		try {
			String qry = "SELECT username, duration, score FROM quizRecords WHERE quizId = " + quizId + " ORDER BY score DESC, duration;";
			System.out.println("qry = " + qry);
			ResultSet rs = stmt.executeQuery(qry);
			if (rs.next()) {
				String username = rs.getString("username");
				String duration = rs.getString("duration");
				String score = rs.getString("score");
				bestScore = username + "," + score + "," + duration;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bestScore;
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
	
	public Map<String, List<String>> getAllQuizTags() {
		Map<String, List<String>> mp = new HashMap<String, List<String>>();
		try {
			//ResultSet rs = stmt.executeQuery("SELECT * FROM quizTags");
			String qry = "SELECT a.quizId, a.tagName, b.title, b.dateCreated FROM quizTags a, quizzes b WHERE a.quizId = b.quizId ORDER BY b.dateCreated DESC";
			System.out.println(qry);
			ResultSet rs = stmt.executeQuery(qry);
			while (rs.next()) {
				String tagName = rs.getString("tagName");
				String quizid = rs.getString("quizId");
				String title = rs.getString("title");
				String dateCreated = rs.getString("dateCreated");
				if (!mp.containsKey(tagName.toLowerCase())) {
					mp.put(tagName.toLowerCase(), new ArrayList<String>());
				}
				List<String> s = mp.get(tagName.toLowerCase());
				s.add(quizid + "," + title + "," + dateCreated);
				mp.put(tagName.toLowerCase(), s);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mp;
	}
	
	public Map<String, List<String>> getAllQuizMatchingTags(String tag) {
		List<String> allTagList = new ArrayList<String>();
		Map<String, List<String>> mp = new HashMap<String, List<String>>();
		try {
			//String qry = "SELECT * FROM quizTags WHERE tagName = \"" + tag.toLowerCase() + "\" INNER JOIN quizzes USING (quizId)";
			String qry = "SELECT a.quizId, b.title, b.dateCreated FROM quizTags a, quizzes b WHERE a.quizId = b.quizId AND a.tagName=\"" + tag.toLowerCase() + "\" ORDER BY b.dateCreated DESC";
			System.out.println(qry);
			ResultSet rs = stmt.executeQuery(qry);
			while (rs.next()) {
				String quizid = rs.getString("quizId");
				String title = rs.getString("title");
				String dateCreated = rs.getString("dateCreated");
				allTagList.add(quizid + "," + title + "," + dateCreated);
			}
			mp.put(tag.toLowerCase(), allTagList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mp;
	}
	
	public boolean getQuizExistbyTag(String tag, int quizId) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM quizTags WHERE tagName = \"" + tag.toLowerCase() + "\" AND quizId = " + quizId);
			return rs.isBeforeFirst();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
}
