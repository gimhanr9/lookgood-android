package models;

public class PaymentDetails {
    String number,cvv,date,type;

    public PaymentDetails() {
    }

    public PaymentDetails(String number, String cvv, String date, String type) {
        this.number = number;
        this.cvv = cvv;
        this.date = date;
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
