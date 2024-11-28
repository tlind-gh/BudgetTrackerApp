package budgerTrackerProgram;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/*Class that has an instance in the main class (BudgetTracker), and which has methods that calls upon methods from other classes.
Class holds income and expense storage (instances of TransactionStorage) which hold the transaction data. Also responsible
for giving new transactions a unique ID*/
public class TransactionSystem {
    private final String filepath_user;
    private final TransactionStorage incomeStorage;
    private final TransactionStorage expenseStorage;
    private int nextTransactionID;

    /*constructor checks if there is a user folder (which there should be if the user has used that app before),
    If there is a folder, it loads the last transactionID from a txt file in this folder.
    Otherwise creates the folder and two subfolders (Income and expense) and sets transactionId to 1 (starting ID) */
    public TransactionSystem(User user) {
        filepath_user = "src/main/BudgetTrackerFileStorage/" + user.userID() + "_" + user.lastName();
        File f1 = new File(filepath_user);
        if (f1.mkdir()) {
            new File(filepath_user + "/Income").mkdir();
            new File(filepath_user + "/Expense").mkdir();
            nextTransactionID = 1;
        } else {
            try {
                File f2 = new File(filepath_user + "/nextTransactionID.txt");
                Scanner fr = new Scanner(f2);
                nextTransactionID = fr.nextInt();
                fr.close();
            } catch (FileNotFoundException e) {
                nextTransactionID = 1;
            }
        }
        incomeStorage = new TransactionStorage(filepath_user, true);
        expenseStorage = new TransactionStorage(filepath_user, false);
    }
    /*GENERAL METHODS INFO: all methods are called from main class many call upon methods for the two TransactionStorage instances
    Checks that arguments (often from user input passed via Main class) are valid for the method
    and if arguments are not valid it prints a statement to the user informing that arguments are not valid*/

    //create an instance of the Date class. Includes print statement if arguments are not a valid date.
    public Date createDate(int month, int day) {
        Date date = null;
        try {
            date = new Date(month, day);
        } catch (IllegalArgumentException e) {
            System.out.println("Not a valid date");
        }
        return date;
    }

    /*creates new transaction (income or expense depending on if amount is positive or negative). Called in conjuction
    w. createDate and printTransactionCategories from the Main class. Sets a unique transaction ID for a new transaction,
    which holds information of if the transaction is: 1) expense or income (first digit, 1 for income or 2 for expense),
    2) transaction month (digit 2 and 3). The nextTransactionID (incremented by 1 after each new transaction) is added
    to end the id to ensure it is unique.*/
    public void newTransaction(Date date, double amount, int categoryNr) {
        if (amount != 0) {
            String id = amount > 0 ? String.format("1%02d%d",date.getMonth(),nextTransactionID) : String.format("2%02d%d",date.getMonth(),nextTransactionID);
            if (amount > 0) {
                Income income = new Income(id, date, amount, categoryNr);
                incomeStorage.addTransaction(income);
            } else {
                Expense expense = new Expense(id, date, amount, categoryNr);
                expenseStorage.addTransaction(expense);
            }
            nextTransactionID += 1;
        } else {
            System.out.println("Transaction amount cannot be zero.");
        }
    }

    //prints a list of either EIncomeCategories or EExpenseCategories enums depending on if transaction amount is neg. or pos.)
    public void printTransactionCategories(double transactionAmount) {
        if (transactionAmount != 0) {
            System.out.println("Choose category: ");
            if (transactionAmount > 0) {
                for (EIncomeCategory enumCategory : EIncomeCategory.values()) {
                    System.out.println((enumCategory.ordinal() + 1) + ". " + enumCategory.name());
                }
            } else {
                for (EExpenseCategory enumCategory : EExpenseCategory.values()) {
                    System.out.println((enumCategory.ordinal() + 1) + ". " + enumCategory.name());
                }
            }
        } else {
            System.out.println("Transaction amount cannot be zero.");
        }
    }

    /*METHOD INFO FOR changeTransaction AND deleteTransaction():
    uses id.startsWith to determine the child class of the transaction based on input ID
    (and calls on income or expenseStorage) accordingly*/

