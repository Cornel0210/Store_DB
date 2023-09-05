package store;

public class Price {
    private int _id;
    private int itemId;
    private double price;

    public Price(int _id, int itemId, double price) {
        this._id = _id;
        this.itemId = itemId;
        this.price = price;
    }

    public int get_id() {
        return _id;
    }

    public int getItemId() {
        return itemId;
    }

    public double getPrice() {
        return price;
    }
}
