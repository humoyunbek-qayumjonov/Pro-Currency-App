package com.humoyunbek.valyuta_uzb.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.humoyunbek.valyuta_uzb.CardFragment
import com.humoyunbek.valyuta_uzb.models.TitleModel

class PagerAdapter(var list:ArrayList<TitleModel>,fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return CardFragment.newInstance(list[position].titleName)
    }

}