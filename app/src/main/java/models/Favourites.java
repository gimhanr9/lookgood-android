package models;

public class Favourites {
    private String id,productId,imageUrl,productName,productTitle;
    private double price;

    public Favourites() {
    }

    public Favourites(String productId, String imageUrl, String productName, String productTitle, double price) {
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.productName = productName;
        this.productTitle=productTitle;
        this.price = price;
    }

    public Favourites(String imageUrl, String productName, String productTitle, double price) {
        this.imageUrl = imageUrl;
        this.productName = productName;
        this.productTitle=productTitle;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
