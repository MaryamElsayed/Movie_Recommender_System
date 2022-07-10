package com.example.gp_rasta;

public class Rank {
    String MovieTweets;
    int Polarity_Score,movieId,rating,userId;

    public Rank(String Movie_Tweets, int Polarity_Score, int movieId, int rating, int userId) {
        this.MovieTweets = Movie_Tweets;
        this.Polarity_Score = Polarity_Score;
        this.movieId = movieId;
        this.rating = rating;
        this.userId = userId;
    }

    public String getMovieTweets() {
        return MovieTweets;
    }

    public int getPolarity_Score() {
        return Polarity_Score;
    }

    public int getMovieId() {
        return movieId;
    }

    public int getRating() {
        return rating;
    }

    public int getUserId() {
        return userId;
    }

    public void setMovieTweets(String movieTweets) {
        MovieTweets = movieTweets;
    }

    public void setPolarity_Score(int polarity_Score) {
        Polarity_Score = polarity_Score;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
