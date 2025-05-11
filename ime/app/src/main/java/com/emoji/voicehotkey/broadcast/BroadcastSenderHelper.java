package com.emoji.voicehotkey.broadcast;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class BroadcastSenderHelper {
    public static void sendStartRecordPressedEvent(Context context) {
        final Intent pressedIntent = new Intent("com.anysoftkeyboard.action.voice_input");
        pressedIntent.putExtra("action", "pressed");
        LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(pressedIntent);
        Log.v("vhk", "sending broadcast: com.anysoftkeyboard.action.voice_input pressed");
    }

    public static void sendStopRecordPressedEvent(Context context) {
        final Intent pressedIntent = new Intent("com.anysoftkeyboard.action.voice_input");
        pressedIntent.putExtra("action", "released");
        LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(pressedIntent);
        Log.v("vhk", "sending broadcast: com.anysoftkeyboard.action.voice_input released");
    }
}
