package com.sj.saurabh.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaModelClass(
//    val title: String,
    val imgUri: Uri,
) : Parcelable
