package com.anysoftkeyboard.ime

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.anysoftkeyboard.keyboards.views.AnyKeyboardView
import com.anysoftkeyboard.keyboards.views.VoiceHotKeyTranscribeModeStateView.ShortcutActions

abstract class AnySoftKeyboardListeningToVHKTranscribeOption : AnySoftKeyboardListeningToVHKState() {
    private val voiceHotKeyTranscribeModeStateBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val inputViewBinder = inputView ?: return
            context ?: return
            intent ?: return

            val mode = intent.getIntExtra("mode", -1)
            if (mode == -1) return

            val anyKeyboardView = inputViewBinder as AnyKeyboardView
            val state = ShortcutActions.entries.find {
                it.value == mode
            } ?: return
            anyKeyboardView.setState(context, state)
        }
    }

    override fun onCreate() {
        super.onCreate()

        LocalBroadcastManager.getInstance(applicationContext)
            .registerReceiver(voiceHotKeyTranscribeModeStateBroadcastReceiver,
                IntentFilter().apply {
                    addAction("com.emoji.voicehotkey.transcribe_mode")
            })
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(applicationContext).unregisterReceiver(voiceHotKeyTranscribeModeStateBroadcastReceiver)
        super.onDestroy()
    }
}