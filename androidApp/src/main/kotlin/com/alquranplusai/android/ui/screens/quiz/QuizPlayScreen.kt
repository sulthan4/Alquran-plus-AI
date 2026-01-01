package com.alquranplusai.android.ui.screens.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alquranplusai.android.ui.viewmodels.QuizPlayViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizPlayScreen(
    quizId: String,
    viewModel: QuizPlayViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onQuizComplete: (String) -> Unit
) {
    LaunchedEffect(quizId) {
        viewModel.loadQuiz(quizId)
    }
    
    val quiz by viewModel.quiz.collectAsState()
    val questions by viewModel.questions.collectAsState()
    val currentIndex by viewModel.currentQuestionIndex.collectAsState()
    val selectedAnswers by viewModel.selectedAnswers.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val submissionResult by viewModel.submissionResult.collectAsState()
    
    val timeRemaining by viewModel.timeRemaining.collectAsState()
    
    // Handle navigation on completion
    LaunchedEffect(submissionResult) {
        submissionResult?.let { result ->
            onQuizComplete(result.attemptId) 
        }
    }
    
    Scaffold(
        topBar = {
             CenterAlignedTopAppBar(
                 title = { 
                     Column(horizontalAlignment = Alignment.CenterHorizontally) {
                         Text(quiz?.title ?: "Quiz")
                         timeRemaining?.let { millis ->
                             val minutes = millis / 1000 / 60
                             val seconds = (millis / 1000) % 60
                             Text(
                                 text = String.format("%02d:%02d", minutes, seconds),
                                 style = MaterialTheme.typography.labelMedium,
                                 color = if (millis < 10000) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                             )
                         }
                     }
                 },
                 navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.Default.Close, "Close") } }
             )
        }
    ) { padding ->
         if (isLoading) {
             Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
         } else if (questions.isNotEmpty()) {
             Column(Modifier.padding(padding).padding(16.dp)) {
                 // Progress
                 LinearProgressIndicator(
                     progress = { (currentIndex + 1) / questions.size.toFloat() },
                     modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape)
                 )
                 Spacer(Modifier.height(16.dp))
                 
                 // Question Counter
                 Text(
                     "Question ${currentIndex + 1}/${questions.size}",
                     style = MaterialTheme.typography.labelLarge,
                     color = MaterialTheme.colorScheme.secondary
                 )
                 
                 Spacer(Modifier.height(24.dp))
                 
                 // Question
                 val question = questions[currentIndex]
                 Text(
                     question.question,
                     style = MaterialTheme.typography.headlineSmall,
                     fontWeight = FontWeight.Bold
                 )
                 
                 Spacer(Modifier.height(32.dp))
                 
                 // Options
                 val correctIndex = question.options.indexOf(question.correctAnswer)
                 val selectedIndex = selectedAnswers[currentIndex]
                 
                 question.options.forEachIndexed { index, option ->
                     val isSelected = selectedIndex == index
                     val isCorrect = selectedIndex != null && index == correctIndex // Show correct answer if ANY answer selected? Or only if correct selected?
                     // Standard: If answered, show correct (Green) and if wrong, show wrong (Red).
                     val showCorrect = selectedIndex != null && index == correctIndex
                     val showWrong = isSelected && index != correctIndex && selectedIndex != null

                     OptionCard(
                         text = option,
                         isSelected = isSelected,
                         isCorrect = showCorrect,
                         isWrong = showWrong,
                         onClick = { 
                             if (selectedIndex == null) { // Only allow selection if not answered yet
                                 viewModel.selectAnswer(currentIndex, index) 
                             }
                         }
                     )
                     Spacer(Modifier.height(12.dp))
                 }
                 
                 Spacer(Modifier.weight(1f))
                                  // Navigation Buttons
                  Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                      if (currentIndex > 0) {
                          OutlinedButton(onClick = { viewModel.previousQuestion() }) {
                              Text("Previous")
                          }
                      } else {
                          Spacer(Modifier.width(1.dp))
                      }
                      
                      val hasAnswered = selectedAnswers.containsKey(currentIndex)
                      
                      if (currentIndex < questions.size - 1) {
                          Button(
                              onClick = { viewModel.nextQuestion() },
                              enabled = hasAnswered
                          ) {
                              Text("Next")
                          }
                      } else {
                          val answeredCount = selectedAnswers.size
                          Button(
                              onClick = { viewModel.submitQuiz() },
                              enabled = answeredCount > 0
                          ) {
                              Text("Submit ($answeredCount/${questions.size})")
                          }
                      }
                  }
             }
         } else {
             Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                 Text("No questions found")
             }
         }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionCard(
    text: String, 
    isSelected: Boolean, 
    isCorrect: Boolean = false,
    isWrong: Boolean = false,
    onClick: () -> Unit
) {
    val containerColor = when {
        isCorrect -> androidx.compose.ui.graphics.Color(0xFFE8F5E9) // Light Green
        isWrong -> androidx.compose.ui.graphics.Color(0xFFFFEBEE) // Light Red
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surface
    }
    
    val borderColor = when {
        isCorrect -> androidx.compose.ui.graphics.Color(0xFF4CAF50) // Green
        isWrong -> androidx.compose.ui.graphics.Color(0xFFEF5350) // Red
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outlineVariant
    }
    
    // Determine icon if needed? Maybe later.

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        border = BorderStroke(if (isSelected || isCorrect || isWrong) 2.dp else 1.dp, borderColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 1.dp
        )
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = borderColor,
                    unselectedColor = borderColor
                )
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}
