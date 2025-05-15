package com.anysoftkeyboard.ime

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.anysoftkeyboard.keyboards.views.AnyKeyboardView
import com.anysoftkeyboard.keyboards.views.VoiceHotKeyStateView

abstract class AnySoftKeyboardListeningToVHKState : AnySoftKeyboardIncognito() {
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.v("VoiceHotKeyStateBroadcastReceiver", "onReceive")
            context ?: return
            intent ?: return

            val state = intent.getIntExtra("state", 0)

            val inputViewBinder = inputView
            if (inputViewBinder != null) {
                val anyKeyboardView = inputViewBinder as AnyKeyboardView
                when (state) {
                    0 -> anyKeyboardView.setState(VoiceHotKeyStateView.VoiceHotKeyState.IDLE)
                    1 -> anyKeyboardView.setState(VoiceHotKeyStateView.VoiceHotKeyState.RECORDING)
                    2 -> anyKeyboardView.setState(VoiceHotKeyStateView.VoiceHotKeyState.TRANSCRIBING)
                    3 -> anyKeyboardView.setState(VoiceHotKeyStateView.VoiceHotKeyState.AI_TRANSCRIBING)
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(receiver, IntentFilter("com.emoji.voicehotkey.state"))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(applicationContext).unregisterReceiver(receiver)
    }
}