package web;

import java.util.*;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Servlet implementation class QuizPlayServlet
 */
@WebServlet("/QuizPlayServlet")
public class QuizPlayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizPlayServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		ServletContext sc = request.getServletContext();
		QuizManager QzManager = (QuizManager) request.getServletContext().getAttribute("Quiz Manager");
		
		int quizId = Integer.parseInt(request.getParameter("quizId"));
		request.getSession().setAttribute("quizId", quizId);

		String quizMode = request.getParameter("quizMode");
		
		try {
			ResultSet rs = QzManager.getQuiz(quizId);
			rs.next();
			String isRandomOrder = rs.getString("isRandom");
			String isOnePageQuiz = rs.getString("isOnePage");
			String isImmediate = rs.getString("isImmediate");
			String isPracticeMode = rs.getString("isPracticeMode");
			boolean randomQuizOrder = false;
			if (isRandomOrder.equals("true")) {
				randomQuizOrder = true;
			}
			Quiz currentQuiz = null;
			if (quizMode.equals("realtest")) {
				currentQuiz = new Quiz(randomQuizOrder);
			} else if (quizMode.equals("practice")) {
				currentQuiz = new PracticeQuiz(randomQuizOrder);
			}
			QuestionsFactory factory = new QuestionsFactory();
			rs = QzManager.getQuestion(quizId);
			List<String> allQuesText = new ArrayList<String>();
			List<String> allQuesType = new ArrayList<String>();
			List<Integer> allQuesId = new ArrayList<Integer>();
			while (rs.next()) {
				allQuesText.add(rs.getString("questionText"));
				allQuesType.add(rs.getString("questionType"));
				allQuesId.add(Integer.parseInt(rs.getString("questionId")));	
			}
			for (int i = 0; i < allQuesId.size(); i++) {
				Question qs = factory.getQuestion(allQuesType.get(i));
				currentQuiz.addQuestion(qs);
				qs.setQuestion(allQuesText.get(i));
				Set<String> ans = new HashSet<String>();
				if (allQuesType.get(i).equals("multiple-choice") || allQuesType.get(i).equals("multiple-choice-multiple-answer")) {
					ResultSet answerOptRs = QzManager.getAnswerOption(allQuesId.get(i));
					List<String> options = new ArrayList<String>();
					while (answerOptRs.next()) {
						if (answerOptRs.getString("isCorrect").equals("true")) {
							ans.add(answerOptRs.getString("optionText").toLowerCase());
							options.add(answerOptRs.getString("optionText"));
						} else {
							options.add(answerOptRs.getString("optionText"));
						}
					}
					qs.setQuestionOptions(options);
				} if (allQuesType.get(i).equals("matching")) {
					ResultSet matchOptRs = QzManager.getMatchingOption(allQuesId.get(i));
					List<String> options = new ArrayList<String>();
					List<String> answers = new ArrayList<String>();
					while (matchOptRs.next()) {
						options.add(matchOptRs.getString("optionText"));
						answers.add(matchOptRs.getString("matchingText").toLowerCase());
					}
					qs.setQuestionOptions(options);
					qs.setAnswerOptions(answers);
				} else {
					ResultSet answerRs = QzManager.getAnswer(allQuesId.get(i));
					while (answerRs.next()) {
						ans.add(answerRs.getString("answer").toLowerCase());	
					}
					if (allQuesType.get(i).equals("multiple-answer-ordered")) {
						qs.setNumSlot(ans.size());
					} else if (allQuesType.get(i).equals("multiple-answer-unordered")) {
						ResultSet slotRs = QzManager.getAnswerSlot(allQuesId.get(i));
						slotRs.next();
						int numSlot = Integer.parseInt(slotRs.getString("numSlot"));
						qs.setNumSlot(numSlot);
					}
				}
				qs.addAnswers(ans);
			}
			int numQuestions = currentQuiz.getNumQuestion();
			session.setAttribute("currentQuiz", currentQuiz);
			session.setAttribute("currentQuizQuestion", 1);
			session.setAttribute("currentQuizTotalQuestions", numQuestions);
			session.setAttribute("isImmediate", isImmediate);
			if (quizMode.equals("practice")) {
				session.setAttribute("isPracticeMode", "true");
			} else {
				session.setAttribute("isPracticeMode", "false");
			}
			currentQuiz.setQuestionIndexes();

				
			RequestDispatcher dispatcher = request.getRequestDispatcher("SinglePageQuizPlay.jsp");
			if (isOnePageQuiz.equals("false") || quizMode.equals("practice")) {				
				currentQuiz.setNextQuestionNumber();
				dispatcher = request.getRequestDispatcher("MultiplePageQuizPlay.jsp");
			}
			long start = System.currentTimeMillis();
			session.setAttribute("startTime", start);
			dispatcher.forward(request, response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
