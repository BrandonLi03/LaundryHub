package com.example.laundryhub.Page

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laundryhub.Adapter.GNewsAdapter
import com.example.laundryhub.Model.Article
import com.example.laundryhub.Model.GNewsResponse
import com.example.laundryhub.Network.GNewsApi
import com.example.laundryhub.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsPage : AppCompatActivity() {

    private val apiKey = "News_API_KEY"

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GNewsAdapter
    private val articles = mutableListOf<Article>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_page)

        val back_btn = findViewById<ImageButton>(R.id.btn_back)
        recyclerView = findViewById(R.id.rv_news)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = GNewsAdapter(articles)
        recyclerView.adapter = adapter

        fetchNews()

        back_btn.setOnClickListener{
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }
    }

    private fun fetchNews() {
        val api = GNewsApi.create()
        api.getTopHeadlines(apiKey).enqueue(object : Callback<GNewsResponse> {
            override fun onResponse(
                call: Call<GNewsResponse>,
                response: Response<GNewsResponse>
            ) {
                if (response.isSuccessful) {
                    val fetchedArticles = response.body()?.articles ?: emptyList()
                    articles.clear()
                    articles.addAll(fetchedArticles)
                    adapter.notifyDataSetChanged()
                    Log.d("GNEWS", "Berhasil ambil ${fetchedArticles.size} artikel")
                } else {
                    Log.e("GNEWS", "Gagal: HTTP ${response.code()} - ${response.message()}")
                    val errorBody = response.errorBody()?.string()
                    Log.e("GNEWS", "Error Body: $errorBody")
                }
            }

            override fun onFailure(call: Call<GNewsResponse>, t: Throwable) {
                Log.e("GNEWS", "Error: ${t.message}")
            }
        })
    }
}
