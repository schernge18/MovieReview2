package at.fh.swengb.he01.solution

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_movie_detail.*
import java.math.RoundingMode
import java.text.DecimalFormat

class MovieDetailActivity : AppCompatActivity() {

    companion object {
        val REVIEW_MOVIE_REQUEST = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        val movieId = intent.getStringExtra(MainActivity.EXTRA_MOVIE_ID)
        updateUi(movieId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REVIEW_MOVIE_REQUEST && resultCode == Activity.RESULT_OK){
            val movieId = intent.getStringExtra(MainActivity.EXTRA_MOVIE_ID)
            updateUi(movieId)
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
        }
    }

    private fun updateUi(movieId: String?) {
        if (movieId == null) {
            handleMovieNotFound()
            return
        }

        MovieRepository.movieById(movieId,
            success = {
                val movie = it

                val decimalFormat = DecimalFormat("#.##")
                decimalFormat.roundingMode = RoundingMode.CEILING

                movie_detail_header.text = movie.title
                movie_detail_director.text = movie.director.name
                movie_detail_actors.text = movie.actors.joinToString(", ") { it.name }
                movie_detail_genre.text = movie.genres.joinToString { it }
                movie_detail_rating_bar.rating = movie.reviewAverage().toFloat()
                movie_detail_rating_value.text = decimalFormat.format(movie.reviewAverage())
                movie_detail_review_count.text = movie.reviews.count().toString()
                movie_detail_release.text = movie.release
                movie_detail_plot.text = movie.plot

                Glide
                    .with(movie_detail_poster)
                    .load(movie.posterImagePath)
                    .into(movie_detail_poster)

                Glide
                    .with(movie_detail_backdrop)
                    .load(movie.backdropImagePath)
                    .into(movie_detail_backdrop)

                open_review.setOnClickListener {
                    val ratingIntent = Intent(this, MovieReviewActivity::class.java)
                    ratingIntent.putExtra(MainActivity.EXTRA_MOVIE_ID, movie.id)
                    startActivityForResult(ratingIntent, REVIEW_MOVIE_REQUEST)

                }

            },
            error = {Log.e("API_CALL", it)})
    }


    private fun handleMovieNotFound() {
        Toast.makeText(this, "Movie could not be found", Toast.LENGTH_LONG).show()
        finish()
    }
}
