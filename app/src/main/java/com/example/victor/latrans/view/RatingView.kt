package com.example.victor.latrans.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.example.victor.latrans.R

/**
 * @author rebeccafranks
 * *
 * @since 2017/08/09.
 */

class RatingView : FrameLayout {


    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    fun init(attributeSet: AttributeSet?) {
        val attributes = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MovieRatingView, 0, 0)
        val ratingText: String
        try {
            ratingText = attributes.getString(R.styleable.MovieRatingView_rating)
        } finally {
            attributes.recycle()
        }
        View.inflate(context, R.layout.rating_badge, this)

        val ratingTextView = findViewById<TextView>(R.id.text_view_rating_text)
        ratingTextView.text = ratingText.toString()

    }
}
