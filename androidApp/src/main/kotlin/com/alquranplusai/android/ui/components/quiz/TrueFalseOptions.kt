package com.alquranplusai.android.ui.components.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TrueFalseOptions(
    selectedOption: Boolean?,
    onOptionSelected: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        AnswerButton(
            text = "True",
            isSelected = selectedOption == true,
            onClick = { onOptionSelected(true) }
        )
        AnswerButton(
            text = "False",
            isSelected = selectedOption == false,
            onClick = { onOptionSelected(false) }
        )
    }
}
