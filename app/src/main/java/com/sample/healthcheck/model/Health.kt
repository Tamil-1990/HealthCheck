package com.sample.healthcheck.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Health(
    val accessible: List<Accessible>,
    val name: String
): Parcelable