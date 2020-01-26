package at.fh.swengb.he01.solution

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_movie_review.*

class MovieReviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_review)

        val movieId = intent.getStringExtra(MainActivity.EXTRA_MOVIE_ID)

        updateUi(movieId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUi(movieId: String?) {
        if (movieId == null) {
            handleMovieNotFound()
            return

        } else {

            MovieRepository.movieById(movieId,
                success = {movie_review_header.text = it.title },
                error = { Log.e("API_CALL", it)})


            review_movie.setOnClickListener {
                val reviewText = movie_reviewText.text.toString()
                val reviewValue = movie_review_bar.rating.toDouble()

                val movieReview = Review(reviewValue, reviewText)

                MovieRepository.reviewMovie(movieId, movieReview,
                    success = {Log.i("API_CALL", "Review was added")},
                    error = {Log.e("API_CALL", it)})

                val resultIntent = Intent()
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    private fun handleMovieNotFound() {
        Toast.makeText(this, "Movie could not be found", Toast.LENGTH_LONG).show()
        finish()
    }
}
