package amu.model;

import java.util.ArrayList;
import java.util.List;


public class Review {

    private Integer rating;
    private String review;
    private Customer author;
    private Integer id;
    private Book book;
    private List<ReviewVote> reviewVotes;

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Customer getAuthor() {
        return author;
    }

    public void setAuthor(Customer author) {
        this.author = author;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ReviewVote> getReviewVotes() {
        return reviewVotes;
    }

    public void setReviewVotes(List<ReviewVote> reviewVotes) {
        this.reviewVotes = reviewVotes;
    }
    
    public void addReviewVote(ReviewVote reviewVote){
        if(reviewVotes==null){
            reviewVotes = new ArrayList<>();
        }
        reviewVotes.add(reviewVote);
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
    
    
    
    


    
}