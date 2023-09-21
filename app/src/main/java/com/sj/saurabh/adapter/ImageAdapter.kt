package com.sj.saurabh.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sj.saurabh.R
import com.sj.saurabh.utils.MediaStoreFile
/**
 **@author Saurabh Kumar 27-03-2023
 */
class ImageAdapter constructor(
    val context: Context,
    val imagesBitmap: List<Bitmap> = emptyList(),
    val mediaList: MutableList<MediaStoreFile>)
    : RecyclerView.Adapter<ImageViewHolder>() {

    constructor(context1: Context, mediaList1: MutableList<MediaStoreFile>) : this(
        context =context1,
        mediaList = mediaList1)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.design_item_gallery, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
/*        val image = images[position]
        val image = mediaList[position].file
        holder.imgItem.setImageBitmap(image) // using bitmap*/

        Glide.with(context).load(mediaList[position].file).into(holder.imgItem)
    }

    override fun getItemCount(): Int {
        return mediaList.size
    }
}

class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imgItem: ImageView = itemView.findViewById(R.id.imgItem)
}
