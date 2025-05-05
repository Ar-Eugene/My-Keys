package com.example.mykeys.main.presentation.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.mykeys.R
import com.example.mykeys.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.BuildConfig
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startFirebase()
        logEventFirebase()


    }

    // Firebase запускается только в релизной версии
    private fun startFirebase() {
        if (!BuildConfig.DEBUG) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(this)
            // Логируем факт открытия приложения
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null)
            // запускаем уведомление о новой версии
            checkForUpdate()
        }
    }

    private fun logEventFirebase() {
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

    // уведомление о новой версии
    private fun checkForUpdate() {
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(mapOf("latest_version" to BuildConfig.VERSION_NAME))

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val latestVersion = remoteConfig.getString("latest_version")
                    val currentVersion = BuildConfig.VERSION_NAME

                    if (latestVersion != currentVersion) {
                        showUpdateSnackbar()
                    }
                }
            }
    }

    // наш Snackbar
    private fun showUpdateSnackbar() {
        val snackbarContainer = findViewById<FrameLayout>(R.id.snackbar_container)

        val snackbar =
            Snackbar.make(snackbarContainer, "Доступна новая версия", Snackbar.LENGTH_LONG)
                .setAction("Обновить") {
                    val ruStoreUrl = "https://www.rustore.ru/catalog/app/com.example.mykeys"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ruStoreUrl))
                    startActivity(intent)
                }
        // закрываем через 5 секунд если ничего с ним не сделали
        snackbar.duration = 5000

        // Переместить Snackbar под статус-бар
        val view = snackbar.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        params.topMargin = getStatusBarHeight()
        view.layoutParams = params

        // Цвета
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.snack_background_color))
        val textView = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))

        val actionView =
            view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
        actionView.setTextColor(ContextCompat.getColor(this, R.color.white))

        snackbar.show()
    }

    // Получение высоты статус-бара
    private fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
    }

}