import java.util.List;
import java.util.Scanner;

public class LibraryManager {
    public static void main(String[] args) {
        Library library = new Library();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to the Library Management System");
            System.out.println("1. Add a new book");
            System.out.println("2. Search for a book by title");
            System.out.println("3. Display all books");
            System.out.println("4. Remove a book by ISBN");
            System.out.println("5. Update a book by ISBN");
            System.out.println("6. List books by author");
            System.out.println("7. Borrow a book");
            System.out.println("8. Return a book");
            System.out.println("9. List borrowed books");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter book title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter book author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter book ISBN: ");
                    String isbn = scanner.nextLine();
                    Book book = new Book(title, author, isbn);
                    library.addBook(book);
                    System.out.println("Book added successfully!");
                    break;

                case 2:
                    System.out.print("Enter book title to search: ");
                    String searchTitle = scanner.nextLine();
                    Book foundBook = library.searchByTitle(searchTitle);
                    if (foundBook != null) {
                        System.out.println("Book found: " + foundBook);
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;

                case 3:
                    System.out.println("Books in the library:");
                    library.displayBooks();
                    break;

                case 4:
                    System.out.print("Enter book ISBN to remove: ");
                    String isbnToRemove = scanner.nextLine();
                    boolean isRemoved = library.removeBookByIsbn(isbnToRemove);
                    if (isRemoved) {
                        System.out.println("Book removed successfully!");
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;

                case 5:
                    System.out.print("Enter book ISBN to update: ");
                    String isbnToUpdate = scanner.nextLine();
                    System.out.print("Enter new title: ");
                    String newTitle = scanner.nextLine();
                    System.out.print("Enter new author: ");
                    String newAuthor = scanner.nextLine();
                    boolean isUpdated = library.updateBookByIsbn(isbnToUpdate, newTitle, newAuthor);
                    if (isUpdated) {
                        System.out.println("Book updated successfully!");
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;

                case 6:
                    System.out.print("Enter author name to search: ");
                    String authorToSearch = scanner.nextLine();
                    List<Book> booksByAuthor = library.searchByAuthor(authorToSearch);
                    if (booksByAuthor.isEmpty()) {
                        System.out.println("No books found by this author.");
                    } else {
                        System.out.println("Books by " + authorToSearch + ":");
                        for (Book b : booksByAuthor) {
                            System.out.println(b);
                        }
                    }
                    break;

                case 7:
                    System.out.print("Enter book ISBN to borrow: ");
                    String borrowIsbn = scanner.nextLine();
                    boolean isBorrowed = library.borrowBook(borrowIsbn);
                    if (isBorrowed) {
                        System.out.println("Book borrowed successfully!");
                    } else {
                        System.out.println("Book not found or already borrowed.");
                    }
                    break;

                case 8:
                    System.out.print("Enter book ISBN to return: ");
                    String returnIsbn = scanner.nextLine();
                    boolean isReturned = library.returnBook(returnIsbn);
                    if (isReturned) {
                        System.out.println("Book returned successfully!");
                    } else {
                        System.out.println("Book not found in borrowed list.");
                    }
                    break;

                case 9:
                    System.out.println("Borrowed books:");
                    List<Book> borrowedBooks = library.getBorrowedBooks();
                    if (borrowedBooks.isEmpty()) {
                        System.out.println("No books currently borrowed.");
                    } else {
                        for (Book borrowedBook : borrowedBooks) {
                            System.out.println(borrowedBook);
                        }
                    }
                    break;

                case 0:
                    System.out.println("Exiting the program. Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
