import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<Book> books;
    private List<Book> borrowed;

    public Library() {
        books = new ArrayList<>();
        borrowed = new ArrayList<>();
        // temp
        addBook(new Book("title", "author", "0"));
        addBook(new Book("title2", "author", "1"));
        addBook(new Book("test", "na", "2"));
        borrowBook("2");
    }
    public void addBook(Book book) {
        books.add(book);
    }
    public Book searchByTitle(String title) {
        for(Book b : books) {
            if(b.getTitle().equalsIgnoreCase(title)) return b;
        }
        return null;
    }
    public void displayBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
        } else {
            for (Book b : books) {
                System.out.println(b.toString());
            }
        }
    }
    public boolean removeBookByIsbn(String isbn) {
        for(Book b : books) {
            if(isbn.equals(b.getIsbn())) {
                books.remove(b);
                return true;
            }
        }
        return false;
    }
    public boolean updateBookByIsbn(String isbn, String newTitle, String newAuthor) {
        for(Book b : books) {
            if(isbn.equals(b.getIsbn())) {
                b.setAuthor(newAuthor);
                b.setTitle(newTitle);
                return true;
            }
        }
        return false;
    }
    public List<Book> searchByAuthor(String author) {
        List<Book> matchingAuther = new ArrayList<>();
        for(Book b : books) {
            if(author.equals(b.getAuthor())) {
                matchingAuther.add(b);
            }
        }
        return matchingAuther;
    }
    public List<Book> getBorrowedBooks() {
        return borrowed;
    }
    public boolean returnBook(String returnIsbn) {
        for(Book b : borrowed) {
            if(b.getIsbn().equals(returnIsbn)) {
                books.add(b);
                borrowed.remove(b);
                return true;
            }
        }
        return false;
    }
    public boolean borrowBook(String borrowIsbn) {
        for(Book b : books) {
            if(b.getIsbn().equals(borrowIsbn)) {
                borrowed.add(b);
                books.remove(b);
                return true;
            }
        }
        return false;
    }
}