package amu.model;

import java.util.ArrayList;
import java.util.List;

public class BookList {

    public String title;
    public String description;
    public List<BookListItem> books;

    public List getBooks() {
        return books;
    }

    public void addBook(Book book) {
        if (books == null) {
            books = new ArrayList<>();
        }
        BookListItem bookListItem = new BookListItem();
        bookListItem.setBook(book);
        bookListItem.setList(this);
        books.add(bookListItem);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBooks(List<BookListItem> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        List books = getBooks();
        String tostring = "";
        for(Object book : books){
            tostring += book;
        }
        return tostring;
    }
    
    
}