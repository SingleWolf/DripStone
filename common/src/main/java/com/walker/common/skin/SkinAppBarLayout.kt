package com.walker.common.skin

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.appbar.AppBarLayout
import com.walker.common.R

class SkinAppBarLayout : AppBarLayout,SkinViewSupport {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun applySkin() {
        background=SkinResources.getInstance().getDrawable(R.color.colorPrimary)
    }
}