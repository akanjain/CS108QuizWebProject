package web;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser {
	private static Quiz quiz;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			File fXmlFile = new File("/xml/bunny.xml");
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(fXmlFile);
			NodeList elemList = document.getChildNodes();
			
			boolean isRandom = false;
			QuestionsFactory questionsFactory = new QuestionsFactory();
			for(int i = 0; i < elemList.getLength(); i++) {
				Node node = elemList.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					Element element= (Element) node;
					String tagName = element.getTagName();
					if (tagName == "quiz") isRandom = Boolean.parseBoolean(element.getAttribute("random"));		
					//title
					else if (tagName == "title") quiz = new Quiz(isRandom, element.getTextContent());	
					//category
					else if(tagName == "category") quiz.setCategory(element.getTextContent());
					//description
					else if(tagName == "description") quiz.setDescription(element.getTextContent());
					//question
					else if(tagName == "question") {
						parseQuestion(element, questionsFactory);
					}	
				}
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void parseQuestion(Element element, QuestionsFactory questionsFactory) {
		String qType = element.getAttribute("type");
		Question question = questionsFactory.getQuestion(qType);
		
		NodeList children = element.getChildNodes();
		for(int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if(child.getNodeType() == Node.ELEMENT_NODE) {
				Element elem = (Element) child;
				String tag = elem.getTagName();
				if(tag == "query") question.setQuestion(elem.getTextContent());
				else if(tag == "image-location") ((PictureResponseQuestion) question).setImageLocation(elem.getTextContent());
				else if(tag == "black-query") /*((FillInBlankQuestion) question) */return;
						//TODO:finish this!!
				
				if(tag == "answer") {
					Set<String> answers = new HashSet<String>();
					answers.add(elem.getTextContent());
					question.addAnswers(answers);
				} else if (tag == "answer-list") {
					//TODO: finish this!!
				}
				
			}
		}
		
	}

}
