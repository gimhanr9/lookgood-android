package models;

public class AvailabilityModel {

    private String imageUrl,name,size;
    private int quantity;

    public AvailabilityModel(String imageUrl, String name, String size, int quantity) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.size=size;
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
