package com.kracubo.core.project

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.VirtualFile
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Service(Service.Level.PROJECT)
class ProjectStructureProvider(val project: Project) {

    fun buildTree() : JsonObject {
        val root = project.guessProjectDir() ?: return buildJsonObject {}

        val rootNode = walk(root)

        return buildJsonObject { if (rootNode != null) { put(root.name, rootNode) } }
    }


    private fun walk(file: VirtualFile): JsonElement? {
        val fileIndex = ProjectFileIndex.getInstance(project)

        if (fileIndex.isExcluded(file) || fileIndex.isUnderIgnored(file)) { return null }

        return buildJsonObject {
            put("type", if (file.isDirectory) "dir" else "file")

            if (file.isDirectory) {
                val childrenJson = buildJsonObject {
                    file.children
                        .filter { !it.name.startsWith(".") }
                        .forEach { child ->
                            walk(child)?.let { put(child.name, it) }
                        }
                }
                put("children", childrenJson)
            }
        }
    }
}