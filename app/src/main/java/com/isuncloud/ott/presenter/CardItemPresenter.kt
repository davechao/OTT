package com.isuncloud.ott.presenter

import android.support.v17.leanback.widget.ImageCardView
import android.support.v17.leanback.widget.Presenter
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.ImageView
import com.isuncloud.ott.R
import com.isuncloud.ott.repository.model.LauncherAppItem
import kotlin.properties.Delegates

class CardItemPresenter: Presenter() {

    companion object {
        private const val CARD_WIDTH = 313
        private const val CARD_HEIGHT = 176
    }

    private var defaultBackgroundColor: Int by Delegates.notNull()
    private var selectedBackgroundColor: Int by Delegates.notNull()

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        defaultBackgroundColor = ContextCompat.getColor(parent.context, R.color.default_background)
        selectedBackgroundColor = ContextCompat.getColor(parent.context, R.color.selected_background)

        val imageCardView = object: ImageCardView(parent.context) {
            override fun setSelected(selected: Boolean) {
                super.setSelected(selected)
                updateCardBackgroundColor(this, selected)
            }
        }
        imageCardView.isFocusable = true
        imageCardView.isFocusableInTouchMode = true
        imageCardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)
        imageCardView.setMainImageScaleType(ImageView.ScaleType.FIT_CENTER)

        updateCardBackgroundColor(imageCardView, false)

        return ViewHolder(imageCardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
      if(item is LauncherAppItem) {
            val imageCardView = viewHolder.view as ImageCardView
            if(!TextUtils.isEmpty(item.appId)) {
                imageCardView.titleText = item.appName
                imageCardView.contentText = item.appId
                imageCardView.mainImage = item.icon
            }
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val imageCardView = viewHolder.view as ImageCardView
        imageCardView.badgeImage = null
        imageCardView.mainImage = null
    }

    private fun updateCardBackgroundColor(view: ImageCardView, selected: Boolean) {
        val color = if (selected) {
            selectedBackgroundColor
        } else {
            defaultBackgroundColor
        }
        view.setBackgroundColor(color)
        view.setInfoAreaBackgroundColor(color)
    }

}