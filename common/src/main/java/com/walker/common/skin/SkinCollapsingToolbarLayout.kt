package com.walker.common.skin

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.walker.common.R

class SkinCollapsingToolbarLayout : CollapsingToolbarLayout, SkinViewSupport {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun applySkin() {
        setContentScrimColor(SkinResources.getInstance().getColor(R.color.colorPrimary))
        setStatusBarScrimColor(SkinResources.getInstance().getColor(R.color.colorPrimary))
    }


}