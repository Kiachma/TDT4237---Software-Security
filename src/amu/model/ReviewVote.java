package amu.model;

public class ReviewVote {

    private Boolean vote;
    private Review review;
    private Customer customer;

    public Boolean getHelpful() {
        return vote;
    }
    
    public void setHelpful() {
        vote=true;
    }

    public Boolean getUnHelpful() {
        return !vote;
    }
    public void setUnHelpful() {
        vote=false;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer=customer;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review=review;
    }
}