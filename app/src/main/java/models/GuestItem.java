package models;

public class GuestItem {

    private String purchaseId,email,name,address;
    private String phone;

    public GuestItem(String purchaseId, String email, String name, String address, String phone) {
        this.purchaseId = purchaseId;
        this.email = email;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public GuestItem(String email, String name, String address, String phone) {
        this.email = email;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
