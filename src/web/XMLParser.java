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
	private static String dateCreated;
	private static boolean isRandom;
	private static boolean isOnePage;
	private static boolean isImmediate;
	private static boolean isPracticeMode;
	private static int numQuestions;
	
	private static Map<String, String> questions; //(query, type)
	private static Map<String, List<String>> answerOptions; //(query, answer options)
	private static Map<String, List<String>> answersCorrect; //(query, correct answers)
	
	
	public XMLParser(String fileName) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			File fXmlFile = new File(fileName);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(fXmlFile);
			
			dateCreated = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
			questions = new HashMap<String, String>();
			answerOptions =  new HashMap<String, List<String>>();
			answersCorrect = new HashMap<String, List<String>>();
			
			NodeList elemList = document.getChildNodes();
			numQuestions = 0; 
			for(int i = 0; i < elemList.getLength(); i++) {
				Node node = elemList.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					Element element= (Element) node;
					String tagName = element.getTagName();
					//question
					if(tagName == "question") parseQuestion(element);
					else if (tagName == "quiz") {
						isRandom = Boolean.parseBoolean(element.getAttribute("random"));
						isOnePage = Boolean.parseBoolean(element.getAttribute("one-page"));
						isImmediate = Boolean.parseBoolean(element.getAttribute("immediate-correction"));
						isPracticeMode = Boolean.parseBoolean(element.getAttribute("practice-mode"));
					}
					else if (tagName == "title") quizTitle = element.getTextContent();	
					else if(tagName == "category") quizCategory = element.getTextContent();
					else if(tagName == "description") quizDescription = element.getTextContent();
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
		numQuestions++;
		
		String qType = element.getAttribute("type");
		String query = "";
		
		NodeList children = element.getChildNodes();
		for(int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if(child.getNodeType() == Node.ELEMENT_NODE) {
				Element elem = (Element) child;
				String tag = elem.getTagName(); 
				//query part
				if(tag == "query" || tag == "image-location") {
					questions.put(elem.getTextContent(), qType);
					query = elem.getTextContent();
					continue;
				}
				else if(tag == "blank-query") {
					String qText = "";
					NodeList parts = element.getChildNodes();
					for(int j = 0; j < parts.getLength(); j++) {
						Node n = parts.item(j);
						if(n.getNodeType() == Node.ELEMENT_NODE) {
							Element part = (Element) n;
							if (part.getTagName() == "blank") qText += "_____";
							else qText += part.getTextContent();
						}
					}
					questions.put(qText, qType);
					query = elem.getTextContent();
					continue;
				}
				//answer part
				if(qType.contains("multiple")) {
					//options
					if(tag == "option") {
						List<String> correctAnswers;
						if(elem.getAttribute("answer") == "answer") {
							if(answersCorrect.containsKey(query)) {
								correctAnswers = answersCorrect.get(query);
							} else {
								correctAnswers = new ArrayList<String>();
							}
							correctAnswers.add(elem.getTextContent());
							answersCorrect.put(query, correctAnswers);
						}
						List<String> options;
						if(answerOptions.containsKey(query)) {
							options = answerOptions.get(query);
						} else {
							options = new ArrayList<String>();
						}
						options.add(elem.getTextContent());
						answerOptions.put(query, options);
					}
				} else {
					if(tag == "answer") {
						//when there is a single answer, only save the answer in the anwerCorrect map
						List<String> correctAnswer = new ArrayList<String>();
						correctAnswer.add(elem.getTextContent());
						answersCorrect.put(query, correctAnswer);
						//picture response answer
					} else if (tag == "answer-list") {
						NodeList options = element.getChildNodes();
						for(int j = 0; j < options.getLength(); j++) {
							Node n = options.item(j);
							if(n.getNodeType() == Node.ELEMENT_NODE) {
								Element answer = (Element) n;
								if (answer.getTagName() == "answer") {
									if(answer.getAttribute("preferred") == "preferred") {
										List<String> correctAnswer = new ArrayList<String>();
										correctAnswer.add(elem.getTextContent());
										answersCorrect.put(query, correctAnswer);
									}
									
									List<String> answerList;
									if(answerOptions.containsKey(query)) {
										answerList = answerOptions.get(query);
									} else {
										answerList = new ArrayList<String>();
									}
									answerList.add(elem.getTextContent());
									answerOptions.put(query, answerList);
								}
							}
						}
						
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
	public String getDateCreated() {
		return dateCreated;
	}
	
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
	
	public Map<String, String> getQuestions() {
		return questions;
	}
	public Map<String, List<String>> getAnswerOptions() {
		return answerOptions;
	}
	public Map<String, List<String>> getCorrectAnswers() {
		return answersCorrect;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
