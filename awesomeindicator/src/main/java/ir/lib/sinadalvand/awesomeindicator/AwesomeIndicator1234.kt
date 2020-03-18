package ir.lib.sinadalvand.awesomeindicator

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import ir.lib.sinadalvand.awesomeindicator.Pager.AwesomeViewPager

class AwesomeIndicator1234 @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), ViewPager.OnPageChangeListener, ViewPager.OnAdapterChangeListener {


    private var morph: Float = 0f
    private var currentPosition = 0
    private var selectedPosition = 0
    private var color: Int = 0
    private var rotate: Float = 0f
    private var switchStatus = false

    private val feature = Indicatorfeature()
    private val drawer: IndicatorDrawer? =
            IndicatorDrawer(feature)

    init {
        AttrsInitialize(context, attrs!!, feature)
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }


    /*  View Override Method   */

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        // default width of view
        val w = (2 * feature.padding + feature.radius) * feature.count + (feature.padding * 2)

        // default height of view
        val h = feature.radius * 5

        setMeasuredDimension(manageDimension(widthMeasureSpec, w.toInt()), manageDimension(heightMeasureSpec, h.toInt()))
    }

    private fun manageDimension(valueMeasureSpec: Int, value_desired: Int): Int {
        var valueFinal = 0
        val value = View.MeasureSpec.getSize(valueMeasureSpec)
        val valueMode = View.MeasureSpec.getMode(valueMeasureSpec)
        if (valueMode == View.MeasureSpec.EXACTLY) {
            valueFinal = value
        } else if (valueMode == View.MeasureSpec.AT_MOST) {
            valueFinal = Math.min(value, value_desired)
        } else {
            valueFinal = value_desired
        }
        return valueFinal
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (feature.count > 0 && feature.indicators.size == 0)
            indicatorGenration()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {


        drawer?.draw(currentPosition, morph, canvas!!, color, rotate)


    }


    /* Implemented Method   */

    override fun onAdapterChanged(viewPager: ViewPager, oldAdapter: PagerAdapter?, newAdapter: PagerAdapter?) {

    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        currentPosition = selectedPosition

        if (positionOffset * 2 < 1)
            morph = positionOffset * 2

        if (positionOffset * 2 > 1)
            morph = (1 - positionOffset) * 2

        if (selectedPosition == position && positionOffset * 2 > 1) {
            currentPosition = selectedPosition + 1
        }


        if (selectedPosition > position && positionOffset * 2 < 1) {
            currentPosition = selectedPosition - 1
        }

        if (feature.rtl) {
            currentPosition = (feature.count - 1) - currentPosition

        }


        val from = FloatArray(3)
        val to = FloatArray(3)
        if ((position == 0 || position == 2)) {
            Color.colorToHSV(Color.parseColor("#FFFFFF"), from)   // from white
            Color.colorToHSV(Color.parseColor("#263238"), to)     // to red
        } else {
            Color.colorToHSV(Color.parseColor("#263238"), from)     // to red
            Color.colorToHSV(Color.parseColor("#FFFFFF"), to)   // from white
        }
        val morphColor = if (positionOffset > 0.99) 1f else positionOffset
        val hsv = FloatArray(3)
        hsv[0] = from[0] + (to[0] - from[0]) * morphColor
        hsv[1] = from[1] + (to[1] - from[1]) * morphColor
        hsv[2] = from[2] + (to[2] - from[2]) * morphColor
        color = Color.HSVToColor(hsv)





        invalidate()


    }

    override fun onPageSelected(position: Int) {
        selectedPosition = position

        if (position == feature.count - 1) {
            feature.viewPager?.setTouchDisable(true)
//            startAnimation()
        }

        if (position != feature.count - 1) {
            stopAnimation()
        }

    }


    /* Defined Method   */

    // set require data to view from Viewpager

    private fun indicatorGenration() {
        var size = feature.padding
        val aray = arrayListOf<Indicator>()
        for (num in 0 until feature.count) {
            size += feature.padding + (feature.radius / 2)
            aray.add(
                    Indicator(
                            feature.viewPager!!.currentItem == num,
                            size,
                            height / 2.toFloat()
                    )
            )
            size += feature.padding + (feature.radius / 2)
        }
        feature.indicators = aray
    }

    // get viewpager
    fun setViewPager(@NonNull pager: AwesomeViewPager) {
        feature.viewPager = pager
        feature.count = pager.adapter!!.count
        pager.addOnAdapterChangeListener(this)
        pager.addOnPageChangeListener(this)
        selectedPosition = pager.currentItem
        requestLayout()
    }

    val rotateAnimator = ValueAnimator.ofFloat(0f, 90f)
    var animtor3 = ValueAnimator.ofFloat(0f, 1f)
    var animtor4 = ValueAnimator.ofFloat(1f, 0f)

    fun startAnimation() {
        canceled = true
        rotateAnimator.duration = 500
        rotateAnimator.addUpdateListener {
            rotate = it.animatedValue as Float
            postInvalidate()
        }
        rotateAnimator.start()


        animtor3.duration = 1000
        animtor3.startDelay = 200
        animtor3.addUpdateListener {
            morph = it.animatedValue as Float
            postInvalidate()
        }
        animtor3.start()


        animtor4.duration = 600
        animtor4.addUpdateListener {
            morph = it.animatedValue as Float
            postInvalidate()
        }




        animtor3.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                Log.e("APP", "This animation Ended")
                if (!canceled) {
                    switcher()
                    animtor4.start()
                }
            }

            override fun onAnimationCancel(animation: Animator?) {
                Log.e("APP", "This animation Cancelled")
            }

            override fun onAnimationStart(animation: Animator?) {

            }

        })

        animtor4.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                Log.e("APP", "This animation Ended")
                if (!canceled)
                    animtor3.start()
            }

            override fun onAnimationCancel(animation: Animator?) {
                Log.e("APP", "This animation Cancelled")
            }

            override fun onAnimationStart(animation: Animator?) {

            }

        })
    }

    var canceled = false;

    private fun stopAnimation() {
        canceled = true
        rotateAnimator.cancel()
        animtor3.cancel()
        animtor4.cancel()
    }

    fun setSelectedColor(color: Int) {
        feature.unselectedColor = color
        invalidate()
    }

    fun setUnSelectedColor(color: Int) {
        feature.selectedColor = color
        invalidate()
    }


    fun switcher() {
        if (switchStatus && (currentPosition in 0..(feature.count - 1))) if (feature.rtl) currentPosition-- else currentPosition++ else if (currentPosition in 0..(feature.count - 1)) if (feature.rtl) currentPosition++ else currentPosition--
        if (currentPosition == 0) switchStatus = !feature.rtl
        if (currentPosition == feature.count - 1) switchStatus = feature.rtl
    }

}