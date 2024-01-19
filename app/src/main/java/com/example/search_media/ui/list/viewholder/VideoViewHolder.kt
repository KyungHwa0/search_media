package com.example.search_media.ui.list.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.search_media.databinding.ItemVideoBinding
import com.example.search_media.ui.list.ItemHandler
import com.example.search_media.domain.model.ListItem
import com.example.search_media.domain.model.VideoItem

class VideoItemViewHolder(private val binding: ItemVideoBinding,
                          private val itemHandler: ItemHandler? = null) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ListItem) {
        item as VideoItem
        binding.item = item
        binding.handler = itemHandler
    }
}