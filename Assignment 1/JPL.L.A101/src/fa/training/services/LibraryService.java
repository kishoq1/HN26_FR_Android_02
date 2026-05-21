package fa.training.services;

import fa.training.entities.Book;
import fa.training.entities.Magazine;
import fa.training.entities.Publication;
import fa.training.utils.Validation;

import java.util.*;

public class LibraryService {
    private final List<Publication> publications;

    public LibraryService() {
        this.publications = new ArrayList<>();
    }

    // 1. Add a book
    public void addBook() {
        System.out.println("--- Add New Book ---");
        String isbn = Validation.getValidIsbn();
        int year = Validation.getInt("Enter Publication Year: ");
        String publisher = Validation.getString("Enter Publisher: ");
        Date date = Validation.getDateMatchingYear("Enter Publication Date", year);
        String place = Validation.getString("Enter Publication Place: ");

        Set<String> authors = new HashSet<>();
        authors.add(Validation.getString("Enter an Author Name: "));

        Book newBook = new Book(year, publisher, date, isbn, authors, place);
        publications.add(newBook);
        System.out.println("Book added successfully!");
    }

    // 2. Add a magazine
    public void addMagazine() {
        System.out.println("--- Add New Magazine ---");
        int year = Validation.getInt("Enter Publication Year: ");
        String publisher = Validation.getString("Enter Publisher: ");
        Date date = Validation.getDateMatchingYear("Enter Publication Date", year);
        String author = Validation.getString("Enter Author Name: ");
        int volume = Validation.getInt("Enter Volume: ");
        int edition = Validation.getInt("Enter Edition: ");

        Magazine newMagazine = new Magazine(year, publisher, date, author, volume, edition);
        publications.add(newMagazine);
        System.out.println("Magazine added successfully!");
    }

    // 3. Display books and magazines by same year and publisher
    public void displayBooksAndMagazines() {
        int year = Validation.getInt("Enter Publication Year to search: ");
        String publisher = Validation.getString("Enter Publisher to search: ");

        List<Publication> result = publications.stream()
                .filter(p -> p.getPublicationYear() == year && p.getPublisher().equalsIgnoreCase(publisher))
                .toList();

        if (result.isEmpty()) {
            System.out.println("No publications found.");
        } else {
            result.forEach(Publication::display);
        }
    }

    // 4. Add author to book
    public void addAuthorToBook() {
        String isbn = Validation.getValidIsbn();
        Optional<Book> bookOpt = publications.stream()
                .filter(p -> p instanceof Book)
                .map(p -> (Book) p)
                .filter(b -> b.getIsbn().equals(isbn))
                .findFirst();

        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            String newAuthor = Validation.getString("Enter new Author Name: ");
            if (book.getAuthor().contains(newAuthor)) {
                System.out.println("Author existed");
            } else {
                book.getAuthor().add(newAuthor);
                System.out.println("Add successfully");
            }
        } else {
            System.err.println("Book with ISBN " + isbn + " not found!");
        }
    }

    // 5. Display top 10 of magazines by volume
    public void displayTop10Magazines() {
        System.out.println("--- Top 10 Magazines by Volume ---");
        publications.stream()
                .filter(p -> p instanceof Magazine)
                .map(p -> (Magazine) p)
                .sorted(Comparator.comparingInt(Magazine::getVolume).reversed())
                .limit(10)
                .forEach(Magazine::display);
    }

    // 6. Search book by (isbn, author, publisher)
    public void searchBook() {
        String keyword = Validation.getString("Enter Search Keyword (ISBN, Author, or Publisher): ").toLowerCase();

        List<Book> result = publications.stream()
                .filter(p -> p instanceof Book)
                .map(p -> (Book) p)
                .filter(b -> b.getIsbn().toLowerCase().contains(keyword) ||
                        b.getPublisher().toLowerCase().contains(keyword) ||
                        b.getAuthor().stream().anyMatch(a -> a.toLowerCase().contains(keyword)))
                // Sort by ISBN, then by Date
                .sorted(Comparator.comparing(Book::getIsbn).thenComparing(Book::getPublicationDate))
                .toList();

        if (result.isEmpty()) {
            System.out.println("No books found matching keyword: " + keyword);
        } else {
            result.forEach(Book::display);
        }
    }
}