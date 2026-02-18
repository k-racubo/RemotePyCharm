package com.kracubo.app.ui.screens.codeeditor.editor.components

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Terminalscreen(isTerminal: Boolean, showTerminal: Boolean, isBlinkingTerminal: String){
    //кароч мои попытки вынести этот терминал в функцию,
    // пока что безуспешно
    //я устал уже и не хочу думать как состояния передать в качестве аргумента и обратиться к ним с целью его изменить
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(Color(0xFF242425))
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Gray
        )
        Column(Modifier.fillMaxWidth().height(25.dp).padding(top = 1.dp).clickable(onClick = {showTerminal = !showTerminal})) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Arrow Down",
                tint = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.Gray
            )
        }
        Spacer(Modifier.height(4.dp))
        Column(Modifier.fillMaxWidth().fillMaxHeight(0.80f).padding(top = 31.dp)){
            Text(
                text = "(.venv) user@Users-MacBook-Air\nProjectFolder\n% ",
                color = Color.White,
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp
            )
            Text(
                text = isBlinkingTerminal,
                color = Color.White,
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp
            )
        }
    }
}