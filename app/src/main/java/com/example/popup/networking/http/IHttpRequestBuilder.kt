package com.example.popup.networking.http

/**
 * An interface that defines an HTTP request class that can be used in a chained builder pattern
 * in order to create and send HTTP request for normal (CRUD) type calls
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/15/2024
 */
interface IHttpRequestBuilder {

    /**
     * Send a post request to a specified url
     *
     * @param url the url to send the request to
     * @return a reference of the IHttpRequestBuilder this method was called on
     */
    fun post(url: String): IHttpRequestBuilder

    /**
     * Send a put request to a specified url
     *
     * @param url the url to send the request to
     * @return a reference of the IHttpRequestBuilder this method was called on
     */
    fun put(url: String): IHttpRequestBuilder

    /**
     * Send a get request to a specified url
     *
     * @param url the url to send the request to
     * @return a reference of the IHttpRequestBuilder this method was called on
     */
    fun get(url: String): IHttpRequestBuilder

    /**
     * Send a delete request to a specified url
     *
     * @param url the url to send the request to
     * @return a reference of the IHttpRequestBuilder this method was called on
     */
    fun delete(url: String): IHttpRequestBuilder

    /**
     * Append a variable to the url path
     *
     * @param variable the path variable to append
     * @return a reference of the IHttpRequestBuilder this method was called on
     */
    fun pathVariable(variable: String): IHttpRequestBuilder

    /**
     * Add a parameter to the http request
     *
     * @param key the string key name of the parameter
     * @param value the value of the parameter
     * @return a reference of the IHttpRequestBuilder this method was called on
     */
    fun param(key: String, value: String) : IHttpRequestBuilder

    /**
     * Add a map of parameters to the http request
     *
     * @param params string map of params to send
     * @return a reference of the IHttpRequestBuilder this method was called on
     */
    fun params(params: Map<String, String>): IHttpRequestBuilder

    /**
     * Add a json request body to the http request
     *
     * @param body some object to be converted to json body
     * @return a reference of the IHttpRequestBuilder this method was called on
     */
    fun json(body: Any?): IHttpRequestBuilder

    /**
     * Add a header to the http request
     *
     * @param key the string key name of the header
     * @param value the string value for the header
     * @return a reference of the IHttpRequestBuilder this method was called on
     */
    fun header(key: String, value: String): IHttpRequestBuilder

    /**
     * Add a map of headers to the http request
     *
     * @param headers string map of headers to be included in the request
     * @return a reference of the IHttpRequestBuilder this method was called on
     */
    fun headers(headers: Map<String, String>): IHttpRequestBuilder

    /**
     * Add a file to be sent in the http request
     *
     * @param file the file to add to the request
     * @return a reference of the IHttpRequestBuilder this method was called on
     */
    fun file(file: HttpFile): IHttpRequestBuilder

    /**
     * Send a post request to a specified url and receive back the response
     *
     * @return the HttpResponse containing the code, content-type, and the body
     */
    suspend fun send(): HttpResponse
}