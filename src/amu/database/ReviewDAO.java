package amu.database;

import amu.model.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public final class ReviewDAO {

    public List<Review> findBybookId(int bookId) {
        List<Review> reviews = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();


            String query = "SELECT * FROM review WHERE review.book=?";

            statement = connection.prepareStatement(query);
            statement.setInt(1, bookId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Review review = new Review();
                CustomerDAO customerDAO = new CustomerDAO();
                ReviewVoteDAO reviewVotesDAO = new ReviewVoteDAO();
                review.setId(resultSet.getInt("review.id"));
                review.setAuthor(customerDAO.findById(resultSet.getInt("review.customer")));
                review.setRating(resultSet.getInt("review.rating"));
                review.setReview(resultSet.getString("review.review"));
                review.setReviewVotes(reviewVotesDAO.findByReviewId(resultSet.getInt("review.id")));
                // TODO: Reviews, Categories
                reviews.add(review);
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }

        return reviews;
    }

    public Review findById(int id) {
        Review review = new Review();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();


            String query = "SELECT * FROM review WHERE review.id=?";

            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                CustomerDAO customerDAO = new CustomerDAO();
                ReviewVoteDAO reviewVotesDAO = new ReviewVoteDAO();
                review.setId(resultSet.getInt("review.id"));
                review.setAuthor(customerDAO.findById(resultSet.getInt("review.customer")));
                review.setRating(resultSet.getInt("review.rating"));
                review.setReview(resultSet.getString("review.rating"));
                review.setReviewVotes(reviewVotesDAO.findByReviewId(resultSet.getInt("review.id")));
                // TODO: Reviews, Categories
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }

        return review;
    }

    public boolean add(Review review) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();

            String query = "INSERT INTO review (customer, review, book, rating) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setInt(1, review.getAuthor().getId());
            statement.setString(2, review.getReview());
            statement.setInt(3, review.getBook().getId());
            statement.setInt(4, review.getRating());

            if (statement.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }

        return false;
    }

    public boolean edit(Review review) {
	Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();

            String query = "update review set review=?, rating=? where id=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, review.getReview());
            statement.setInt(2, review.getRating());
	    statement.setInt(3, review.getId());

            if (statement.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }

        return false;
    }
}
