package fa.training.entities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
public class Book extends Publication {
    private String isbn;
    private Set<String> author;
    private String publicationPlace;

    public Book() {
        super();
        this.author = new HashSet<>();
    }

    public Book(int publicationYear, String publisher, Date publicationDate, String isbn, Set<String> author, String publicationPlace){
        super(publicationYear, publisher, publicationDate);
        this.isbn = isbn;
        this.author = author;
        this.publicationPlace = publicationPlace;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Set<String> getAuthor() {
        return author;
    }

    public void setAuthor(Set<String> author) {
        this.author = author;
    }

    public String getPublicationPlace() {
        return publicationPlace;
    }

    public void setPublicationPlace(String publicationPlace) {
        this.publicationPlace = publicationPlace;
    }

    @Override
    public void display() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("--- Book Info ---");
        System.out.println("ISBN: " + isbn);
        System.out.println("Publication Year: " + getPublicationYear());
        System.out.println("Publisher: " + getPublisher());
        System.out.println("Publication Date: " + sdf.format(getPublicationDate()));
        System.out.println("Authors: " + author.toString());
        System.out.println("Publication Place: " + publicationPlace);
        System.out.println("-----------------");
    }
}
