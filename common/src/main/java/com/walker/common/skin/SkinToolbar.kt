package com.walker.common.skin

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import com.walker.common.R

class SkinToolbar : Toolbar, SkinViewSupport {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun applySkin() {
        setTitleTextColor(SkinResources.getInstance().getColor(R.color.colorPrimary))
    }

}