package com.example.search_media.list.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.search_media.databinding.ItemVideoBinding
import com.example.search_media.model.ImageItem
import com.example.search_media.model.ListItem
import com.example.search_media.model.VideoItem

class VideoViewHolder (
    private val binding: ItemVideoBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind (item : ListItem) {
        item as VideoItem
        binding.item = item
    }
}