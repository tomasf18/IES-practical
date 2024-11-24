package ies.lab3.ex3.entity;

public class Message {
    // message = {
    //     "movieId": movie["id"],
    //     "movieTitle": movie["title"],
    //     "movieYear": movie["year"],
    //     "quote": quote
    // }

    private int movieId;
    private String movieTitle;
    private String movieYear;
    private String quote;

    public Message() {
    }

    public Message(int movieId, String movieTitle, String movieYear, String quote) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movieYear = movieYear;
        this.quote = quote;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieYear() {
        return movieYear;
    }

    public void setMovieYear(String movieYear) {
        this.movieYear = movieYear;
    }


    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    @Override
    public String toString() {
        return "{" +
                "movieId: " + movieId +
                ", movieTitle: '" + movieTitle + '\'' +
                ", movieYear: " + movieYear +
                ", quote: '" + quote + '\'' +
                '}';
    }
}
