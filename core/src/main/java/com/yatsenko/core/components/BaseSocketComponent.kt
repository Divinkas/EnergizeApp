package com.yatsenko.core.components

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.yatsenko.core.bean.response.EnergizeResponse
import com.yatsenko.core.bean.response.Meta
import com.yatsenko.core.utils.isNotEmpty
import org.json.JSONObject
import org.koin.core.KoinComponent
import org.koin.core.inject

open class BaseSocketComponent : KoinComponent {

    protected val gson: Gson by inject()

    protected fun <T> parseErrorResponse(liveData: MutableLiveData<EnergizeResponse<T>>, response: JSONObject) {
        val meta = gson.fromJson(response.getString("meta"), Meta::class.java)
        liveData.postValue(EnergizeResponse.Error(meta))
    }

    protected fun <T> parseSuccessResponse(
        liveData: MutableLiveData<EnergizeResponse<T>>,
        response: JSONObject,
        parsedClass: Class<T>
    ) {
        val data = gson.fromJson(response.getString("data"), parsedClass)
        liveData.postValue(EnergizeResponse.Success(data))
    }

    protected fun isSuccessResponse(metaObject: JSONObject): Boolean {
        return metaObject.isNotEmpty() && metaObject.getString("status") == "200"
    }
}