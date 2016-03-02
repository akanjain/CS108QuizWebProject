package web;

import java.util.*;

public class PracticeQuiz extends Quiz {
	private Map<Integer, Integer> countMap;
	
	public PracticeQuiz(boolean isRandom) {
		super(isRandom);
		countMap = new HashMap<Integer, Integer>();
	}
	
	public void addQuestion(Question qs) {
		super.addQuestion(qs);
		countMap.put(super.getQuestionListSize()-1, 0);
	}
	
	public boolean increaseCount(int key) {
		if (countMap.get(key) == 2) {
			System.out.println("Removing question");
			countMap.remove(key);
			removeQuestionIndex(key);
			return true;
		} else {
			countMap.put(key, countMap.get(key)+1);
			return false;
		}
	}
	
	public void removePracticeQuestion(int key) {
		if (countMap.containsKey(key)) {
			System.out.println("Removing question");
			countMap.remove(key);
			removeQuestionIndex(key);
		}
	}
}
