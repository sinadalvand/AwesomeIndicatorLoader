package ir.lib.sinadalvand.awesomeindicator

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

class IndicatorDrawer constructor(val feature: Indicatorfeature) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    fun draw(position: Int, percent: Float, canvas: Canvas, color: Int, rotate: Float = 0f) {
        val radius = feature.radius
        val selectedColor = feature.selectedColor
        val unselectedColor = feature.unselectedColor
        val per = 1 - percent
        for (num in 0 until feature.indicators.size) {
            val indicate: Indicator = feature.indicators[num]
            if (num == position) {
                paint.color = selectedColor

                canvas.save()
                canvas.rotate(rotate, indicate.xcoordinate, indicate.ycoordinate)
                canvas.drawRect(
                        RectF(
                                indicate.xcoordinate - (radius * per),
                                indicate.ycoordinate - (radius / 2),
                                indicate.xcoordinate + (radius * per),
                                indicate.ycoordinate + (radius / 2)
                        ), paint
                )
                canvas.drawCircle(indicate.xcoordinate - (radius * per), indicate.ycoordinate, radius / 2, paint)
                canvas.drawCircle(indicate.xcoordinate + (radius * per), indicate.ycoordinate, radius / 2, paint)
                canvas.restore()
            } else {
                paint.color = color
                canvas.drawCircle(indicate.xcoordinate, indicate.ycoordinate, radius / 2, paint)
            }
        }

    }
}