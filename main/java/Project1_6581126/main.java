package Project1_6581126;
import java.io.*;
import java.util.*;

class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}
class Product {
    private String code;
    private String name;
    private double unitPrice;
    private int totalUnitsSold = 0;
    private double totalSales = 0.0;

    public Product(String code, String name, double unitPrice) {
        this.code = code;
        this.name = name;
        this.unitPrice = unitPrice;
    }

    public void addSale(int quantity) {
        totalUnitsSold += quantity;
        totalSales += quantity * unitPrice;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getTotalUnitsSold() {
        return totalUnitsSold;
    }

    public double getTotalSales() {
        return totalSales;
    }
}

class Customer {
    private String name;
    private int points;

    public Customer(String name) {
        this.name = name;
        this.points = 0;
    }

    public void addPoints(double amount) {
        points += (int) (amount / 500);
    }

    public boolean redeemPoints() {
        if (points >= 100) {
            points -= 100;
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }
}


class Order {
    private int orderID;
    private String customerName;
    private String productCode;
    private int units;
    private int installmentMonths;

    public Order(int orderID, String customerName, String productCode, int units, int installmentMonths) {
        this.orderID = orderID;
        this.customerName = customerName;
        this.productCode = productCode;
        this.units = units;
        this.installmentMonths = installmentMonths;
    }

    public int getOrderID() {
        return orderID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getProductCode() {
        return productCode;
    }

    public int getUnits() {
        return units;
    }

    public int getInstallmentMonths() {
        return installmentMonths;
    }
}

class InstallmentPlan {
    private int months;
    private double interestRate;

    public InstallmentPlan(int months, double interestRate) {
        this.months = months;
        this.interestRate = interestRate;
    }

    public double calculateTotalPayment(double amount) {
        return amount + (amount * interestRate * months);
    }

    public double calculateMonthlyPayment(double amount) {
        return calculateTotalPayment(amount) / months;
    }

    public int getMonths() {
        return months;
    }

    public double getInterestRate() {
        return interestRate;
    }
}

class ReadInputFile {
    public List<Product> loadProducts(String filename) throws FileNotFoundException {
        List<Product> productList = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filename)); // Throws FileNotFoundException if missing

        if (scanner.hasNextLine()) scanner.nextLine(); // Skip header
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split(",");
            if (parts.length == 3) {
                productList.add(new Product(parts[0].trim(), parts[1].trim(), Double.parseDouble(parts[2].trim())));
            }
        }
        scanner.close();
        return productList;
    }

    public List<Order> loadOrders(String filename) throws FileNotFoundException {
        List<Order> orderList = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filename)); // Throws FileNotFoundException if missing

        if (scanner.hasNextLine()) scanner.nextLine(); // Skip header
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split(",");
            if (parts.length == 5) {
                try {
                    int orderID = Integer.parseInt(parts[0].trim());
                    String customerName = parts[1].trim();
                    String productCode = parts[2].trim();
                    int units = Integer.parseInt(parts[3].trim());
                    int installmentMonths = Integer.parseInt(parts[4].trim());

                    if (units > 0 && installmentMonths >= 0) {
                        orderList.add(new Order(orderID, customerName, productCode, units, installmentMonths));
                    } else {
                        System.err.println("Skipping invalid order: " + Arrays.toString(parts));
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Skipping invalid order format: " + Arrays.toString(parts));
                }
            }
        }
        scanner.close();
        return orderList;
    }
    public List<InstallmentPlan> loadInstallments(String filename) throws FileNotFoundException {
        List<InstallmentPlan> installmentPlanList = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filename)); // Throws FileNotFoundException if missing

        if (scanner.hasNextLine()) scanner.nextLine(); // Skip header
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split(",");
            if (parts.length == 2) {
                try {
                    int months = Integer.parseInt(parts[0].trim());
                    double interestRate = Double.parseDouble(parts[1].trim());
                    if (months >= 0 && interestRate >= 0) {
                        installmentPlanList.add(new InstallmentPlan(months, interestRate));
                    } else {
                        System.err.println("Skipping invalid installment: " + Arrays.toString(parts));
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Skipping invalid installment format: " + Arrays.toString(parts));
                }
            }
        }
        scanner.close();
        return installmentPlanList;
    }
}




