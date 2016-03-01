package web;

import java.util.*;

public class Quiz {
	public static final String NOT_SPECIFIED = "Not Specified";
	
	private String title;
	private String category;
	private String description;
	private List<Question> allQuestions;
	private List<Integer> index;
	private Map<Integer, List<String>> userAnswers;
	private int currentQuestion;
	private boolean isRandom;
	
	public Quiz(boolean isRandom, String title) {
		this.title = title;
		this.category = NOT_SPECIFIED;
		this.description = NOT_SPECIFIED;
		this.allQuestions = new ArrayList<Question>();
		this.index = new ArrayList<Integer>();
		this.userAnswers = new HashMap<Integer, List<String>>();
		this.isRandom = isRandom;
		this.currentQuestion = -1;
	}
	
	public void addQuestion(Question qs) {
		this.index.add(this.index.size());
		this.allQuestions.add(qs);
	}
	
	public void setQuestionIndexes() {
		if (isRandom) {
			Collections.shuffle(index);
		}
	}
	
	public Question getCurrentQuestion() {
		return this.allQuestions.get(getCurrentQuestionNumber());
	}
	
	public int getCurrentQuestionNumber() {
		return this.index.get(currentQuestion);
	}
	
	public Question getCurrentQuestion(int index) {
		return this.allQuestions.get(index);
	}
	
	public void setNextQuestionNumber() {
		this.currentQuestion++;
	}
	
	public void setPreviousQuestionNumber() {
		this.currentQuestion--;
	}
	
	public void setAnswers(int quesNumber, List<String> answerList) {
		userAnswers.put(quesNumber, answerList);
	}
	
	public int calculateScore() {
		int score = 0;
		for (int key : userAnswers.keySet()) {
			List<String> inputans = userAnswers.get(key);
			Question q = allQuestions.get(key);
			if (q instanceof MatchingQuestion) {
				List<String> allAnswerOption = q.getAnswerOptions();
				for (int i = 0; i < allAnswerOption.size(); i++) {
					if (allAnswerOption.get(i).equalsIgnoreCase(inputans.get(i))) {
						score++;
					}
				}
			} else {
				Set<String> validAnswers = q.getAnswers();
				for (String s : inputans) {
					if (validAnswers.contains(s.toLowerCase())) {
						score++;
					}
				}
			}
		}
		return score;
	}
	
	public int getScore(int key) {
		int score = 0;
		List<String> inputans = userAnswers.get(key);
		Question q = allQuestions.get(key);
		if (q instanceof MatchingQuestion) {
			List<String> allAnswerOption = q.getAnswerOptions();
			for (int i = 0; i < allAnswerOption.size(); i++) {
				if (allAnswerOption.get(i).equalsIgnoreCase(inputans.get(i))) {
					score++;
				}
			}
		} else {
			Set<String> validAnswers = q.getAnswers();
			for (String s : inputans) {
				if (validAnswers.contains(s.toLowerCase())) {
					score++;
				}
			}
		}
		return score;
	}
	
	public int getMaxScore(int key) {
		Question q = allQuestions.get(key);
		if (q instanceof MatchingQuestion) {
			List<String> allAnswerOption = q.getAnswerOptions();
			return allAnswerOption.size();
		} else if (q instanceof MultipleChoiceAnswerQuestion) {
			Set<String> validAnswers = q.getAnswers();
			return validAnswers.size();
		} else if (q instanceof MultipleAnswerUnOrderedQuestion) {
			return q.getNumSlot();
		} else if (q instanceof MultipleAnswerOrderedQuestion) {
			return q.getAnswers().size();
		} else {
			return 1;
		}
	}
	
	public String getAnswerAtQuestion(int quesNumber) {
		if (userAnswers.containsKey(quesNumber)) {
			return userAnswers.get(quesNumber).get(0);
		}
		return "";
	}
	
	public int getNumQuestion() {
		return allQuestions.size();
	}
	
	public String getQuizTitle() {
		return title;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getCategory() {
		return this.category;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
	
}
