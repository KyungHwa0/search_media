package com.example.search_media.ui.list.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.search_media.databinding.ItemImageBinding
import com.example.search_media.ui.list.ItemHandler
import com.example.search_media.domain.model.ImageItem
import com.example.search_media.domain.model.ListItem

class ImageItemViewHolder(
    private val binding: ItemImageBinding,
    private val itemHandler: ItemHandler? = null
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ListItem) {
        item as ImageItem
        binding.item = item
        binding.handler = itemHandler
    }
}