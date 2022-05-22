package com.yatsenko.testhelper.base

import androidx.appcompat.app.AppCompatActivity
import com.yatsenko.testhelper.EnergizerApplication

open class BaseActivity : AppCompatActivity() {

    protected val testApplication get() = application as EnergizerApplication
}