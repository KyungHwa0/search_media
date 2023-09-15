package com.example.search_media

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import com.example.search_media.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 데이터 바인딩 초기화, UI 설정
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            view = this@MainActivity
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // menu 리소스 파일을 액션바에 메뉴를 추가
        menuInflater.inflate(R.menu.search_menu, menu)

        // search_menu를 SearchView로 변환
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    // 검색 버튼을 눌렀을때, 검색이 호출됨
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    // 검색어를 입력 할때마다 호출됨
                    return false
                }
            })
        }
        return true
    }
}