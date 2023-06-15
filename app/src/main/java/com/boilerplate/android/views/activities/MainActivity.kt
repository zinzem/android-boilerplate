package com.boilerplate.android.views.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.*
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.boilerplate.android.R
import com.boilerplate.android.core.views.activities.BaseActivity
import com.boilerplate.android.databinding.ActivityMainBinding
import com.boilerplate.android.views.adapters.SelectorAdapter
import com.boilerplate.android.views.adapters.SelectorItem
import com.orhanobut.logger.Logger

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSelector1()
    }

    private fun setupSelector1() {
        val snapHelper = LinearSnapHelper()
        binding.selector1Character.apply {
            snapHelper.attachToRecyclerView(this)
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            adapter = SelectorAdapter().apply {
                submitList(listOf(
                    SelectorItem.SingleItem(R.drawable.baby_daisy),
                    SelectorItem.DuoItem(R.drawable.baby_daisy, R.drawable.baby_peach),
                    SelectorItem.TrioItem(R.drawable.baby_daisy, R.drawable.baby_daisy, R.drawable.baby_daisy),
                    SelectorItem.QuadItem(R.drawable.baby_peach, R.drawable.baby_peach, R.drawable.baby_peach, R.drawable.baby_peach),
                    SelectorItem.DuoItem(R.drawable.baby_daisy, R.drawable.baby_peach),
                    SelectorItem.SingleItem(R.drawable.baby_daisy),
                    SelectorItem.DuoItem(R.drawable.baby_daisy, R.drawable.baby_peach),
                    SelectorItem.SingleItem(R.drawable.baby_daisy),
                ))
            }
            addOnScrollListener(object : OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    snapHelper.findSnapView(layoutManager)?.let {
                        findContainingViewHolder(it)?.adapterPosition?.let {
                            Logger.e("snap pos = $it")
                        }
                    }
                }
            })
        }
    }
}