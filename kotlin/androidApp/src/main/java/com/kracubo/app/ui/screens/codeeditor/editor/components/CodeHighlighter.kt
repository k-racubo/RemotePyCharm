package com.kracubo.app.ui.screens.codeeditor.editor.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle

private val kotlinKeywords = listOf(
    "package", "import", "class", "fun", "val", "var",
    "if", "else", "for", "while", "return",
    "override", "object", "interface", "private",
    "protected", "public", "internal", "when",
    "try", "catch", "finally", "throw", "null",
    "true", "false", "this", "super", "as", "is",
    "in", "out", "by", "companion", "data", "enum",
    "sealed", "abstract", "open", "final", "const",
    "lateinit", "suspend", "async", "await", "with",
    "let", "run", "apply", "also", "repeat", "break",
    "continue", "do", "when", "typealias"
)

private val pythonKeywords = listOf(
    "and", "as", "assert", "async", "await", "break", "class", "continue",
    "def", "del", "elif", "else", "except", "False", "finally", "for",
    "from", "global", "if", "import", "in", "is", "lambda", "None",
    "nonlocal", "not", "or", "pass", "raise", "return", "True", "try",
    "while", "with", "yield"
)

private val pythonBuiltins = listOf(
    "abs", "all", "any", "ascii", "bin", "bool", "bytearray", "bytes",
    "callable", "chr", "classmethod", "compile", "complex", "delattr",
    "dict", "dir", "divmod", "enumerate", "eval", "exec", "filter",
    "float", "format", "frozenset", "getattr", "globals", "hasattr",
    "hash", "help", "hex", "id", "input", "int", "isinstance", "issubclass",
    "iter", "len", "list", "locals", "map", "max", "memoryview", "min",
    "next", "object", "oct", "open", "ord", "pow", "print", "property",
    "range", "repr", "reversed", "round", "set", "setattr", "slice",
    "sorted", "staticmethod", "str", "sum", "super", "tuple", "type",
    "vars", "zip", "__import__", "asyncio", "sleep"
)

fun highlightKotlin(code: String): AnnotatedString {
    val builder = AnnotatedString.Builder(code)

    fun highlight(regex: Regex, color: Color) {
        regex.findAll(code).forEach { matchResult ->
            builder.addStyle(
                SpanStyle(color = color),
                matchResult.range.first,
                matchResult.range.last + 1
            )
        }
    }

    // Комментарии (однострочные)
    highlight(Regex("//.*"), CodeColors.Comment)
    
    // Многострочные комментарии
    highlight(Regex("/\\*[\\s\\S]*?\\*/"), CodeColors.Comment)

    // Строки (включая экранированные символы)
    highlight(Regex("\"(\\\\.|[^\"])*\""), CodeColors.String)
    highlight(Regex("'(\\\\.|[^'])*'"), CodeColors.String)
    highlight(Regex("`[^`]*`"), CodeColors.String)

    // Числа
    highlight(Regex("\\b\\d+(\\.\\d+)?[fFdDlL]?\\b"), CodeColors.Number)
    highlight(Regex("\\b0x[0-9a-fA-F]+\\b"), CodeColors.Number)

    // Ключевые слова
    kotlinKeywords.forEach { keyword ->
        highlight(Regex("\\b$keyword\\b"), CodeColors.Keyword)
    }

    // Типы (имена классов с заглавной буквы)
    highlight(Regex("\\b[A-Z][A-Za-z0-9_]*\\b"), CodeColors.Type)

    return builder.toAnnotatedString()
}

fun highlightPython(code: String): AnnotatedString {
    val builder = AnnotatedString.Builder(code)

    fun highlight(regex: Regex, color: Color) {
        regex.findAll(code).forEach { matchResult ->
            builder.addStyle(
                SpanStyle(color = color),
                matchResult.range.first,
                matchResult.range.last + 1
            )
        }
    }

    // Комментарии
    highlight(Regex("#.*"), CodeColors.Comment)

    // Строки (включая экранированные символы)
    highlight(Regex("\"(\\\\.|[^\"])*\""), CodeColors.String)
    highlight(Regex("'(\\\\.|[^'])*'"), CodeColors.String)
    highlight(Regex("\"\"\"[\\s\\S]*?\"\"\""), CodeColors.String)
    highlight(Regex("'''[\\s\\S]*?'''"), CodeColors.String)

    // Числа
    highlight(Regex("\\b\\d+(\\.\\d+)?\\b"), CodeColors.Number)

    // Ключевые слова Python
    pythonKeywords.forEach { keyword ->
        highlight(Regex("\\b$keyword\\b"), CodeColors.Keyword)
    }

    // Встроенные функции (light blue)
    pythonBuiltins.forEach { builtin ->
        highlight(Regex("\\b$builtin\\b"), CodeColors.Function)
    }

    // Остальной текст - белый (переменные и т.д.)
    // Это будет дефолтный цвет

    return builder.toAnnotatedString()
}

fun handleSmartInput(
    oldText: String,
    newText: String
): String {
    if (newText.length <= oldText.length) return newText

    val insertedChar = newText.lastOrNull() ?: return newText

    val closingChar = when (insertedChar) {
        '{' -> "}"
        '(' -> ")"
        '[' -> "]"
        '"' -> "\""
        '\'' -> "'"
        '`' -> "`"
        else -> return newText
    }

    // Проверяем, что закрывающая скобка еще не добавлена сразу после открывающей
    // Это предотвращает дублирование при повторном вводе
    if (newText.length >= 2) {
        val lastTwoChars = newText.takeLast(2)
        if (lastTwoChars == insertedChar.toString() + closingChar) {
            return newText
        }
    }

    return newText + closingChar
}

