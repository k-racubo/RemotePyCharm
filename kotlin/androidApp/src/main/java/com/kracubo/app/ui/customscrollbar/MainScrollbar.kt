package com.kracubo.app.ui.customscrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

//The main function scrollbar, use in ui/screens/mainmenu/MainScreen.kt
@Composable
fun scrollbar() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 30.dp, end = 30.dp, top = 10.dp, bottom = 10.dp)
            .border(
                width = 2.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(15.dp)
            ).clip(RoundedCornerShape(15.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF323232))
                .verticalScrollWithScrollbar(
                    rememberScrollState(),
                    rememberScrollbarState(),
                    scrollbarConfig = ScrollbarConfig(
                        padding = PaddingValues(end = 10.dp, top = 10.dp, bottom = 10.dp),
                        indicatorColor = ColorType.Solid(MaterialTheme.colorScheme.onSecondaryContainer),
                        indicatorThickness = 16.dp,
                        barThickness = 0.dp,
                        indicatorPadding = PaddingValues(2.dp),
                        barColor = ColorType.Solid(MaterialTheme.colorScheme.onSecondaryContainer)
                    )
                ).padding(horizontal = 16.dp)
        ) {
            Text("MIT License")
            Text("Copyright (c) 2025 k-racubo")
            Text("Permission is hereby granted, free of charge, to any person obtaining a copy")
            Text("of this software and associated documentation files (the ), to deal")
            Text("in the Software without restriction, including without limitation the rights")
            Text("to use, copy, modify, merge, publish, distribute, sublicense, and/or sell")
            Text("copies of the Software, and to permit persons to whom the Software is")
            Text("furnished to do so, subject to the following conditions:")

            Text("The above copyright notice and this permission notice shall be included in all")
            Text("copies or substantial portions of the Software.")

            Text("THE SOFTWARE IS PROVIDED , WITHOUT WARRANTY OF ANY KIND, EXPRESS OR")
            Text("IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,")
            Text("FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE")
            Text("AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER")
            Text("LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,")
            Text("OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE")
            Text("SOFTWARE.")
        }
    }
}