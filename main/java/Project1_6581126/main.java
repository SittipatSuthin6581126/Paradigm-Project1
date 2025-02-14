package Project1_6581126;
import java.util.*;
import java.io.*;

// Class representing a product with a code, name, price, and sales data
class Product {
    private String code; // Unique product code
    private String name; // Name of the product
    private double unitPrice; // Price per unit
    private int totalUnitsSold = 0; // Tracks total units sold
    private double totalSales = 0.0; // Tracks total sales revenue

    // Constructor to initialize a product
    public Product(String code, String name, double unitPrice) {
        this.code = code;
        this.name = name;
        this.unitPrice = unitPrice;
    }

    // Method to record a sale of a specified quantity of the product
    public void addSale(int quantity) {
        totalUnitsSold += quantity;
        totalSales += quantity * unitPrice;
    }

    // Getter methods to access private attributes
    public String getCode() { return code; }
    public String getName() { return name; }
    public double getUnitPrice() { return unitPrice; }
    public int getTotalUnitsSold() { return totalUnitsSold; }
    public double getTotalSales() { return totalSales; }

    // Method to load product data from a file and store it in a Map
    public static void loadProducts(String filename, Map<String, Product> products) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // Split line by comma
                if (parts.length == 3) { // Ensure correct data format
                    products.put(parts[0], new Product(parts[0], parts[1], Double.parseDouble(parts[2])));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading products: " + e.getMessage());
        }
    }
}

// Class representing a customer who can accumulate and redeem points
class Customer {
    private String name; // Customer's name
    private int points; // Reward points earned

    // Constructor to initialize customer with a name and zero points
    public Customer(String name) {
        this.name = name;
        this.points = 0;
    }

    // Method to add points based on the amount spent (1 point per 500 currency units)
    public void addPoints(double amount) {
        points += (int) (amount / 500);
    }

    // Method to redeem 100 points if available, returning true if successful
    public boolean redeemPoints() {
        if (points >= 100) {
            points -= 100;
            return true; // Redemption successful
        }
        return false; // Not enough points
    }

    public static void loadCustomers(String filename, Map<String, Customer> customers) {  //scanner scan from order.txt
        try (Scanner scanner = new Scanner(new File(filename))) {
            if (scanner.hasNextLine()) scanner.nextLine(); // Skip header line

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(","); // Split line by comma

                if (parts.length >= 2) { //at least order ID and customer name exist
                    String customerName = parts[1].trim(); // Extract customer name
                    customers.putIfAbsent(customerName, new Customer(customerName));   // If customer is not already in the map, add them
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File " + filename + " not found.");
        }
    }
    // Getter methods to access private attributes
    public String getName() { return name; }
    public int getPoints() { return points; }
}

class Order {
    private int orderID;
    private String customerName;
    private String productCode;
    private int units;
    private int installmentMonths;

    public Order(int orderID, String customerName, String productCode, int units, int installmentMonths){
        this.orderID = orderID;
        this.customerName = customerName;
        this.productCode = productCode;
        this.units = units;
        this.installmentMonths = installmentMonths;
    }
    // Getter methods
    public int getOrderID() { return orderID; }
    public String getCustomerName() { return customerName; }
    public String getProductCode() { return productCode; }
    public int getUnits() { return units; }
    public int getInstallmentMonths() { return installmentMonths; }
    public String getProductName(){
        if (productCode.equals("AP")){
            return "Air Purifiers";

        } else if (productCode.equals("DL")) {
            return "Digital Locks";
        }else {return "Hand Dryers";

        }
    }


    public static void loadOrders(String filename, List<Order> orders) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            if (scanner.hasNextLine()) scanner.nextLine(); // Skip header line

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(","); // Split line by comma

                if (parts.length >= 5) { // Ensure correct format
                    int orderID = Integer.parseInt(parts[0].trim());
                    String customerName = parts[1].trim();
                    String productCode = parts[2].trim();
                    int units = Integer.parseInt(parts[3].trim());
                    int installmentMonths = Integer.parseInt(parts[4].trim());

                    orders.add(new Order(orderID, customerName, productCode, units, installmentMonths));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File " + filename + " not found.");
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format in " + filename);
        }
    }

}

// Class representing an installment payment plan
class InstallmentPlan {
    private int months; // Duration of the installment in months
    private double interestRate; // Monthly interest rate