public class Main {
    public static void main(String[] args) {
        String path = "src/main/java/Project1_6581126/";
        ReadInputFile reader = new ReadInputFile();
        Scanner userInput = new Scanner(System.in);

        List<Product> productList = null;
        List<Order> orderList = null;
        List<InstallmentPlan> installmentPlanList = null;
        Map<String, Customer> customers = new HashMap<>();
        Map<String, List<Order>> productOrders = new HashMap<>();
        String productFileName = path + "products.txt";
        String installmentFileName = path + "installments.txt";
        String orderFileName = path + "orders.txt";

        boolean productsLoaded = false;
        boolean ordersLoaded = false;
        boolean installmentsLoaded = false;

        // Load products
        while (!productsLoaded) {
            try {
                productList = reader.loadProducts(productFileName);
                productsLoaded = true;
            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + productFileName);
                System.out.print("Enter new product file name: ");
                productFileName = userInput.nextLine().trim();
                if (!productFileName.contains("/")) productFileName = path + productFileName;
            }
        }
        System.out.println("\nRead from " + productFileName);
        for (Product product : productList) {
            System.out.printf("%-20s (%-2s)   unit price = %,6.0f\n", product.getName(), product.getCode(), product.getUnitPrice());
        }

        // Load installment plans
        while (!installmentsLoaded) {
            try {
                installmentPlanList = reader.loadInstallments(installmentFileName);
                installmentsLoaded = true;
            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + installmentFileName);
                System.out.print("Enter new installment file name: ");
                installmentFileName = userInput.nextLine().trim();
                if (!installmentFileName.contains("/")) installmentFileName = path + installmentFileName;
            }
        }
        System.out.println("\nRead from " + installmentFileName);
        for (InstallmentPlan plan : installmentPlanList) {
            System.out.printf("%2d-month plan     monthly interest = %.2f%%\n", plan.getMonths(), plan.getInterestRate());
        }

        // Load order
        while (!ordersLoaded) {
            try {
                orderList = reader.loadOrders(orderFileName);
                ordersLoaded = true;
            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + orderFileName);
                System.out.print("Enter new order file name: ");
                orderFileName = userInput.nextLine().trim();
                if (!orderFileName.contains("/")) orderFileName = path + orderFileName;
            }
        }
        System.out.println("\nRead from " + orderFileName);
        for (Order order : orderList) {
            System.out.printf("Order %2d >> %5s  %-15s x %2d   %2d-month installments\n",
                    order.getOrderID(), order.getCustomerName(), getProductName(order.getProductCode()), order.getUnits(), order.getInstallmentMonths());
        }

        System.out.println("\n=== Order processing ===");
        for (Order order : orderList) {
            try {
                Product productOrder = null;
                for (Product product : productList) {
                    if (product.getCode().equals(order.getProductCode())) {
                        productOrder = product;
                        break;
                    }
                }
                if (productOrder == null) throw new InvalidInputException("Invalid product code: " + order.getProductCode());

                if (order.getUnits() < 0) throw new InvalidInputException("Invalid units: " + order.getUnits());

                productOrder.addSale(order.getUnits());
                customers.putIfAbsent(order.getCustomerName(), new Customer(order.getCustomerName()));
                Customer customer = customers.get(order.getCustomerName());

                double subTotal1 = productOrder.getUnitPrice() * order.getUnits();
                double discount = 0.0;
                int previousPoints = customer.getPoints();
                boolean usedPoints = false;
                if (previousPoints >= 100) {
                    discount = Math.floor(subTotal1 * 0.05);
                    customer.redeemPoints();
                    usedPoints = true;
                } else {
                    discount = 200.0;
                }

                double subTotal2 = subTotal1 - discount;
                int pointsEarned = (int)(subTotal1 / 500);
                customer.addPoints(pointsEarned * 500);

                double totalInterest = 0;
                double monthlyPayment = 0;
                if (order.getInstallmentMonths() > 0) {
                    InstallmentPlan installmentPlan = installmentPlanList.stream()
                            .filter(p -> p.getMonths() == order.getInstallmentMonths())
                            .findFirst().orElse(null);

                    if (installmentPlan != null) {
                        totalInterest = (subTotal2 * (installmentPlan.getInterestRate() / 100)) * installmentPlan.getMonths();
                        monthlyPayment = (subTotal2 + totalInterest) / installmentPlan.getMonths();
                    }
                }

                productOrders.putIfAbsent(productOrder.getCode(), new ArrayList<>());
                productOrders.get(productOrder.getCode()).add(order);

                // Print formatted order summary
                System.out.printf("%2d. %-7s  (%4d pts)  order   = %-15s x %2d   sub-total(1)   = %,12.2f THB  (+ %d pts next order)\n",
                        order.getOrderID(), customer.getName(), (previousPoints),
                        productOrder.getName(), order.getUnits(), subTotal1, pointsEarned);
                System.out.printf("%25sdiscount = %6.2f                sub-total(2)   = %,12.2f THB", "", discount, subTotal2);

                if (discount > 200.0) {
                    System.out.printf("      (- %d pts)", 100);
                }
                System.out.println();

                if (order.getInstallmentMonths() == 0) {
                    System.out.printf("%37s\n", "full payment");
                }

                if (order.getInstallmentMonths() > 0) {
                    System.out.printf("%24s%2d-month installments              total interest = %,12.2f \n", "", order.getInstallmentMonths(), totalInterest);
                    System.out.printf("%25stotal    = %,12.2f            monthly total  = %,12.2f \n", "", subTotal2 + totalInterest, monthlyPayment);
                } else {
                    System.out.printf("%25stotal    = %,12.2f \n", "", subTotal2);
                }
                System.out.println();

            } catch (InvalidInputException e) {
                System.err.println(e.getMessage());
                continue;
            }
        }

        generateProductSummary(productList, productOrders);
        generateCustomerSummary(customers);
    }

    private static void generateProductSummary(List<Product> products, Map<String, List<Order>> productOrders) {
        System.out.println("\n=== Product summary ===");
        products.sort(Comparator.comparing(Product::getTotalUnitsSold).reversed().thenComparing(Product::getName));
        Random random = new Random();
        for (Product product : products) {
            System.out.printf("%-14s  total sales = %d units = %,12.2f    ",
                    product.getName(),
                    product.getTotalUnitsSold(), product.getTotalSales());
            List<Order> eligibleOrders = productOrders.getOrDefault(product.getCode(), new ArrayList<>());
            if (!eligibleOrders.isEmpty()) {
                Order luckyWinner = eligibleOrders.get(new Random().nextInt(eligibleOrders.size()));
                System.out.printf("lucky draw winner = %s (order %2d)\n", luckyWinner.getCustomerName(), luckyWinner.getOrderID());
            }
        }
    }

    private static void generateCustomerSummary(Map<String, Customer> customers) {
        System.out.println("\n=== Customer summary ===");
        customers.values().stream()
                .sorted(Comparator.comparing(Customer::getPoints).reversed().thenComparing(Customer::getName))
                .forEach(c -> System.out.printf("%s   remaining points = %,5d\n", c.getName(), c.getPoints()));
    }

    private static String getProductName(String productCode) {
        switch (productCode) {
            case "AP": return "Air Purifiers";
            case "DL": return "Digital Locks";
            case "HD": return "Hand Dryers";
            default: return "Unknown";
        }
    }
}

