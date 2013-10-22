package amu.database;

import amu.model.Review;
import amu.model.ReviewVote;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public final class ReviewVoteDAO {

    public List<ReviewVote> findByReviewId(int bookId) {
        List<ReviewVote> reviewVotes = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();


            String query = "SELECT * FROM reviewVote WHERE reviewVote.review=?";

            statement = connection.prepareStatement(query);
            statement.setInt(1, bookId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ReviewVote reviewVote = new ReviewVote();
                CustomerDAO customerDAO = new CustomerDAO();
                ReviewDAO reviewDAO = new ReviewDAO();
                reviewVote.setCustomer(customerDAO.findById(resultSet.getInt("reviewVote.customer")));
                if(resultSet.getBoolean("reviewVote.vote")){
                    reviewVote.setHelpful();
                } else {
                    reviewVote.setUnHelpful();
                }
                // TODO: Reviews, Categories
                reviewVotes.add(reviewVote);
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }

        return reviewVotes;
    }

    public boolean add(ReviewVote reviewVote) {
	Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();

            String query = "INSERT INTO reviewVote (vote,review,customer) VALUES (?, ?, ?)";
            statement = connection.prepareStatement(query);
	    if(reviewVote.getHelpful()){
		statement.setInt(1, 1);
	    } else if (reviewVote.getUnHelpful()){
		statement.setInt(1, 0);
	    }
            statement.setInt(2, reviewVote.getReview().getId());
            statement.setInt(3, reviewVote.getCustomer().getId());
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
