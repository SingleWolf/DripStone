package com.walker.demo.feedback

sealed class FeedbackHideState {
    object NONE : FeedbackHideState()
    object LEFT : FeedbackHideState()
    object RIGHT : FeedbackHideState()
}
