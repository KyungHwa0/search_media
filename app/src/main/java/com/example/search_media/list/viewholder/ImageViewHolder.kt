package com.example.search_media.list.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.search_media.databinding.ItemImageBinding
import com.example.search_media.model.ImageItem
import com.example.search_media.model.ListItem

class ImageViewHolder(
    private val binding: ItemImageBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ListItem) {
        item as ImageItem
        binding.item = item
    }
}