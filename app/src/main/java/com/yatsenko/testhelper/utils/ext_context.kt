package com.yatsenko.testhelper.utils

import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

fun Context.toast(mgs: String = "") {
    Toast.makeText(this, mgs, Toast.LENGTH_SHORT).show()
}

fun Context.getVerticalLinearLayoutManager(): LinearLayoutManager =
    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
