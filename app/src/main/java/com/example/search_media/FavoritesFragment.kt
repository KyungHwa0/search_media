package com.example.search_media

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.search_media.databinding.FragmentFavoritesBinding
import com.example.search_media.list.ItemHandler
import com.example.search_media.list.ListAdapter
import com.example.search_media.model.ListItem
import com.example.search_media.repository.SearchRepositoryImpl

class FavoritesFragment : Fragment() {
    private var binding : FragmentFavoritesBinding? = null

    private val adapter by lazy { ListAdapter(Handler()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentFavoritesBinding.inflate(inflater, container, false).apply {
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            recyclerView.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.apply {
            if (Common.favoritesList.isEmpty()) {
                emptyTextView.isVisible = true
                recyclerView.isVisible = false
            } else {
                emptyTextView.isVisible = false
                recyclerView.isVisible = true
            }
        }
        adapter.submitList(Common.favoritesList.sortedByDescending { it.dateTime })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    inner class Handler : ItemHandler {
        override fun onClickFavorite(item: ListItem) {
            if(item.isFavorite) {
                (activity as? MainActivity)?.favoritesChangedListener?.onFavoriteDeleted(item)
                adapter.submitList(Common.favoritesList.sortedByDescending { it.dateTime })
            }
        }
    }

}