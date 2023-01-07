package models;

public class UpdateItem {

    private String productId,size;
    private int quantity;

    public UpdateItem(String productId,String size, int quantity) {
        this.productId = productId;
        this.size=size;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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
}
