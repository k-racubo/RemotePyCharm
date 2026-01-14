package com.kracubo.extensions

import core.ApiJson
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
fun String.prettyJson(): String {
    return try {
        val jsonElement = Json.parseToJsonElement(this)

        return ApiJson.pretty.encodeToString(jsonElement)
    } catch (e: Exception) {
        this // Return this string
    }
}