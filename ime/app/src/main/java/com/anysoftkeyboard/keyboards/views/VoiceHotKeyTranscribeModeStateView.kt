package com.anysoftkeyboard.keyboards.views

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.core.graphics.withSave
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.airbnb.lottie.LottieListener
import com.menny.android.anysoftkeyboard.R
import kotlin.math.min

open class VoiceHotKeyTranscribeModeStateView : AnyKeyboardViewBase {
    private var currentLottieDrawable: LottieDrawable? = null

    enum class ShortcutActions(val value: Int) {
        RECORD_AND_TRANSCRIBE(0),
        CONVERT_TO_TEXT_START_WITH_LOWER_CASE(1),
        CONVERT_TO_TEXT_ALL_CAPS(2),
        ASK_AI(3),
        ADD_EMOJI(4),
        FIX_GRAMMAR(5),
        ASK_AI_TONE_PROFESSIONAL(6),
        ASK_AI_TONE_FORMAL(7),
        ASK_AI_TONE_FUN(8),
        ASK_AI_TONE_FLIRT(9),
        ASK_AI_TONE_POSITIVE(10),
        ASK_AI_TONE_NEGATIVE(11),
        ASK_AI_TONE_NEUTRAL(12),
        TRANSLATE(13),
        ASK_AI_CUSTOM_PROMPT(14),
        MANAGE_SHORTCUTS(Int.Companion.MAX_VALUE - 1),
        DISMISS(Int.Companion.MAX_VALUE);

        companion object {
            @JvmStatic
            fun fromInt(type: Int): ShortcutActions? {
                return ShortcutActions.entries.associateBy { it.value }[type]
            }
        }
    }

    private var currentState: ShortcutActions? = ShortcutActions.DISMISS

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    private fun createDrawable(composition: LottieComposition?): LottieDrawable {
        val drawable = LottieDrawable()
        drawable.setComposition(composition)
        drawable.addAnimatorListener(object: AnimatorListener {
            override fun onAnimationStart(animation: Animator) { }
            override fun onAnimationCancel(animation: Animator) { }
            override fun onAnimationRepeat(animation: Animator) { }
            override fun onAnimationEnd(animation: Animator) {
                if (drawable == currentLottieDrawable) {
                    currentLottieDrawable = null
                }
            }
        })
        drawable.playAnimation()
        return drawable
    }

    fun forceReloadState(context: Context) {
        currentState?.also {
            setState(context, it)
        }
    }

    fun setState(context: Context, state: ShortcutActions) {
        currentState = state
        val resourceInt = when(state) {
            ShortcutActions.RECORD_AND_TRANSCRIBE -> R.raw.lottie_rocket
            ShortcutActions.CONVERT_TO_TEXT_START_WITH_LOWER_CASE -> R.raw.lottie_rocket
            ShortcutActions.CONVERT_TO_TEXT_ALL_CAPS -> R.raw.lottie_rocket
            ShortcutActions.ASK_AI -> R.raw.lottie_question
            ShortcutActions.ADD_EMOJI -> R.raw.lottie_sparkles
            ShortcutActions.FIX_GRAMMAR -> R.raw.lottie_pencil // should be simple text
            ShortcutActions.ASK_AI_TONE_PROFESSIONAL -> R.raw.lottie_pencil // should be simple text
            ShortcutActions.ASK_AI_TONE_FORMAL -> R.raw.lottie_pencil
            ShortcutActions.ASK_AI_TONE_FUN -> R.raw.lottie_party_pop
            ShortcutActions.ASK_AI_TONE_FLIRT -> R.raw.lottie_flirt
            ShortcutActions.ASK_AI_TONE_POSITIVE -> R.raw.lottie_blush
            ShortcutActions.ASK_AI_TONE_NEGATIVE -> R.raw.lottie_sad
            ShortcutActions.ASK_AI_TONE_NEUTRAL -> R.raw.lottie_neutral
            ShortcutActions.TRANSLATE -> R.raw.lottie_pencil
            ShortcutActions.ASK_AI_CUSTOM_PROMPT -> R.raw.lottie_pencil
            ShortcutActions.MANAGE_SHORTCUTS -> null
            ShortcutActions.DISMISS -> null
        } ?: return

        LottieCompositionFactory.fromRawRes(context, resourceInt)
            .addListener(LottieListener { composition: LottieComposition? ->
                currentLottieDrawable = createDrawable(composition)
                requestLayout() // trigger onSizeChanged
                invalidate()
            })
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        currentLottieDrawable?.also {
            updateDrawableBounds(it, w, h)
        }
    }

    private fun updateDrawableBounds(drawable: LottieDrawable?, viewWidth: Int, viewHeight: Int) {
        if (drawable == null || drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) return

        val dw = drawable.intrinsicWidth
        val dh = drawable.intrinsicHeight

        val scale = min(
            (viewWidth.toFloat() / dw).toDouble(),
            (viewHeight.toFloat() / dh).toDouble()
        ).toFloat()
        val scaledWidth = (dw * scale).toInt()
        val scaledHeight = (dh * scale).toInt()

        val left = (viewWidth - scaledWidth) / 2
        val top = (viewHeight - scaledHeight) / 2
        val right = left + scaledWidth
        val bottom = top + scaledHeight

        drawable.setBounds(left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val toDraw = currentLottieDrawable ?: return

        val canvasWidth = width
        val canvasHeight = height
        val drawableWidth = toDraw.intrinsicWidth
        val drawableHeight = toDraw.intrinsicHeight

        if (drawableWidth > 0 && drawableHeight > 0) {
            val scale = min(
                (canvasWidth.toFloat() / drawableWidth).toDouble(),
                (canvasHeight.toFloat() / drawableHeight).toDouble()
            ).toFloat()

            canvas.withSave {
                val dx = (canvasWidth - drawableWidth * scale) / 2f
                val dy = (canvasHeight - drawableHeight * scale) / 2f
                canvas.translate(dx, dy)
                canvas.scale(scale, scale)

                toDraw.draw(canvas)
            }
        }

        if (toDraw.isAnimating()) {
            invalidate()
        }
    }
}
