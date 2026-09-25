import java.io.Serializable;

public class SportProduct implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String brand;
    private String category;
    private String size;
    private double price;
    private int stock;

    public SportProduct(String name, String brand, String category, String size, double price, int stock) {
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.size = size;
        this.price = price;
        this.stock = stock;
    }

    public String getName() { return name; }
    public String getBrand() { return brand; }
    public String getCategory() { return category; }
    public String getSize() { return size; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    public void setName(String name) { this.name = name; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setCategory(String category) { this.category = category; }
    public void setSize(String size) { this.size = size; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }

    public boolean reduceStock(int quantity) {
        if (stock >= quantity) {
            stock -= quantity;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return name + " (" + brand + ") - " + category + ", размер: " + size + ", $" + price + ", в наличии: " + stock;
    }
}