package models;


public class ProductItem {

    private String imageUrl,id,name,title,description,category,brand;
    private double price;
    private int small,medium,large,xlarge, xxl;

    public ProductItem() {
    }

    public ProductItem(String imageUrl,String id, String name,String title, String description, String category, String brand, double price,int small,int medium,int large,int xlarge,int xxl) {
        this.imageUrl=imageUrl;
        this.id = id;
        this.name = name;
        this.title=title;
        this.description=description;
        this.category=category;
        this.brand=brand;
        this.price = price;
        this.small=small;
        this.medium=medium;
        this.large=large;
        this.xlarge=xlarge;
        this.xxl = xxl;
    }

    public ProductItem(String imageUrl,String name,String title, String description, String category, String brand, double price, int small, int medium, int large, int xlarge, int xxl) {
        this.imageUrl=imageUrl;
        this.name = name;
        this.title=title;
        this.description=description;
        this.category = category;
        this.brand = brand;
        this.price = price;
        this.small = small;
        this.medium = medium;
        this.large = large;
        this.xlarge = xlarge;
        this.xxl = xxl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getSmall() {
        return small;
    }

    public void setSmall(int small) {
        this.small = small;
    }

    public int getMedium() {
        return medium;
    }

    public void setMedium(int medium) {
        this.medium = medium;
    }

    public int getLarge() {
        return large;
    }

    public void setLarge(int large) {
        this.large = large;
    }

    public int getXlarge() {
        return xlarge;
    }

    public void setXlarge(int xlarge) {
        this.xlarge = xlarge;
    }

    public int getXxl() {
        return xxl;
    }

    public void setXxl(int xxl) {
        this.xxl = xxl;
    }
}
