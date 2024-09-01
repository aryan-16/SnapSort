package com.example.imageorganizer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OnBoardingItemAdapter(private val onboardingItems : List<OnboardingItem> )  :
    RecyclerView.Adapter<OnBoardingItemAdapter.onboardingItemViewHolder>(){

    inner class onboardingItemViewHolder (view : View) : RecyclerView.ViewHolder(view){
        private val imageOnboarding = view.findViewById<ImageView>(R.id.imageOnboarding)
        private val textTitle = view.findViewById<TextView>(R.id.textTitle)
        private val textDescription = view.findViewById<TextView>(R.id.textDescription)


        fun bind(onboardingItem: OnboardingItem){
            imageOnboarding.setImageResource(onboardingItem.onboardingImage)
            textTitle.text = onboardingItem.title
            textDescription.text = onboardingItem.description

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): onboardingItemViewHolder {
        return onboardingItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.onboarding_item_container , parent , false
            )
        )
    }

    override fun getItemCount(): Int {
        return onboardingItems.size
    }

    override fun onBindViewHolder(holder: onboardingItemViewHolder, position: Int) {
        holder.bind(onboardingItems[position])
    }
}