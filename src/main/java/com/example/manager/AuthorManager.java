package com.example.manager;

import com.example.db.DBConnectionProvider;
import com.example.model.Author;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorManager {
    Connection connection = DBConnectionProvider.getInstance().getConnection();


    public AuthorManager() {
    }

    public void add(Author author) {
        String query = "INSERT INTO author(name, surname, age,email) VALUES ('%s','%s', '%d', '%s')";
        String sql = String.format(query, author.getName(), author.getSurname(), author.getAge(), author.getEmail());
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                author.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Author> getAllAuthors() {
        String sql = "SELECT * from author";
        List<Author> authors = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                String email = resultSet.getString("email");
                int age = resultSet.getInt("age");
                Author author = new Author(id, name, surname,age,email);
                authors.add(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
    }

    public void removeById(int authorId) {
        String sql = "DELETE FROM author WHERE id = " + authorId;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    public Author getById(int id){
        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT  * from author where id = " + id);
            if (resultSet.next()){
                return getAuthorFromResult(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Author getAuthorByEmail(String email){
        String sql = "SELECT  * from author where email = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                int age = resultSet.getInt("age");
                return new Author(id,name,surname,age,email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateAuthor(Author author){
        if (getById(author.getId()) == null){
            System.out.println("Author with " + author.getId() + " id does not exists");
            return;
        }
        String query = "UPDATE author SET name='%s', surname='%s', email = '%s',age='%d' WHERE id = '%d'";
        String sql = String.format(query,author.getName(),author.getSurname(),author.getEmail(),author.getAge(),author.getId());
        try(Statement statement = connection.createStatement()){
            statement.executeUpdate(sql);
            System.out.println("Author updated");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
