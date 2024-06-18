package id.ac.istts.ecotong.util

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan

fun createSpannableString(title: String, description: String): SpannableStringBuilder {
    val spannableBuilder = SpannableStringBuilder()
    val boldSpan = StyleSpan(Typeface.BOLD)
    spannableBuilder.append(title)
    spannableBuilder.append(" — ")
    spannableBuilder.setSpan(
        boldSpan,
        0,
        title.length,
        SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannableBuilder.append(description)
    return spannableBuilder
}