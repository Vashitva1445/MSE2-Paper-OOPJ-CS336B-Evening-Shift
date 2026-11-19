package com.kiet.orders;

public class Order {
    private final int orderId;
    private final String customerName;
    private final String productName;
    private final int quantity;
    private final double unitPrice;
    private final String status;

    public Order(int orderId, String customerName, String productName,
                 int quantity, double unitPrice, String status) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.status = status;
    }

    public int getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public String getStatus() { return status; }
}
