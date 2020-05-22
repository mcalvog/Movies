package com.marcoscg.movies.ui.home.master

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.marcoscg.movies.R
import com.marcoscg.movies.common.glide.load
import com.marcoscg.movies.data.sources.remote.api.ApiClient
import com.marcoscg.movies.model.Movie
import kotlinx.android.synthetic.main.row_movie_list.view.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class PopularMoviesAdapter(val context: Context?, var items: List<Movie> = ArrayList()) : RecyclerView.Adapter<ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(movie: Movie, container: View) // pass ImageView to make shared transition
    }

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_movie_list, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = items[position]

        holder.tvMovieTitle.text = movie.title
        holder.tvMovieDescription.text = context?.getString(R.string.movie_row_desc_pattern,
            LocalDate.parse(movie.release_date).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
            getRating(movie)
        )
        holder.ivMoviePoster.setImageResource(R.drawable.poster_placeholder)
        holder.ivMoviePoster.transitionName = movie.id.toString()
        holder.llMovieTextContainer.setBackgroundColor(Color.DKGRAY)

        holder.ivMoviePoster.load(ApiClient.POSTER_BASE_URL + movie.poster_path, true) { color ->
            holder.llMovieTextContainer.setBackgroundColor(color)
        }

        holder.cvMovieContainer.setOnClickListener {
            onItemClickListener?.onItemClick(movie, holder.ivMoviePoster)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setOnMovieClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun fillList(items: List<Movie>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun getRating(movie: Movie): String {
        return if (movie.vote_count == 0 && context != null) {
            context.getString(R.string.no_ratings)
        } else {
            val starIcon = 9733.toChar()
            "${movie.vote_average} $starIcon"
        }
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val cvMovieContainer: CardView = view.cv_movie_container
    val llMovieTextContainer: LinearLayout = view.ll_text_container
    val tvMovieTitle: TextView = view.tv_movie_title
    val tvMovieDescription: TextView = view.tv_movie_description
    val ivMoviePoster: ImageView = view.iv_movie_poster
}