package com.example;

import com.example.commands.Commands;
import com.example.manager.AuthorManager;
import com.example.manager.BookManager;
import com.example.manager.CategoryManager;
import com.example.model.Author;
import com.example.model.Book;
import com.example.model.Category;
import com.example.util.DateUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main implements Commands {
    private static AuthorManager authorManager = new AuthorManager();
    private static BookManager bookManager = new BookManager();
    private static CategoryManager categoryManager = new CategoryManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean isRun = true;
        while (isRun) {
            Commands.printCommands();
            String command = scanner.nextLine();
            switch (command) {
                case EXIT:
                    isRun = false;
                    break;
                case ADD_AUTHOR:
                    addAuthor();
                    break;
                case PRINT_ALL_AUTHORS:
                    for (Author author : authorManager.getAllAuthors()) {
                        System.out.println(author);
                    }
                    break;
                case ADD_BOOK:
                    addBook();
                    break;
                case DELETE_AUTHOR:
                    deleteAuthor();
                    break;
                case DELETE_BOOK:
                    deleteBook();
                    break;
                case UPDATE_AUTHOR:
                    updateAuthor();
                    break;
                case UPDATE_BOOK:
                    updateBook();
                    break;
                case ADD_CATEGORY:
                    addCategory();
                    break;
                case PRINT_ALL_CATEGORIES:
                    for (Category category : categoryManager.getCategories()) {
                        System.out.println(category);
                    }
                    break;
                case PRINT_ALL_BOOKS:
                    for (Book allBook : bookManager.getAllBooks()) {
                        System.out.println(allBook);
                    }
                    break;
                default:
                    System.out.println("Incorrect command!");
            }
        }
    }

    private static void addCategory() {
        System.out.println("Please input category name");
        String categoryName = scanner.nextLine();
        if (categoryManager.getCategoryByName(categoryName) == null) {
            categoryManager.add(Category.builder()
                    .name(categoryName)
                    .build());
        } else {
            System.out.println("Category already exists");
        }

    }

    private static void updateBook() {
        List<Book> allBooks = bookManager.getAllBooks();
        for (Book books : allBooks) {
            System.out.println(books.getId() + " -> " + books.getTitle());
        }
        System.out.println("Please input book id");
        int bookId = Integer.parseInt(scanner.nextLine());
        Book BookByID = bookManager.getBookById(bookId);
        if (BookByID != null) {
            List<Author> allAuthors = authorManager.getAllAuthors();
            for (Author allAuthor : allAuthors) {
                System.out.println(allAuthor.getId() + " -> " + allAuthor.getName() + " " + allAuthor.getSurname());
            }
            System.out.println("Please input author id");
            int authorId = Integer.parseInt(scanner.nextLine());
            Author authorByID = authorManager.getById(authorId);
            if (authorByID != null) {
                System.out.println("Please input title,price, publish_date(yyy-MM-dd)");
                String bookDataStr = scanner.nextLine();
                String[] bookDataArr = bookDataStr.split(",");
                try {
                    Book book = Book.builder()
                            .id(bookId)
                            .title(bookDataArr[0])
                            .price(Double.parseDouble(bookDataArr[1]))
                            .publishedDate(DateUtil.convertToStringToDate(bookDataArr[2]))
                            .author(authorByID)
                            .build();
                    bookManager.updateBook(book);
                } catch (ParseException e) {
                    System.out.println("Date format is wrong!");
                }
            }
        }
    }

    private static void updateAuthor() {
        List<Author> allAuthors = authorManager.getAllAuthors();
        for (Author allAuthor : allAuthors) {
            System.out.println(allAuthor.getId() + " -> " + allAuthor.getName() + " " + allAuthor.getSurname());
        }
        System.out.println("Please input author id");
        int authorId = Integer.parseInt(scanner.nextLine());
        Author authorByID = authorManager.getById(authorId);
        if (authorByID != null) {
            System.out.println("Please select category ids by");
            String categoryStr = scanner.nextLine();
            String[] categoryArr = categoryStr.split(",");
            List<Category> categories = new ArrayList<>();
            for (String categoryID : categoryArr) {
                categories.add(categoryManager.getCategoryById(Integer.parseInt(categoryID)));
            }
            System.out.println("Please input name,surname,age,email");
            String authorDataStr = scanner.nextLine();
            String[] authorDatArr = authorDataStr.split(",");
            String email = authorDatArr[3];

            Author author = Author.builder()
                    .id(authorId)
                    .name(authorDatArr[0])
                    .surname(authorDatArr[1])
                    .age(Integer.parseInt(authorDatArr[2]))
                    .email(authorDatArr[3])
                    .build();
            authorManager.updateAuthor(author);

        }
    }

    private static void deleteBook() {
        List<Book> allBooks = bookManager.getAllBooks();
        for (Book books : allBooks) {
            System.out.println(books.getId() + " -> " + books.getTitle());
        }
        System.out.println("Please input book id");
        int bookId = Integer.parseInt(scanner.nextLine());
        Book BookByID = bookManager.getBookById(bookId);
        if (BookByID != null) {
            bookManager.deleteBookById(bookId);
        } else {
            System.out.println("Wrong id");
        }
    }

    private static void deleteAuthor() {
        List<Author> allAuthors = authorManager.getAllAuthors();
        for (Author allAuthor : allAuthors) {
            System.out.println(allAuthor.getId() + " -> " + allAuthor.getName() + " " + allAuthor.getSurname());
        }
        System.out.println("Please input author id");
        int authorId = Integer.parseInt(scanner.nextLine());
        Author authorByID = authorManager.getById(authorId);
        if (authorByID != null) {
            List<Book> allBooksByAuthorId = bookManager.getAllBooksByAuthorId(authorId);
            if (allBooksByAuthorId.isEmpty()) {
                authorManager.removeById(authorId);
            } else {
                System.out.println("There are " + allBooksByAuthorId.size() + " books. Remove all books?(yes/no)");
                String answer = scanner.nextLine();
                if ("yes".equalsIgnoreCase(answer)) {
                    bookManager.deleteBooksByAuthorId(authorId);
                    authorManager.removeById(authorId);
                }
            }
        }

    }

    private static void addBook() {
        List<Author> allAuthors = authorManager.getAllAuthors();
        for (Author allAuthor : allAuthors) {
            System.out.println(allAuthor.getId() + " -> " + allAuthor.getName() + " " + allAuthor.getSurname());
        }
        System.out.println("Please input author id");
        int authorId = Integer.parseInt(scanner.nextLine());
        Author authorByID = authorManager.getById(authorId);
        if (authorByID != null) {
            for (Category category : categoryManager.getCategories()) {
                System.out.println(category);
            }
            System.out.println("Please select category ids");
            String categoriesStr = scanner.nextLine();
            String[] categoryIds = categoriesStr.split(",");
            List<Category> categories = new ArrayList<>();
            for (String categoryId : categoryIds) {
                categories.add(categoryManager.getCategoryById(Integer.parseInt(categoryId)));
            }
            System.out.println("Please input title,price, publish_date(yyy-MM-dd)");
            String bookDataStr = scanner.nextLine();
            String[] bookDataArr = bookDataStr.split(",");
            try {
                Book book = Book.builder()
                        .title(bookDataArr[0])
                        .price(Double.parseDouble(bookDataArr[1]))
                        .publishedDate(DateUtil.convertToStringToDate(bookDataArr[2]))
                        .author(authorByID)
                        .categories(categories)
                        .build();
                bookManager.add(book);
            } catch (ParseException e) {
                System.out.println("Date format is wrong!");
            }
        }
    }

    private static void addAuthor() {
        System.out.println("Please input name,surname,age,email");
        String authorDataStr = scanner.nextLine();
        String[] authorDatArr = authorDataStr.split(",");
        String email = authorDatArr[3];
        Author authorByEmail = authorManager.getAuthorByEmail(email);
        if (authorByEmail == null) {
            Author author = Author.builder()
                    .name(authorDatArr[0])
                    .surname(authorDatArr[1])
                    .age(Integer.parseInt(authorDatArr[2]))
                    .email(authorDatArr[3])
                    .build();
            authorManager.add(author);
        } else {
            System.out.println("Author with email " + email + " already exist");
        }

    }

    private static void printCommands() {
        System.out.println("Please input 0 for EXIT");
        System.out.println("Please input 1 for ADD AUTHOR");
        System.out.println("Please input 2 for PRINT ALL AUTHORS");
        System.out.println("Please input 3 for ADD BOOK");
        System.out.println("Please input 4 for DELETE AUTHOR");
        System.out.println("Please input 5 for DELETE BOOK");
        System.out.println("Please input 6 for UPDATE AUTHOR");
        System.out.println("Please input 7 for DELETE BOOK");
        System.out.println("Please input 8 for ADD CATEGORY");
        System.out.println("Please input 9 for PRINT ALL CATEGORIES");
        System.out.println("Please input 10 for PRINT ALL BOOKS");
    }
}
