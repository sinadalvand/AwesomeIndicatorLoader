package ir.lib.sinadalvand.awesomeindicator.solo

import android.content.res.Resources
import android.view.View

internal object ViewUtils {
    fun manageDimension(valueMeasureSpec: Int, value_desired: Int): Int {
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

    val Int.dp: Int
        get() = (this / Resources.getSystem().displayMetrics.density).toInt()

    val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()
}