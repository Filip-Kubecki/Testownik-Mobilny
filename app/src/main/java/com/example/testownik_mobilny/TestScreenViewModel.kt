package com.example.testownik_mobilny

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.testownik_mobilny.TestLogic.Question
import com.example.testownik_mobilny.TestLogic.QuestionDatabase
import kotlin.random.Random

class TestScreenViewModel: ViewModel() {
//    Values used for whole database
    var questions: List<Question> = mutableStateListOf<Question>()
        private set

    var testInformation: TestInfo = TestInfo()
        private set

//    Values used for each question
    var toggledButtons = mutableStateListOf<ToggleState>()
        private set

    var currentQuestion by mutableStateOf(Question(-999, "", listOf(), listOf()))
        private set

    private var currentQuestionIndex by mutableStateOf(0)
    private var mistakeCounter by mutableStateOf(0)

    fun initButtonStates(){
        toggledButtons.clear()
        repeat(currentQuestion.answers.size) { it ->
            toggledButtons.add(ToggleState.IDLE)
        }
    }

    fun toggleButton(index: Int){
        if(toggledButtons[index] == ToggleState.IDLE) {
            toggledButtons[index] = ToggleState.TOGGLED
        }else if(toggledButtons[index] == ToggleState.TOGGLED) {
            toggledButtons[index] = ToggleState.IDLE
        }
    }

    fun init(database: QuestionDatabase){
//        Pass database from outside
        questions = database.questions

        Log.d("SELF", "Test Initialization ${database.name}")
//        Test info initialization
        testInformation.name = database.name
        testInformation.numberOfQuestions = database.numberOfQuestions
        repeat(
            database.numberOfQuestions
        ) { it ->
            testInformation.unvisitedQuestions.add(it, it)
        }
//        Init current question with random value
        nextRandomQuestion()

//        Init button states
        initButtonStates()
    }

    fun nextRandomQuestion(){
        currentQuestionIndex = if (Random.nextInt(100) < 5 && testInformation.answeredQuestions.isNotEmpty()) {
//            5% chance to get question from answeredQuestions
            testInformation.answeredQuestions.random()
        } else {
//            95% chance to get question from unvisitedQuestions
            testInformation.unvisitedQuestions.random()
        }

        currentQuestion = questions[currentQuestionIndex]
        Log.d("SELF", "Next question: ${currentQuestion.id}. ${currentQuestion.question}")
    }

    fun checkAnswers(){
        Log.d("SELF", "CHECK STATUS")
        currentQuestion.correctAnswers.forEachIndexed { index, value ->
            if (value && toggledButtons[index] == ToggleState.TOGGLED){
                toggledButtons[index] = ToggleState.CORRECT
            }else if (!value && toggledButtons[index] == ToggleState.TOGGLED){
                toggledButtons[index] = ToggleState.WRONG
                mistakeCounter++
            }else if (value && toggledButtons[index] == ToggleState.IDLE){
                toggledButtons[index] = ToggleState.UNMARKED
                mistakeCounter++
            }else{
                toggledButtons[index] = ToggleState.DISABLED
            }
//            DEBUG
//            Log.d("SELF", "VALUE = $value, INDEX = $index, TOGGLE-STATE = ${toggledButtons[index].name}")
        }
    }

    fun confirmButtonCheck(){
//        Save progress
        if (mistakeCounter == 0){
            if (testInformation.unvisitedQuestions.contains(currentQuestionIndex)){
                testInformation.unvisitedQuestions.remove(currentQuestionIndex)
                testInformation.answeredQuestions.add(currentQuestionIndex)
            }else if (testInformation.answeredQuestions.contains(currentQuestionIndex)){
                testInformation.answeredQuestions.remove(currentQuestionIndex)
                testInformation.memorizedQuestions.add(currentQuestionIndex)
            }
        }

//        Go to next question
        mistakeCounter = 0
        nextRandomQuestion()
        initButtonStates()
    }
}

enum class ToggleState{
    IDLE, TOGGLED, CORRECT, WRONG, UNMARKED, DISABLED
}

data class TestInfo(
    var name: String = "",
    var numberOfQuestions: Int = 0,
    var memorizedQuestions: MutableList<Int> = mutableListOf(),  // Indexes of memorized questions
    var answeredQuestions: MutableList<Int> = mutableListOf(),   // Indexes of questions that have been answered
                                                                 // at least once in current run
    var unvisitedQuestions: MutableList<Int> = mutableListOf(),  // Questions that weren't answered in current run
    var timeSpent: Int = 0,                 // How much time has passed since the test was first opened
){
    override fun toString(): String {
        return "Database: $name, Fully memorized questions: ${memorizedQuestions.size} \n" +
                "Questions answered at least once in current run: ${answeredQuestions.size}"
    }
}