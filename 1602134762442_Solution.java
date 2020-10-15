import java.util.Arrays;
import java.util.Scanner;

public class Solution {

	public static Book[] booksWithPriceRange(double minPrice, double maxPrice, Book[] books) {
		Book[] result = new Book[2];
		int j = 0;
		for (int i = 0; i < books.length; i++) {
			if (books[i].getPrice() >= minPrice && books[i].getPrice() <= maxPrice) {
				result[j] = books[i];
				j++;
			}
		}
		return result;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int id;
		String title, author;
		double price, minPrice, maxPrice;
		Book[] books = new Book[4];
		for (int i = 0; i < 4; i++) {
			id = sc.nextInt();
			title = sc.next();
			author = sc.next();
			price = sc.nextDouble();
			books[i] = new Book(id, title, author, price);
		}
		minPrice = sc.nextDouble();
		maxPrice = sc.nextDouble();
		Arrays.sort(books);
		Book[] result = booksWithPriceRange(minPrice, maxPrice, books);
		for (int i = 0; i < result.length; i++) {
			System.out.println(result[i].getId());
		}
	}
}

class Book implements Comparable<Book> {
	private int id;
	private String title;
	private String author;
	private double price;

	public Book() {
		super();
	}

	public Book(int id, String title, String author, double price) {
		super();
		this.id = id;
		this.title = title;
		this.author = author;
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", author=" + author + ", price=" + price + "]";
	}

	@Override
	public int compareTo(Book b) {
		// TODO Auto-generated method stub
		if (this.id != b.id) {
			return this.id - b.id;
		}
		return toString().compareTo(b.toString());
	}

}
