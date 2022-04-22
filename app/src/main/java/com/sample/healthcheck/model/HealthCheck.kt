package com.sample.healthcheck.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HealthCheck(
    val `data`: Data,
    val statusCode: Int,
    val success: Boolean
): Parcelable