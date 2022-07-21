package com.example.moviedb.ui.adapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import android.widget.Filter
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedb.BuildConfig.IMAGE_BASE_URL
import com.example.moviedb.R
import com.example.moviedb.data.entities.Movie
import com.example.moviedb.data.entities.MovieDetails
import com.example.moviedb.utils.Resource
import com.example.moviedb.utils.StringUtils.getStringFormatted
import com.example.moviedb.constants.Constants
import kotlinx.android.synthetic.main.movie_item.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class MovieAdapter(private val context: Context, private val movieList: ArrayList<Movie>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    Filterable {
    private var networkState: Resource.Status? = null
    private var listener: itemClickListener? = null

    var movieFilterList = ArrayList<Movie>()

    init {
        movieFilterList = movieList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == Constants.MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.movie_item, parent, false)
            return MovieItemViewHolder(
                view
            )
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(
                view
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == Constants.MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(movieFilterList[position],context)
            holder.itemView.setOnClickListener{
                movieFilterList[position].let { it1 -> listener?.onItemClicked(it1) }
            }
        }
        else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    fun setItemListener(listener: itemClickListener){
        this.listener = listener
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != Resource.Status.SUCCESS
    }

    override fun getItemCount() = movieFilterList.size

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            Constants.NETWORK_VIEW_TYPE
        } else {
            Constants.MOVIE_VIEW_TYPE
        }
    }

    class MovieItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(movie: Movie?, context: Context) {
            itemView.cv_movie_title.text = movie?.title
            itemView.cv_movie_release_date.text = getStringFormatted(movie?.releaseDate.toString())
            itemView.cv_movie_rating.text =  movie?.rating.toString()
            val moviePosterURL = IMAGE_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterURL)
                .into(itemView.cv_iv_movie_poster);
            itemView.setOnClickListener{
                val intent = Intent(context, MovieDetails::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }
        }
    }

    class NetworkStateItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        fun bind(networkState: Resource.Status?) {
            if (networkState != null && networkState == Resource.Status.LOADING) {
                itemView.progress_bar_item.visibility = View.VISIBLE
            }
            else  {
                itemView.progress_bar_item.visibility = View.GONE
            }
            if (networkState != null && networkState == Resource.Status.ERROR) {
                itemView.error_msg_item.visibility = View.VISIBLE
            }
            else {
                itemView.error_msg_item.visibility = View.GONE
            }
        }
    }

    fun addItems(list: ArrayList<Movie>){
        movieFilterList.clear()
        movieFilterList.addAll(list)
        notifyDataSetChanged()
    }

    interface itemClickListener{
        fun onItemClicked(movie:Movie)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    movieFilterList = movieList
                } else {
                    val resultList = ArrayList<Movie>()
                    for (row in movieList) {
                        if (row.title.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    movieFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = movieFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                movieFilterList = results?.values as ArrayList<Movie>
                notifyDataSetChanged()
            }
        }
    }
}
