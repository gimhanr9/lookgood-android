package models;

public class User {


    private String email,password,name,address;
    private String phone;

    public User() {
    }

    public User(String email, String password, String name, String address, String phone) {
        this.email = email;
        this.password=password;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public User(String email, String name, String address, String phone) {
        this.email = email;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
