package budgerTrackerProgram;

import java.util.Scanner;

public class BudgetTracker {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        User defaultUser = new User(1, "Test", "User");

        TransactionSystem transactionSystem = new TransactionSystem(defaultUser);

        System.out.println("Welcome to the Budget Tracker Application, "+defaultUser.firstName()+"_"+defaultUser.lastName()+"!");
        //call on transaction storage

        int month;
        int userMenuInput;

        do {
            System.out.println("\nMAIN MENU");
            System.out.println("1. Show transaction history");
            System.out.println("2. Add new transaction");
            System.out.println("3. Change existing transaction");
            System.out.println("4. Change date");
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
                    System.out.print("Choose date\nMonth: ");
                    month = sc.nextInt();
                    System.out.print("\nDay: ");
                    Date date = transactionSystem.createDate(month, sc.nextInt());
                    if (date == null) {
                        System.out.println("Returned to main menu due to invalid input.");
                        break;
                    } else {
                        System.out.print("Amount (positive number for income, negative number for expense): ");
                        double amount = sc.nextDouble();
                        System.out.println("Choose category: ");
                        transactionSystem.printTransactionCategories(amount);
                        int category = sc.nextInt();
                        transactionSystem.newTransaction(date, amount, category);
                    }
                    break;

                case 3:
                    System.out.println("-------------");
                    break;

                case 4:
                    /*System.out.println("-------------");
                    System.out.println("CHANGE DATE");
                    //2024 is hard coded. Program only handles 1 year.
                    System.out.println("Current date: "+ transactionSystem.getCurrentDate());
                    System.out.print("Set new month: ");
                    int month = sc.nextInt();
                    System.out.print("Set new day: ");
                    int day = sc.nextInt();
                    transactionSystem.changeDate(month, day);*/

                    break;

                case 5:
                    transactionSystem.closeSystem();
                    System.out.println("Program terminated");
                    break;
            }

        } while (userMenuInput != 5);




    }
}


