package com.kracubo.app.core.networking.handlers

import core.API
import core.ApiJson

class Handler {
    suspend fun resolve(message: String) {
        try {
            when (ApiJson.instance.decodeFromString<API>(message)) {

                else -> {}
            }
        } catch (e: Exception) {

        }
    }
}