    /*changeTransaction method has two variants (method overloading). If the 2nd input argument for changeTransaction is
    a double -> change amount, and if it is an int -> change category*/
    public void findTransaction(String id) {
        Transaction transaction = null;
        if (id.startsWith("1")) {
            transaction = incomeStorage.findTransaction(id);
        } else if (id.startsWith("2")) {
            transaction = expenseStorage.findTransaction(id);
        }
        if (transaction != null) {
            System.out.println("\nTransaction found:\n"+Transaction.getTransactionHeader()+"\n"+transaction);
        } else {
            System.out.println("\nNo transaction with id "+id+" exists");
        }
    }

    public void changeTransaction(String id, double amount) {
        Transaction transaction = null;
        if (id.startsWith("1")) {
            transaction = incomeStorage.findTransaction(id);
        } else if (id.startsWith("2")) {
            transaction = expenseStorage.findTransaction(id);
        }
        if (transaction != null) {
            try {
                transaction.setAmount(amount);
                System.out.println("\nTransaction amount has been changed\n"+Transaction.getTransactionHeader()+"\n"+transaction);
            } catch (IllegalArgumentException e) {
                System.out.println("\nTransaction amount not valid. Must be positive number for income and negative for expense.");
            }
        } else {
            System.out.println("\nNo transaction with id "+id+" exists");
        }
    }

    public void changeTransaction(String id, int categoryIndex) {
        Transaction transaction = null;
        if (id.startsWith("1")) {
            transaction = incomeStorage.findTransaction(id);
        } else if (id.startsWith("2")) {
            transaction = expenseStorage.findTransaction(id);
        }
        if (transaction != null) {
            transaction.setCategory(categoryIndex);
            System.out.println("\nTransaction category has been changed\n"+Transaction.getTransactionHeader()+"\n"+transaction);
        } else {
            System.out.println("\nNo transaction with id "+id+" exists");
        }
    }

    public void deleteTransaction(String id) {
        if (id.startsWith("1")) {
            incomeStorage.removeTransaction(id);
        } else if (id.startsWith("2")) {
            expenseStorage.removeTransaction(id);
        } else {
            System.out.println("\nNo transaction with id " + id + " exists");
        }
    }

    /*method for printing a month (1st argument is 1-12) or year (1st argument is 0) depending on input.
    Can print income, expense or all depending on second input argument*/
    public void printTransactions(int month, int choice) {
        if (month >= 0 && month <= 12 && choice >= 1 && choice <= 3) {
            int[] listOfMonths = (month == 0) ? new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12} : new int[]{month};
            String timePeriod = (month == 0) ? "2024" : Date.getMonthAsString(month);
            double incomeSum = 0;
            double expenseSum = 0;
            if (choice == 1 || choice == 3) {
                System.out.println("-------------");
                System.out.println("INCOME - "+timePeriod.toUpperCase());
                incomeSum = incomeStorage.printTransactionsReturnSum(listOfMonths);
                if (incomeSum != 0) {
                    System.out.println("Total income "+timePeriod+": "+ incomeSum);
                } else {
                    System.out.println("No income posts for "+ timePeriod);
                }
            }
            if (choice == 2 || choice == 3) {
                System.out.println("-------------");
                System.out.println("EXPENSE - "+timePeriod.toUpperCase());
                expenseSum = expenseStorage.printTransactionsReturnSum(listOfMonths);
                if (expenseSum != 0) {
                    System.out.println("Total expense "+timePeriod+": "+ expenseSum);
                } else {
                    System.out.println("No expense posts for "+ timePeriod);
                }
            }
            if (choice == 3) {
                System.out.println("-------------");
                System.out.println("TOTAL BALANCE " + timePeriod.toUpperCase() + ": " + (incomeSum + expenseSum));
            }
        } else {
            System.out.println("Input choices not valid");
        }
    }

    //writes the current transactionID to file and calls on the write to file method for the respective TransactionStorages
    public void closeSystem() {
        incomeStorage.writeToFile();
        expenseStorage.writeToFile();
        try {
            Files.write(Paths.get(filepath_user + "/nextTransactionID.txt"), String.valueOf(nextTransactionID).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
