package com.example.testownik_mobilny.TestLogic

data class QuestionDatabase(
    val name: String,
    val numberOfQuestions: Int,
    val questions: List<Question>,
){
    override fun toString(): String {
        return "Name: $name, number of questions: $numberOfQuestions"
    }
}

data class Question(
    val id: Int,
//    Content of question
    val question: String,
//    List of all answers - correct and wrong ones
    val answers: List<String>,
//    List containing indexes of only correct answers
    val correctAnswers: List<Boolean>
){
    override fun toString(): String {
        return "Question: $question\n ${answers.joinToString("\n")}"
    }
}