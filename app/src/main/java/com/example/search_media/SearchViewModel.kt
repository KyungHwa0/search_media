package com.example.search_media

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.search_media.model.ImageItem
import com.example.search_media.model.ListItem
import com.example.search_media.model.VideoItem
import com.example.search_media.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider

class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {

    private val _listLiveData = MutableLiveData<List<ListItem>>()
    val listLiveData: LiveData<List<ListItem>> get() = _listLiveData

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> get() = _showLoading

    fun search(query: String) {
        // 코루틴으로 네트워크 요청을 비동기적으로 처리
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _showLoading.postValue(true)
                val list = searchRepository.search(query)
                val limitedList = list.take(80)
                _listLiveData.postValue(list)
            } catch (e: Exception) {
                _listLiveData.postValue(emptyList())
            } finally {
                _showLoading.postValue(false)
            }
        }
    }

    fun toggleFavorite(item: ListItem) {
        _listLiveData.value = _listLiveData.value?.map {
            if (it == item) {
                when (it) {
                    // 선택된 아이템의 즐겨찾기 상태 변경
                    is ImageItem -> {
                        it.copy(isFavorite = !item.isFavorite)
                    }
                    is VideoItem -> {
                        it.copy(isFavorite = !item.isFavorite)
                    }
                    else -> {
                        it
                    }
                }.also { changeItem ->
                    // 즐겨찾기 아이템의 상태를 Common 객체에서도 변경
                    if (Common.favoritesList.contains(item)) {
                        Common.favoritesList.remove(item)
                    } else {
                        Common.favoritesList.add(changeItem)
                    }
                }
            } else {
                it
            }
        }
    }

    // ViewModel을 생성하기 위한 Factory 클래스
    class SearchViewModelFactory(private val searchRepository: SearchRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            // ViewModel 인스턴스 생성 시에 Repository를 주입
            return SearchViewModel(searchRepository) as T
        }
    }
}
