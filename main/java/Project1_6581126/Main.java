package Project1_6581126;
import java.util.*;

public class Main {
    public static void main(String[] args) {

    }
}
// Task 1: Implement Product Class
// - Read data from products.txt
// - Store product code, name, and unit price
// - Keep track of total sales in units & cash
// - Implement a method to update sales when an order is placed

// Task 2: Implement Customer Class
// - Read customer names from orders.txt
// - Store customer name and points
// - Implement a method to calculate points (1 point per 500 THB)
// - Implement a method to redeem points (100 points = 5% discount)

// Task 3: Implement Order Class
// - Read data from orders.txt
// - Store order ID, customer name, product code, units, installment plan
// - Implement methods to:
// - Calculate sub-total (1) = product price × units
// - Calculate points earned
// - Store the order for lucky draw

// Task 4: Implement InstallmentPlan Class
// - Read data from installments.txt
// - Store number of months and interest rate
// - Implement methods to:
// - Calculate total interest (subtotal(2) * interest * months)
// - Calculate total and monthly payments

// Task 5: Implement Main Class (OrderProcessing)
// - Read data from all input files
// - Process each order:
// - Calculate sub-total (1) from product price and units
// - Calculate points earned
// - Apply discount (first order = 200 THB, returning customer = 5% discount for 100 points)
// - Compute total and monthly payments based on installment plan
// - Store order ID and customer name for lucky draw
// - Generate reports:
// - Product summary (sorted by unit sales and name)
// - Lucky draw winner for each product
// - Customer summary (sorted by points and name)

// Task 5.1: Read Input Files
// - Read products.txt → Store in a Product list
// - Read orders.txt → Store in an Order list
// - Read installments.txt → Store in an InstallmentPlan list

// Task 5.2: Process Orders
// - Calculate sub-total (1) (product price × units)
// - Calculate points earned (1 point per 500 THB)
// - Apply discount:
// - First order → Discount 200 THB
// - Returning customer → Use 100 points for 5% discount
// - If full payment → totalPayment = subTotal(2)
// - If installment plan:
// - Compute total interest
// - Compute total & monthly payments
// - Store order ID and customer name for lucky draw

// Task 5.3: Generate Product Summary
// - Sort products by:
// - Units sold (descending)
// - Alphabetical order (if tied)
// - Select a lucky winner for each product

// Task 5.4: Generate Customer Summary
// - Sort customers by:
// - Points (descending)
// - Alphabetical order (if tied)
// - Show remaining points for each customer

// Task 6: Implement Error Handling
// - Handle missing files: if a file is missing, display an error and continue execution
// - Handle input errors in orders.txt:
// - Missing values
// - Format issues (e.g., "O" instead of "0")
// - Invalid values (e.g., units <= 0, wrong product code, invalid installment plan)
// - Skip invalid lines but continue processing the rest
// - Ensure program runs correctly even when errors occur