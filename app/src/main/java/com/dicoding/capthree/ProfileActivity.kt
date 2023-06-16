package com.dicoding.capthree

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capthree.databinding.ProfileActivityBinding

@Suppress("DEPRECATION")
class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ProfileActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProfileActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.moreLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }


        binding.ivProfileBack.setOnClickListener {
            onBackPressed()
        }

        binding.moreTheme.setOnClickListener{
            val intent = Intent(this, ThemeActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
//
//        binding.moreLanguage.setOnClickListener {
//            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
//        }

//        val userPreferences = UserPreferences.getInstance(dataStore)
//        profileViewModel = ViewModelProvider(this, FactoryProfile(userPreferences))[ProfileViewModel::class.java]
//
//        lifecycleScope.launchWhenStarted {
//            userPreferences.getSession().collect { userSession ->
//                binding.textName.text = userSession.name
//                progressBar.visibility = View.GONE
//            }
//        }
//        binding.btnLogout.setOnClickListener {
//            profileViewModel.logout()
//            val intent = Intent(this, LoginActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//            val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_up, R.anim.slide_down)
//            startActivity(intent, options.toBundle())
//            finish()
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}