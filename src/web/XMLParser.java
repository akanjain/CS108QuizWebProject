package web;

import java.io.File;

import java.io.IOException;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.tomcat.jni.Time;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class XMLParser {
	private static String quizTitle;
	private static String quizDescription;
	private static String quizCategory;
	//private static String dateCreated;
	private static boolean isRandom;
	private static boolean isOnePage;
	private static boolean isImmediate;
	private static boolean isPracticeMode;
	private static int numQuestions;

	private static List<String> questions; //query
	private static List<String> qTypes; //type
	private static Map<Integer, List<String>> answerOptions; // (question index, answer options)
	private static Map<Integer, List<String>> answersCorrect; // (question index, correct answers)
	private static Map<Integer, Integer> corrAnswerSlots; // (question index, correct answer slots): for multi answer unordered questions

	public XMLParser(String path) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			File fXmlFile = new File(path);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(fXmlFile);

			//dateCreated = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
			questions = new ArrayList<String>();
			qTypes = new ArrayList<String>();
			answerOptions =  new HashMap<Integer, List<String>>();
			answersCorrect = new HashMap<Integer, List<String>>();
			corrAnswerSlots = new HashMap<Integer, Integer>();

			NodeList quizes = document.getElementsByTagName("quiz");
			Node quizNode = quizes.item(0);
			if(quizNode.getNodeType() == Node.ELEMENT_NODE) {
				Element quiz = (Element) quizNode;
				isRandom = Boolean.parseBoolean(quiz.getAttribute("random"));
				isOnePage = Boolean.parseBoolean(quiz.getAttribute("one-page"));
				isImmediate = Boolean.parseBoolean(quiz.getAttribute("immediate-correction"));
				isPracticeMode = Boolean.parseBoolean(quiz.getAttribute("practice-mode"));
					
				numQuestions = 0; 
				NodeList elemList = quiz.getChildNodes();
				for(int j = 0; j < elemList.getLength(); j++) {
					Node node = elemList.item(j);
					if(node.getNodeType() == Node.ELEMENT_NODE) {
						Element element= (Element) node;
						String tagName = element.getTagName();
						//question
						if(tagName == "question") {
							parseQuestion(element);
							numQuestions++;
						}
						else if (tagName == "title") quizTitle = element.getTextContent();	
						else if(tagName == "category") quizCategory = element.getTextContent();
						else if(tagName == "description") quizDescription = element.getTextContent();
					}
				}
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}


	private static void parseQuestion(Element element) {
		//index of current question
		int index = qTypes.size();

		String qType = element.getAttribute("type");
		qTypes.add(qType);

		NodeList children = element.getChildNodes();
		for(int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if(child.getNodeType() == Node.ELEMENT_NODE) {
				Element elem = (Element) child;
				String tag = elem.getTagName(); 

				//query part
				if(tag == "query" || tag == "image-location") {
					questions.add(elem.getTextContent());
					continue;
				}
				else if(tag == "blank-query") {
					String query = "";
					NodeList parts = element.getChildNodes();
					for(int j = 0; j < parts.getLength(); j++) {
						Node n = parts.item(j);
						if(n.getNodeType() == Node.ELEMENT_NODE) {
							Element part = (Element) n;
							if (part.getTagName() == "blank") query += "_____";
							else query += part.getTextContent();
						}
					}
					questions.add(query);
					continue;
				}

				//answer part
				if(qType.equals("multiple-choice") || qType.equals("multiple-choice-multiple-answer") || qType.equals("matching")) {
					//options
					if(tag == "option") {
						//only put in correct answers
						if(elem.getAttribute("answer").equals("answer")) {
							List<String> correctAnswers;
							if(answersCorrect.containsKey(index)) {
								correctAnswers = answersCorrect.get(index);
							} else {
								correctAnswers = new ArrayList<String>();
							}
							correctAnswers.add(elem.getTextContent());
							answersCorrect.put(index, correctAnswers);
						}
						//put all options(including the correct answers above)
						//matching will only deal with these without the answer attribute
						List<String> options;
						if(answerOptions.containsKey(index)) {
							options = answerOptions.get(index);
						} else {
							options = new ArrayList<String>();
						}
						options.add(elem.getTextContent());
						answerOptions.put(index, options);
					}
				} else {
					if(tag == "answer") {
						//there can be multiple correct answers
						List<String> correctAnswers;
						if(answersCorrect.containsKey(index)) {
							correctAnswers = answersCorrect.get(index);
						} else {
							correctAnswers = new ArrayList<String>();
						}
						correctAnswers.add(elem.getTextContent());
						answersCorrect.put(index, correctAnswers);
						//picture response answer and matching answer list
					} else if (tag == "answer-list") {
						//there can be multiple correct answers
						List<String> correctAnswers;
						NodeList answer_list = element.getElementsByTagName("answer");
						for(int j = 0; j < answer_list.getLength(); j++) {
							Node n = answer_list.item(j);
							if(n.getNodeType() == Node.ELEMENT_NODE) {
								Element answer = (Element) n;
								if(answersCorrect.containsKey(index)) {
									correctAnswers = answersCorrect.get(index);
								} else {
									correctAnswers = new ArrayList<String>();
								}
								correctAnswers.add(answer.getTextContent());
								answersCorrect.put(index, correctAnswers);
							}
						}
					} else if (tag == "numSlots") {
						//qType.equals("multiple-answer-unordered"
						corrAnswerSlots.put(index, Integer.parseInt(elem.getTextContent()));
					}
				}
			}
		}
	}

	//public methods to access quiz information
	public String getQuizTitle() {
		return quizTitle;
	}
	public String getQuizDescription() {
		return quizDescription;
	}
	public String getQuizCategory() {
		return quizCategory;
	}
	/*public String getDateCreated() {
		return dateCreated;
	}*/

	public boolean isRandom() {
		return isRandom;
	}
	public boolean isOnePage() {
		return isOnePage;
	}
	public boolean isImmediate() {
		return isImmediate;
	}
	public boolean isPracticeMode() {
		return isPracticeMode;
	}
	public int numQuestions() {
		return numQuestions;
	}

	public List<String> getQuestions() {
		return questions;
	}
	public List<String> getQuestionTypes() {
		return qTypes;
	}
	public Map<Integer, List<String>> getAnswerOptions() {
		return answerOptions;
	}
	public Map<Integer, List<String>> getCorrectAnswers() {
		return answersCorrect;
	}
	public int getNumSlots(int index) {
		return corrAnswerSlots.get(index);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
