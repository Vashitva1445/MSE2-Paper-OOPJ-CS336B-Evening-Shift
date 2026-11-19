package com.kiet.orders;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // Q1(a): PreparedStatement insertion. Duplicate primary keys are handled.
    public void insertOrder(Order order) {
        String sql = "INSERT INTO Orders " +
                "(order_id, customer_name, product_name, quantity, unit_price, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, order.getOrderId());
            ps.setString(2, order.getCustomerName());
            ps.setString(3, order.getProductName());
            ps.setInt(4, order.getQuantity());
            ps.setDouble(5, order.getUnitPrice());
            ps.setString(6, order.getStatus());
            ps.executeUpdate();
            System.out.println("Order inserted successfully: " + order.getOrderId());
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Duplicate order ID " + order.getOrderId()
                    + ". This order was not inserted.");
        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                System.out.println("Duplicate/constraint error: " + e.getMessage());
            } else {
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }

    // Q1(b): Filter by status and quantity > 2; sort unit price descending.
    public void displayByStatusAndQuantity(String status) {
        String sql = "SELECT * FROM Orders WHERE status = ? AND quantity > 2 " +
                "ORDER BY unit_price DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                printOrders(rs);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // Q1(c): Increase the quantity of an existing order.
    public void increaseQuantity(int orderId, int additionalQuantity) {
        if (additionalQuantity <= 0) {
            System.out.println("Additional quantity must be greater than zero.");
            return;
        }
        String sql = "UPDATE Orders SET quantity = quantity + ? WHERE order_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, additionalQuantity);
            ps.setInt(2, orderId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Quantity updated successfully.");
            } else {
                System.out.println("Order ID " + orderId + " does not exist.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // Q1(d): Validate status, verify order, update and display updated details.
    public void updateStatus(int orderId, String newStatus) {
        if (!isValidStatus(newStatus)) {
            System.out.println("Invalid status. Use Pending, Shipped, or Delivered.");
            return;
        }

        String findSql = "SELECT * FROM Orders WHERE order_id = ?";
        String updateSql = "UPDATE Orders SET status = ? WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection()) {
            try (PreparedStatement find = con.prepareStatement(findSql)) {
                find.setInt(1, orderId);
                try (ResultSet rs = find.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Order ID " + orderId + " does not exist.");
                        return;
                    }
                }
            }

            try (PreparedStatement update = con.prepareStatement(updateSql)) {
                update.setString(1, normalizeStatus(newStatus));
                update.setInt(2, orderId);
                update.executeUpdate();
            }

            try (PreparedStatement findUpdated = con.prepareStatement(findSql)) {
                findUpdated.setInt(1, orderId);
                try (ResultSet rs = findUpdated.executeQuery()) {
                    System.out.println("Updated order details:");
                    printOrders(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // Q1(e): Display SUM(quantity * unit_price).
    public void displayTotalOrderValue() {
        String sql = "SELECT COALESCE(SUM(quantity * unit_price), 0) AS total_value FROM Orders";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                System.out.printf("Total order value: %.2f%n", rs.getDouble("total_value"));
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // Q1(e): Delete only when the selected order's status is Cancelled.
    public void deleteCancelledOrder(int orderId) {
        String sql = "DELETE FROM Orders WHERE order_id = ? AND status = 'Cancelled'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Cancelled order deleted successfully.");
            } else {
                System.out.println("Order not deleted: it does not exist or its status is not Cancelled.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void displayAllOrders() {
        String sql = "SELECT * FROM Orders ORDER BY order_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            printOrders(rs);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private boolean isValidStatus(String status) {
        return status != null &&
                (status.equalsIgnoreCase("Pending")
                || status.equalsIgnoreCase("Shipped")
                || status.equalsIgnoreCase("Delivered"));
    }

    private String normalizeStatus(String status) {
        if (status.equalsIgnoreCase("pending")) return "Pending";
        if (status.equalsIgnoreCase("shipped")) return "Shipped";
        return "Delivered";
    }

    private void printOrders(ResultSet rs) throws SQLException {
        boolean found = false;
        while (rs.next()) {
            found = true;
            System.out.printf(
                    "ID: %d | Customer: %s | Product: %s | Quantity: %d | Unit Price: %.2f | Status: %s%n",
                    rs.getInt("order_id"),
                    rs.getString("customer_name"),
                    rs.getString("product_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("unit_price"),
                    rs.getString("status"));
        }
        if (!found) {
            System.out.println("No matching orders found.");
        }
    }
}
