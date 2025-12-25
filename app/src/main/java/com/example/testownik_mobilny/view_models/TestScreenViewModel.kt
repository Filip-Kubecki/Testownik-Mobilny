package com.example.testownik_mobilny.view_models

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testownik_mobilny.logic.TestInfo
import com.example.testownik_mobilny.logic.Question
import com.example.testownik_mobilny.logic.QuestionDatabase
import com.example.testownik_mobilny.logic.TestInfoManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
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
    var questions: List<Question> = mutableStateListOf()
        private set
    var testInformation by mutableStateOf(TestInfo())
        private set
//    Values used for each question
    var toggledButtons = mutableStateListOf<ToggleState>()
        private set
    var currentQuestion by mutableStateOf(Question(-999, "", listOf(), listOf()))
        private set

    var questionContentImageVisibility by mutableStateOf( true )
        private set

    var shuffledAnswerIndices by mutableStateOf(listOf<Int>())
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
    private lateinit var databaseDirectory: File

    var timeElapsed by mutableIntStateOf(0)

    private var lastQuestionIndex: Int = -999


//    LOGIC ----------------------------------------
    fun init(database: QuestionDatabase){
        databaseDirectory = database.directory!!
        questions = database.questions

        viewModelScope.launch(Dispatchers.IO) {
            val savedProgress = TestInfoManager.loadTestInfo(databaseDirectory)

            withContext(Dispatchers.Main) {
                if (savedProgress != null) {
                    testInformation = savedProgress
                } else {
                    val freshInfo = TestInfo(
                        name = database.name,
                        numberOfQuestions = database.numberOfQuestions
                    )
                    repeat(database.numberOfQuestions) { freshInfo.unvisitedQuestions.add(it) }
                    testInformation = freshInfo
                }

                timeElapsed = testInformation.timeSpent
                startTimer()

                nextRandomQuestion()
                initButtonStates()
            }
        }
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
        // Check if the test is finished before picking a question
        if (testInformation.unvisitedQuestions.isEmpty() && testInformation.answeredQuestions.isEmpty()) {
            testIsDone()
            return
        }

        val unvisitedPool = testInformation.unvisitedQuestions.filter { it != lastQuestionIndex }
        val answeredPool = testInformation.answeredQuestions.filter { it != lastQuestionIndex }

        val nextIndex = when {
            // 15% chance for Answered (if pool is not empty)
            Random.nextInt(100) > 85 && answeredPool.isNotEmpty() -> answeredPool.random()

            // 85% chance for Unvisited (if pool is not empty)
            unvisitedPool.isNotEmpty() -> unvisitedPool.random()

            // Emergency Fallback: If filtered pools are empty, use the unfiltered ones
            testInformation.unvisitedQuestions.isNotEmpty() -> testInformation.unvisitedQuestions.random()
            else -> testInformation.answeredQuestions.random()
        }

        lastQuestionIndex = nextIndex
        currentQuestionIndex = nextIndex
        currentQuestion = questions[currentQuestionIndex]

        shuffleAnswers()

        Log.d("SELF", "Next question: ${currentQuestion.id}. ${currentQuestion.question}")
    }

    private fun shuffleAnswers() {
        shuffledAnswerIndices = currentQuestion.answers.indices.shuffled()
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

//        Saves testInfo data to json file
        saveProgress()
    }

    fun resetContentImageVisibility() {
        questionContentImageVisibility = true
    }

    fun changeContentImageVisibility(){
        questionContentImageVisibility = !questionContentImageVisibility
    }

    private fun testIsDone(){
        finishedScreenState = true
        saveProgress()
        Log.d("SELF", "Test Completed Successfully")
    }

    private fun saveProgress() {
        viewModelScope.launch(Dispatchers.IO) {
            TestInfoManager.saveTestInfo(databaseDirectory, testInformation)
        }
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (true) {
                delay(1000L)
                timeElapsed++
                testInformation.timeSpent = timeElapsed
            }
        }
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
