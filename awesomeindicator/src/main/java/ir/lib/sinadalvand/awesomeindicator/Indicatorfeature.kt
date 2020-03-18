package ir.lib.sinadalvand.awesomeindicator

import ir.lib.sinadalvand.awesomeindicator.Pager.AwesomeViewPager

data class Indicatorfeature(
    var viewPager: AwesomeViewPager? = null, // viewpager
    var count: Int = 3, // count of viewpager items
    var padding: Float = 0f, // padding of each indicator
    var radius: Float = 0f, // radius of each indicator
    var selectedColor: Int = 0,  // selected color indicator
    var unselectedColor: Int = 0, // unselected color indicator
    var rtl: Boolean = false, // rtl mode  0==> ltr   1==> rtl   2==> auto
    var indicators: ArrayList<Indicator> = arrayListOf()
)