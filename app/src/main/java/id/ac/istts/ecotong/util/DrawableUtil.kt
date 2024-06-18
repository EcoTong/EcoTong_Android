package id.ac.istts.ecotong.util

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

fun areDrawablesIdentical(drawable1: Drawable?, drawable2: Drawable?): Boolean {
    if (drawable1 == null || drawable2 == null) {
        return false
    }

    if (drawable1 is BitmapDrawable && drawable2 is BitmapDrawable) {
        val bitmap1 = drawable1.bitmap
        val bitmap2 = drawable2.bitmap
        return bitmap1.sameAs(bitmap2)
    }

    return false
}

