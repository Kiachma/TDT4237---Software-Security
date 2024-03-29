package amu.model;

public class BookListItem {

  private BookList list;

  private Book book;

    public BookList getList() {
        return list;
    }

    public void setList(BookList list) {
        this.list = list;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public String toString() {
        return book.toString();
    }
  
  

}