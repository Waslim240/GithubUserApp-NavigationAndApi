package waslim.githubuserapp.view.splashscreen

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import waslim.githubuserapp.R
import waslim.githubuserapp.view.home.MainActivity

@RequiresApi(Build.VERSION_CODES.M)
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }, DELAY)
    }

    companion object {
        const val DELAY = 2000L
    }
}