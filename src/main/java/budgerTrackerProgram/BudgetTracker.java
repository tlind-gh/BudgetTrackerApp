package budgerTrackerProgram;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BudgetTracker {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        List<? extends Transaction> x = new ArrayList<Income>();

        //hard coded user
        User defaultUser = new User(1, "Test", "User");

        //initiate the transaction system and greet user
        TransactionSystem transactionSystem = new TransactionSystem(defaultUser);
        System.out.println("Welcome to the Budget Tracker Application, "+defaultUser.firstName()+"_"+defaultUser.lastName()+"!");

        //variables that are used in more than one switch/case section of the menu
        long id;
        int month;
        Date date;
        int userMenuInput;

        do {
            System.out.println("\nMAIN MENU");
            System.out.println("1. Show transaction history");
            System.out.println("2. Add new transaction");
            System.out.println("3. Change transaction");
            System.out.println("4. Remove transaction");
            System.out.println("5. Exit program");
            userMenuInput = sc.nextInt();

            switch (userMenuInput) {
                case 1:
                    System.out.println("-------------");
                    System.out.println("TRANSACTION HISTORY");
                    System.out.print("Choose month (1-12) or zero (0) for all: ");
                    month = sc.nextInt();
                    System.out.println("Income (1), Expense (2) or All (3)");
                    transactionSystem.printTransactions(month, sc.nextInt());
                    break;

                case 2:
                    System.out.println("-------------");
                    System.out.println("ADD TRANSACTION");
                    while (true) {
                        System.out.print("Choose date\nMonth: ");
                        month = sc.nextInt();
                        System.out.print("Day: ");
                        date = transactionSystem.createDate(month, sc.nextInt());
                        if (date == null) {
                            System.out.println("Returned to main menu due to invalid input.");
                            break;
                        } else {
                            System.out.print("Amount (positive number for income, negative number for expense): ");
                            double amount = sc.nextDouble();
                            System.out.println("Choose category: ");
                            transactionSystem.printTransactionCategories(amount);
                            transactionSystem.newTransaction(date, amount, sc.nextInt());
                        }
                        System.out.println("Add another transaction (any key) or return to main menu (0)");
                        sc.nextLine();
                        if (sc.nextLine().equals("0")) {
                            break;
                        }
                    }
                    break;

                case 3:
                    System.out.println("-------------");
                    System.out.println("CHANGE TRANSACTION");
                    System.out.println("Use print transaction history in main menu to find id");
                    System.out.print("Specify transaction id (or press 0 to return to main menu): ");
                    id = sc.nextInt();
                    if (id == 0) {
                        break;
                    }
                    System.out.println("Set new amount (1) or category (2)");
                    switch (sc.nextInt()) {
                        case 1:
                            System.out.println("Choose amount: ");
                            transactionSystem.changeTransaction(id, sc.nextDouble());
                            break;

                        case 2:
                            System.out.println("Choose category: ");
                            int input = id % 2 == 0 ? 1 : -1;
                            transactionSystem.printTransactionCategories(input);
                            transactionSystem.changeTransaction(id, sc.nextInt());
                            break;

                        default:
                            System.out.println("Returned to main menu due to invalid input.");
                    }
                    break;


                case 4:
                    System.out.println("-------------");
                    System.out.println("REMOVE TRANSACTION");
                    System.out.println("Use print transaction history in main menu to find id");
                    System.out.print("Specify transaction id (or press 0 to return to main menu): ");
                    id = sc.nextInt();
                    if (id == 0) {
                        break;
                    }
                    transactionSystem.deleteTransaction(id);
                    break;

                case 5:
                    transactionSystem.closeSystem();
                    System.out.println("Program terminated");
                    break;
            }

        } while (userMenuInput != 5);




    }
}


