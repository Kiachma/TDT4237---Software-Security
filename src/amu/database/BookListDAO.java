/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package amu.database;
import amu.model.*;
import java.sql.*;
import java.util.*;
import amu.model.Customer;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author JohnArne
 */
public final class BookListDAO {
    
    /*
     * Adds the booklist passed as parameter to 
     * the database
     */
    public boolean addBooklist(BookList booklist, Customer customer){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet results = null;
        String query = "INSERT INTO booklist (customer_id, title, description) VALUES(?,?,?)";
        
        try{
            connection = Database.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, customer.getId());
            statement.setString(2, booklist.getTitle());
            statement.setString(3, booklist.getDescription());
            
            if(statement.executeUpdate()>0){
                return true;
            }
        }catch(SQLException e){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Exception with query: "+query);
            e.printStackTrace();
        }finally{
            Database.close(connection, statement, results);
        }
        return false;
    }
    /*
     * Return a booklist object containing all the booklistitems in 
     * that booklist.
     */
    public BookList getListByID(int id){
        BookList booklist = new BookList();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet results = null;
        String query = "SELECT title, description FROM booklist WHERE booklist_id = ?";
        
        try{
            connection = Database.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            results = statement.executeQuery();
            
            while(results.next()){
                booklist.setTitle(results.getString("title"));
                booklist.setDescription(results.getString("description"));
            }
        }catch(SQLException e){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "SQL exception in "+query);
            e.printStackTrace();
        }finally{
            Database.close(connection, statement, results);
        }
        
        //Fetch and add books with title, id, and isbn13 to the list
        query = "SELECT title.name, book.id, book.isbn13 "
                + "FROM title, book, booklist, booklist_x_book "
                + "WHERE title.id=book.title_id "
                + "AND book.title_id=booklist_x_book.book_title_id "
                + "AND booklist_x_book.booklist_id = ?";
        
        try{
            connection = Database.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            results = statement.executeQuery();
            
            while(results.next()){
                System.out.println(results.getString("name")+" "+results.getString("id")+" "+results.getString("isbn13"));
 
                Book book = new Book();
                book.setId(results.getInt("id"));
                Title title = new Title();
                title.setName(results.getString("name"));
                book.setTitle(title);
                book.setIsbn13(results.getString("isbn13"));
                booklist.addBook(book);
            }
        }catch(SQLException e){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "SQL exception in "+query);
            e.printStackTrace();
        }finally{
            Database.close(connection, statement, results);
        }
        
        return booklist;
    }
    
    /*
     * Fetch titles in DB, return a string list of the titles
     */
    public HashMap getBookListsTitles(Customer customer){
        HashMap booklists = new HashMap();
        Connection connection = null;
        ResultSet results = null;
        PreparedStatement statement = null;
        
        try{
            connection = Database.getConnection();
            String query = "SELECT booklist_id, title FROM booklist WHERE customer_id=?";
            statement = connection.prepareStatement(query);

            statement.setInt(1, customer.getId());

            results = statement.executeQuery();
            
            while(results.next()){
                booklists.put(results.getInt("booklist_id"), results.getString("title"));
            }
        }catch(SQLException e){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Tried to get booklist titles");
        }finally{
            Database.close(connection, statement, results);
        }
        /*
         * TODO:
         * Fetch booklists from database
         */
//        try{
//            
//        }catch(SQLException e){
//            
//        }
        
        return booklists;
    }
}
