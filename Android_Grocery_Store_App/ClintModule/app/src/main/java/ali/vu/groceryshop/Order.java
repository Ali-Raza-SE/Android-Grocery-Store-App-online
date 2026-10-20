package ali.vu.groceryshop;

import java.util.List;

public class Order {
    private String orderId;
    private String userID;
    private String name;
    private String number;
    private String address;
    private String city;
    private String deliveryTimeSlot;
    private String paymentMethod;
    private String orderStatus;
    private List<String> orderedItems;
    private double totalBill;

    public Order() {
        // Default constructor required for Firebase
    }

    public Order(String orderId, String userID, String name, String number, String address, String city,
                 String deliveryTimeSlot, String paymentMethod, String orderStatus,
                 List<String> orderedItems, double totalBill) {
        this.orderId = orderId;
        this.userID = userID; // Initialize userID
        this.name = name;
        this.number = number;
        this.address = address;
        this.city = city;
        this.deliveryTimeSlot = deliveryTimeSlot;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
        this.orderedItems = orderedItems;
        this.totalBill = totalBill;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDeliveryTimeSlot() {
        return deliveryTimeSlot;
    }

    public void setDeliveryTimeSlot(String deliveryTimeSlot) {
        this.deliveryTimeSlot = deliveryTimeSlot;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<String> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(List<String> orderedItems) {
        this.orderedItems = orderedItems;
    }

    public double getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(double totalBill) {
        this.totalBill = totalBill;
    }
}
