package com.walker.common.view.floatview

/**
 * Author  : walker
 * Date    : 2022/2/18  2:32 下午
 * Email   : feitianwumu@163.com
 * Summary : 悬浮框显示行为
 */
interface IShowFloat {
    fun getTag(): String
    fun show()
    fun dismiss()
    fun isShow(): Boolean
}