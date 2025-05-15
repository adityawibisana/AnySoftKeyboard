package com.anysoftkeyboard.ime

import android.view.inputmethod.InputConnection
import com.anysoftkeyboard.AnySoftKeyboard
import com.anysoftkeyboard.ime.AnySoftKeyboardColorizeNavBar
import com.anysoftkeyboard.ime.AnySoftKeyboardWithGestureTyping
import com.anysoftkeyboard.keyboards.views.AnyKeyboardView

abstract class AnySoftKeyboardListeningToVHKTranscribeOption : AnySoftKeyboardIncognito() {
    override fun onCreate() {
        super.onCreate()

        val inputViewBinder = inputView
        if (inputViewBinder == null) return
        val anyKeyboardView = inputViewBinder as AnyKeyboardView


    }


}