package com.walker.demo.shortcut

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.walker.common.aop.AopTrace
import com.walker.common.isEmptyOrNull
import com.walker.core.base.mvc.BaseFragment
import com.walker.core.util.ToastUtils
import com.walker.demo.CheckOut
import com.walker.demo.R

/**
 * Author  : walker
 * Date    : 2021/8/19  10:41 上午
 * Email   : feitianwumu@163.com
 * Summary : 快捷方式
 */
class ShortcutFragment : BaseFragment() {

    companion object {
        const val KEY_ID = "key_demo_short_cut_fragment"
        const val IMG_URL =
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fnimg.ws.126.net%2F%3Furl%3Dhttp%253A%252F%252Fdingyue.ws.126.net%252F2021%252F0819%252Fa0d32de8j00qy2mh2001ec000hs00cnm.jpg%26thumbnail%3D650x2147483647%26quality%3D80%26type%3Djpg&refer=http%3A%2F%2Fnimg.ws.126.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1631951895&t=cc200e2189f29f7aa593a87eb948df3d"

        fun instance() = ShortcutFragment()
    }

    private lateinit var tvGenShortcut: TextView
    private lateinit var etId: EditText
    private lateinit var etName: EditText
    private lateinit var etIcon: EditText
    private lateinit var etDataValue: EditText

    override fun buildView(baseView: View, savedInstanceState: Bundle?) {
        etId = baseView.findViewById(R.id.etId)
        etName = baseView.findViewById(R.id.etName)
        etIcon = baseView.findViewById(R.id.etIcon)
        etDataValue = baseView.findViewById(R.id.etDataValue)

        tvGenShortcut = baseView.findViewById(R.id.tvGenShortcut)
        tvGenShortcut.setOnClickListener {
            transactAddShortcut()
        }
    }

    override fun getLayoutId() = R.layout.fragment_short_cut

    @AopTrace(point = CheckOut::class)
    private fun transactAddShortcut() {
        val id = etId.text.toString()
        val name = etName.text.toString()
        var icon = etIcon.text.toString()
        val data = etDataValue.text.toString()

        if (icon.isEmptyOrNull()) {
            icon = IMG_URL
        }
        if (id.isEmptyOrNull() || name.isEmptyOrNull() || data.isEmptyOrNull()) {
            ToastUtils.showCenter("输入完整参数")
            return
        }

        ShortcutHelper.add(requireContext(), ShortcutBean(id, name, icon, data))
    }
}