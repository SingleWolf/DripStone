package com.walker.common.skin

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.walker.common.R

class SkinBottomNavigationView : BottomNavigationView, SkinViewSupport {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun applySkin() {
        itemIconTintList =
            SkinResources.getInstance().getColorStateList(R.color.color_state_menu_navi)
        itemTextColor = SkinResources.getInstance().getColorStateList(R.color.color_state_menu_navi)
    }
}