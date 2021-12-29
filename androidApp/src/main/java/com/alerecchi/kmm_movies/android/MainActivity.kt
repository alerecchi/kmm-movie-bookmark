package com.alerecchi.kmm_movies.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alerecchi.kmm_movies.Greeting
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.alerecchi.kmm_movies.movie_list.MovieListStateMachine
import kotlinx.coroutines.launch

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            val movies = MovieListStateMachine().test()
            print(movies)
        }
        val tv: TextView = findViewById(R.id.text_view)
        tv.text = greet()
    }
}
