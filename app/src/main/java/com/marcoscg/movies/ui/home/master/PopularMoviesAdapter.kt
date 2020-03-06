package com.marcoscg.movies.ui.home.master

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.marcoscg.movies.R
import com.marcoscg.movies.common.glide.PaletteGlideListener
import com.marcoscg.movies.data.sources.remote.api.ApiClient
import com.marcoscg.movies.model.Movie
import kotlinx.android.synthetic.main.row_movie_list.view.*

class PopularMoviesAdapter(val context: Context?, var items: List<Movie> = ArrayList()) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_movie_list, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = items.get(position)

        holder.tvMovieTitle.text = movie.title
        holder.tvMovieDescription.text = movie.overview
        holder.ivMoviePoster.setImageResource(R.drawable.poster_placeholder)
        holder.llMovieTextContainer.setBackgroundColor(Color.DKGRAY)

        Glide.with(holder.itemView)
            .asBitmap()
            .load(ApiClient.POSTER_BASE_URL + movie.poster_path)
            .placeholder(R.drawable.poster_placeholder)
            .transition(withCrossFade())
            .listener(PaletteGlideListener(holder.llMovieTextContainer))
            .into(holder.ivMoviePoster)

        holder.flMovieContainer.setOnClickListener {
            // TODO
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun fillList(items: List<Movie>) {
        this.items = items
        notifyDataSetChanged()
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val flMovieContainer: FrameLayout = view.fl_movie_container
    val llMovieTextContainer: LinearLayout = view.ll_text_container
    val tvMovieTitle: TextView = view.tv_movie_title
    val tvMovieDescription: TextView = view.tv_movie_description
    val ivMoviePoster: ImageView = view.iv_movie_poster
}