package com.kracubo.app.core.viewmodels.codeditor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.kracubo.app.core.networking.handlers.Handler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class CodeEditorViewModel : BaseViewModel() {
    var onProjectDownOnServer by mutableStateOf(false)

    private val _projectTree = MutableStateFlow<FileNode?>(null)

    var isProjectRunning by mutableStateOf(false)

    val projectTree = _projectTree.asStateFlow()

    init {
        Handler.setCurrentViewmodel(this)
    }

    fun onProjectClosedOnServer() { onProjectDownOnServer = true }

    fun updateProjectTree(fileTree: JsonObject) {
        val projectName = fileTree.keys.firstOrNull() ?: "Project"
        val projectData = fileTree[projectName]?.jsonObject

        if (projectData != null) {
            val rootNode = parseFileTree(projectName, projectData)
            _projectTree.value = rootNode
        }
    }

    private fun parseFileTree(name: String, node: JsonObject): FileNode {
        val type = node["type"]?.jsonPrimitive?.content

        return if (type == "dir") {
            // Это папка - парсим детей
            val children = node["children"]?.jsonObject?.mapValues { (childName, childValue) ->
                parseFileTree(childName, childValue.jsonObject)
            } ?: emptyMap()

            FileNode(
                name = name,
                path = name, // путь накапливается при рендере
                type = FileType.FOLDER,
                children = children.values.toList()
            )
        } else {
            // Это файл
            FileNode(
                name = name,
                path = name,
                type = FileType.FILE,
                children = null
            )
        }
    }

    fun runProject() {
        viewModelScope.launch {
            Handler.runProject()
            isProjectRunning = true
        }
    }

    fun stopProject() {
        viewModelScope.launch {
            Handler.stopProject()
            isProjectRunning = false
        }
    }

    override fun onCleared() {
        Handler.clearCurrentViewmodel()
    }
}

data class FileNode(
    val name: String,
    val path: String,
    val type: FileType,
    val children: List<FileNode>? = null
)

enum class FileType {
    FILE,
    FOLDER
}