package com.example.popup.model.domain.common

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Represents the backend model for a UTCTime format
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/20/2024
 */
class UTCTime (
    val day: Int,
    val month: Int,
    val year: Int,
    val hour: Int,
    val minute: Int
) {
    companion object {

        fun now(): UTCTime {
            val zonedUtcTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ZonedDateTime.now(ZoneId.of("UTC"))
            } else {
                TODO("VERSION.SDK_INT < O")
            }

            return UTCTime(
                day = zonedUtcTime.dayOfMonth,
                month = zonedUtcTime.monthValue,
                year = zonedUtcTime.year,
                hour = zonedUtcTime.hour,
                minute = zonedUtcTime.minute
            )
        }
    }
}