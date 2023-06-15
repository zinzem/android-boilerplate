package com.boilerplate.android.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boilerplate.android.R
import com.boilerplate.android.databinding.SelectorItemDuoBinding
import com.boilerplate.android.databinding.SelectorItemQuadBinding
import com.boilerplate.android.databinding.SelectorItemSingleBinding
import com.boilerplate.android.databinding.SelectorItemTrioBinding
import com.boilerplate.android.views.adapters.SelectorItem.*
import com.boilerplate.android.views.adapters.SelectorItemVH.*

private val diffUtil = object : DiffUtil.ItemCallback<SelectorItem>() {
    override fun areItemsTheSame(oldItem: SelectorItem, newItem: SelectorItem) = oldItem == newItem
    override fun areContentsTheSame(oldItem: SelectorItem, newItem: SelectorItem) = oldItem == newItem
}

class SelectorAdapter : ListAdapter<SelectorItem, SelectorItemVH>(diffUtil) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SingleItem -> R.layout.selector_item_single
            is DuoItem -> R.layout.selector_item_duo
            is TrioItem -> R.layout.selector_item_trio
            else ->  R.layout.selector_item_quad
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectorItemVH {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.selector_item_single -> SingleItemVH(SelectorItemSingleBinding.inflate(inflater, parent, false))
            R.layout.selector_item_duo -> DuoItemVH(SelectorItemDuoBinding.inflate(inflater, parent, false))
            R.layout.selector_item_trio -> TrioItemVH(SelectorItemTrioBinding.inflate(inflater, parent, false))
            else -> QuadItemVH(SelectorItemQuadBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: SelectorItemVH, position: Int) {
        val item = getItem(position)
        when {
            item is QuadItem && holder is QuadItemVH -> holder.bind(
                item.imageId1,
                item.imageId2,
                item.imageId3,
                item.imageId4
            )
        }
    }
}

sealed class SelectorItem {
    data class SingleItem(val iconId: Int) : SelectorItem()
    data class DuoItem(val iconId: Int, val iconId2: Int) : SelectorItem()
    data class TrioItem(val iconId: Int, val iconId2: Int, val iconId3: Int) : SelectorItem()
    data class QuadItem(val imageId1: Int, val imageId2: Int, val imageId3: Int, val imageId4: Int) : SelectorItem()
}

sealed class SelectorItemVH(root: View) : RecyclerView.ViewHolder(root) {
    data class SingleItemVH(val binding: SelectorItemSingleBinding) : SelectorItemVH(binding.root) {
        fun bind() {}
    }

    data class DuoItemVH(val binding: SelectorItemDuoBinding) : SelectorItemVH(binding.root) {
        fun bind() {}
    }

    data class TrioItemVH(val binding: SelectorItemTrioBinding) : SelectorItemVH(binding.root) {
        fun bind() {}
    }

    data class QuadItemVH(val binding: SelectorItemQuadBinding) : SelectorItemVH(binding.root) {
        fun bind(imageId1: Int, imageId2: Int, imageId3: Int, imageId4: Int) = binding.apply {
            image1.setImageResource(imageId1)
            image2.setImageResource(imageId2)
            image3.setImageResource(imageId3)
            image4.setImageResource(imageId4)
        }
    }
}