package store;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class PricesTable {
    public void createPricesTable(){
        Statement statement = null;
        try {
            statement = Store.connection.createStatement();
            if (statement.execute("CREATE TABLE IF NOT EXISTS prices (_id INTEGER PRIMARY KEY, itemId INTEGER, price REAL)")) {
                System.out.println("prices table created");
            }
        } catch (SQLException e){
            System.out.println("Cannot create the prices table.");
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

    public void addPricesIntoTable(Price price) {
        if (price != null){
            PreparedStatement prep = null;
            try {
                prep = Store.connection.prepareStatement("INSERT INTO prices (itemId, price) SELECT ?, ? WHERE NOT EXISTS(SELECT _id FROM prices WHERE itemId = ?)");
                prep.setInt(1, price.getItemId());
                prep.setDouble(2, price.getPrice());
                prep.setInt(3, price.getItemId());
                prep.executeUpdate();
                ResultSet row = prep.getGeneratedKeys();
                if (row.next()){
                    System.out.println("Price inserted.");
                }
            } catch (SQLException e){
                System.out.println("Cannot insert the price into the stocks table.");
                e.printStackTrace();
            } finally {
                if (prep != null){
                    try {
                        prep.close();
                    } catch (SQLException e2){
                        System.out.println(e2.getMessage());
                    }
                }
            }
        }
    }

    public void updatePriceForAnItem (Item item, double price) {
        if (item != null && price >= 0.0d){
            PreparedStatement prep = null;
            try {
                prep = Store.connection.prepareStatement("UPDATE prices SET price = ? WHERE itemId = ?");
                prep.setDouble(1, price);
                prep.setInt(2, item.get_id());
                int rowsAffected = prep.executeUpdate();
                if (rowsAffected == 1){
                    System.out.println("Price for " + item.getName() + " was updated to " + price);
                } else {
                    System.out.println("Cannot update the price for " + item.getName());
                }
            } catch (SQLException e){
                System.out.println("Cannot update the price for " + item.getName());
            } finally {
                if (prep != null){
                    try {
                        prep.close();
                    } catch (SQLException e2){
                        System.out.println(e2.getMessage());
                    }
                }
            }
        }
    }

    public double queryPriceForItem (Item item) {
        if (item != null){
            PreparedStatement prep = null;
            try {
                prep = Store.connection.prepareStatement("SELECT price FROM prices WHERE itemId = ?");
                prep.setInt(1, item.get_id());
                ResultSet result = prep.executeQuery();
                if (result.next()){
                    return result.getDouble(1);
                }
            } catch (SQLException e){
                System.out.println("Cannot get the price for " + item.getName());
            } finally {
                if (prep != null){
                    try {
                        prep.close();
                    } catch (SQLException e2){
                        System.out.println(e2.getMessage());
                    }
                }
            }
        }
        return -1;
    }

    public void deletePriceForItem (Item item){
        if (item != null){
            PreparedStatement prep = null;
            try {
                prep = Store.connection.prepareStatement("DELETE FROM prices WHERE itemId = ?");
                prep.setInt(1, item.get_id());
                prep.execute();
                System.out.println("price for " + item.getName() + " has been deleted.");
            } catch (SQLException e){
                System.out.println("Cannot delete the price for " + item.getName());
            } finally {
                if (prep != null){
                    try {
                        prep.close();
                    } catch (SQLException e2){
                        System.out.println(e2.getMessage());
                    }
                }
            }
        }
    }

    public List<Price> getPrices(){
        Statement statement = null;
        List<Price> prices = new LinkedList<>();
        try {
            statement = Store.connection.createStatement();
            statement.execute("SELECT * FROM prices");
            ResultSet results = statement.getResultSet();
            while (results.next()){
                Price price = new Price(results.getInt(1), results.getInt(2), results.getDouble(3));
                prices.add(price);
            }
        } catch (SQLException e){
            System.out.println("Error at getPrices");
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
        return prices;
    }
}
