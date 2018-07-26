package javaapplication67;
public class Book {

    private final String firstName;
    private final String lastName;
    private final int ISBN;
    private final String title;
    private final int editionNumber;
    private final int copyRight;
    private int authorsID;
    private int authorID;
    
    
    public Book(String firstName, String lastName, int ISBN, String title, int editionNumber, int copyRight) {
        this.firstName=firstName;
        this.lastName=lastName;
        this.ISBN=ISBN;
        this.title=title;
        this.editionNumber=editionNumber;
        this.copyRight=copyRight;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getISBN() {
        return ISBN;
    }

    public String getTitle() {
        return title;
    }

    public int getEditionNumber() {
        return editionNumber;
    }

    public int getCopyRight() {
        return copyRight;
    }

    public int getAuthorID() {
        return authorID;
    }

    public int getAuthorsID() {
        return authorsID;
    }
    
    public void setAuthorsID(int authorsID) {
        this.authorsID = authorsID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }
    
    @Override
    public String toString() {
        return getAuthorsID()+" "+getFirstName()+" "+getLastName()+" "+getAuthorID()+" "+getISBN()+" "+getTitle()+" "+getEditionNumber()+" "+getCopyRight();
    }
}
