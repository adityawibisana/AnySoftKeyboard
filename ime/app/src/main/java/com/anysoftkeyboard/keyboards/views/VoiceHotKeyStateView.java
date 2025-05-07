package com.anysoftkeyboard.keyboards.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieComposition;
import com.menny.android.anysoftkeyboard.R;

public class VoiceHotKeyStateView extends AnyKeyboardViewBase {
    private LottieDrawable lottieRecording;
    private LottieDrawable lottieTranscribing;
    private LottieDrawable lottieAI;

    public enum VoiceHotKeyState { IDLE, RECORDING, TRANSCRIBING, AI_TRANSCRIBING }

    private VoiceHotKeyState currentState = VoiceHotKeyState.IDLE;

    public VoiceHotKeyStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VoiceHotKeyStateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        LottieCompositionFactory.fromRawRes(context, R.raw.lottie_recording)
                .addListener(composition -> {
                    lottieRecording = createDrawable(composition);
                    requestLayout(); // trigger onSizeChanged
                });

        LottieCompositionFactory.fromRawRes(context, R.raw.lottie_transcribing)
                .addListener(composition -> {
                    lottieTranscribing = createDrawable(composition);
                    requestLayout();
                });

        LottieCompositionFactory.fromRawRes(context, R.raw.lottie_ai)
                .addListener(composition -> {
                    lottieAI = createDrawable(composition);
                    requestLayout();
                });
    }

    private LottieDrawable createDrawable(LottieComposition composition) {
        LottieDrawable drawable = new LottieDrawable();
        drawable.setComposition(composition);
        drawable.setRepeatCount(LottieDrawable.INFINITE);
        drawable.playAnimation();
        return drawable;
    }

    public void setState(VoiceHotKeyState state) {
        currentState = state;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateDrawableBounds(lottieRecording, w, h);
        updateDrawableBounds(lottieTranscribing, w, h);
        updateDrawableBounds(lottieAI, w, h);
    }

    private void updateDrawableBounds(LottieDrawable drawable, int viewWidth, int viewHeight) {
        if (drawable == null || drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) return;

        int dw = drawable.getIntrinsicWidth();
        int dh = drawable.getIntrinsicHeight();

        float scale = Math.min((float) viewWidth / dw, (float) viewHeight / dh);
        int scaledWidth = (int) (dw * scale);
        int scaledHeight = (int) (dh * scale);

        int left = (viewWidth - scaledWidth) / 2;
        int top = (viewHeight - scaledHeight) / 2;
        int right = left + scaledWidth;
        int bottom = top + scaledHeight;

        drawable.setBounds(left, top, right, bottom);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        LottieDrawable toDraw = switch (currentState) {
            case RECORDING -> lottieRecording;
            case TRANSCRIBING -> lottieTranscribing;
            case AI_TRANSCRIBING -> lottieAI;
            default -> null;
        };

        if (toDraw != null) {
            int canvasWidth = getWidth();
            int canvasHeight = getHeight();
            int drawableWidth = toDraw.getIntrinsicWidth();
            int drawableHeight = toDraw.getIntrinsicHeight();

            if (drawableWidth > 0 && drawableHeight > 0) {
                float scale = Math.min(
                        (float) canvasWidth / drawableWidth,
                        (float) canvasHeight / drawableHeight
                );

                int saveCount = canvas.save();

                float dx = (canvasWidth - drawableWidth * scale) / 2f;
                float dy = (canvasHeight - drawableHeight * scale) / 2f;
                canvas.translate(dx, dy);
                canvas.scale(scale, scale);

                toDraw.draw(canvas);

                canvas.restoreToCount(saveCount);
            }

            if (toDraw.isAnimating()) {
                invalidate();
            }
        }
    }
}
