package oj

import oj.beans.Question
import oj.beans.Quiz
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component

@Component
interface QuestionDB extends MongoRepository<Question, String> {
    List<Question> findByQuizId(String quizId)
}
@Component
interface QuizDB extends MongoRepository<Quiz, String> {
}
