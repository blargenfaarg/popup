package com.example.popup.networking.http

import java.io.File

/**
 * Describes a filetype to be used in a multipart form data http request
 *
 * @author Benjamin Michael
 * Project: KotlinApi
 * Created on: 9/19/2024
 */
data class HttpFile (
    val fieldName: String,
    val filename: String? = null,
    val contentType: String,
    val content: ByteArray
) {

    companion object {
        private const val JSON_CONTENT_TYPE = "application/json"
        private const val FILE_CONTENT_TYPE = "application/form-data"

        /**
         * Build a HttpFile instance for a json text field
         */
        fun fromJson(json: String, fieldName: String = "json"): HttpFile {
            return HttpFile(
                fieldName = fieldName,
                contentType = JSON_CONTENT_TYPE,
                content = json.encodeToByteArray()
            )
        }

        /**
         * Build a HttpFile instance for a file
         */
        fun fromFile(file: File, fieldName: String): HttpFile {
            return HttpFile(
                fieldName = fieldName,
                filename = file.name,
                contentType = FILE_CONTENT_TYPE,
                content = file.readBytes()
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HttpFile

        if (fieldName != other.fieldName) return false
        if (filename != other.filename) return false
        if (contentType != other.contentType) return false
        if (!content.contentEquals(other.content)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fieldName.hashCode()
        result = 31 * result + (filename?.hashCode() ?: 0)
        result = 31 * result + contentType.hashCode()
        result = 31 * result + content.contentHashCode()
        return result
    }
}