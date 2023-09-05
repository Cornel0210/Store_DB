package store;

public class Item {
    private int _id;
    private String name;

    public Item(int _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public int get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }
}
