package budgerTrackerProgram;

import java.util.InputMismatchException;
import java.util.Scanner;

/*Main class of the program. Handles mainly printing of the menu to console and receiving user input and passing it to
an instance of the TransactionSystem class. Has some simple static methods to keep the program from crashing if user input
does not match the variable type it will be assigned to*/

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
            System.out.println("\n-------------");
            System.out.println("MAIN MENU");
            System.out.println("1. Show transaction history");
            System.out.println("2. Add new transaction");
            System.out.println("3. Find transaction by id");
            System.out.println("4. Change or remove transaction");
            System.out.println("5. Exit program");
            userMenuInput = userInputInt();

            switch (userMenuInput) {
                //prints transactions and balance of printed transactions.
                case 1:
                    System.out.println("-------------");
                    System.out.println("TRANSACTION HISTORY");
                    System.out.print("Choose month (1-12) or zero (0) for all: ");
                    month = userInputInt();
                    System.out.print("Income (1), Expense (2) or All (3): ");
                    transactionSystem.printTransactions(month, userInputInt());
                    break;

                //one or multiple income or expenses
                case 2:
                    System.out.println("-------------");
                    System.out.println("ADD TRANSACTION");
                    while (true) {
                        //set date (create an instance of the Date class)
                        System.out.print("Choose date\nMonth: ");
                        month = userInputInt();
                        System.out.print("Day: ");
                        date = transactionSystem.createDate(month, userInputInt());
                        //if the date input is not valid, the user is booted back to main menu
                        if (date == null) {
                            System.out.println("Returned to main menu due to invalid input.");
                            break;
                        } else {
                            System.out.print("Amount (positive number for income, negative number for expense): ");
                            double amount = userInputDouble();
                            /*prints Transaction category choices, takes amount as input to determine printing of income
                            or expense categories*/
                            transactionSystem.printTransactionCategories(amount);
                            transactionSystem.newTransaction(date, amount, userInputInt());
                        }
                        //choice to add another transaction or return to main menu
                        System.out.println("Add another transaction (any key) or return to main menu (0)");
                        if (sc.nextLine().equals("0")) {
                            break;
                        }
                    }
                    break;

                //find a specific transaction by entering the transaction id
                case 3:
                    System.out.println("-------------");
                    System.out.println("FIND TRANSACTION BY ID");
                    System.out.print("Specify transaction id: ");
                    transactionSystem.findTransaction(sc.nextLine());
                    break;

                //change amount or category of an existing transaction, or remove a transaction
                case 4:
                    System.out.println("-------------");
                    System.out.println("CHANGE OR REMOVE TRANSACTION");
                    System.out.println("Note: use print transaction history to find id");
                    System.out.print("Specify transaction id (or press 0 to return to main menu): ");

                    //give the user the choice to return to main menu if they do not know the id
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
                            /*prints income or expense categories by checking if the transaction id is for an expense
                            or income and giving the printTransactionCategories() method negative or positive input*/
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

                //close program (call methods that trigger printing to file of data)
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


