package com.example.mykeys.main.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.mykeys.R
import com.example.mykeys.databinding.ActivityMainBinding
import com.google.firebase.BuildConfig

import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase запускается только в релизной версии
        if (!BuildConfig.DEBUG) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(this)
            // Логируем факт открытия приложения
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null)
        }
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        // Отслеживаем переход на экран DescriptionCategoryFragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.descriptionCategoryFragment) {
                firebaseAnalytics.logEvent("open_description_category", null)
            }
        }

    }

}
