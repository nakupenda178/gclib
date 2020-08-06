package com.github.guqt178.binding.viewadapter.viewpager

import android.databinding.BindingAdapter
import android.support.v4.view.ViewPager
import com.github.guqt178.binding.command.BindingConsumer

@BindingAdapter(
    value = ["onPageScrolledCommand", "onPageSelectedCommand", "onPageScrollStateChangedCommand"],
    requireAll = false
)
fun onScrollChangeCommand(
    viewPager: ViewPager,
    onPageScrolledCommand: BindingConsumer<ViewPagerDataWrapper>?,
    onPageSelectedCommand: BindingConsumer<Int>?,
    onPageScrollStateChangedCommand: BindingConsumer<Int>?
) {
    viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        private var state = 0
        private lateinit var wrapper: ViewPagerDataWrapper
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            if (onPageScrolledCommand != null) {
                if (!this::wrapper.isInitialized) {
                    wrapper = ViewPagerDataWrapper()
                }
                wrapper.position = position.toFloat()
                wrapper.positionOffset = positionOffset
                wrapper.positionOffsetPixels = positionOffsetPixels
                wrapper.state = state
                onPageScrolledCommand.call(wrapper)
            }
        }

        override fun onPageSelected(position: Int) {
            onPageSelectedCommand?.call(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            this.state = state
            onPageScrollStateChangedCommand?.call(state)
        }
    })
}

class ViewPagerDataWrapper {
    var positionOffset = 0f
    var position = 0f
    var positionOffsetPixels = 0
    var state = 0
    override fun toString(): String {
        return "ViewPagerDataWrapper{" +
                "positionOffset=" + positionOffset +
                ", position=" + position +
                ", positionOffsetPixels=" + positionOffsetPixels +
                ", state=" + state +
                '}'
    }
}