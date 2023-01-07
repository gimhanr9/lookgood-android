package models;

public class CartItem {
    private String id,productId,imageUrl,productName,size;
    private  int quantity;
    private double price;

    public CartItem() {
    }

    public CartItem(String id, String productId, String imageUrl, String productName, String size, int quantity, double price) {
        this.id=id;
        this.productId=productId;
        this.imageUrl = imageUrl;
        this.productName = productName;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
    }

    public CartItem(String productId,String imageUrl, String productName, String size, int quantity, double price) {
        this.productId=productId;
        this.imageUrl = imageUrl;
        this.productName = productName;
        this.size = size;
        this.quantity = quantity;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


}
