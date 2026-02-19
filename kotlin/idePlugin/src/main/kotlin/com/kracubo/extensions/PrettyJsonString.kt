package com.kracubo.extensions

import core.ApiJson
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

@OptIn(ExperimentalSerializationApi::class)
fun String.prettyJson(): String {
     try {
        val jsonElement = Json.parseToJsonElement(this)

        if (jsonElement !is JsonObject) { return this }

        return ApiJson.pretty.encodeToString(jsonElement)
    } catch (_: Exception) {
        return this // Return this string
    }
}