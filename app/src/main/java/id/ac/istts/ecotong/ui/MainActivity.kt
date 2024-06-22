package id.ac.istts.ecotong.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
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

        with(binding) {
            val navHost =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            val navController = navHost.navController
            ViewCompat.setOnApplyWindowInsetsListener(main) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            ViewCompat.setOnApplyWindowInsetsListener(
                bottomNavigation
            ) { v: View?, _: WindowInsetsCompat? ->
                ViewCompat.onApplyWindowInsets(
                    v!!, WindowInsetsCompat.CONSUMED
                )
            }
            bottomNavigation.setupWithNavController(navController)
            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                when (destination.id) {
                    R.id.loginFragment, R.id.signUpFragment, R.id.welcomeFragment, R.id.toLoginFragment, R.id.scanResultFragment,
                    R.id.scanFragment, R.id.postFragment, R.id.settingsFragment, R.id.appInfoFragment -> {
                        bottomNavigation.gone()
                        fragmentContainerView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                            bottomMargin = 0
                        }
                    }

                    else -> {
                        bottomNavigation.visible()
                        bottomNavigation.viewTreeObserver.addOnGlobalLayoutListener(object :
                            ViewTreeObserver.OnGlobalLayoutListener {
                            override fun onGlobalLayout() {
                                bottomNavigation.viewTreeObserver.removeOnGlobalLayoutListener(this)
                                val bottomAppBarHeight = bottomNavigation.height
                                fragmentContainerView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                                    bottomMargin = bottomAppBarHeight
                                }
                            }
                        })
                    }
                }
            }

        }
        onBackPressedDispatcher.addCallback(this) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            val navController = navHostFragment.navController
            if (navController.currentDestination?.id == R.id.homeFragment
//                || navController.currentDestination?.id == R.id.profileFragment
//                || navController.currentDestination?.id == R.id.listeningFragment
            ) {
                finish()
            } else {
                if (!navController.popBackStack()) {
                    finish()
                }
            }
        }
    }

    fun hideBottomNavigation() {
        with(binding) {
            bottomNavigation.visibility = View.GONE
        }
    }

    fun showBottomNavigation() {
        with(binding) {
            bottomNavigation.visibility = View.VISIBLE
        }
    }
}