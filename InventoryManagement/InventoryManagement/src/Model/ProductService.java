package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProductService {

    public static void selectAll(List<Product> targetList, DatabaseConnection database) {

        PreparedStatement statement = database.newStatement("SELECT id, name FROM Products ORDER BY name");

        try {
            if (statement != null) {

                ResultSet results = database.executeQuery(statement);

                if (results != null) {
                    while (results.next()) {
                        targetList.add(new Product(
                                results.getInt("id"),
                                results.getString("name")
                        ));
                    }
                }
            }
        } catch (SQLException resultsException) {
            System.out.println("Database select all error: " + resultsException.getMessage());
        }
    }

    public static Product selectById(int id, DatabaseConnection database) {

        Product result = null;

        PreparedStatement statement = database.newStatement("SELECT id, name FROM Products WHERE id = ?");

        try {
            if (statement != null) {

                statement.setInt(1, id);
                ResultSet results = database.executeQuery(statement);

                if (results != null) {
                    result = new Product(
                            results.getInt("id"),
                            results.getString("name"));
                }
            }
        } catch (SQLException resultsException) {
            System.out.println("Database select by id error: " + resultsException.getMessage());
        }

        return result;
    }

    public static void selectProductColours(Product selectedItem, List<ProductColour> targetList, DatabaseConnection database) {

        PreparedStatement statement = database.newStatement("SELECT productId, colourId FROM ProductColours WHERE productId = ?");

        try {
            if (statement != null) {
                statement.setInt(1, selectedItem.getId());

                ResultSet results = database.executeQuery(statement);

                if (results != null) {
                    while (results.next()) {
                        targetList.add(new ProductColour(
                                results.getInt("productId"),
                                results.getInt("colourId")
                        ));
                    }
                }
            }
        } catch (SQLException resultsException) {
            System.out.println("Database select all error: " + resultsException.getMessage());
        }
    }

    public static void save(Product itemToSave, DatabaseConnection database) {

        Product existingItem = null;
        if (itemToSave.getId() != 0) existingItem = selectById(itemToSave.getId(), database);

        try {
            if (existingItem == null) {
                PreparedStatement statement = database.newStatement("INSERT INTO Products (name) VALUES (?)");
                statement.setString(1, itemToSave.getName());
                database.executeUpdate(statement);
            }
            else {
                PreparedStatement statement = database.newStatement("UPDATE Products SET name = ? WHERE id = ?");
                statement.setString(1, itemToSave.getName());
                statement.setInt(2, itemToSave.getId());
                database.executeUpdate(statement);
            }
        } catch (SQLException resultsException) {
            System.out.println("Database saving error: " + resultsException.getMessage());
        }
    }

    public static void saveProductColour(ProductColour itemToSave, DatabaseConnection database) {

        PreparedStatement statement = database.newStatement("INSERT INTO ProductColours (productId, colourId) VALUES (?, ?)");
        try {
            statement.setInt(1, itemToSave.getProductId());
            statement.setInt(2, itemToSave.getColourId());
            database.executeUpdate(statement);
        }
        catch (SQLException resultsException) {
            System.out.println("Database saving error: " + resultsException.getMessage());
        }

    }

    public static void deleteProductColour(int productId, int colourId, DatabaseConnection database) {

        PreparedStatement statement = database.newStatement("DELETE FROM ProductColours WHERE productId = ? AND colourId = ?");
        try {
            statement.setInt(1, productId);
            statement.setInt(2, colourId);
            database.executeUpdate(statement);
        }
        catch (SQLException resultsException) {
            System.out.println("Database deletion error: " + resultsException.getMessage());
        }

    }

    @SuppressWarnings("Duplicates")
    public static void deleteById(int id, DatabaseConnection database) {

        PreparedStatement statement = database.newStatement("DELETE FROM Products WHERE id = ?");

        try {
            statement.setInt(1, id);
            database.executeUpdate(statement);
        }
        catch (SQLException resultsException) {
            System.out.println("Database deletion error: " + resultsException.getMessage());
        }
    }

    @SuppressWarnings("Duplicates")
    public static void deleteProductColoursByProductId(int id, DatabaseConnection database) {

        PreparedStatement statement = database.newStatement("DELETE FROM ProductColours WHERE productId = ?");

        try {
            statement.setInt(1, id);
            database.executeUpdate(statement);
        }
        catch (SQLException resultsException) {
            System.out.println("Database deletion error: " + resultsException.getMessage());
        }
    }

}
