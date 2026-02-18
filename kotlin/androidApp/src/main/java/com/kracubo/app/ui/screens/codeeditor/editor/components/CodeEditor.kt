package com.kracubo.app.ui.screens.codeeditor.editor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import com.kracubo.app.ui.screens.codeeditor.editor.components.highlightPython

@Composable
fun CodeEditor(
    modifier: Modifier = Modifier,
    code: String,
    onCodeChange: (String) -> Unit
) {
    val lines = code.lines()
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(CodeColors.Background)
    ) {
        // Номера строк
        Column(
            modifier = Modifier
                .width(50.dp)
                .background(CodeColors.LineNumberBackground)
                .padding(vertical = 8.dp, horizontal = 8.dp),
            horizontalAlignment = End
        ) {
            lines.forEachIndexed { index, _ ->
                Text(
                    text = "${index + 1}",
                    color = CodeColors.LineNumberText,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }

        // Поле ввода с подсветкой
        BasicTextField(
            value = code,
            onValueChange = { newText ->
                val smartText = handleSmartInput(code, newText)
                onCodeChange(smartText)
            },
            textStyle = TextStyle(
                color = CodeColors.Default, // Белый текст по умолчанию
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace,
                lineHeight = 20.sp
            ),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
            decorationBox = { innerTextField ->
                // Отображаем подсвеченный текст как фон
                Text(
                    text = highlightPython(code),
                    modifier = Modifier.fillMaxSize(),
                    style = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                )
                // Поле ввода поверх подсветки
                innerTextField()
            }
        )
    }
}

