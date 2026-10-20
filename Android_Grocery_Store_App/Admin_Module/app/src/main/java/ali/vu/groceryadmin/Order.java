package ali.vu.groceryadmin;

import java.util.List;

public class Order {
    private String orderId;
    private String name;
    private String number;
    private String address;
    private String city;
    private String deliveryTimeSlot;
    private String paymentMethod;
    private String orderStatus;
    private List<String> productDetails;
    private double totalBill;

    // Default constructor required for Firebase
    public Order() {
    }

    public Order(String orderId, String name, String number, String address, String city,
                 String deliveryTimeSlot, String paymentMethod, String orderStatus,
                 List<String> productDetails, double totalBill) {
        this.orderId = orderId;
        this.name = name;
        this.number = number;
        this.address = address;
        this.city = city;
        this.deliveryTimeSlot = deliveryTimeSlot;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
        this.productDetails = productDetails;
        this.totalBill = totalBill;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<String> getProductDetails() {
        return productDetails;
    }

    public double getTotalBill() {
        return totalBill;
    }
}
