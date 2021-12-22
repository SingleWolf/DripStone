package com.walker.common.location.base

interface ILocation {
    fun onStart()
    fun onStop()
    fun onDestroy()
    fun setResultListener(listener: ILocCallback)
    fun getType():String
}