package models;

public class PurchasesItem {
    private String id,productId,imageUrl,productName,size;
    private  int quantity;
    private double price;
    private boolean isRated;

    public PurchasesItem() {
    }

    public PurchasesItem(String id, String productId, String imageUrl, String productName, String size, int quantity, double price,boolean isRated) {
        this.id=id;
        this.productId=productId;
        this.imageUrl = imageUrl;
        this.productName = productName;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
        this.isRated=isRated;
    }

    public PurchasesItem(String productId,String imageUrl, String productName, String size, int quantity, double price,boolean isRated) {
        this.productId=productId;
        this.imageUrl = imageUrl;
        this.productName = productName;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
        this.isRated=isRated;
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

    public boolean isRated() {
        return isRated;
    }

    public void setRated(boolean rated) {
        isRated = rated;
    }
}
