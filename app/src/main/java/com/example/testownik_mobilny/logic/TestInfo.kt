package com.example.testownik_mobilny.logic

import kotlinx.serialization.Serializable


/**
 * Holds information about test database and about progress in learning
 * @param name name of the current test database
 * @param numberOfQuestions number of all questions (answered, memorized and unvisited)
 * @param memorizedQuestions indexes of memorized questions (memorized question is one that will not appear again in test)
 * @param answeredQuestions indexes of questions that have been answered at least once
 * @param unvisitedQuestions indexes of questions that weren't answered since first test run
 * @param timeSpent The duration of time that has passed since the test started.
 * This value increases only while the test is actively running, and pauses when the test is inactive.
 */
@Serializable
data class TestInfo(
    var name: String = "",
    var numberOfQuestions: Int = 0,
    var memorizedQuestions: MutableList<Int> = mutableListOf(),
    var answeredQuestions: MutableList<Int> = mutableListOf(),
    var unvisitedQuestions: MutableList<Int> = mutableListOf(),
    var timeSpent: Int = 0,
){
    override fun toString(): String {
        return "Database: $name, Fully memorized questions: ${memorizedQuestions.size}\n" +
                "Questions answered at least once in current run: ${answeredQuestions.size}\n" +
                "All questions: $numberOfQuestions"
    }
}