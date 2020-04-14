package com.walker.collect.api

import android.text.TextUtils
import com.walker.core.util.DateTimeUtils
import com.walker.network.retrofit.base.RetrofitNetworkApi
import com.walker.network.retrofit.errorhandler.ExceptionHandle
import io.reactivex.functions.Function
import okhttp3.Interceptor

class JuheNetworkApi2 private constructor(): RetrofitNetworkApi() {

    companion object{
        @JvmStatic
        fun get():JuheNetworkApi2{
            return Holder.instance
        }

        @JvmStatic
        fun <T> getService(service: Class<T>): T {
            return get().getRetrofit(service).create(service)
        }
    }

    private object Holder{
        val instance=JuheNetworkApi2()
    }

    override fun getInterceptor(): Interceptor {
        return Interceptor { chain ->
            val timeStr = DateTimeUtils.getNormalDate()
            val builder = chain.request().newBuilder()
            builder.addHeader("Source", "source")
            builder.addHeader("Date", timeStr)
            chain.proceed(builder.build())
        }
    }

    override fun <T> getAppErrorHandler(): Function<T, T> {
        return Function { response ->
            //response中code码不会0 出现错误
            if (response is JuheBaseResponse && (response as JuheBaseResponse).errorCode !== 0) {
                val exception = ExceptionHandle.ServerException()
                exception.setCode((response as JuheBaseResponse).errorCode)
                var reason = (response as JuheBaseResponse).reason
                if(TextUtils.isEmpty(reason)){
                    exception.setMessage("")
                }else{
                    exception.setMessage(reason)
                }
                throw exception
            }
            response
        }
    }

    override fun getFormal(): String = "http://apis.juhe.cn/"

    override fun getTest(): String = "http://apis.juhe.cn/"
}