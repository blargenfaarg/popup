package com.example.popup.model.request.post

import com.example.popup.model.domain.common.Location

/**
 * Represents the request object used to get map data
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/20/2024
 */
data class GetMapDataRequest(
    val topLeft: Location,
    val topRight: Location,
    val bottomRight: Location,
    val bottomLeft: Location
)
