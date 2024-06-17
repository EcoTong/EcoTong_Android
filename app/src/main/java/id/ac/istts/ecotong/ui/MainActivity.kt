package id.ac.istts.ecotong.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
                    R.id.loginFragment, R.id.signUpFragment, R.id.welcomeFragment, R.id.toLoginFragment,
                    R.id.scanFragment,R.id.postFragment, R.id.activeQuestFragment,
                    R.id.settingsFragment, R.id.appInfoFragment -> {
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

                    R.id.scanFragment -> {
                        fragmentContainerView.findNavController()
                            .navigate(R.id.action_global_scanFragment)
                        true
                    }

                    R.id.historyFragment -> {
                        fragmentContainerView.findNavController()
                            .navigate(R.id.action_global_historyFragment)
                        true
                    }

                    R.id.profileFragment -> {
                        fragmentContainerView.findNavController()
                            .navigate(R.id.action_global_profileFragment)
                        true
                    }

                    else -> false
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