package com.example.manager;

import com.example.db.DBConnectionProvider;
import com.example.model.Author;
import com.example.model.Book;
import com.example.model.Category;
import com.example.util.DateUtil;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class BookManager {
    Connection connection = DBConnectionProvider.getInstance().getConnection();

    private AuthorManager authorManager = new AuthorManager();
private CategoryManager categoryManager = new CategoryManager();
    public BookManager() {
    }

    public void add(Book book) {
        String query = "INSERT INTO book(title, price, published_fate, author_id) VALUES ('%s','%s', '%s', '%s')";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, book.getTitle());
            ps.setDouble(2, book.getPrice());
            ps.setString(3, DateUtil.convertDateToString(book.getPublishedDate()));
            ps.setInt(4, book.getAuthor().getId());
            Statement statement = connection.createStatement();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                book.setId(id);
            }
            addBookCategories(book);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addBookCategories(Book book) {
        if (book.getCategories() != null && book.getCategories().isEmpty()) {
            for (Category category : book.getCategories()){
                String sql = "INSERT INTO book_category(category_id,book_id) VALUES(?,?);";
                try(PreparedStatement ps = connection.prepareStatement(sql)){
                    ps.setInt(1,category.getId());
                    ps.setInt(2, book.getId());
                    ps.executeUpdate();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Book> getAllBooks() {
        String sql = "SELECT * FROM book";
        List<Book> books = new ArrayList<>();
        try(  Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                double price = resultSet.getDouble("price");
                Date publishedDate = DateUtil.convertToStringToDate(resultSet.getString("published_date"));
                int authorId = resultSet.getInt("author_id");
                Author author = authorManager.getById(authorId);
                List<Category> categories = getBookCategory(id);
                Book book = new Book(id, title, price, publishedDate, author,categories);
                books.add(book);
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<Book> getAllBooksByAuthorId(int authorId) {
        String sql = "SELECT * FROM book WHERE author_id " + authorId;
        List<Book> books = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                double price = resultSet.getDouble("price");
                Date publishedDate = DateUtil.convertToStringToDate(resultSet.getString("published_date"));
                Author author = authorManager.getById(authorId);
                List<Category> categories = getBookCategory(id);
                Book book = new Book(id, title, price, publishedDate, author,categories);
                books.add(book);
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return books;
    }

    public void deleteBookById(int id) {
        if (getBookById(id) == null) {
            System.out.println("Book with " + id + " does not exists");
            return;
        }
        String sql = "DELETE FROM book WHERE id = " + id;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("Book was removed");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteBooksByAuthorId(int authorId) {

        String sql = "DELETE FROM book WHERE author_id = " + authorId;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("Books were removed");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Book getBookById(int id) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT  * from author where id = " + id);
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                double price = resultSet.getDouble("price");
                Date publishedDate = DateUtil.convertToStringToDate(resultSet.getString("published_date"));
                int authorId = resultSet.getInt("author_id");
                Author author = authorManager.getById(authorId);
                List<Category> categories = getBookCategory(id);
                return new Book(id, title, price, publishedDate, author,categories);
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Category> getBookCategory(int id) {
        String sql = "SELECT * FROM book_category WHERE book_id =" + id;
        List<Category> result = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                int categoryId = resultSet.getInt("category_id");
                result.add(categoryManager.getCategoryById(categoryId));

            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    private Author getAuthorFromResult(ResultSet resultSet) throws SQLException {
        Author author = new Author();
        author.setId(resultSet.getInt("id"));
        author.setName(resultSet.getString("name"));
        author.setSurname(resultSet.getString("surname"));
        author.setEmail(resultSet.getString("email"));
        author.setAge(resultSet.getInt("age"));
        return author;
    }

    public void updateBook(Book book) {
        if (getBookById(book.getId()) == null) {
            System.out.println("Book with " + book.getId() + " id does not exist");
            return;
        }
        String query = "UPDATE book SET title = ?,price = ?, published_date = ?, author_id = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, book.getTitle());
            ps.setDouble(2, book.getPrice());
            ps.setString(3, DateUtil.convertDateToString(book.getPublishedDate()));
            ps.setInt(4, book.getAuthor().getId());
            ps.executeUpdate();
            deleteBookCategories(book);
            addBookCategories(book);
            System.out.println("Book updated");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteBookCategories(Book book) {
        String sql = "DELETE FROM book_category WHERE book_id = " + book.getId();
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
