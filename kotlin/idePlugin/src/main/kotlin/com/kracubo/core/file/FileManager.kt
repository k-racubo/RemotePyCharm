package com.kracubo.core.file

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager

@Service(Service.Level.PROJECT)
class FileManager(private val project: Project) {

    fun getFileContent(relativeFilePath: String): List<String> {
        val vfs = VirtualFileManager.getInstance()
        val fileUrl = "file://${project.basePath}/$relativeFilePath"

        val virtualFile = vfs.findFileByUrl(fileUrl)
            ?: return emptyList()

        return runCatching {
            virtualFile.inputStream.bufferedReader().readLines()
        }.getOrElse { emptyList() }
    }
}