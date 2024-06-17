package id.ac.istts.ecotong.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import id.ac.istts.ecotong.databinding.FragmentSearchBinding
import id.ac.istts.ecotong.ui.MainActivity
import id.ac.istts.ecotong.ui.base.BaseFragment

class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun setupUI() {
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setupListeners() {
        with(binding) {
            searchBar.setOnQueryTextFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    mainActivity.hideBottomNavigation()
                } else {
                    mainActivity.showBottomNavigation()
                }
            }

            searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchBar.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })

            root.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val imm = getSystemService(mainActivity, InputMethodManager::class.java)
                    imm?.hideSoftInputFromWindow(root.windowToken, 0)
                    val focusedView = mainActivity.currentFocus
                    if (focusedView == searchBar) {
                        mainActivity.hideBottomNavigation()
                    } else {
                        mainActivity.showBottomNavigation()
                    }
                }
                false
            }
        }
    }

    override fun setupObservers() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupListeners()
        setupObservers()
    }
}