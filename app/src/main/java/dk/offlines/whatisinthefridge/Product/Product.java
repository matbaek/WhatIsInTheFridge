package dk.offlines.whatisinthefridge.Product;

public class Product {
    private int id;
    private String name;
    private String expire;
    private String added;

    public Product(int id, String name, String expire, String added) {
        this.id = id;
        this.name = name;
        this.expire = expire;
        this.added = added;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpire() { return expire; }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getAdded() {
        return added;
    }

    public void setAdded(String added) {
        this.added = added;
    }
}
