package com.alerecchi.kmm_movies.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alerecchi.kmm_movies.android.trending.TrendingMoviesFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        supportFragmentManager
            .beginTransaction()
            .add(R.id.containerView, TrendingMoviesFragment())
            .commit()
    }
}
