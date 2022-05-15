package com.walker.aop_optimize_plugin.plugins.antishake

class AntiShakeConfig(
    private val shakeCheckClass: String = "com.walker.optimize.group.antishake.AntiShakeChecker",
    val shakeCheckMethodName: String = "canClick",
    val shakeCheckMethodDescriptor: String = "(Landroid/view/View;)Z",
    private val checkViewOnClickAnnotation: String = "com.walker.optimize.group.antishake.CheckViewOnClick",
    private val uncheckViewOnClickAnnotation: String = "com.walker.optimize.group.antishake.UncheckViewOnClick",
    val hookPointList: List<AntiShakeHookPoint> = extraHookPoints
) {

    val formatShakeCheckClass: String
        get() = shakeCheckClass.replace(".", "/")

    val formatCheckViewOnClickAnnotation: String
        get() = "L" + checkViewOnClickAnnotation.replace(".", "/") + ";"

    val formatUncheckViewOnClickAnnotation: String
        get() = "L" + uncheckViewOnClickAnnotation.replace(".", "/") + ";"

}

data class AntiShakeHookPoint(
    val interfaceName: String,
    val methodName: String,
    val methodSign: String,
) {

    val interfaceSignSuffix = "L$interfaceName;"

}

private val extraHookPoints = listOf(
    AntiShakeHookPoint(
        interfaceName = "android/view/View\$OnClickListener",
        methodName = "onClick",
        methodSign = "onClick(Landroid/view/View;)V"
    ),
    AntiShakeHookPoint(
        interfaceName = "com/chad/library/adapter/base/BaseQuickAdapter\$OnItemClickListener",
        methodName = "onItemClick",
        methodSign = "onItemClick(Lcom/chad/library/adapter/base/BaseQuickAdapter;Landroid/view/View;I)V"
    ),
    AntiShakeHookPoint(
        interfaceName = "com/chad/library/adapter/base/BaseQuickAdapter\$OnItemChildClickListener",
        methodName = "onItemChildClick",
        methodSign = "onItemChildClick(Lcom/chad/library/adapter/base/BaseQuickAdapter;Landroid/view/View;I)V",
    )
)