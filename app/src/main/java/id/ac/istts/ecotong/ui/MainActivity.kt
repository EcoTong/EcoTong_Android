package id.ac.istts.ecotong.ui

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.updateLayoutParams
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import id.ac.istts.ecotong.R
import id.ac.istts.ecotong.databinding.ActivityMainBinding
import id.ac.istts.ecotong.util.gone
import id.ac.istts.ecotong.util.visible

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHost.navController
        with(binding) {
            bottomNavigation.setupWithNavController(navController)
            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                when (destination.id) {
                    R.id.loginFragment, R.id.signUpFragment, R.id.welcomeFragment, R.id.toLoginFragment -> {
                        bottomNavigation.gone()
                        fragmentContainerView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                            bottomMargin = 0
                        }
                    }

                    else -> {
                        bottomNavigation.visible()
                        fragmentContainerView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                            bottomMargin = 80
                        }
                    }
                }
            }
            bottomNavigation.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.homeFragment -> {
                        fragmentContainerView.findNavController()
                            .navigate(R.id.action_global_homeFragment)
                        true
                    }

                    R.id.searchFragment -> {
                        fragmentContainerView.findNavController()
                            .navigate(R.id.action_global_searchFragment)
                        true
                    }

                    R.id.scan -> {
                        true
                    }

                    R.id.history -> {
                        true
                    }

                    R.id.profile -> {
                        true
                    }

                    else -> false
                }
            }
        }
    }
}