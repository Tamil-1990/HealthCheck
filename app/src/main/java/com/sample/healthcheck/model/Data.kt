package com.sample.healthcheck.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    val health: List<Health>,
    val message: String
): Parcelable