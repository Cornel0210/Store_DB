package store;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class Store {
    private final static String CONN_PATH = "jdbc:sqlite:E:\\05. java\\05. repositories\\Store\\src\\store\\store.db";
    public static Connection connection;

    public boolean openConnection(){
        try {
            connection = DriverManager.getConnection(CONN_PATH);
            return true;
        } catch (SQLException e){
            System.out.println("Cannot open the connection to the DB.");
            return false;
        }
    }

    public void closeConnection(){
        if (connection != null){
            try {
                connection.close();
            } catch (SQLException e){
                System.out.println("Troubles at closing the connection with the DB.");
            }
        }
    }

    public void dropTable(String name){
        name = name.toLowerCase();
        Statement statement = null;
        try {
            statement = Store.connection.createStatement();
            if (statement.execute("DROP TABLE IF EXISTS " + name)){
                System.out.println(name + " table dropped.");
            }
        } catch (SQLException e){
            System.out.println("Cannot drop the " + name + " table.");
        } finally {
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException e2){
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public List<StoreItem> getStoreItems(){
        Statement statement = null;
        List<StoreItem> list = new LinkedList<>();
        try {
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT items.name, stocks.stock, prices.price FROM items" +
                    " JOIN stocks ON items._id = stocks.itemId " +
                    " JOIN prices ON items._id = prices.itemId");
            while (results.next()){
                StoreItem storeItem = new StoreItem(results.getString(1), results.getInt(2), results.getDouble(3));
                list.add(storeItem);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException e2){
                    System.out.println(e2.getMessage());
                }
            }
        }
        return list;
    }
}
