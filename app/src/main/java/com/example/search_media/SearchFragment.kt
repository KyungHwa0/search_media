package com.example.search_media

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.search_media.databinding.FragmentSearchResultBinding
import com.example.search_media.list.ItemHandler
import com.example.search_media.list.ListAdapter
import com.example.search_media.model.ListItem
import com.example.search_media.repository.SearchRepositoryImpl

class SearchFragment : Fragment(), MainActivity.FavoritesChangedListener {

    // RetrofitManager의 searchService를 사용하여 SearchRepositoryImpl을 생성
    private val viewModel : SearchViewModel by viewModels {
        SearchViewModel.SearchViewModelFactory(SearchRepositoryImpl(RetrofitManager.searchService))
    }
    // null 로 설정 하므로서 메모리 누수 방지
    private var binding : FragmentSearchResultBinding? = null

    // ListAdapter 를 초기화 하고, Handler로 전달..?
    private val adapter by lazy { ListAdapter(Handler(viewModel)) }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("SEARCH_PREF", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSearchResultBinding.inflate(inflater, container, false).apply {
            binding = this
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@SearchFragment.viewModel
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            recyclerView.adapter = adapter

            val lastSearchQuery = sharedPreferences.getString("LAST_SEARCH_QUERY", "")
        }
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // null 로 만들어 주면서 메모리 누수를 방지 시킴
        binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // MainActivity에 FavoritesChangedListener 설정
        if (context is MainActivity) {
            context.favoritesChangedListener = this
        }
    }

    override fun onDetach() {
        super.onDetach()
        // FavoritesChangedListener 해제
        (activity as? MainActivity)?.favoritesChangedListener = null
    }

    // 검색어 입력 받아서 뷰모델을 통해 검색을 함
    fun searchKeyword(text: String) {
        viewModel.search(text)

        // 저장
        with(sharedPreferences.edit()) {
            putString("LAST_SEARCH_QUERY", text)
            apply()
        }
    }

    private fun observeViewModel() {
        viewModel.listLiveData.observe(viewLifecycleOwner) {
            binding?.apply {
                // 리스트 있고 없고에 따른 뷰 표시
                if(it.isEmpty()) {
                    emptyTextView.isVisible = true
                    recyclerView.isVisible = false
                } else {
                    emptyTextView.isVisible = false
                    recyclerView.isVisible = true
                }
            }
            adapter.submitList(it)
        }
    }

    // 즐겨찾기 삭제
    override fun onFavoriteDeleted(item: ListItem) {
        if(item.isFavorite) viewModel.toggleFavorite(item)
    }
}

// 아이템 클릭시 핸들러 관련 처리?
class Handler(private val viewModel: SearchViewModel) : ItemHandler {
    override fun onClickFavorite(item: ListItem) {
        viewModel.toggleFavorite(item)
    }
}