package store;

public class Stock {
    private int _id;
    private int itemId;
    private int stock;

    public Stock(int _id, int itemId, int stock) {
        this._id = _id;
        this.itemId = itemId;
        this.stock = stock;
    }

    public int get_id() {
        return _id;
    }

    public int getItemId() {
        return itemId;
    }

    public int getStock() {
        return stock;
    }
}
