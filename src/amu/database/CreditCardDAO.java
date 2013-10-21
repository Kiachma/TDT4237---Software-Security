package amu.database;

import amu.model.CreditCard;
import amu.model.Customer;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class CreditCardDAO {

    private CustomerDAO customerDAO;

    public List<CreditCard> browse(Customer customer) {
	List<CreditCard> creditCards = new ArrayList<CreditCard>();

	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;

	try {
	    connection = Database.getConnection();
	    String query = "SELECT id, cc_number, expiry_date, cardholder_name FROM credit_card WHERE customer_id=?";
	    statement = connection.prepareStatement(query);

	    statement.setInt(1, customer.getId());

	    resultSet = statement.executeQuery();

	    while (resultSet.next()) {
		Calendar expiryDate = Calendar.getInstance();
		expiryDate.setTime(resultSet.getDate("expiry_date"));

		creditCards.add(new CreditCard(
			resultSet.getInt("id"),
			customer,
			resultSet.getString("cc_number"),
			expiryDate,
			resultSet.getString("cardholder_name")));
	    }
	} catch (SQLException exception) {
	    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
	} finally {
	    Database.close(connection, statement, resultSet);
	}

	return creditCards;
    }

    public CreditCard read(int id) {
	customerDAO = new CustomerDAO();
	CreditCard creditCard = null;

	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;

	try {
	    connection = Database.getConnection();

	    String query = "SELECT * FROM credit_card WHERE id=?";
	    statement = connection.prepareStatement(query);
	    statement.setInt(1, id);

	    resultSet = statement.executeQuery();

	    if (resultSet.next()) {
		Customer customer = customerDAO.findById(resultSet.getInt("customer_id"));
		Calendar expiryDate = Calendar.getInstance();
		expiryDate.setTime(resultSet.getDate("expiry_date"));

		creditCard = new CreditCard(
			id,
			customer,
			resultSet.getString("cc_number"),
			expiryDate,
			resultSet.getString("cardholder_name"));
	    }
	} catch (SQLException exception) {
	    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
	} finally {
	    Database.close(connection, statement, resultSet);
	}

	return creditCard;
    }

    public boolean add(CreditCard creditCard) {
	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;

	try {
	    connection = Database.getConnection();

	    String query = "INSERT INTO credit_card (customer_id, cc_number, expiry_date, cardholder_name) VALUES (?, ?, ?, ?)";
	    statement = connection.prepareStatement(query);
	    statement.setInt(1, creditCard.getCustomer().getId());
	    statement.setString(2, creditCard.getEncryptedCreditCardNumber());
	    statement.setDate(3, new Date(creditCard.getExpiryDate().getTimeInMillis()));
	    statement.setString(4, creditCard.getCardholderName());

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

    public boolean delete(int id) {
	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;

	try {
	    connection = Database.getConnection();

	    String query = "DELETE FROM credit_card WHERE id=?";
	    statement = connection.prepareStatement(query);
	    statement.setInt(1, id);

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

    public boolean update(CreditCard creditCard) {
	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;
	try {
	    connection = Database.getConnection();

	    String query = "UPDATE credit_card SET cc_number=? WHERE id=?";
	    statement = connection.prepareStatement(query);
	    statement.setString(1, creditCard.getEncryptedCreditCardNumber());
	    statement.setInt(2, creditCard.getId());

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
