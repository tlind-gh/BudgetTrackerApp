package budgerTrackerProgram;

import java.util.InputMismatchException;
import java.util.Scanner;

public class BudgetTracker {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //hard coded user
        User defaultUser = new User(1, "Test", "User");

        //initiate the transaction system and greet user
        TransactionSystem transactionSystem = new TransactionSystem(defaultUser);
        System.out.println("Welcome to the Budget Tracker Application, "+defaultUser.firstName()+"_"+defaultUser.lastName()+"!");

        //variables that are used in more than one switch/case section of the menu
        String id;
        int month;
        Date date;
        int userMenuInput;

        //menu, loops while user has not chosen to exit the application;
        do {
            System.out.println("-------------");
            System.out.println("MAIN MENU");
            System.out.println("1. Show transaction history");
            System.out.println("2. Add new transaction");
            System.out.println("3. Find transaction by id");
            System.out.println("4. Change or remove transaction");
            System.out.println("5. Exit program");
            userMenuInput = userInputInt();

            switch (userMenuInput) {
                case 1:
                    System.out.println("-------------");
                    System.out.println("TRANSACTION HISTORY");
                    System.out.print("Choose month (1-12) or zero (0) for all: ");
                    month = userInputInt();
                    System.out.print("Income (1), Expense (2) or All (3): ");
                    transactionSystem.printTransactions(month, userInputInt());
                    break;

                case 2:
                    System.out.println("-------------");
                    System.out.println("ADD TRANSACTION");
                    while (true) {
                        System.out.print("Choose date\nMonth: ");
                        month = userInputInt();
                        System.out.print("Day: ");
                        date = transactionSystem.createDate(month, userInputInt());
                        if (date == null) {
                            System.out.println("Returned to main menu due to invalid input.");
                            break;
                        } else {
                            System.out.print("Amount (positive number for income, negative number for expense): ");
                            double amount = userInputDouble();
                            transactionSystem.printTransactionCategories(amount);
                            transactionSystem.newTransaction(date, amount, userInputInt());
                        }
                        System.out.println("Add another transaction (any key) or return to main menu (0)");
                        if (sc.nextLine().equals("0")) {
                            break;
                        }
                    }
                    break;

                case 3:
                    System.out.println("-------------");
                    System.out.println("FIND TRANSACTION BY ID");
                    System.out.print("Specify transaction id: ");
                    transactionSystem.findTransaction(sc.nextLine());
                    break;

                case 4:
                    System.out.println("-------------");
                    System.out.println("CHANGE OR REMOVE TRANSACTION");
                    System.out.println("Note: use print transaction history to find id");
                    System.out.print("Specify transaction id (or press 0 to return to main menu): ");
                    id = sc.nextLine();
                    if (id.equals("0")) {
                        break;
                    }

                    System.out.println("Change amount (1), change category (2) or remove transaction (3)");
                    switch (userInputInt()) {
                        case 1:
                            System.out.println("Choose amount: ");
                            transactionSystem.changeTransaction(id, userInputDouble());
                            break;

                        case 2:
                            transactionSystem.printTransactionCategories(id.startsWith("1") ? 1 : -1);
                            transactionSystem.changeTransaction(id, userInputInt());
                            break;

                        case 3:
                            transactionSystem.deleteTransaction(id);
                            break;

                        default:
                            System.out.println("Returned to main menu due to invalid input.");
                    }
                    break;

                case 5:
                    transactionSystem.closeSystem();
                    System.out.println("Program terminated");
                    break;

                default:
                    System.out.println("Not a valid menu choice");
            }

        } while (userMenuInput != 5);




    }

    //Just some input methods to avoid in case of InputMismatchException from user input
    public static int userInputInt() {
        Scanner sc = new Scanner(System.in);
        int input;
        while(true) {
            try {
                input = sc.nextInt();
                sc.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.print("Input must be a number. Try again: ");
                sc.nextLine();
            }
        }
        return input;
    }

    public static double userInputDouble() {
        Scanner sc = new Scanner(System.in);
        double input;
        while(true) {
            try {
                input = sc.nextInt();
                sc.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.print("Input must be a number. Try again: ");
                sc.nextLine();
            }
        }
        return input;
    }
}


