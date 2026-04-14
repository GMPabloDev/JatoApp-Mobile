package com.example.projectofinalmobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectofinalmobile.R

class ImagePagerAdapter(
    private val images: List<String>
) : RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_slider, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivImage: ImageView = itemView.findViewById(R.id.ivSliderImage)

        fun bind(imageUrl: String) {
            try {
                val handler = android.os.Handler(android.os.Looper.getMainLooper())
                Thread {
                    try {
                        val url = java.net.URL(imageUrl)
                        val bitmap = android.graphics.BitmapFactory.decodeStream(url.openStream())
                        handler.post {
                            ivImage.setImageBitmap(bitmap)
                        }
                    } catch (e: Exception) {
                        handler.post {
                            ivImage.setImageResource(android.R.drawable.ic_menu_gallery)
                        }
                    }
                }.start()
            } catch (e: Exception) {
                ivImage.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        }
    }
}
