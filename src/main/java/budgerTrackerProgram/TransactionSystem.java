package budgerTrackerProgram;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/*The only class that is called from the main class (BudgetTracker), and which has methods that calls upon other classes.
Class holds income and expense storage (instances of TransactionStorage) which hold the transaction data*/
public class TransactionSystem {
    private final String filepath_user;
    private final TransactionStorage incomeStorage;
    private final TransactionStorage expenseStorage;
    private int nextTransactionID;
    private final String header;

    /*constructor checks if there is a user folder (which there should be if the user has used that app before),
    If there is a folder, it loads the last transactionID from a txt file in this folder.
    Otherwise creates the folder and two subfolders (Income and expense) and sets transactionId to 10 (starting ID) */
    public TransactionSystem(User user) {
        filepath_user = "src/main/BudgetTrackerFileStorage/" + user.userID() + "_" + user.lastName();
        header = String.format("\n%-20s %-20s %-20s %-20s\n", "Date","Amount","Category","Transaction ID");
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
    /*METHODS: all public methods are called from main class and also checks that arguments are valid for the method.
    If arguments are not valid it prints a statement to the user informing that arguments are not valid*/

    //create an instance of the Date class and if arguments are not a valid date.
    public Date createDate(int month, int day) {
        Date date = null;
        try {
            date = new Date(month, day);
        } catch (IllegalArgumentException e) {
            System.out.println("Not a valid date");
        }
        return date;
    }

    /*creates new transaction (income or expense depending on if amount is positive). Called in conjuction w. createDate
    and printTransactionCategories from the Main class. Sets a unique transaction ID, which holds information of if the
    transaction is: 1) expense or income (first digit, 1 for income or 2 for expense), 2) transaction month (digit 2 and 3).
    The nextTransactionID (incremented by 1 after each new transaction) is added to end the id to ensure it is unique.*/
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

    //The following 4 methods uses id.startsWith to determine the child class of the transaction and calls on income or expenseStorage
    public boolean isTransactionIdValid(String id) {
        boolean isTransactionIdValid = false;
        if (id.startsWith("1")) {
            isTransactionIdValid = incomeStorage.findTransaction(id) != null;
        } else if (id.startsWith("2")) {
            isTransactionIdValid = expenseStorage.findTransaction(id) != null;
        }
        return isTransactionIdValid;
    }

    public void deleteTransaction(String id) {
        if (id.startsWith("1")) {
            incomeStorage.removeTransaction(id);
        } else if (id.startsWith("2")) {
            expenseStorage.removeTransaction(id);
        } else {
            System.out.println("No transaction with id " + id + " exists");
        }
    }

    /*Two methods for changing transaction w. the same name. Java calls "correct" methods based on input arguments types
    (i.e., if second argument is double or int)*/
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
                System.out.println("Transaction amount has been changed"+header+transaction);
            } catch (IllegalArgumentException e) {
                System.out.println("Transaction amount not valid. Must be positive number for income and negative for expense.");
            }
        } else {
            System.out.println("No transaction with id "+id+" exists");
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
            System.out.println("Transaction category has been changed"+header+transaction);
        } else {
            System.out.println("No transaction with id "+id+" exists");
        }
    }

    public void printTransactionsByDate(Date date) {
        System.out.println("\nIncome");
        incomeStorage.printByDate(date);
        System.out.println("Expenses");
        expenseStorage.printByDate(date);
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

    //method for printing a month and year, respectively, for the whole year or for a specified month.
    public void printTransactionsMonth(int month, int choice) {
        if (month >= 0 && month <= 12 && choice >= 1 && choice <= 3) {
            double incomeSumMonth = 0;
            double expenseSumMonth = 0;
            System.out.println("---------------------------");
            if (choice == 1 || choice == 3) {
                incomeSumMonth = incomeStorage.printMonthReturnSum(month, true);
            }
            if (choice == 2 || choice == 3) {
                expenseSumMonth = expenseStorage.printMonthReturnSum(month, true);
            }
            if (choice == 3) {
                System.out.println("TOTAL BALANCE " + Date.getMonthAsString(month).toUpperCase() + ": " + (incomeSumMonth + expenseSumMonth));
                System.out.println("---------------------------");
            }
        } else {
            System.out.println("Input choices not valid");
        }
    }

    public void printTransactionsYear(int choice) {
        double incomeSumYear = 0;
        double expenseSumYear = 0;
        if (choice >= 1 && choice <= 3) {
            System.out.println("---------------------------");
            if (choice == 1 || choice == 3) {
                incomeSumYear += incomeStorage.printYearReturnSum();
            }
            if (choice == 2 || choice == 3) {
                expenseSumYear += expenseStorage.printYearReturnSum();
            }
            if (choice == 3) {
                System.out.println("TOTAL BALANCE 2024: " + (incomeSumYear + expenseSumYear));
                System.out.println("---------------------------");
            }
        } else {
            System.out.println("Input choice not valid");
        }
    }

    //writes the current transactionID to file and also calls on the write to file method for the respective transaction systems
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
