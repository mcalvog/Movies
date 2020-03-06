package com.marcoscg.movies.common.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.TextViewCompat
import com.marcoscg.movies.R

class CenteredToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.toolbarStyle
) : Toolbar(context, attrs, defStyleAttr) {

    private val titleView = AppCompatTextView(getContext())

    init {
        titleView.maxLines = 1
        titleView.ellipsize = TextUtils.TruncateAt.END

        TextViewCompat.setTextAppearance(titleView, R.style.TextAppearance_AppCompat_Widget_ActionBar_Title)

        addView(titleView, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        titleView.x = ((width - titleView.width) / 2).toFloat()
    }

    override fun setTitle(title: CharSequence) {
        titleView.text = title
    }

}
