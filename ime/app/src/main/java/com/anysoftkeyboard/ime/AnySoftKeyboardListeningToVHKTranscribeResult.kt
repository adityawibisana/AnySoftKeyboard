package com.anysoftkeyboard.ime

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager

abstract class AnySoftKeyboardListeningToVHKTranscribeResult : AnySoftKeyboardIncognito() {
    private val receiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.v("AnySoftKeyboardVoiceHotKeyBroadcastReceiver", "onReceive")

            context ?: return
            intent ?: return

            val result = intent.getStringExtra("result")

            val ic = currentInputConnection
            ic?.commitText("$result ", 1) // add space after result
        }
    }

    override fun onCreate() {
        super.onCreate()
        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(receiver, IntentFilter("com.emoji.voicehotkey.transcribe"))
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(applicationContext).unregisterReceiver(receiver)
        super.onDestroy()
    }
}