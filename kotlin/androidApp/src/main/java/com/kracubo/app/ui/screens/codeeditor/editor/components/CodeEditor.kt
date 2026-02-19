package com.kracubo.app.ui.screens.codeeditor.editor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign

@Composable
fun CodeEditor(
    modifier: Modifier = Modifier,
    code: String,
) {
    val lines = code.lines()
    val scrollState = rememberScrollState()
    val horizontalScroll = rememberScrollState()
    val lineScrollState = rememberScrollState()

    val fontSize = 14.sp
    val lineHeight = 20.sp

    val lineHeightDp = with(LocalDensity.current) { lineHeight.toDp() }

    // 🟢 Синхронизация скролла
    LaunchedEffect(scrollState.value) {
        lineScrollState.scrollTo(scrollState.value)
    }

    LaunchedEffect(lineScrollState.value) {
        scrollState.scrollTo(lineScrollState.value)
    }

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(end = 8.dp)
                .verticalScroll(lineScrollState, enabled = true),
            horizontalAlignment = Alignment.End
        ) {
            lines.forEachIndexed { index, _ ->
                Text(
                    text = "${index + 1}",
                    color = Color(0xFF606366),
                    fontSize = fontSize,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .height(lineHeightDp)
                )
            }

            Spacer(modifier = Modifier.height(lineHeightDp * 4))
        }

        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(Color(0xFF3C3F41))
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(horizontalScroll)
                .verticalScroll(scrollState)
        ) {
            Column {
                Text(
                    text = highlightKotlin(code),
                    modifier = Modifier.fillMaxWidth(),
                    softWrap = false,
                    style = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = fontSize,
                        color = Color(0xFFA9B7C6),
                        lineHeight = lineHeight
                    )
                )

                Spacer(modifier = Modifier.height(lineHeightDp * 4))
            }
        }
    }
}

