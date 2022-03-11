package com.walker.optimize.group.oom

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Process
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import com.lzf.easyfloat.interfaces.OnInvokeView
import com.lzf.easyfloat.interfaces.OnTouchRangeListener
import com.lzf.easyfloat.utils.DragUtils
import com.lzf.easyfloat.widget.switch.BaseSwitchView
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.ExplainReasonCallback
import com.permissionx.guolindev.callback.RequestCallback
import com.walker.common.BaseApplication
import com.walker.core.util.DateTimeUtils
import com.walker.core.util.ToastUtils
import com.walker.optimize.R
import com.walker.optimize.group.oom.leakcanary.LeakCanaryHelper


class OOMFragment : Fragment() {
    private var floatWindowIsShow = false
    private var tvShowInfo: TextView? = null

    companion object {
        const val TAG = "OOMFragment"
        const val KEY_ID = "key_optimize_oom"
        fun instance(): Fragment {
            return OOMFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_optimize_oom, container, false)

        val tvGenDumpFile: TextView = view.findViewById(R.id.tvGenDumpFile)
        tvGenDumpFile.setOnClickListener { onGenDumpFileTapped() }

        val tvGetAppMemorySize: TextView = view.findViewById(R.id.tvGetAppMemorySize)
        tvGetAppMemorySize.setOnClickListener { onGetAppMemorySizeTapped() }

        val tvGetStatisticsInfo: TextView = view.findViewById(R.id.tvGetStatisticsInfo)
        tvGetStatisticsInfo.setOnClickListener { onGetStatisticsInfoTapped() }

        val tvMockJavaObjLeak: TextView = view.findViewById(R.id.tvMockJavaObjLeak)
        tvMockJavaObjLeak.setOnClickListener { onMockJavaObjLeakTapped() }

        val tvMockIncreaseJavaHeap: TextView = view.findViewById(R.id.tvMockIncreaseJavaHeap)
        tvMockIncreaseJavaHeap.setOnClickListener { onMockIncreaseJavaHeapTapped() }

        val tvMockGC: TextView = view.findViewById(R.id.tvMockGC)
        tvMockGC.setOnClickListener { onMockGCJavaHeapTapped() }

        val tvMockIncreaseThread: TextView = view.findViewById(R.id.tvMockIncreaseThread)
        tvMockIncreaseThread.setOnClickListener { onMockIncreaseThreadTapped() }

        val tvMockIncreaseFD: TextView = view.findViewById(R.id.tvMockIncreaseFD)
        tvMockIncreaseFD.setOnClickListener { onMockIncreaseFDTapped() }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDismissFloatWindow()
    }

    override fun onDestroy() {
        super.onDestroy()
        LeakCanaryHelper.get().watch(this, "OOMFragment")
    }

    fun onDismissFloatWindow() {
        if (floatWindowIsShow) {
            EasyFloat.dismiss(TAG, false)
            floatWindowIsShow = false
        }
    }

    fun onGenDumpFileTapped() {
        OOMHelper.get().genDumpFile() {
            ToastUtils.showCenterLong("获取内存快照成功：\nfilePath=$it")

        }
    }

    fun onGetAppMemorySizeTapped() {
        startFloatWindow()
        val limitSize = OOMHelper.get().getAppMemorySize2M()
        tvShowInfo?.append("${DateTimeUtils.getNormalDate()}\t\t\t设备对app内存限制为${limitSize}M\r\n")
        val memoryInfo = OOMHelper.get().getMemoryInfo()
        tvShowInfo?.append("${DateTimeUtils.getNormalDate()}\t\t\t${memoryInfo}")
    }

    fun onGetStatisticsInfoTapped() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.SYSTEM_ALERT_WINDOW
            )
            .onExplainRequestReason(ExplainReasonCallback { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "该功能需要以下权限",
                    "允许",
                    "拒绝"
                )
            })
            .request(
                RequestCallback { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        startFloatWindow()
                    }
                }
            )

        val info = OOMHelper.get().listStatisticsInfo(Process.myPid())
        tvShowInfo?.append("${DateTimeUtils.getNormalDate()}\t\t\t${info}")
    }

    private fun onMockJavaObjLeakTapped() {
        val test = BitmapFactory.decodeResource(resources, R.drawable.custom)
        OOMTest.get().testLeakObject(test)
        LeakCanaryHelper.get().watch(test, "onMockJavaObjLeakTapped")
    }

    fun onMockIncreaseJavaHeapTapped() {
        OOMTest.get().testAllocateJavaHeap(50 * 1024 * 1024)
    }

    fun onMockGCJavaHeapTapped() {
        OOMTest.get().testGCAndDeallocate()
    }

    fun onMockIncreaseThreadTapped() {
        OOMTest.get().testIncreaseThread(500)
    }

    fun onMockIncreaseFDTapped() {
        OOMTest.get().testIncreaseFD(100)
    }

    fun startFloatWindow() {
        if (floatWindowIsShow) {
            return
        }
        try {
            EasyFloat.with(requireContext().applicationContext)
                .setShowPattern(ShowPattern.ALL_TIME)
                .setSidePattern(SidePattern.RESULT_SIDE)
                //.setImmersionStatusBar(true)
                .setDragEnable(true)
                .setGravity(Gravity.END, -20, 10)
                .setLayout(R.layout.layout_optimize_oom_floatwindow, OnInvokeView {
                    tvShowInfo = it.findViewById(R.id.tvContentInfo)
                    tvShowInfo?.movementMethod = ScrollingMovementMethod.getInstance()

                    it.findViewById<ImageView>(R.id.ivClose)
                        .setOnClickListener { onDismissFloatWindow() }

                    tvShowInfo?.apply {
                        //解决拖拽滑动冲突
                        setOnTouchListener { _, event ->
                            // 监听 ListView 的触摸事件，手指触摸时关闭拖拽，手指离开重新开启拖拽
                            EasyFloat.dragEnable(event?.action == MotionEvent.ACTION_UP, TAG)
                            false
                        }
                        setOnClickListener {
                            if (BaseApplication.isAppBack()) {
                                BaseApplication.gotoMainPage(requireActivity())
                            }
                        }
                    }
                })
                .registerCallback {
                    drag { _, motionEvent ->
                        DragUtils.registerDragClose(motionEvent, object : OnTouchRangeListener {
                            override fun touchInRange(inRange: Boolean, view: BaseSwitchView) {
                                view.findViewById<TextView>(com.lzf.easyfloat.R.id.tv_delete).text =
                                    if (inRange) "松手删除" else "删除浮窗"

                                view.findViewById<ImageView>(com.lzf.easyfloat.R.id.iv_delete)
                                    .setImageResource(
                                        if (inRange) com.lzf.easyfloat.R.drawable.icon_delete_selected
                                        else com.lzf.easyfloat.R.drawable.icon_delete_normal
                                    )
                            }

                            override fun touchUpInRange() {
                                onDismissFloatWindow()
                            }
                        }, showPattern = ShowPattern.ALL_TIME)
                    }
                }
                .setTag(TAG)
                .show()
            floatWindowIsShow = true
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            floatWindowIsShow = false
        }
    }
}