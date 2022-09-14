package uz.kmax.a15puzzle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.kmax.a15puzzle.controller.FragmentController
import uz.kmax.a15puzzle.fragment.SplashFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FragmentController.init(R.id.container, supportFragmentManager)
        FragmentController.controller?.startMainFragment(SplashFragment())
    }
}