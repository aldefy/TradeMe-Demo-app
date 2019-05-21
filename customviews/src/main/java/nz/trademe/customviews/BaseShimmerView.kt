package nz.trademe.customviews

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.LinearLayout

class BaseShimmerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    init {
        orientation = VERTICAL
    }

    companion object {
        private val TAG = "LifeShimmerView"
        private val DST_IN_PORTER_DUFF_XFERMODE = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

        private fun clamp(min: Float, max: Float, value: Float): Float {
            return Math.min(max, Math.max(min, value))
        }

        /**
         * Creates a bitmap with the given width and height.
         *
         *
         * If it fails with an OutOfMemory error, it will force a GC and then try to create the bitmap
         * one more time.
         *
         * @param width  width of the bitmap
         * @param height height of the bitmap
         */
        protected fun createBitmapAndGcIfNecessary(width: Int, height: Int): Bitmap {
            try {
                return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            } catch (e: OutOfMemoryError) {
                System.gc()
                return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            }
        }
    }

    private val mAlphaPaint: Paint
    private val mMaskPaint: Paint

    private val mMask: Mask
    private var mMaskTranslation: MaskTranslation? = null

    private var mRenderMaskBitmap: Bitmap? = null
    private var mRenderUnmaskBitmap: Bitmap? = null

    /**
     * Is 'auto start' enabled for this layout. When auto start is enabled, the layout will start animating automatically
     * whenever it is attached to the current window.
     *
     * @return True if 'auto start' is enabled, false otherwise
     */
    /**
     * Enable or disable 'auto start' for this layout. When auto start is enabled, the layout will start animating
     * automatically whenever it is attached to the current window.
     *
     * @param autoStart Whether auto start should be enabled or not
     */
    var isAutoStart: Boolean = false
        set(autoStart) {
            field = autoStart
            resetAll()
        }
    /**
     * Get the duration of the current animation i.e. the time it takes for the highlight to move from one end
     * of the layout to the other. The default value is 1000 ms.
     *
     * @return Duration of the animation, in milliseconds
     */
    /**
     * Set the duration of the animation i.e. the time it will take for the highlight to move from one end of the layout
     * to the other.
     *
     * @param duration Duration of the animation, in milliseconds
     */
    var duration = 2000
        set(duration) {
            field = duration
            resetAll()
        }
    /**
     * Get the number of times of the current animation will repeat. The default value is -1, which means the animation
     * will repeat indefinitely.
     *
     * @return Number of times the current animation will repeat, or -1 for indefinite.
     */
    /**
     * Set the number of times the animation should repeat. If the repeat count is 0, the animation stops after reaching
     * the end. If greater than 0, or -1 (for infinite), the repeat mode is taken into account.
     *
     * @param repeatCount Number of times the current animation should repeat, or -1 for indefinite.
     */
    var repeatCount = ValueAnimator.INFINITE
        set(repeatCount) {
            field = repeatCount
            resetAll()
        }
    /**
     * Get the delay after which the current animation will repeat. The default value is 0, which means the animation
     * will repeat immediately, unless it has ended.
     *
     * @return Delay after which the current animation will repeat, in milliseconds.
     */
    /**
     * Set the delay after which the animation repeat, unless it has ended.
     *
     * @param repeatDelay Delay after which the animation should repeat, in milliseconds.
     */
    var repeatDelay = 0
        set(repeatDelay) {
            field = repeatDelay
            resetAll()
        }
    /**
     * Get what the current animation will do after reaching the end. One of
     * [REVERSE](http://developer.android.com/reference/android/animation/ValueAnimator.html#REVERSE) or
     * [RESTART](http://developer.android.com/reference/android/animation/ValueAnimator.html#RESTART)
     *
     * @return Repeat mode of the current animation
     */
    /**
     * Set what the animation should do after reaching the end. One of
     * [REVERSE](http://developer.android.com/reference/android/animation/ValueAnimator.html#REVERSE) or
     * [RESTART](http://developer.android.com/reference/android/animation/ValueAnimator.html#RESTART)
     *
     * @param repeatMode Repeat mode of the animation
     */
    var repeatMode: Int = 0
        set(repeatMode) {
            field = repeatMode
            resetAll()
        }

    private var mMaskOffsetX: Int = 0
    private var mMaskOffsetY: Int = 0

    /**
     * Whether the shimmer animation is currently underway.
     *
     * @return True if the shimmer animation is playing, false otherwise.
     */
    var isAnimationStarted: Boolean = false
        private set
    private var mGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    private var mAnimator: ValueAnimator? = null
    private var mMaskBitmap: Bitmap? = null

    /**
     * Get the alpha currently used to render the base view i.e. the unhighlighted view over which the highlight is drawn.
     *
     * @return Alpha (opacity) of the base view
     */
    /**
     * Set the alpha to be used to render the base view i.e. the unhighlighted view over which the highlight is drawn.
     *
     * @param alpha Alpha (opacity) of the base view
     */
    var baseAlpha: Float
        get() = mAlphaPaint.alpha.toFloat() / 0xff
        set(alpha) {
            mAlphaPaint.alpha = (alpha * 0xff).toInt()
            resetAll()
        }


    /**
     * Get the angle at which the highlight mask is animated. One of:
     *
     *  * [MaskAngle.CW_0] which animates left to right,
     *  * [MaskAngle.CW_90] which animates top to bottom,
     *  * [MaskAngle.CW_180] which animates right to left, or
     *  * [MaskAngle.CW_270] which animates bottom to top
     *
     *
     * @return The [MaskAngle] of the current animation
     */
    /**
     * Set the angle of the highlight mask animation. One of:
     *
     *  * [MaskAngle.CW_0] which animates left to right,
     *  * [MaskAngle.CW_90] which animates top to bottom,
     *  * [MaskAngle.CW_180] which animates right to left, or
     *  * [MaskAngle.CW_270] which animates bottom to top
     *
     *
     * @param angle The [MaskAngle] of the new animation
     */
    var angle: MaskAngle?
        get() = mMask.angle
        set(angle) {
            mMask.angle = angle
            resetAll()
        }

    /**
     * Get the dropoff of the current animation's highlight mask. Dropoff controls the size of the fading edge of the
     * highlight.
     *
     *
     * The default value of dropoff is 0.5.
     *
     * @return Dropoff of the highlight mask
     */
    /**
     * Set the dropoff of the animation's highlight mask, which defines the size of the highlight's fading edge.
     *
     *
     * It is the relative distance from the center at which the highlight mask's opacity is 0 i.e it is fully transparent.
     * For a linear mask, the distance is relative to the center towards the edges. For a radial mask, the distance is
     * relative to the center towards the circumference. So a dropoff of 0.5 on a linear mask will create a band that
     * is half the size of the corresponding edge (depending on the [MaskAngle]), centered in the layout.
     *
     * @param dropoff
     */
    var dropoff: Float
        get() = mMask.dropoff
        set(dropoff) {
            mMask.dropoff = dropoff
            resetAll()
        }

    /**
     * Get the fixed width of the highlight mask, or 0 if it is not set. By default it is 0.
     *
     * @return The width of the highlight mask if set, in pixels.
     */
    /**
     * Set the fixed width of the highlight mask, regardless of the size of the layout.
     *
     * @param fixedWidth The width of the highlight mask in pixels.
     */
    var fixedWidth: Int
        get() = mMask.fixedWidth
        set(fixedWidth) {
            mMask.fixedWidth = fixedWidth
            resetAll()
        }

    /**
     * Get the fixed height of the highlight mask, or 0 if it is not set. By default it is 0.
     *
     * @return The height of the highlight mask if set, in pixels.
     */
    /**
     * Set the fixed height of the highlight mask, regardless of the size of the layout.
     *
     * @param fixedHeight The height of the highlight mask in pixels.
     */
    var fixedHeight: Int
        get() = mMask.fixedHeight
        set(fixedHeight) {
            mMask.fixedHeight = fixedHeight
            resetAll()
        }

    /**
     * Get the intensity of the highlight mask, in the [0..1] range. The intensity controls the brightness of the
     * highlight; the higher it is, the greater is the opaque region in the highlight. The default value is 0.
     *
     * @return The intensity of the highlight mask
     */
    /**
     * Set the intensity of the highlight mask, in the [0..1] range.
     *
     *
     * Intensity is the point relative to the center where opacity starts dropping off, so an intensity of 0 would mean
     * that the highlight starts becoming translucent immediately from the center (the spread is controlled by 'dropoff').
     *
     * @param intensity The intensity of the highlight mask.
     */
    var intensity: Float
        get() = mMask.intensity
        set(intensity) {
            mMask.intensity = intensity
            resetAll()
        }

    /**
     * Get the width of the highlight mask relative to the layout's width. The default is 1.0, meaning that the mask is
     * of the same width as the layout.
     *
     * @return Relative width of the highlight mask.
     */
    val relativeWidth: Float
        get() = mMask.relativeWidth

    /**
     * Get the height of the highlight mask relative to the layout's height. The default is 1.0, meaning that the mask is
     * of the same height as the layout.
     *
     * @return Relative height of the highlight mask.
     */
    val relativeHeight: Float
        get() = mMask.relativeHeight

    /**
     * Get the tilt angle of the highlight, in degrees. The default value is 20.
     *
     * @return The highlight's tilt angle, in degrees.
     */
    /**
     * Set the tile angle of the highlight, in degrees.
     *
     * @param tilt The highlight's tilt angle, in degrees.
     */
    var tilt: Float
        get() = mMask.tilt
        set(tilt) {
            mMask.tilt = tilt
            resetAll()
        }

    // Return the mask bitmap, creating it if necessary.
    private// We need to increase the rect size to account for the tilt
    val maskBitmap: Bitmap?
        get() {
            if (mMaskBitmap != null) {
                return mMaskBitmap
            }

            val width = mMask.maskWidth(width)
            val height = mMask.maskHeight(height)

            mMaskBitmap = createBitmapAndGcIfNecessary(width, height)
            val canvas = Canvas(mMaskBitmap!!)
            val gradient: Shader
            val x1: Int
            val y1: Int
            val x2: Int
            val y2: Int
            when (mMask.angle) {
                MaskAngle.CW_0 -> {
                    x1 = 0
                    y1 = 0
                    x2 = width
                    y2 = 0
                }
                MaskAngle.CW_90 -> {
                    x1 = 0
                    y1 = 0
                    x2 = 0
                    y2 = height
                }
                MaskAngle.CW_180 -> {
                    x1 = width
                    y1 = 0
                    x2 = 0
                    y2 = 0
                }
                MaskAngle.CW_270 -> {
                    x1 = 0
                    y1 = height
                    x2 = 0
                    y2 = 0
                }
                else -> {
                    x1 = 0
                    y1 = 0
                    x2 = width
                    y2 = 0
                }
            }
            gradient = LinearGradient(
                x1.toFloat(), y1.toFloat(),
                x2.toFloat(), y2.toFloat(),
                mMask.gradientColors,
                mMask.gradientPositions,
                Shader.TileMode.REPEAT)

            canvas.rotate(mMask.tilt, (width / 2).toFloat(), (height / 2).toFloat())
            val paint = Paint()
            paint.shader = gradient
            val padding = (Math.sqrt(2.0) * Math.max(width, height)).toInt() / 2
            canvas.drawRect((-padding).toFloat(), (-padding).toFloat(), (width + padding).toFloat(), (height + padding).toFloat(), paint)

            return mMaskBitmap
        }

    // Get the shimmer <a href="http://developer.android.com/reference/android/animation/Animator.html">Animator</a>
    // object, which is responsible for driving the highlight mask animation.
    private val shimmerAnimation: Animator
        get() {
            if (mAnimator != null) {
                return mAnimator!!
            }
            val width = width
            val height = height
            when (mMask.angle) {
                MaskAngle.CW_0 -> mMaskTranslation!![-width, 0, width] = 0
                MaskAngle.CW_90 -> mMaskTranslation!![0, -height, 0] = height
                MaskAngle.CW_180 -> mMaskTranslation!![width, 0, -width] = 0
                MaskAngle.CW_270 -> mMaskTranslation!![0, height, 0] = -height
                else -> mMaskTranslation!![-width, 0, width] = 0
            }

            mAnimator = ValueAnimator.ofFloat(0.0f, 1.0f + repeatDelay.toFloat() / duration)
            mAnimator!!.duration = (duration + repeatDelay).toLong()
            mAnimator!!.repeatCount = repeatCount
            mAnimator!!.repeatMode = repeatMode
            mAnimator!!.addUpdateListener { animation ->
                val value = Math.max(0.0f, Math.min(1.0f, animation.animatedValue as Float))
                setMaskOffsetX((mMaskTranslation!!.fromX * (1 - value) + mMaskTranslation!!.toX * value).toInt())
                setMaskOffsetY((mMaskTranslation!!.fromY * (1 - value) + mMaskTranslation!!.toY * value).toInt())
            }

            return mAnimator!!
        }


    // unimportant code

    private val layoutListener: ViewTreeObserver.OnGlobalLayoutListener
        get() = ViewTreeObserver.OnGlobalLayoutListener {
            val animationStarted = isAnimationStarted
            resetAll()
            if (isAutoStart || animationStarted) {
                startShimmerAnimation()
            }
        }

    // enum specifying the shape of the highlight mask applied to the contained view
    enum class MaskShape {
        LINEAR,
        RADIAL
    }

    // enum controlling the angle of the highlight mask animation
    enum class MaskAngle {
        CW_0, // left to right
        CW_90, // top to bottom
        CW_180, // right to left
        CW_270
        // bottom to top
    }

    // struct storing various mask related parameters, which are used to construct the mask bitmap
    private class Mask {

        var angle: MaskAngle? = null
        var tilt: Float = 0.toFloat()
        var dropoff: Float = 0.toFloat()
        var fixedWidth: Int = 0
        var fixedHeight: Int = 0
        var intensity: Float = 0.toFloat()
        var relativeWidth: Float = 0.toFloat()
        var relativeHeight: Float = 0.toFloat()
        var shape: MaskShape? = null

        /**
         * Get the array of colors to be distributed along the gradient of the mask bitmap
         *
         * @return An array of black and transparent colors
         */
        val gradientColors: IntArray
            get() = intArrayOf(Color.GRAY, Color.TRANSPARENT, Color.TRANSPARENT, Color.GRAY)

        /**
         * Get the array of relative positions [0..1] of each corresponding color in the colors array
         *
         * @return A array of float values in the [0..1] range
         */
        val gradientPositions: FloatArray
            get() =
                floatArrayOf(Math.max((1.0f - intensity - dropoff) / 2, 0.0f), Math.max((1.0f - intensity) / 2, 0.0f), Math.min((1.0f + intensity) / 2, 1.0f), Math.min((1.0f + intensity + dropoff) / 2, 1.0f))

        fun maskWidth(width: Int): Int {
            return if (fixedWidth > 0) fixedWidth else (width * relativeWidth).toInt()
        }

        fun maskHeight(height: Int): Int {
            return if (fixedHeight > 0) fixedHeight else (height * relativeHeight).toInt()
        }
    }

    // struct for storing the mask translation animation values
    private class MaskTranslation {

        var fromX: Int = 0
        var fromY: Int = 0
        var toX: Int = 0
        var toY: Int = 0

        operator fun set(fromX: Int, fromY: Int, toX: Int, toY: Int) {
            this.fromX = fromX
            this.fromY = fromY
            this.toX = toX
            this.toY = toY
        }

    }

    init {

        setWillNotDraw(false)

        mMask = Mask()
        mAlphaPaint = Paint()
        mMaskPaint = Paint()
        mMaskPaint.isAntiAlias = true

        mMaskPaint.isDither = true
        mMaskPaint.xfermode = DST_IN_PORTER_DUFF_XFERMODE
        //dependent on dither and xfermode
        mMaskPaint.isFilterBitmap = true

        useDefaults()

    }

    /**
     * Resets the layout to its default state. Any parameters that were set or modified will be reverted back to their
     * original value. Also, stops the shimmer animation if it is currently playing.
     */
    fun useDefaults() {
        // Set defaults
        isAutoStart = true
        duration = duration
        repeatCount = ObjectAnimator.INFINITE
        repeatDelay = 0
        repeatMode = ObjectAnimator.RESTART

        mMask.angle = MaskAngle.CW_0
        mMask.dropoff = 0.9f   // width of moving with area
        mMask.fixedWidth = 0
        mMask.fixedHeight = 0
        mMask.intensity = 0.0f
        mMask.relativeWidth = 1.0f
        mMask.relativeHeight = 1.0f
        mMask.tilt = 0f


        mMaskTranslation = MaskTranslation()

        baseAlpha = 0.8f

        resetAll()
    }

    /**
     * Set the width of the highlight mask relative to the layout's width, in the [0..1] range.
     *
     * @param relativeWidth Relative width of the highlight mask.
     */
    fun setRelativeWidth(relativeWidth: Int) {
        mMask.relativeWidth = relativeWidth.toFloat()
        resetAll()
    }

    /**
     * Set the height of the highlight mask relative to the layout's height, in the [0..1] range.
     *
     * @param relativeHeight Relative height of the highlight mask.
     */
    fun setRelativeHeight(relativeHeight: Int) {
        mMask.relativeHeight = relativeHeight.toFloat()
        resetAll()
    }

    /**
     * Start the shimmer animation. If the 'auto start' property is set, this method is called automatically when the
     * layout is attached to the current window. Calling this method has no effect if the animation is already playing.
     */
    fun startShimmerAnimation() {
        if (isAnimationStarted) {
            return
        }
        val animator = shimmerAnimation
        animator.start()
        isAnimationStarted = true
    }

    /**
     * Stop the shimmer animation. Calling this method has no effect if the animation hasn't been started yet.
     */
    fun stopShimmerAnimation() {
        if (mAnimator != null) {
            mAnimator!!.end()
            mAnimator!!.removeAllUpdateListeners()
            mAnimator!!.cancel()
        }
        mAnimator = null
        isAnimationStarted = false
    }

    /**
     * Translate the mask offset horizontally. Used by the animator.
     *
     * @param maskOffsetX Horizontal translation offset of the mask
     */
    private fun setMaskOffsetX(maskOffsetX: Int) {
        if (mMaskOffsetX == maskOffsetX) {
            return
        }
        mMaskOffsetX = maskOffsetX
        invalidate()
    }

    /**
     * Translate the mask offset vertically. Used by the animator.
     *
     * @param maskOffsetY Vertical translation offset of the mask
     */
    private fun setMaskOffsetY(maskOffsetY: Int) {
        if (mMaskOffsetY == maskOffsetY) {
            return
        }
        mMaskOffsetY = maskOffsetY
        invalidate()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (mGlobalLayoutListener == null) {
            mGlobalLayoutListener = layoutListener
        }
        viewTreeObserver.addOnGlobalLayoutListener(mGlobalLayoutListener)
    }


    override fun dispatchDraw(canvas: Canvas) {
        if (!isAnimationStarted || width <= 0 || height <= 0) {
            super.dispatchDraw(canvas)
            return
        }
        dispatchDrawUsingBitmap(canvas)
    }

    /**
     * Draws and masks the children using a Bitmap.
     *
     * @param canvas Canvas that the masked children will end up being drawn to.
     */
    private fun dispatchDrawUsingBitmap(canvas: Canvas): Boolean {
        val unmaskBitmap = tryObtainRenderUnmaskBitmap()
        val maskBitmap = tryObtainRenderMaskBitmap()
        if (unmaskBitmap == null || maskBitmap == null) {
            return false
        }
        // First draw a desaturated version
        drawUnmasked(Canvas(unmaskBitmap))
        canvas.drawBitmap(unmaskBitmap, 0f, 0f, mAlphaPaint)

        // Then draw the masked version
        drawMasked(Canvas(maskBitmap))
        canvas.drawBitmap(maskBitmap, 0f, 0f, null)

        return true
    }

    private fun tryObtainRenderUnmaskBitmap(): Bitmap? {
        if (mRenderUnmaskBitmap == null) {
            mRenderUnmaskBitmap = tryCreateRenderBitmap()
        }
        return mRenderUnmaskBitmap
    }

    private fun tryObtainRenderMaskBitmap(): Bitmap? {
        if (mRenderMaskBitmap == null) {
            mRenderMaskBitmap = tryCreateRenderBitmap()
        }
        return mRenderMaskBitmap
    }

    private fun tryCreateRenderBitmap(): Bitmap? {
        val width = width
        val height = height
        try {
            return createBitmapAndGcIfNecessary(width, height)
        } catch (error: OutOfMemoryError) {
            /*String logMessage = "ShimmerFrameLayout failed to create working bitmap";
            StringBuilder logMessageStringBuilder = new StringBuilder(logMessage);
            logMessageStringBuilder.append(" (width = ");
            logMessageStringBuilder.append(width);
            logMessageStringBuilder.append(", height = ");
            logMessageStringBuilder.append(height);
            logMessageStringBuilder.append(")\n\n");
            for (StackTraceElement stackTraceElement :
                    Thread.currentThread().getStackTrace()) {
                logMessageStringBuilder.append(stackTraceElement.toString());
                logMessageStringBuilder.append("\n");
            }
            logMessage = logMessageStringBuilder.toString();
            Log.d(TAG, logMessage);*/
            Log.d(TAG, error.toString())
        }

        return null
    }

    // Draws the children without any mask.
    private fun drawUnmasked(renderCanvas: Canvas) {
        renderCanvas.drawColor(0, PorterDuff.Mode.CLEAR)  // remove the color
        super.dispatchDraw(renderCanvas)
    }

    // Draws the children and masks them on the given Canvas.
    private fun drawMasked(renderCanvas: Canvas) {
        val maskBitmap = maskBitmap ?: return

        renderCanvas.clipRect(
            mMaskOffsetX,
            mMaskOffsetY,
            mMaskOffsetX + maskBitmap.width,
            mMaskOffsetY + maskBitmap.height)
        renderCanvas.drawColor(0, PorterDuff.Mode.CLEAR)
        super.dispatchDraw(renderCanvas)

        renderCanvas.drawBitmap(maskBitmap, mMaskOffsetX.toFloat(), mMaskOffsetY.toFloat(), mMaskPaint)
    }

    private fun resetAll() {
        stopShimmerAnimation()
        resetMaskBitmap()
        resetRenderedView()
    }

    // If a mask bitmap was created, it's recycled and set to null so it will be recreated when needed.
    private fun resetMaskBitmap() {
        if (mMaskBitmap != null) {
            mMaskBitmap!!.recycle()
            mMaskBitmap = null
        }
    }

    // If a working bitmap was created, it's recycled and set to null so it will be recreated when needed.
    private fun resetRenderedView() {
        if (mRenderUnmaskBitmap != null) {
            mRenderUnmaskBitmap!!.recycle()
            mRenderUnmaskBitmap = null
        }

        if (mRenderMaskBitmap != null) {
            mRenderMaskBitmap!!.recycle()
            mRenderMaskBitmap = null
        }
    }

    override fun onDetachedFromWindow() {
        stopShimmerAnimation()
        if (mGlobalLayoutListener != null) {
            viewTreeObserver.removeGlobalOnLayoutListener(mGlobalLayoutListener)
            mGlobalLayoutListener = null
        }
        super.onDetachedFromWindow()
    }
}
