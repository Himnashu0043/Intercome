package com.application.intercom.user.walkthrough

import android.view.LayoutInflater
import androidx.viewpager.widget.ViewPager
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.databinding.ActivityWalkThroughBinding

class WalkThroughActivity :
    BaseActivity<ActivityWalkThroughBinding>(),
    ViewPager.OnPageChangeListener {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_walk_through)
//    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun getLayout(): ActivityWalkThroughBinding {
        return ActivityWalkThroughBinding.inflate(layoutInflater)
    }
}