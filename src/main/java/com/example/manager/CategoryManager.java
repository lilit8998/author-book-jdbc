package com.example.manager;

import com.example.db.DBConnectionProvider;
import com.example.model.Author;
import com.example.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryManager {
    Connection connection = DBConnectionProvider.getInstance().getConnection();


    public CategoryManager() {
    }

    public void add(Category category) {
        String query = "INSERT INTO category(name) VALUES (?)";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, category.getName());
            ps.execute();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                category.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteById(int authorId) {
        String sql = "DELETE FROM authors WHERE id = " + authorId;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Category getCategoryById(int id) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT  * from category where id = " + id);
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                return Category.builder()
                        .id(id)
                        .name(name)
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Category getCategoryByName(String name) {
        String sql = "SELECT * FROM category WHERE name = " + name;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                return Category.builder()
                        .id(id)
                        .name(name)
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Category> getCategories() {
        String sql = "SELECT * FROM category";
        List<Category> categories = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                categories.add(Category.builder()
                        .id(id)
                        .name(name)
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

}
