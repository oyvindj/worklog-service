package oj.beans

import groovy.transform.ToString

@ToString
class Question {
    String id
    String userId
    String quizId
    String question
    String answer1
    String answer2
    String answer3
    Integer correctAnswer
}
