package store;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class StocksTable {
    public void createStocksTable(){
        Statement statement = null;
        try {
            statement = Store.connection.createStatement();
            if (statement.execute("CREATE TABLE IF NOT EXISTS stocks (_id INTEGER PRIMARY KEY, itemId INTEGER, stock INTEGER)")) {
                System.out.println("stocks table created");
            }
        } catch (SQLException e){
            System.out.println("Cannot create the stocks table.");
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

    public void addStockIntoTable(Stock stock) {
        if (stock != null){
            PreparedStatement prep = null;
            try {
                prep = Store.connection.prepareStatement("INSERT INTO stocks (itemId, stock) SELECT ?, ? WHERE NOT EXISTS(SELECT _id FROM stocks WHERE itemId = ?)");
                prep.setInt(1, stock.getItemId());
                prep.setInt(2, stock.getStock());
                prep.setInt(3, stock.getItemId());
                prep.executeUpdate();
                ResultSet row = prep.getGeneratedKeys();
                if (row.next()){
                    System.out.println("Stock inserted.");
                }
            } catch (SQLException e){
                System.out.println("Cannot insert the stock into the stocks table.");
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

    public void updateStockForAnItem(Item item, int stock) {
        if (item != null && stock >= 0){
            PreparedStatement prep = null;
            try {
                prep = Store.connection.prepareStatement("UPDATE stocks SET stock = ? WHERE itemId = ?");
                prep.setInt(1, stock);
                prep.setInt(2, item.get_id());
                int rowsAffected = prep.executeUpdate();
                if (rowsAffected == 1){
                    System.out.println("Stock for " + item.getName() + " was updated to " + stock);
                } else {
                    System.out.println("Cannot update the stock for " + item.getName());
                }
            } catch (SQLException e){
                System.out.println("Cannot update the stock for " + item.getName());
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

    public int queryStockForItem (Item item) {
        if (item != null){
            PreparedStatement prep = null;
            try {
                prep = Store.connection.prepareStatement("SELECT stock FROM stocks WHERE itemId = ?");
                prep.setInt(1, item.get_id());
                ResultSet result = prep.executeQuery();
                if (result.next()){
                    return result.getInt(1);
                }
            } catch (SQLException e){
                System.out.println("Cannot get the stock for " + item.getName());
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

    public void deleteStockForItem (Item item){
        if (item != null){
            PreparedStatement prep = null;
            try {
                prep = Store.connection.prepareStatement("DELETE FROM stocks WHERE itemId = ?");
                prep.setInt(1, item.get_id());
                prep.execute();
                System.out.println("stock for " + item.getName() + " has been deleted.");
            } catch (SQLException e){
                System.out.println("Cannot delete the stock for " + item.getName());
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

    public List<Stock> getStocks(){
        Statement statement = null;
        List<Stock> stocks = new LinkedList<>();
        try {
            statement = Store.connection.createStatement();
            statement.execute("SELECT * FROM stocks");
            ResultSet results = statement.getResultSet();
            while (results.next()){
                Stock stock = new Stock(results.getInt(1), results.getInt(2), results.getInt(3));
                stocks.add(stock);
            }
        } catch (SQLException e){
            System.out.println("Error at getStocks");
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
        return stocks;
    }
}
