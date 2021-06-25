package com.walker.collect.summary

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.scwang.smartrefresh.header.WaterDropHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.walker.collect.R
import com.walker.collect.cook.cooklist.CookListActivity
import com.walker.collect.news.headline.NewsSummaryFragment
import com.walker.common.activity.ShowActivity
import com.walker.common.media.image.ImageLoadHelper
import com.walker.common.view.banner.holder.HolderCreator
import com.walker.common.view.banner.holder.ViewHolder
import com.walker.common.view.banner.view.MultiBanner
import com.walker.common.view.banner.view.MultiBanner.OnPageClickListener
import com.walker.core.base.mvc.BaseFragment
import com.walker.core.util.DisplayUtils


@Suppress("DEPRECATION")
class SummaryFragment : BaseFragment(), Observer<SummaryListBean> {

    private val viewModel by lazy { ViewModelProvider(this).get(SummaryViewModel::class.java) }
    private lateinit var banner: MultiBanner<Summary>
    private lateinit var refreshLayout: SmartRefreshLayout

    override fun buildView(baseView: View, savedInstanceState: Bundle?) {
        banner = baseView.findViewById(R.id.banner)
        refreshLayout = baseView.findViewById(R.id.refreshLayout)
        initBanner()
        initRefresh()
        if (viewModel != null) {
            lifecycle.addObserver(viewModel)
        }
        viewModel.dataList.observe(this, this)
        viewModel.getData()
    }

    private fun initRefresh() {
        refreshLayout.setRefreshHeader(WaterDropHeader(requireContext()))
        refreshLayout.setRefreshFooter(
            BallPulseFooter(requireContext()).setSpinnerStyle(
                SpinnerStyle.Scale
            )
        )
        refreshLayout.setOnRefreshListener(OnRefreshListener {
            viewModel.getData()
        })
        refreshLayout.isEnableLoadMore = false
    }

    private fun initBanner() {
        //设置布局（也可使用默认布局）
        //banner.setViewPagerLayout(R.layout.multi_layout_view_pager_home,R.id.vp_main,R.id.ll_main_dot);
        //  设置指示器位置
        banner.setIndicatorGravity(MultiBanner.CENTER)
        //  是否显示指示器
        banner.isShowIndicator(true)
        banner.setIndicatorColor(Color.parseColor("#eeeeee"), Color.parseColor("#bbbbbb"))
        //  设置图片切换时间间隔
        banner.setInterval(5000)
        //  设置是否无限循环
        banner.setCanLoop(true)
        //  设置是否自动轮播
        banner.setAutoPlay(true)
        //设置Page间间距
        banner.setPageMargin(DisplayUtils.dp2px(requireContext(), 10f).toInt())
        //设置缓存的页面数量
        banner.setOffscreenPageLimit(10)
        //banner.setPageTransformer(ScaleInTransformer())
        //  设置页面点击事件
        banner.setOnPageClickListener(OnPageClickListener { position ->
            val dataList: List<Summary> = banner.list
            if (dataList.isEmpty()) {
                return@OnPageClickListener
            }
            val pos = position + 1
            if (pos < 0 || dataList.size <= pos) {
                return@OnPageClickListener
            }
            transactBannerClick(dataList[pos])
        })
        //根据屏幕宽高适配布局大小（可选）
        //mBannerClipWidth = mDisplayMetrics.widthPixels - (int) (40 * mDisplayMetrics.density + 0.5f);//dp转化为px
        //mBannerClipHeight = (int) (mBannerClipWidth * CommonConstant.CLIP_BALANCE);//宽和高比例为(680，280)
        //banner.setContentLayoutParams(0, mBannerClipHeight);
        //  设置数据
        banner.setPages(mutableListOf<Summary>(), HolderCreator { BannerViewHolder() })
        lifecycle.addObserver(banner)
    }

    override fun getLayoutId() = R.layout.fragment_collect_summary

    class BannerViewHolder : ViewHolder<Summary> {
        private lateinit var imageView: ImageView

        override fun createView(context: Context, position: Int): View {
            // 返回页面布局文件
            val view = LayoutInflater.from(context).inflate(R.layout.item_collect_banner, null)
            imageView = view.findViewById<View>(R.id.ivShow) as ImageView
            return view
        }

        override fun onBind(context: Context?, data: Summary, position: Int, size: Int) {
            ImageLoadHelper.loadUrl(imageView, data.imageUrl, null)
        }

    }

    override fun onChanged(data: SummaryListBean?) {
        data?.bannerList?.apply {
            banner.onReload(this)
        }
        refreshLayout.finishRefresh()
    }

    private fun transactBannerClick(value: Summary) {
        if (TextUtils.equals(CookListActivity.CHANNEL_ID, value.key)) {
            CookListActivity.start(requireContext())
        } else {
            ShowActivity.start(requireContext(), value.key, value.title, ::genInstance)
        }
    }

    fun genInstance(channelId: String): Fragment? {
        val fragment: Fragment? = when (channelId) {
            NewsSummaryFragment.channel_id -> NewsSummaryFragment()
            else -> null
        }
        return fragment
    }
}