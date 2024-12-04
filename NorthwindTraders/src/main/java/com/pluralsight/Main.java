package com.pluralsight;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import com.sun.jdi.ClassNotPreparedException;
import java.sql.*;


public class Main {
    public static void main(String[] args) {

        String username = "newuser";
        String password = "yearup";

        // load the MySQL Driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Cannot continue, cannot load SQL driver...");
            System.exit(1);
        }


        String options = """
                Please select from the following choices:
                1 - Display All Products
                2 - Display All Customers
                3 - Display All Categories
                0 - Exit
                >>>\s""";

        int selection;

        // User Interface Loop
        do {
            selection = Console.PromptForInt(options);
            switch (selection) {
                case 1 -> processDisplayAllProducts(username, password);
                case 2 -> processDisplayAllCustomers(username, password);
                case 0 -> System.exit(0);
                default -> System.out.println("Invalid selection. Please try again.");
                try {
                    do {
                        selection = Console.PromptForInt(options);
                        switch (selection) {
                            case 1 -> processDisplayAllProducts(username, password);
                            case 2 -> processDisplayAllCustomers(username, password);
                            case 3 -> processDisplayAllCategories(username, password);
                            case 0 -> System.exit(0);
                            default -> System.out.println("Invalid selection. Please try again.");
                        }
                    } while (true);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            private static void processDisplayAllCategories (String username, String password){
                // 1. open a connection to the database
                // use the database URL to point to the correct database
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind", username, password);) {
                    try (
                            PreparedStatement statement = connection.prepareStatement("SELECT CategoryId, CategoryName FROM Categories");
                            ResultSet results = statement.executeQuery();
                    ) {
                        displayCategoriesHeader();
                        // process the results
                        while (results.next()) {
                            displayCategoriesRow(results.getInt(1), results.getString(2));
                        }
                    }
                    int selectedCategoryId = Console.PromptForInt("Please enter a Category ID: ");
                    try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM Products WHERE CategoryId = ?")) {
                        statement.setInt(1, selectedCategoryId);
                        try (ResultSet results = statement.executeQuery()) {
                            displayProductsHeader();
                            // process the results
                            while (results.next()) {
                                displayProductsRow(
                                        results.getInt(1),
                                        results.getString(2),
                                        results.getDouble(3),
                                        results.getInt(4)
                                );
                            }
                        }
                    }
                }
                while (true) ;


            } catch(SQLException e){
                throw new RuntimeException(e);
            }
        }

        private static void displayCategoriesRow ( int categoryId, String categoryName){
            System.out.printf("%10d %-40s \n", categoryId, categoryName);
        }

        private static void displayCategoriesHeader () {
            System.out.printf("%-10s %-40s \n", "Id", "Name");
        }

        private static void processDisplayAllCustomers (String username, String password){


            // 1. open a connection to the database
            // use the database URL to point to the correct database
            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet results = null;
            try {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/northwind",
                        username,
                        password);

                // create statement
                // the statement is tied to the open connection
                statement = connection.prepareStatement("""
                        SELECT\s
                        ContactName, CompanyName, City, Country, Phone
                        FROM northwind.customers
                        WHERE Country IS NOT NULL
                        ORDER BY Country;""");


                // 2. Execute your query
                results = statement.executeQuery();

                displayCustomersHeader();
                // process the results
                while (results.next()) {

                    displayCustomersRow(
                            results.getString(1),
                            results.getString(2),
                            results.getString(3),
                            results.getString(4),
                            results.getString(5)
                    );


                }

                // 3. Close the connection

                results.close();
                statement.close();
                connection.close();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {

                if (results != null) {
                    try {
                        results.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }


            }

        }

        private static void displayCustomersHeader () {
            System.out.printf("%40s %40s %16s %20s %16s\n", "contact Name", "company Name", "city", "country", "phone");
        }

        private static void displayCustomersRow (String contactName, String companyName, String city, String
        country, String phone){
            //ContactName, CompanyName, City, Country, Phone
            System.out.printf("%40s %40s %16s %16s %20s\n", contactName, companyName, city, country, phone);
        }


        private static void processDisplayAllProducts (String username, String password){

            // 1. open a connection to the database
            // use the database URL to point to the correct database
            Connection connection;
            try {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/northwind",
                        username,
                        password);

                // create statement
                // the statement is tied to the open connection
                Statement statement = connection.createStatement();

                // define your query
                String query = "SELECT * FROM Products ";

                // 2. Execute your query
                ResultSet results = statement.executeQuery(query);

                displayProductsHeader();
                // process the results
                while (results.next()) {

                    displayProductsRow(
                            results.getInt(1),
                            results.getString(2),
                            results.getDouble(3),
                            results.getInt(4)
                    );


                }

                // 3. Close the connection

                results.close();
                statement.close();
                connection.close();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }


        public static void displayProductsHeader () {
            System.out.printf("%-10s %-40s %-10s %-10s \n", "Id", "Name", "Price", "Stock");
        }

        public static void displayProductsRow ( int productId, String Object productName;
        productName,double productPrice,
        int productUnitsOnHand ){
            System.out.printf("%10d %-40s %10.2f %10d \n", productId, productName, productPrice, productUnitsOnHand);
        }
    }
}