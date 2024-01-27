package superstore.DataControllers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import oracle.jdbc.OracleTypes;
import superstore.Data.Product;

public class ProductController {
    List<Product> products;

    public ProductController(Connection connection) {
        this.products = retrieveAllProducts(connection);
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Product getMyProduct(int index){
        return products.get(index);
    }

    // Gets the names of all products
    public List<String> getProductsName () {
        List<String> names = new ArrayList<>();
        for (Product product : products) {
            names.add(product.getName());
        }
        return names;
    }

    // Gets specific product based by name
    public Product getProductByName (String name) {
        Product newProduct = null;
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(name)){
                newProduct = product;
            }
        }
        return newProduct;
    }

    // Gets all categories of products
    public List<String> getProductsCategory () {
        Set <String> categories = new HashSet<>();
        for (Product product : products) {
            categories.add(product.getCategory());
        }
        return new ArrayList<>(categories);
    }
    

    // Gets product list based on categogry.
    public List<Product> getProductsByCategory (String category) {
        List<Product> list = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategory() != null){
                if (product.getCategory().equalsIgnoreCase(category)){
                    list.add(product);
                }
            }
        }
        return list;
    }

    // Gets products based on price range
    public List<Product> getByPriceRange (double min, double max) {
        List<Product> filteredProducts = new ArrayList<>();

        for (Product product : products) {
            if (product.getPrice() >= min && product.getPrice() <= max) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }
    // Gets product by id
    public Product getById (int id) {
        for (Product product : this.products) {
            if (product.getProductId() == id) {
                return product;
            }
        }
        return null;
    }

    // Gets all products from the database
    public List<Product> retrieveAllProducts (Connection connection) {
        List<Product> products = new ArrayList<Product>();
        try (CallableStatement statement = connection.prepareCall("{? = call RETRIEVEDATA.GETALLPRODUCTS}");){
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet) statement.getObject(1);

            while(resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                String name = resultSet.getString("name");
                String category = resultSet.getString("category");
                double price = resultSet.getDouble("price");
                products.add(new Product(productId, name, category, price));
        }
        } 
        catch (SQLException e) {
            e.getStackTrace();
            System.out.println("Error : Unable to get all products!");
        }
        return products;
    }
}
