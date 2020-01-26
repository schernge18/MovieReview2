package at.fh.swengb.he01.solution

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
open class Movie(
    val id: String,
    val title: String,
    val release: String,
    val posterImagePath: String,
    val backdropImagePath: String,
    val reviews: MutableList<Review>

) {

    fun reviewAverage(): Double {
        return reviews
            .map{ it.reviewValue }
            .average()
            .run { if(isNaN()) 0.0 else this }
    }
}
