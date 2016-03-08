package web;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class XMLQuizServlet
 */
@WebServlet("/XMLQuizServlet")
public class XMLQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public XMLQuizServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		ServletContext sc = request.getServletContext();
		QuizManager QzManager = (QuizManager) sc.getAttribute("Quiz Manager");
		//create quiz
		String creatorName = "ADMIN";
		String fileName = request.getParameter("fileName");
		String path = sc.getRealPath("/xml/") + fileName;
		System.out.println(path);
		XMLParser xml = new XMLParser(path);
		String quizTitle = xml.getQuizTitle();
		String quizDescription = xml.getQuizDescription();
		String quizCategory = xml.getQuizCategory();
		if (quizCategory == null) quizCategory = "NOT SPECIFIED";
		String dateCreated = ClockTimeStamp.getTimeStamp();
		boolean isRandom = xml.isRandom();
		boolean isOnePage = xml.isOnePage();
		boolean isImmediate = xml.isImmediate();
		boolean isPracticeMode = xml.isPracticeMode();
		int numQuestions = xml.numQuestions();
		int quizNumber = QzManager.createQuiz(quizTitle, quizDescription, quizCategory, creatorName, dateCreated, isRandom, isOnePage, isImmediate, isPracticeMode, numQuestions);


		List<String> questions = xml.getQuestions();
		List<String> questionTypes = xml.getQuestionTypes();
		Map<Integer, List<String>> answerOptions = xml.getAnswerOptions(); // (question index, answer options)
		Map<Integer, List<String>> answersCorrect = xml.getCorrectAnswers(); // (question index, correct answers)
		for(int i = 0; i < numQuestions; i++) {
			//add questions
			int quesNumber;
			if (questionTypes.get(i).equals("multiple-answer-unordered")) {
				int numSlots = xml.getNumSlots(i);
				quesNumber = QzManager.addUnorderedQuestion(quizNumber, questionTypes.get(i), questions.get(i), numSlots);
			} else {
				quesNumber = QzManager.addQuestion(quizNumber, questionTypes.get(i), questions.get(i));
			}

			//add answers
			if (questionTypes.get(i).equals("multiple-choice") || questionTypes.get(i).equals("multiple-choice-multiple-answer") || questionTypes.get(i).equals("multiple-choice-multiple-answer")) {
				//access both answers list and options list
				List<String> options = answerOptions.get(i);
				Set<String> answers = new HashSet<String>(answersCorrect.get(i));
				boolean[] correctArray = new boolean[options.size()];
				for (int j = 0; j < options.size(); j++) {
					if(answers.contains(options.get(j))){
						correctArray[j] = true;
					} else {
						correctArray[j] = false;
					}
				}
				String[] optionsArray = options.toArray(new String[options.size()]);
				QzManager.addMultiChoiceAnswer(quesNumber, optionsArray, correctArray);
				
			} else if (questionTypes.get(i).equals("matching")) {
				List<String> options = answerOptions.get(i);
				String[] choiceArray = options.toArray(new String[options.size()]);
				
				List<String> answers = answersCorrect.get(i);
				String[] ansArray = answers.toArray(new String[answers.size()]);
				QzManager.addMatchingAnswer(quesNumber, choiceArray, ansArray);
			} else {
				//just answers list
				List<String> answers = answersCorrect.get(i);
				String[] ansArray = answers.toArray(new String[answers.size()]);
				QzManager.addAnswer(quesNumber, ansArray);
			}
		}
		QzManager.updateQuizCreation(quizNumber);
		//RequestDispatcher dispatcher = request.getRequestDispatcher("///");
		//dispatcher.forward(request, response);
	}

}
