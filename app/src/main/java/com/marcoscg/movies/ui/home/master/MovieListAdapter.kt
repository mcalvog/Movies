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
import com.marcoscg.movies.common.utils.dp
import com.marcoscg.movies.data.sources.remote.api.ApiClient
import com.marcoscg.movies.databinding.RowMovieListBinding
import com.marcoscg.movies.model.Movie
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class MovieListAdapter(val context: Context?, var items: List<Movie> = ArrayList()) : RecyclerView.Adapter<ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(movie: Movie, container: View) // pass ImageView to make shared transition
    }

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowMovieListBinding.inflate(LayoutInflater.from(context), parent, false))
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

        holder.ivMoviePoster.load(url = ApiClient.POSTER_BASE_URL + movie.poster_path,
            crossFade = true, width = 160.dp, height = 160.dp) { color ->

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
        this.items += items
        notifyDataSetChanged()
    }

    fun clear() {
        this.items = emptyList()
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

class ViewHolder (binding: RowMovieListBinding) : RecyclerView.ViewHolder(binding.root) {
    val cvMovieContainer: CardView = binding.cvMovieContainer
    val llMovieTextContainer: LinearLayout = binding.llTextContainer
    val tvMovieTitle: TextView = binding.tvMovieTitle
    val tvMovieDescription: TextView = binding.tvMovieDescription
    val ivMoviePoster: ImageView = binding.ivMoviePoster
}