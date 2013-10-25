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
        String query = "INSERT INTO booklist (customer_id, title, description, is_public) VALUES(?,?,?,?)";
        
        try{
            connection = Database.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, customer.getId());
            statement.setString(2, booklist.getTitle());
            statement.setString(3, booklist.getDescription());
            statement.setBoolean(4, false);
            
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
    
    public boolean removeBookList(int booklistID){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet results = null;
        boolean deletedList = false;
        boolean deletedListedBooks = false;
        
        System.out.println("DELETE LIST:    "+booklistID);
        String query = "DELETE FROM booklist WHERE booklist_id=? ";
        try{
            connection = Database.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, booklistID);
            deletedList = (statement.executeUpdate()>0);
        }catch(SQLException e){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Exception in query: "+query);
            e.printStackTrace();
        }finally{
            Database.close(connection, statement, results);
        }
        
        removeListedBooks(booklistID);
        
        return deletedList;
    }
    
    public boolean removeListedBooks(int booklistID){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet results = null;
        System.out.println("DELETE LISTED BOOKS:    "+booklistID);
        String query = "DELETE FROM booklist_x_book WHERE booklist_id=? ";
        try{
            connection = Database.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, booklistID);
            if(statement.executeUpdate()>0){
                return true;
            }
        }catch(SQLException e){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Exception in query: "+query);
            e.printStackTrace();
        }finally{
            Database.close(connection, statement, results);
        }
        return false;
    }
    
    public boolean removeBookFromList(int titleID, int booklistID){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet results = null;
        System.out.println("DELETE TITLE: "+titleID);
        System.out.println("FROM LIST:    "+booklistID);
        String query = "DELETE FROM booklist_x_book WHERE book_title_id=? AND booklist_id=? ";
        try{
            connection = Database.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, titleID);
            statement.setInt(2, booklistID);
            if(statement.executeUpdate()>0){
                return true;
            }
        }catch(SQLException e){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Exception in query: "+query);
            e.printStackTrace();
        }finally{
            Database.close(connection, statement, results);
        }
        
        return false;
    }
    
    /*
     * Adds the received book to the list in the database
     * 
     */
    public boolean addBookToList(int titleID, int booklistID){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet results = null;
        String query = "INSERT INTO booklist_x_book (book_title_id, booklist_id) VALUES(?,?)";
        try{
            connection = Database.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, titleID);
            statement.setInt(2, booklistID);
            if(statement.executeUpdate()>0){
                return true;
            }
        }catch(SQLException e){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Exception in query: "+query);
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
    public BookList getListByID(int id, int customer_id){
        BookList booklist = new BookList();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet results = null;
        String query = "SELECT title, description, is_public FROM booklist WHERE booklist_id = ?";
        
        try{
            connection = Database.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            results = statement.executeQuery();
            
            while(results.next()){
                booklist.setTitle(results.getString("title"));
                booklist.setDescription(results.getString("description"));
                booklist.setIspublic(results.getBoolean("is_public"));
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
                + "AND booklist_x_book.booklist_id = ? "
                + "AND booklist.customer_id = ? "
                + "AND booklist_x_book.booklist_id = booklist.booklist_id";
        
        try{
            connection = Database.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.setInt(2, customer_id);
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
    
    public boolean makeBookListPublic(int booklistkey){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet results = null;
        String query = "UPDATE booklist SET is_public = ? WHERE booklist_id = ?";
        
        try{
            connection = Database.getConnection();
            statement = connection.prepareStatement(query);
            statement.setBoolean(1, true);
            statement.setInt(2, booklistkey);
            if(statement.executeUpdate()>0){
                return true;
            }
        }catch(SQLException e){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Exception in query: "+query);
            e.printStackTrace();
        }finally{
            Database.close(connection, statement, results);
        }
        return false;
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
