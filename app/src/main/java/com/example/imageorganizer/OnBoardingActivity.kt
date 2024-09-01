package com.example.imageorganizer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var onBoardingItemAdapter: OnBoardingItemAdapter
    private lateinit var indicatorsContainer : LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        setOnboardingItems()
        setupIndicators()
        setCurrentIndicators(0)
    }

    private fun setOnboardingItems (){
        onBoardingItemAdapter = OnBoardingItemAdapter(
            listOf(
                OnboardingItem(
                    onboardingImage = R.drawable.app_logo,
                    title = "Welcome to MemorizeIt",
                    description = "Your personal photo organizer. Effortlessly manage and sort your memories," +
                            " ensuring every special moment is just a tap away."
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.sort_events,
                    title = "Organize by Events",
                    description = "Automatically group your photos by events, making it easy to" +
                            " relive your favorite moments, one event at a time."
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.organize,
                    title = "Sort by Faces",
                    description = "Our smart face detection groups your photos by the people in them, " +
                            "so you can quickly find pictures of those who matter most."
                )

            )
        )

        val onboardingViewPager = findViewById<ViewPager2>(R.id.onBoardingViewPager)
        onboardingViewPager.adapter = onBoardingItemAdapter
        onboardingViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicators(position)
            }
        })
        (onboardingViewPager.getChildAt(0) as RecyclerView).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER
        findViewById<ImageView>(R.id.imageNext).setOnClickListener{
            if (onboardingViewPager.currentItem + 1 < onBoardingItemAdapter.itemCount){
                onboardingViewPager.currentItem +=1
            }else{
                navigationToHomeActivity()
            }

        }
        findViewById<TextView>(R.id.textSkip).setOnClickListener {
            navigationToHomeActivity()
        }
        findViewById<MaterialButton>(R.id.btnGetStarted).setOnClickListener {
            navigationToHomeActivity()
        }
    }

    private fun navigationToHomeActivity(){
        startActivity(Intent(applicationContext , SignUpActivity::class.java))
        finish()
    }
    private fun setupIndicators(){
        indicatorsContainer = findViewById(R.id.indicatorsContainer)
        val indicators = arrayOfNulls<ImageView>(onBoardingItemAdapter.itemCount)

        val layoutParams  : LinearLayout.LayoutParams  =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(8 , 0 , 8 , 0)
        for (i in indicators .indices){
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext ,
                        R.drawable.indicator_inactive_background

                    )

                )
                it.layoutParams = layoutParams
                indicatorsContainer.addView(it)
            }
        }

    }
    private fun setCurrentIndicators(position : Int){
        val childCount = indicatorsContainer.childCount
        for (i in 0 until childCount){
            val imageView = indicatorsContainer.getChildAt(i) as ImageView
            if( i == position){
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                    applicationContext ,
                    R.drawable.indicator_active_background
                )
                )

            }else{
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_background
                    )
                )
            }
        }

    }
}