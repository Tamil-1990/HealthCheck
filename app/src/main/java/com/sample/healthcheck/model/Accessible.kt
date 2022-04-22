package com.sample.healthcheck.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Accessible(
    val api: String?,
    val message: String?,
    val name: String?,
    val status: Int?,
    val success: Boolean?,
    val type: String?
): Parcelable