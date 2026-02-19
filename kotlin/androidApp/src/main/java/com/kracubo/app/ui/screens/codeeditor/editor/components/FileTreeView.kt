package com.kracubo.app.ui.screens.codeeditor.editor.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kracubo.app.core.viewmodels.codeditor.FileNode
import com.kracubo.app.core.viewmodels.codeditor.FileType

@Composable
fun FileTreeView(
    node: FileNode,
    level: Int = 0,
    onFileSelected: (String, String) -> Unit
) {
    var expanded by remember { mutableStateOf(level == 0) }

    Column(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures { }
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = (level * 20).dp)
                .clickable {
                    when (node.type) {
                        FileType.FOLDER -> expanded = !expanded
                        FileType.FILE -> onFileSelected(node.name, node.path.substringAfter("/"))
                    }
                }
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            Icon(
                imageVector = when (node.type) {
                    FileType.FOLDER -> if (expanded) Icons.Default.PlayArrow else Icons.Default.PlayArrow
                    FileType.FILE -> Icons.Default.Menu
                },
                contentDescription = null,
                tint = when (node.type) {
                    FileType.FOLDER -> Color(0xFF64B5F6)
                    FileType.FILE -> Color.White.copy(alpha = 0.7f)
                },
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = node.name,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }

        if (expanded && node.type == FileType.FOLDER && !node.children.isNullOrEmpty()) {
            node.children.forEach { child ->
                FileTreeView(
                    node = child,
                    level = level + 1,
                    onFileSelected = onFileSelected
                )
            }
        }
    }
}