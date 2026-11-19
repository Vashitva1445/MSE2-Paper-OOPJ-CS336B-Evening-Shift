package com.kiet.orders;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final OrderDAO dao = new OrderDAO();

    public static void main(String[] args) {
        // Test the JDBC connection before showing the menu.
        try (Connection ignored = DBConnection.getConnection()) {
            System.out.println("Connected to ecommerce_db successfully!");
        } catch (SQLException | RuntimeException e) {
            System.out.println("Could not connect to MySQL: " + e.getMessage());
            System.out.println("Check MySQL Server, database script, and db.properties.");
            return;
        }

        int choice;
        do {
            System.out.println("\n===== ORDER MANAGEMENT SYSTEM =====");
            System.out.println("1. Insert three sample orders");
            System.out.println("2. Display orders by status (quantity > 2)");
            System.out.println("3. Increase order quantity");
            System.out.println("4. Update order status");
            System.out.println("5. Display total order value");
            System.out.println("6. Delete order only if Cancelled");
            System.out.println("7. Display all orders");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            choice = readInt();
            switch (choice) {
                case 1 -> insertSamples();
                case 2 -> {
                    System.out.print("Enter status: ");
                    dao.displayByStatusAndQuantity(scanner.nextLine().trim());
                }
                case 3 -> {
                    System.out.print("Enter order ID: ");
                    int id = readInt();
                    System.out.print("Enter additional quantity: ");
                    int quantity = readInt();
                    dao.increaseQuantity(id, quantity);
                }
                case 4 -> {
                    System.out.print("Enter order ID: ");
                    int id = readInt();
                    System.out.print("Enter new status (Pending/Shipped/Delivered): ");
                    String status = scanner.nextLine().trim();
                    dao.updateStatus(id, status);
                }
                case 5 -> dao.displayTotalOrderValue();
                case 6 -> {
                    System.out.print("Enter order ID to delete: ");
                    dao.deleteCancelledOrder(readInt());
                }
                case 7 -> dao.displayAllOrders();
                case 0 -> System.out.println("Exiting application.");
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 0);
    }

    private static void insertSamples() {
        dao.insertOrder(new Order(101, "Aarav Sharma", "Laptop", 3, 55000.00, "Pending"));
        dao.insertOrder(new Order(102, "Diya Verma", "Mouse", 5, 750.00, "Shipped"));
        dao.insertOrder(new Order(103, "Kabir Singh", "Keyboard", 2, 1500.00, "Cancelled"));
    }

    private static int readInt() {
        while (true) {
            String value = scanner.nextLine().trim();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid integer: ");
            }
        }
    }
}
