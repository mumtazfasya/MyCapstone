package com.dicoding.capthree

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView

@Suppress("DEPRECATION")
class DetailArticleActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_PERSON = "extra_person"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_article)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Detail Articles"

        val imgPlayers: ImageView = findViewById(R.id.img_players)
        val nmPlayers: TextView = findViewById(R.id.name_players)
        val nmBrief: TextView = findViewById(R.id.name_brief)

        val person = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<Hero>(EXTRA_PERSON)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_PERSON)
        }

        if (person != null) {
            val imagePlayer = person.photo
            imgPlayers.setImageResource(imagePlayer)
            val namePlayer = person.name
            nmPlayers.text = namePlayer
            val nameBrief = person.description
            nmBrief.text = nameBrief
        }

        val btnShare = findViewById<TextView>(R.id.action_share)
        btnShare.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val shareBody = "Download our app = https://www.youtube.com/watch?v=HlOb3VLZ-zE"

        val shrIntent = Intent(Intent.ACTION_SEND)
        shrIntent.type = "text/plain"

        shrIntent.putExtra(Intent.EXTRA_TEXT, shareBody)

        startActivity(shrIntent)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}