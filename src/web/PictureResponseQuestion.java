package web;

import java.util.List;
import java.util.Set;

public class PictureResponseQuestion extends Question {

	private Set<String> ans;
	private String questionText;
	
	@Override
	public String getQuestion() {
		// TODO Auto-generated method stub
		return questionText;
	}

	@Override
	public void setQuestion(String s) {
		// TODO Auto-generated method stub
		questionText = s;
	}

	@Override
	public int getQuestionNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setQuestionNumber(int i) {
		// TODO Auto-generated method stub

	}
	
	public void setQuestionOptions(List<String> options) {
		
	}
	
	public List<String> getQuestionOptions() {
		return null;
	}
	
	public void addAnswers(Set<String> ans) {
		this.ans = ans;
	}
	
	public Set<String> getAnswers() {
		return this.ans;
	}

}
