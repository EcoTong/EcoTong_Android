package id.ac.istts.ecotong.util

import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity

fun View.visible() {
    this.isVisible = true
}

fun View.invisible() {
    this.isInvisible = true
}

fun View.gone() {
    this.isGone = true
}

fun FragmentActivity.toastLong(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun FragmentActivity.toastShort(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}