package com.example.search_media

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import com.example.search_media.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var binding: ActivityMainBinding
    private val searchFragment = SearchFragment()
    private val fragmentList = listOf(searchFragment, FavoritesFragment())
    private val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, fragmentList)

    // 코루틴
    private lateinit var job: Job
    // 디바운스 시간
    private val debounceTime = 500L

    // sharedProferences
    private lateinit var sharedPreferences: SharedPreferences

    // 코루틴스코프 인터페이스
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 코루틴 초기화
        job = Job()
        sharedPreferences = getSharedPreferences("SEARCH_PREF", Context.MODE_PRIVATE)
        initView()
    }

    // View 초기화
    private fun initView() {
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            view = this@MainActivity
            viewPager.adapter = adapter

            // TabLayout ViewPager 연결
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = if (fragmentList[position] is SearchFragment) {
                    "검색 결과"
                } else {
                    "즐겨 찾기"
                }
            }.attach()
        }
    }

    // 액티비티가 파괴되면 코루틴 취소
    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        var searchJob: Job? = null

        // 마지막 검색어 저장
        val lastSearchQuery = sharedPreferences.getString("LAST_SEARCH_QUERY", "")
        searchView.setQuery(lastSearchQuery, false)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchFragment.searchKeyword(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                binding.viewPager.setCurrentItem(0, true)
                searchJob?.cancel()
                searchJob = launch {
                    delay(debounceTime)
                    searchFragment.searchKeyword(newText)

                    // Save the search keyword
                    with(sharedPreferences.edit()) {
                        putString("LAST_SEARCH_QUERY", newText)
                        apply()
                    }
                }
                return false
            }
        })
        return true
    }
}
