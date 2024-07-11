package com.laurens.klasifikasibanana.view.home

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.laurens.klasifikasibanana.R

class DetailHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_home)

        val name = intent.getStringExtra("EXTRA_NAME")
        val description = intent.getStringExtra("EXTRA_DESCRIPTION")
        val photoId = intent.getIntExtra("EXTRA_PHOTO", 0)

        val detailTitle: TextView = findViewById(R.id.tvHeroName)
        val detailDesc: TextView = findViewById(R.id.desc)
        val detailImage: ImageView = findViewById(R.id.imageDETAIL)

        detailTitle.text = name
        detailDesc.text = description
        detailImage.setImageResource(photoId)
    }
}