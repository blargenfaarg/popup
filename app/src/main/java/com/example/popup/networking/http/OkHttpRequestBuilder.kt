package com.example.popup.networking.http

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * An HTTP request builder that creates and sends http requests.
 * This class specifically is based on the okhttp library -> https://square.github.io/okhttp/
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/20/2024
 */
class OkHttpRequestBuilder(
    private val mapper: ObjectMapper = jacksonObjectMapper(),
    private val client: OkHttpClient = OkHttpClient()
): IHttpRequestBuilder {

    /**
     * Logger to warn of any errors
     */
    private val logger = KotlinLogging.logger {}
    private val EMPTY_REQUEST = ByteArray(0).toRequestBody()

    companion object {
        const val MEDIA_TYPE_JSON = "application/json"

        fun builder(): OkHttpRequestBuilder = OkHttpRequestBuilder()
    }

    /**
     * Absolutely critical variables that MUST be initialized before the request can be made
     */
    private lateinit var requestMethod: HttpMethod
    private lateinit var url: String

    /**
     * Other data for the request
     */
    private var json: String? = null
    private var headers: MutableMap<String, String> = mutableMapOf()
    private var params: MutableMap<String, String> = mutableMapOf()
    private var files: MutableList<HttpFile> = mutableListOf()

    override fun post(url: String): IHttpRequestBuilder {
        this.url = url
        requestMethod = HttpMethod.POST

        return this
    }

    override fun put(url: String): IHttpRequestBuilder {
        requestMethod = HttpMethod.PUT
        this.url = url

        return this
    }

    override fun get(url: String): IHttpRequestBuilder {
        requestMethod = HttpMethod.GET
        this.url = url

        return this
    }

    override fun delete(url: String): IHttpRequestBuilder {
        requestMethod = HttpMethod.DELETE
        this.url = url

        return this
    }

    override fun pathVariable(variable: String): IHttpRequestBuilder {
        require(::url.isInitialized) {
            "The http URL must be initialized before calling #pathVariable!"
        }

        this.url = this.url.plus("/$variable")
        return this
    }

    override fun param(key: String, value: String): IHttpRequestBuilder {
        params[key] = value

        return this
    }

    override fun params(params: Map<String, String>): IHttpRequestBuilder {
        params.forEach { (key, value) ->
            this.params[key] = value
        }

        return this
    }

    override fun json(body: Any?): IHttpRequestBuilder {
        json = mapper.writeValueAsString(body)

        return this
    }

    override fun header(key: String, value: String): IHttpRequestBuilder {
        headers[key] = value

        return this
    }

    override fun headers(headers: Map<String, String>): IHttpRequestBuilder {
        headers.forEach { (key, value) ->
            this.headers[key] = value
        }

        return this
    }

    override fun file(file: HttpFile): IHttpRequestBuilder {
        files.add(file)

        return this
    }

    override suspend fun send(): HttpResponse {
        validateHttpRequest()

        val requestBuilder = Request.Builder().url(buildUrl())
        addHeadersToRequest(requestBuilder)
        addRequestBodyToRequest(requestBuilder)

        val request = requestBuilder.build()

        return withContext(Dispatchers.IO) {
            val response = client.newCall(request).execute()
            HttpResponse(
                responseCode = response.code,
                contentType = response.body?.contentType().toString(),
                responseBody = response.body?.string() ?: ""
            )
        }
    }

    /**
     * Check to ensure the http request is valid before sending it
     */
    private fun validateHttpRequest() {
        if (!::url.isInitialized || !::requestMethod.isInitialized) {
            logger.error{ "HttpRequest missing url and http method! Cannot create call..." }
            throw RuntimeException()
        }
        if (json != null && files.isNotEmpty()) {
            logger.error{ "JSON and form-data files included, this is not correct. If you wish to use JSON and files," +
                    " include JSON as file with correct field name" }
            throw RuntimeException()
        }
    }

    /**
     * Add headers to the request builder
     */
    private fun addHeadersToRequest(request: Request.Builder) {
        if (headers.isNotEmpty()) {
            headers.forEach { (key, value) ->
                request.addHeader(key, value)
            }
        }
    }

    /**
     * Adds the request body to the http request IF needed. If there was no JSON or files specified, then the request
     * will not have a body (headers and params are added separate)
     */
    private fun addRequestBodyToRequest(request: Request.Builder) {
        if (json != null) {
            request.method(
                requestMethod.toString(),
                json!!.toRequestBody(MEDIA_TYPE_JSON.toMediaTypeOrNull())
            )
        } else if (files.isNotEmpty()) {
            val multipartBody = MultipartBody.Builder()
            files.forEach { file ->
                multipartBody
                    .addFormDataPart(
                        file.fieldName,
                        file.filename,
                        file.content.toRequestBody(
                            contentType = file.contentType.toMediaTypeOrNull(),
                            offset = 0,
                            byteCount = file.content.size
                        )
                    )
            }

            request.method(
                requestMethod.toString(),
                multipartBody.build()
            )
        } else {
            val requestBody = if(requestMethod == HttpMethod.GET) null else EMPTY_REQUEST
            request.method(requestMethod.toString(), requestBody)
        }
    }

    /**
     * Builds the url to send the request to. If there were no parameters specified during the build process, this will
     * simply return the url of the class. If there are params, they will be appended to the url
     */
    private fun buildUrl(): String {
        if (params.isNotEmpty()) {
            var i = 0
            for (param in params) {
                url = when(i == 0) {
                    true -> url.plus("?${param.key}=${param.value}")
                    false -> url.plus("&${param.key}=${param.value}")
                }
                i += 1
            }
        }

        return url
    }
}