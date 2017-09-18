package oj

import groovy.util.logging.Slf4j
import oj.beans.Question
import oj.beans.Quiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

import javax.websocket.server.PathParam
import java.security.Principal


@CrossOrigin(origins = "http://localhost:8080")
@RestController
@Slf4j
class RestService {
    @Autowired QuizDB quizDB
    @Autowired QuestionDB questionDB

    @DeleteMapping("/question")
    public void deleteQuestionsForQuiz(@RequestParam String quizId, Principal principal) {
        String userId = getUserId(principal)
        def list = questionDB.findByQuizId(quizId)
        assert (list.get(0).userId == userId)
        for (Question item : list) {
           questionDB.delete(item.id)
        }
    }
    @GetMapping("/question")
    public List<Question> getQuestionsForQuiz(@RequestParam String quizId, Principal principal) {
        String userId = getUserId(principal)
        def list = questionDB.findByQuizId(quizId)
        assert (list.get(0).userId == userId)
        return list
    }

    @PostMapping("/question")
    public Question createQuestion(@RequestBody Question question, Principal principal) {
        question.userId = getUserId(principal)
        return questionDB.save(question)
    }
    @DeleteMapping("/question/{id}")
    public void deleteQuestion(@PathVariable("id") String id, Principal principal) {
        log.info "deleting question: " + id
        questionDB.delete(id)
    }
    @DeleteMapping("/quiz")
    public void deleteQuiz(@RequestParam String id, Principal principal) {
        log.info "deleting quiz: " + id
        quizDB.delete(id)
    }

    @PostMapping("/quiz")
    public Quiz createQuiz(@RequestBody Quiz quiz, Principal principal) {
        log.info "creating quiz as user: " + getUserId(principal)
        quiz.userId = getUserId(principal)
        return quizDB.save(quiz)
    }

    @GetMapping("/quiz")
    public @ResponseBody List<Quiz> getQuizes(Principal principal) {
        return quizDB.findAll()
    }
    @GetMapping("/users")
    public @ResponseBody String getUsers(Principal principal) {
        log.info "getting users as user: " + getUserId(principal)
        return "{\"users\":[{\"firstname\":\"Richard\", \"lastname\":\"Feynman\"}," +
                "{\"firstname\":\"Marie\",\"lastname\":\"Curie\"}]}";
    }

    static String getUserId(Principal principal) {
        def userId = principal.name.split("\\|")[1]
        return userId
    }
}
