package com.example.materialandroid.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.commit
import com.example.materialandroid.R
import com.example.materialandroid.view.picture.POTDFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {


    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


        val prefs = getPreferences(MODE_PRIVATE)
        when (prefs.getInt(getString(R.string.THEME_KEY), -1)) {
            1 -> setTheme(R.style.Theme_Mars)
            2 -> setTheme(R.style.Theme_Mercury)
            3 -> setTheme(R.style.Theme_Uranus)
            4 -> setTheme(R.style.Theme_MaterialAndroid)
            else -> setTheme(R.style.Theme_MaterialAndroid)
        }


        savedInstanceState.let {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.container, POTDFragment.newInstance())
            }

        }
    }

    override fun recreate() {
        finish()
        overridePendingTransition(
            R.anim.fade_in,
            R.anim.fade_out
        )
        startActivity(intent)
        overridePendingTransition(
            R.anim.slide_up,
            R.anim.slide_down
        )
    }

}