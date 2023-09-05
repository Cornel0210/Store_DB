package store;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class ItemsTable {

    

    public void createItemsTable(){
        Statement statement = null;
        try {
            statement = Store.connection.createStatement();
            if (statement.execute("CREATE TABLE IF NOT EXISTS items (_id INTEGER PRIMARY KEY, name TEXT NOT NULL)")) {
                System.out.println("items table created");
            }
        } catch (SQLException e){
            System.out.println("Cannot create the items table.");
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

    public void addItemIntoTable(Item item) {
        if (item != null){
            PreparedStatement prep = null;
            try {
                prep = Store.connection.prepareStatement("INSERT INTO items (name) SELECT ? WHERE NOT EXISTS(SELECT _id FROM items WHERE name = ?)", Statement.RETURN_GENERATED_KEYS);
                prep.setString(1, item.getName());
                prep.setString(2, item.getName());
                int rowsAffected = prep.executeUpdate();
                if (rowsAffected != 0){
                    ResultSet row = prep.getGeneratedKeys();
                    if (row.next()){
                        System.out.println("inserted " + item.getName() + " at row " + row.getInt(1));
                    }
                }
            } catch (SQLException e){
                System.out.println("Cannot add " + item.getName() + " to the items table.");
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

    public void updateItem (String oldName, String newName) {
        if (oldName != null && newName != null){
            PreparedStatement prep = null;
            try {
                prep = Store.connection.prepareStatement("UPDATE items SET name = ? WHERE name = ?");
                prep.setString(1, newName);
                prep.setString(2, oldName);
                if (prep.execute()){
                    System.out.println(oldName + " was changed with " + newName);
                } else {
                    System.out.println("Cannot update " + oldName);
                }
            } catch (SQLException e){
                System.out.println("Cannot update " + oldName);
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

    public int queryItem (Item item) {
        if (item != null){
            PreparedStatement prep = null;
            try {
                prep = Store.connection.prepareStatement("SELECT _id FROM items WHERE name = ?");
                prep.setString(1, item.getName());
                ResultSet result = prep.executeQuery();
                if (result.next()){
                    return result.getInt(1);
                }
            } catch (SQLException e){
                System.out.println("Cannot query for the inserted item");
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

    public int queryItemLike (String name) {
        if (name != null){
            PreparedStatement prep = null;
            try {
                prep = Store.connection.prepareStatement("SELECT * FROM items WHERE items.name LIKE ?");
                prep.setString(1, name);
                ResultSet result = prep.executeQuery();
                if (result.next()){
                    return result.getInt(1);
                }
            } catch (SQLException e){
                System.out.println("Cannot query for the inserted item." + e.getMessage());
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
        return -1;
    }

    public void deleteItem (Item item){
        if (item != null){
            PreparedStatement prep = null;
            try {
                prep = Store.connection.prepareStatement("DELETE FROM items WHERE items.name = ?");
                prep.setString(1, item.getName());
                prep.execute();
                System.out.println(item.getName() + " has been deleted.");
            } catch (SQLException e){
                System.out.println("Cannot delete " + item.getName());
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

    public List<Item> getItems(){
        Statement statement = null;
        List<Item> items = new LinkedList<>();
        try {
            statement = Store.connection.createStatement();
            statement.execute("SELECT * FROM items");
            ResultSet results = statement.getResultSet();
            while (results.next()){
                Item item = new Item(results.getInt(1), results.getString(2));
                items.add(item);
            }
        } catch (SQLException e){
            System.out.println("Error at getItems");
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
        return items;
    }
}
