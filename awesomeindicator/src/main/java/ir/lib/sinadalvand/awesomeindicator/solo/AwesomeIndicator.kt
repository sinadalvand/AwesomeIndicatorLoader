package ir.lib.sinadalvand.awesomeindicator.solo

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import ir.lib.awesomeindicator.R
import ir.lib.sinadalvand.awesomeindicator.solo.ViewUtils.px

class AwesomeIndicator @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private var padding: Float = 5f
    private var radius: Float = 5f
    private var selectedColor: Int
    private var unselectedColor: Int
    private var isRtl = false


    private var indicatorCount = 5
    private var indicatorList: ArrayList<Indicator> = arrayListOf()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var rotate = 0f
    private var morph = 1f

    private val rotateAnimator = ValueAnimator.ofFloat(0f, 100f)
    private val morphAnimator = ValueAnimator.ofFloat(0f, 1f)

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AwesomeIndicator, 0, 0)
        padding = typedArray.getDimension(R.styleable.AwesomeIndicator_indicator_padding, context.resources.getDimension(R.dimen.indicate_default_padding))
        radius = typedArray.getDimension(R.styleable.AwesomeIndicator_indicator_radius, context.resources.getDimension(R.dimen.indicate_default_radius))
        selectedColor = typedArray.getInt(R.styleable.AwesomeIndicator_indicator_selectedColor, ContextCompat.getColor(context, R.color.SelectedColor))
        unselectedColor = typedArray.getInt(R.styleable.AwesomeIndicator_indicator_unselectedColor, ContextCompat.getColor(context, R.color.UnSelectedColor))
        isRtl = TextUtilsCompat.getLayoutDirectionFromLocale(context.resources.configuration.locale) == ViewCompat.LAYOUT_DIRECTION_RTL
        typedArray.recycle()


        setLayerType(LAYER_TYPE_HARDWARE, null)


        paint.color = selectedColor

        rotateAnimator.addUpdateListener { rotate = it.animatedValue as Float; invalidate() }
        morphAnimator.addUpdateListener { morph = it.animatedValue as Float; invalidate() }



    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // default width of view
//        val w = (2 * padding + radius) * indicatorCount + (padding * 2)
        val w = coordinateCalculator(indicatorCount) + (padding * 2)

        // default height of view
        val h = radius * 5

        setMeasuredDimension(ViewUtils.manageDimension(widthMeasureSpec, w.toInt()), ViewUtils.manageDimension(heightMeasureSpec, h.toInt()))
    }


    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (indicatorCount > 0) {
            indicatorList.clear()
            for (i in 0..indicatorCount) {
                indicatorList.add(Indicator(coordinateCalculator(i), height / 2f, 0f, i == 0))
            }
        }

    }


    private fun coordinateCalculator(item: Int): Float {
        val zula = padding + radius

        return ((zula + padding) * item) + zula
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (i in indicatorList)
            if (i.selected) {
                canvas.save()
                canvas.rotate(rotate, i.xcoordinate, i.ycoordinate)
                canvas.drawRect(RectF(i.xcoordinate - (radius * morph), i.ycoordinate - radius, i.xcoordinate + (radius * morph), i.ycoordinate + radius), paint)
                canvas.drawCircle(i.xcoordinate - (radius * morph), i.ycoordinate, radius, paint)
                canvas.drawCircle(i.xcoordinate + (radius * morph), i.ycoordinate, radius, paint)
                canvas.restore()
            } else {
                canvas.drawCircle(i.xcoordinate, i.ycoordinate, radius, paint)
            }

    }
}