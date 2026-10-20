package ali.vu.groceryadmin;

public class ReturnRequest {
    private String returnRequestID;
    private String elaboration;
    private String imageURL;
    private String orderName;
    private String orderStatus;
    private String phoneNumber;
    private String address;
    private String city;
    private String deliveryTimeSlot;
    private String paymentMethod;
    private String totalBill;


    public ReturnRequest() {
        // Default constructor required for Firebase
    }

    public ReturnRequest(String returnRequestID, String elaboration, String imageURL, String orderName, String orderStatus,
                         String phoneNumber, String address, String city, String deliveryTimeSlot,
                         String paymentMethod, String totalBill) {
        this.returnRequestID = returnRequestID;
        this.elaboration = elaboration;
        this.imageURL = imageURL;
        this.orderName = orderName;
        this.orderStatus = orderStatus;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.deliveryTimeSlot = deliveryTimeSlot;
        this.paymentMethod = paymentMethod;
        this.totalBill = totalBill;
    }

    public String getReturnRequestID() {
        return returnRequestID;
    }

    public String getElaboration() {
        return elaboration;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getOrderName() {
        return orderName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getDeliveryTimeSlot() {
        return deliveryTimeSlot;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getTotalBill() {
        return totalBill;
    }
}
