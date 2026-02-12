package com.kracubo.app.ui.screens.codeeditor

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun CodeEditorScreen(
    fileName: String = "MainActivity.kt",
    initialCode: String? = null,
    onBack: () -> Unit = {}
) {
    var code by remember {
        mutableStateOf(
            initialCode ?: """
            package com.example.myapp

            import android.os.Bundle
            import androidx.appcompat.app.AppCompatActivity
            import kotlinx.coroutines.*

            class MainActivity : AppCompatActivity() {

                override fun onCreate(savedInstanceState: Bundle?) {
                    super.onCreate(savedInstanceState)
                    setContentView(R.layout.activity_main)
                    
                    // val button: Button = findViewById(R.id.myButton)
                    val button: Button = findViewById(R.id.myButton)
                    button.setOnClickListener {
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(1000L)
                            showToast("Button clicked!")
                        }
                    }
                }
            }
            """.trimIndent()
        )
    }

    Scaffold(
        topBar = {
            EditorTopBar(
                fileName = fileName,
                onSearchClick = { /* TODO: Implement search */ },
                onRunClick = { /* TODO: Implement run */ },
                onMenuClick = { /* TODO: Implement menu */ }
            )
        },
        bottomBar = {
            EditorBottomBar(
                onInsert = { insert ->
                    code += insert
                }
            )
        }
    ) { padding ->
        CodeEditor(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            code = code,
            onCodeChange = { code = it }
        )
    }
}

