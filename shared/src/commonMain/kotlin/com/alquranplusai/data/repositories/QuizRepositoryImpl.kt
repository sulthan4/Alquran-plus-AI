package com.alquranplusai.data.repositories

import com.alquranplusai.data.database.AlQuranDatabaseWrapper
import com.alquranplusai.data.quiz.QuizQuestionGenerator
import com.alquranplusai.domain.models.*
import com.alquranplusai.domain.repositories.QuizRepository
import com.alquranplusai.domain.repositories.QuranRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class QuizRepositoryImpl(
    private val database: AlQuranDatabaseWrapper,
    private val quranRepository: QuranRepository
) : QuizRepository {
    
    private val questionGenerator = QuizQuestionGenerator(quranRepository)
    private val activeSessions = mutableMapOf<String, QuizSession>()
    
    // Quiz operations
    override suspend fun getAllQuizzes(): Flow<List<Quiz>> = flow {
        val quizzes = database.quizQueries.selectAllQuizzes().executeAsList().map { entity ->
            Quiz(
                id = entity.id,
                title = entity.title,
                description = entity.description ?: "",
                category = QuizCategory.valueOf(entity.category),
                difficulty = QuizDifficulty.valueOf(entity.difficulty),
                questionCount = entity.questionCount.toInt(),
                timeLimit = entity.timeLimit?.toInt(),
                passingScore = entity.passingScore.toInt(),
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
            )
        }
        emit(quizzes)
    }

    override suspend fun getQuizById(id: String): Flow<Quiz?> = flow {
        val entity = database.quizQueries.selectQuizById(id).executeAsOneOrNull()
        emit(entity?.let {
            Quiz(
                id = it.id,
                title = it.title,
                description = it.description ?: "",
                category = QuizCategory.valueOf(it.category),
                difficulty = QuizDifficulty.valueOf(it.difficulty),
                questionCount = it.questionCount.toInt(),
                timeLimit = it.timeLimit?.toInt(),
                passingScore = it.passingScore.toInt(),
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        })
    }

    override suspend fun getQuizzesByCategory(category: QuizCategory): Flow<List<Quiz>> = flow {
        val quizzes = database.quizQueries.selectQuizzesByCategory(category.name).executeAsList().map { entity ->
            Quiz(
                id = entity.id,
                title = entity.title,
                description = entity.description ?: "",
                category = QuizCategory.valueOf(entity.category),
                difficulty = QuizDifficulty.valueOf(entity.difficulty),
                questionCount = entity.questionCount.toInt(),
                timeLimit = entity.timeLimit?.toInt(),
                passingScore = entity.passingScore.toInt(),
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
            )
        }
        emit(quizzes)
    }

    override suspend fun getQuizzesByDifficulty(difficulty: QuizDifficulty): Flow<List<Quiz>> = flow {
        val quizzes = database.quizQueries.selectQuizzesByDifficulty(difficulty.name).executeAsList().map { entity ->
            Quiz(
                id = entity.id,
                title = entity.title,
                description = entity.description ?: "",
                category = QuizCategory.valueOf(entity.category),
                difficulty = QuizDifficulty.valueOf(entity.difficulty),
                questionCount = entity.questionCount.toInt(),
                timeLimit = entity.timeLimit?.toInt(),
                passingScore = entity.passingScore.toInt(),
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
            )
        }
        emit(quizzes)
    }

    override suspend fun generateQuiz(
        category: QuizCategory,
        difficulty: QuizDifficulty,
        questionCount: Int
    ): Flow<Quiz> = flow {
        val quizId = "generated_${Clock.System.now().toEpochMilliseconds()}"
        val now = Clock.System.now().toEpochMilliseconds()
        
        // Generate questions using the question generator
        val questions = questionGenerator.generateQuestions(category, difficulty, questionCount)
        
        // Create quiz
        val quiz = Quiz(
            id = quizId,
            title = "${category.name} Quiz - ${difficulty.name}",
            description = "Auto-generated $questionCount question quiz",
            category = category,
            difficulty = difficulty,
            questionCount = questions.size,
            timeLimit = questions.sumOf { it.timeLimit ?: 30 },
            passingScore = 70,
            createdAt = now,
            updatedAt = now
        )
        
        // Store quiz in database
        database.quizQueries.insertQuiz(
            id = quiz.id,
            title = quiz.title,
            description = quiz.description,
            category = quiz.category.name,
            difficulty = quiz.difficulty.name,
            questionCount = quiz.questionCount.toLong(),
            timeLimit = quiz.timeLimit?.toLong(),
            passingScore = quiz.passingScore.toLong(),
            createdAt = quiz.createdAt,
            updatedAt = quiz.updatedAt,
            attemptCount = 0,
            averageScore = 0.0,
            isPublished = 1
        )
        
        // Store questions
        questions.forEachIndexed { index, question ->
            database.questionQueries.insertQuestion(
                id = question.id,
                quizId = quiz.id,
                type = question.type.name,
                question = question.question,
                surahNumber = question.surahNumber?.toLong(),
                ayahNumber = question.ayahNumber?.toLong(),
                correctAnswer = question.correctAnswer,
                explanation = question.explanation,
                hint = null,
                points = question.points.toLong(),
                timeLimit = question.timeLimit?.toLong(),
                difficulty = quiz.difficulty.name,
                position = index.toLong()
            )
            
            // Insert options
            question.options.forEachIndexed { optIndex, optionText ->
                database.questionQueries.insertQuestionOption(
                    questionId = question.id,
                    optionText = optionText,
                    position = optIndex.toLong()
                )
            }
        }
        
        emit(quiz)
    }

    override suspend fun getDailyChallenge(): Flow<DailyChallenge?> = flow {
        val now = Clock.System.now()
        val localDate = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val dateString = localDate.toString()
        
        // Check if we already have a daily challenge for today
        val existingQuizzes = getAllQuizzes().first()
        val todayChallenge = existingQuizzes.find { it.title.contains("Daily Challenge $dateString") }
        
        val quiz = todayChallenge ?: run {
            // Generate new daily challenge
            generateQuiz(
                category = QuizCategory.values().random(),
                difficulty = QuizDifficulty.MEDIUM,
                questionCount = 10
            ).first()
        }
        
        emit(DailyChallenge(
            id = "daily_challenge_$dateString",
            date = dateString,
            quizId = quiz.id,
            title = "Daily Challenge - $dateString",
            description = quiz.description ?: ""
        ))
    }

    override suspend fun getCompletedQuizzes(): Flow<List<String>> = flow {
        val results = getQuizHistory().first()
        emit(results.map { it.quizId }.distinct())
    }

    override suspend fun getQuizQuestions(quizId: String): Flow<List<Question>> = flow {
        val questions = database.questionQueries.selectQuestionsByQuiz(quizId).executeAsList().map { entity ->
            // Fetch options for each question
            val options = database.questionQueries.selectOptionsByQuestion(entity.id).executeAsList().map { it.optionText }
            
            Question(
                id = entity.id,
                quizId = entity.quizId,
                question = entity.question,
                type = QuestionType.valueOf(entity.type),
                options = options,
                correctAnswer = entity.correctAnswer,
                explanation = entity.explanation ?: "",
                points = entity.points.toInt(),
                timeLimit = entity.timeLimit?.toInt(),
                surahNumber = entity.surahNumber?.toInt(),
                ayahNumber = entity.ayahNumber?.toInt(),
                position = entity.position.toInt(),
                difficulty = QuizDifficulty.valueOf(entity.difficulty)
            )
        }
        emit(questions)
    }
    
    override suspend fun getQuestionsByQuiz(quizId: String): Flow<List<Question>> = getQuizQuestions(quizId)

    override suspend fun startQuizSession(quizId: String): Flow<String> = flow {
        val sessionId = "session_${Clock.System.now().toEpochMilliseconds()}"
        val questions = getQuizQuestions(quizId).first()
        
        activeSessions[sessionId] = QuizSession(
            sessionId = sessionId,
            quizId = quizId,
            startTime = Clock.System.now().toEpochMilliseconds(),
            answers = mutableMapOf(),
            questions = questions
        )
        
        emit(sessionId)
    }
    
    override suspend fun submitAnswer(sessionId: String, questionId: String, answerId: String) {
        activeSessions[sessionId]?.let { session ->
            session.answers[questionId] = answerId
        }
    }
    
    override suspend fun endQuizSession(sessionId: String): Flow<QuizResult> = flow {
        val session = activeSessions[sessionId] ?: run {
            emit(QuizResult(
                attemptId = "0",
                quizId = "0",
                score = 0,
                percentage = 0f,
                correctCount = 0,
                wrongCount = 0,
                skippedCount = 0,
                totalQuestions = 0,
                timeSpent = 0,
                isPassed = false
            ))
            return@flow
        }
        
        val endTime = Clock.System.now().toEpochMilliseconds()
        val timeSpent = ((endTime - session.startTime) / 1000) // in seconds
        
        var correctCount = 0
        var wrongCount = 0
        
        session.questions.forEach { question ->
            val userAnswer = session.answers[question.id]
            when {
                userAnswer == null -> {} // skipped
                userAnswer == question.correctAnswer -> correctCount++
                else -> wrongCount++
            }
        }
        
        val skippedCount = session.questions.size - correctCount - wrongCount
        val totalQuestions = session.questions.size
        val score = session.questions.filter { session.answers[it.id] == it.correctAnswer }.sumOf { it.points }
        val percentage = if (totalQuestions > 0) (correctCount.toFloat() / totalQuestions * 100) else 0f
        val isPassed = percentage >= 70f
        
        val result = QuizResult(
            attemptId = "attempt_${Clock.System.now().toEpochMilliseconds()}",
            quizId = session.quizId,
            score = score,
            percentage = percentage,
            correctCount = correctCount,
            wrongCount = wrongCount,
            skippedCount = skippedCount,
            totalQuestions = totalQuestions,
            timeSpent = timeSpent,
            isPassed = isPassed
        )
        
        // Store result in database
        database.quizResultQueries.insertQuizResult(
            id = result.attemptId,
            quizId = result.quizId,
            userId = "default_user",
            startedAt = session.startTime,
            completedAt = endTime,
            score = result.score.toLong(),
            totalPoints = result.totalQuestions.toLong(),
            correctAnswers = result.correctCount.toLong(),
            wrongAnswers = result.wrongCount.toLong(),
            skippedAnswers = result.skippedCount.toLong(),
            timeSpent = result.timeSpent,
            isPassed = if (result.isPassed) 1 else 0
        )
        
        // Clean up session
        activeSessions.remove(sessionId)
        
        emit(result)
    }
    
    override suspend fun submitQuizResult(quizId: String, score: Int, totalQuestions: Int) {
        val now = Clock.System.now().toEpochMilliseconds()
        val percentage = if (totalQuestions > 0) (score.toFloat() / totalQuestions * 100) else 0f
        
        database.quizResultQueries.insertQuizResult(
            id = "result_$now",
            quizId = quizId,
            userId = "default_user",
            startedAt = now,
            completedAt = now,
            score = score.toLong(),
            totalPoints = totalQuestions.toLong(),
            correctAnswers = score.toLong(),
            wrongAnswers = (totalQuestions - score).toLong(),
            skippedAnswers = 0,
            timeSpent = 0,
            isPassed = if (percentage >= 70) 1 else 0
        )
    }

    override suspend fun getQuizHistory(): Flow<List<QuizResult>> = flow {
        val results = database.quizResultQueries.selectUserQuizResults("default_user").executeAsList().map { entity ->
            QuizResult(
                attemptId = entity.id,
                quizId = entity.quizId,
                score = entity.score.toInt(),
                percentage = (entity.score.toFloat() / entity.totalPoints.toFloat()) * 100f,
                correctCount = entity.correctAnswers.toInt(),
                wrongCount = entity.wrongAnswers.toInt(),
                skippedCount = entity.skippedAnswers.toInt(),
                totalQuestions = entity.totalPoints.toInt(),
                timeSpent = entity.timeSpent,
                isPassed = entity.isPassed == 1L
            )
        }
        emit(results)
    }
    
    override suspend fun getQuizResults(): Flow<List<QuizResult>> = getQuizHistory()
    
    override suspend fun getQuizResultById(id: String): Flow<QuizResult?> = flow {
        emit(database.quizResultQueries.selectQuizResult(id).executeAsOneOrNull()?.let { entity ->
            QuizResult(
                attemptId = entity.id,
                quizId = entity.quizId,
                score = entity.score.toInt(),
                percentage = (entity.score.toFloat() / entity.totalPoints.toFloat()) * 100f,
                correctCount = entity.correctAnswers.toInt(),
                wrongCount = entity.wrongAnswers.toInt(),
                skippedCount = entity.skippedAnswers.toInt(),
                totalQuestions = entity.totalPoints.toInt(),
                timeSpent = entity.timeSpent,
                isPassed = entity.isPassed == 1L
            )
        })
    }
    
    override suspend fun deleteQuizResult(id: String) {
        database.quizResultQueries.deleteQuizResult(id)
    }
}

/**
 * Internal model for active quiz sessions
 */
private data class QuizSession(
    val sessionId: String,
    val quizId: String,
    val startTime: Long,
    val answers: MutableMap<String, String>,
    val questions: List<Question>
)
