package models;

public class RatingItem {
    private String ratingId,productId,purchaseId,name,ratingText,size,date;
    private float ratingValue;

    public RatingItem() {

    }

    public RatingItem(String ratingId, String productId, String purchaseId, String name, String ratingText, String size, String date, float ratingValue) {
        this.ratingId = ratingId;
        this.productId = productId;
        this.purchaseId=purchaseId;
        this.name = name;
        this.ratingText = ratingText;
        this.size=size;
        this.date = date;
        this.ratingValue = ratingValue;
    }

    public RatingItem(String purchaseId, String name, String ratingText, String size, String date, float ratingValue) {
        this.purchaseId=purchaseId;
        this.name = name;
        this.ratingText = ratingText;
        this.size=size;
        this.date = date;
        this.ratingValue = ratingValue;
    }

    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRatingText() {
        return ratingText;
    }

    public void setRatingText(String ratingText) {
        this.ratingText = ratingText;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(float ratingValue) {
        this.ratingValue = ratingValue;
    }
}