    // Constructor to initialize an installment plan
    public InstallmentPlan(int months, double interestRate) {
        this.months = months;
        this.interestRate = interestRate;
    }

    // Method to calculate the total interest payable on an amount
    public double calculateTotalInterest(double amount) {
        return amount * interestRate * months;
    }

    // Method to calculate the total amount payable including interest
    public double calculateTotalPayment(double amount) {
        return amount + calculateTotalInterest(amount);
    }

    // Method to calculate the monthly payment for the given amount
    public double calculateMonthlyPayment(double amount) {
        return calculateTotalPayment(amount) / months;
    }
    public int getMonths() { return months; }
    public double getInterestRate() { return interestRate; }

    // Method to load installment plans from a file and store them in a list
    public static void loadInstallments(String filename, List<InstallmentPlan> installmentPlans) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // Split line by comma
                if (parts.length == 2) { // Ensure correct data format
                    int months = Integer.parseInt(parts[0]);
                    double interestRate = Double.parseDouble(parts[1]);
                    installmentPlans.add(new InstallmentPlan(months, interestRate));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading installments: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid format in installments file.");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        String path = "src/main/java/Project1_6581126/";
        String orderFile = path + "orders.txt";
        String productsFile = path + "products.txt";
        String installmentsFile = path + "Installments.txt";

        File inOrder = new File(orderFile);
        File products = new File(productsFile);
        File installment = new File(installmentsFile);

        List<Product> productList = new ArrayList<>();
        List<Order> orderList = new ArrayList<>();
        List<InstallmentPlan> installmentPlanList = new ArrayList<>();

        productList = ReadInputFile.loadProducts(productsFile);
        orderList = ReadInputFile.loadOrders(orderFile);
        installmentPlanList = ReadInputFile.loadInstallments(installmentsFile);

        System.out.println("\nProducts loaded:");
        System.out.println("Read from "+products.getAbsolutePath());
        for (Product product : productList) {
            System.out.printf("%-15s %-5s unit price = %.2f\n", product.getName(), product.getCode(), product.getUnitPrice());
        }

        System.out.println("\nInstallments loaded:");
        System.out.println("Read from "+installment.getAbsolutePath());
        for (InstallmentPlan installmentPlan : installmentPlanList) {
            System.out.printf("%2d-month plan     monthly interest = %.2f%%\n", installmentPlan.getMonths(), installmentPlan.getInterestRate());
        }

        System.out.println("\nOrders loaded:");
        System.out.println("Read from "+inOrder.getAbsolutePath());
        for (Order order : orderList) {
            System.out.printf("Order %2d >> %6s  %-15s x %2d   %2d-month installments\n",order.getOrderID(),order.getCustomerName(),order.getProductName(),order.getUnits(),order.getInstallmentMonths());
        }

    }
}
class ReadInputFile {

    public static List<Product> loadProducts(String filename) {
        List<Product> productList = new ArrayList<>();
        try (FileReader fr = new FileReader(filename); Scanner scanner = new Scanner(fr)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    try {
                        String code = parts[0].trim();
                        String name = parts[1].trim();
                        double unitPrice = Double.parseDouble(parts[2].trim()); // Parse unit price
                        productList.add(new Product(code, name, unitPrice));
                    } catch (NumberFormatException e) {
                        System.out.println();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return productList;
    }

    public static List<Order> loadOrders(String filename) {
        List<Order> orderList = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filename))) {
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        String productCode = parts[2].trim();
                        int unit = Integer.parseInt(parts[3].trim());
                        int installment = Integer.parseInt(parts[4].trim());

                        orderList.add(new Order(id, name, productCode, unit, installment));
                    } catch (NumberFormatException e) {
                        System.out.println();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error loading orders: " + e.getMessage());
        }
        return orderList;
    }


    public static List<InstallmentPlan> loadInstallments(String filename) {
        List<InstallmentPlan> installmentPlanList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;


            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    try {
                        int months = Integer.parseInt(parts[0].trim());
                        double interestRate = Double.parseDouble(parts[1].trim());
                        installmentPlanList.add(new InstallmentPlan(months, interestRate));
                    } catch (NumberFormatException e) {
                        System.out.println();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading installments: " + e.getMessage());
        }
        return installmentPlanList;
    }
}

