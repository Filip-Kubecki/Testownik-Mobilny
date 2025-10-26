package com.example.testownik_mobilny.view_models

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.testownik_mobilny.logic.Question
import com.example.testownik_mobilny.logic.QuestionDatabase
import kotlin.random.Random

/**
 * Holds test logic, states and variables.
 *
 * [questions] contains list off all questions in the database (each element is of type [Question])
 *
 * [testInformation] holds information about test database and about progress in learning
 *
 * [toggledButtons] holds state of button (if it was selected, correct, wrong, etc.)
 *
 * [currentQuestion]
 */
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

//    Two states 0 for fade in, 1 for fade out
//    var screenState by mutableStateOf(0)
//        private set

    var questionContentImageVisibility by mutableStateOf( true )
        private set

    var finishedScreenState by mutableStateOf(false)
        private set

    private var currentQuestionIndex by mutableIntStateOf(0)
    private var mistakeCounter by mutableIntStateOf(0)
// TODO: Change how test works. Count how many times question was answered
//    correctly and with each correct answer add point to counter and with each
//    wrong answer subtract from counter. If the counter achieves set value (eg 3)
//    test marks question as memorized.
/**
 * How does the test work?
 *
 * There are three lists containing different types of questions:
 *
 * - **unvisitedQuestions** – questions that have never appeared or were answered incorrectly before.
 * - **answeredQuestions** – questions that have appeared before and were answered correctly once.
 * - **memorizedQuestions** – questions that were answered correctly twice in a row.
 *
 * When you answer a question correctly, it first moves to the *answeredQuestions* list.
 * After a second correct answer, it moves to the *memorizedQuestions* list.
 * If a question is answered incorrectly at any stage, it returns to the *unvisitedQuestions* list.
 *
 * If both the *unvisitedQuestions* and *answeredQuestions* lists are empty,
 * it means all questions have been memorized and the test ends with the finish screen.
 */
//    Logic
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

    fun initButtonStates(){
        toggledButtons.clear()
        repeat(currentQuestion.answers.size) {
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

    fun nextRandomQuestion(){
        currentQuestionIndex = (
        if (Random.nextInt(100) > 15 && testInformation.answeredQuestions.isNotEmpty()) {
    //            15% chance to get question from answeredQuestions
            testInformation.answeredQuestions.random()
        } else if(testInformation.unvisitedQuestions.isNotEmpty()){
    //            85% chance to get question from unvisitedQuestions
            testInformation.unvisitedQuestions.random()
        } else if(testInformation.answeredQuestions.isNotEmpty()) {
//            When there is no questions in unvisitedQuestions
            testInformation.answeredQuestions.random()
        } else {

        }) as Int

        currentQuestion = questions[currentQuestionIndex]
        Log.d("SELF", "Next question: ${currentQuestion.id}. ${currentQuestion.question}")
    }

    fun checkAnswers(){
//        Log.d("SELF", "CHECK STATUS")
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
//            Correct answer
            if (testInformation.unvisitedQuestions.contains(currentQuestionIndex)){
                testInformation.unvisitedQuestions.remove(currentQuestionIndex)
                testInformation.answeredQuestions.add(currentQuestionIndex)
            }else if (testInformation.answeredQuestions.contains(currentQuestionIndex)){
                testInformation.answeredQuestions.remove(currentQuestionIndex)
                testInformation.memorizedQuestions.add(currentQuestionIndex)
            }
        }else{
            Log.d("SELF", "Remove on wrong answer")
//            Wrong answer
            if (testInformation.answeredQuestions.contains(currentQuestionIndex)) {
                Log.d("SELF", "Remove on wrong answer")
                Log.d("SELF", "Before: ${testInformation.unvisitedQuestions.count()}")
                testInformation.answeredQuestions.remove(currentQuestionIndex)
                Log.d("SELF", "After: ${testInformation.unvisitedQuestions.count()}")
                testInformation.unvisitedQuestions.add(currentQuestionIndex)
            }
        }


        Log.d(
            "SELF",
            "Un: ${testInformation.unvisitedQuestions.count()} " +
            "Ans: ${testInformation.answeredQuestions.count()} " +
            "Mem: ${testInformation.memorizedQuestions.count()}"
        )

//        Checks for ending state
        if (testInformation.unvisitedQuestions.isEmpty() &&
            testInformation.answeredQuestions.isEmpty()
        ){
            Log.d("SELF", "YOU WON YAY")
            testIsDone()
            return
        }

//        Go to next question
        mistakeCounter = 0
        nextRandomQuestion()
        initButtonStates()
    }

    fun resetContentImageVisibility() {
        questionContentImageVisibility = true
    }

    fun changeContentImageVisibility(){
        questionContentImageVisibility = !questionContentImageVisibility
    }
    private fun testIsDone(){
        finishedScreenState = true

    }
}

/**
 * Represents possible states of buttons used for answering
 */
enum class ToggleState{
/** BEFORE CHECKING ANSWERS
 *
 *  default state - GRAY
 **/
    IDLE,
    /** after selecting button - BLUE */
    TOGGLED,
/** AFTER CHECKING THE ANSWERS
 *
 *  TOGGLED answer was correct - GREEN
 */
    CORRECT,
/** TOGGLED answer was wrong - RED */
    WRONG,
    /** answer was correct but wasn't selected - YELLOW */
    UNMARKED,
    /** unselected question that wasn't correct - GRAY */
    DISABLED
}

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
data class TestInfo(
    var name: String = "",
    var numberOfQuestions: Int = 0,
    var memorizedQuestions: MutableList<Int> = mutableListOf(),
    var answeredQuestions: MutableList<Int> = mutableListOf(),
    var unvisitedQuestions: MutableList<Int> = mutableListOf(),
    var timeSpent: Int = 0,
){
    override fun toString(): String {
        return "Database: $name, Fully memorized questions: ${memorizedQuestions.size} \n" +
                "Questions answered at least once in current run: ${answeredQuestions.size}"
    }
}