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
    private long nextTransactionID;
    private final String header;

    /*constructor checks if there is a user folder (which there should be if the user has used that app before),
    If there is a folder, it loads the last transactionID from a txt file in this folder.
    Otherwise creates the folder and two subfolders (Income and expense) and sets transactionId to 10 (starting ID) */
    public TransactionSystem(User user) {
        filepath_user = "src/main/BudgetTrackerFileStorage/" + user.userID() + "_" + user.lastName();
        header = String.format("\n%-20s %-20s %-20s %-20s\n", "Date","Amount","Category","ID");
        File f1 = new File(filepath_user);
        if (f1.mkdir()) {
            new File(filepath_user + "/Income").mkdir();
            new File(filepath_user + "/Expense").mkdir();
            nextTransactionID = 10;
        } else {
            try {
                File f2 = new File(filepath_user + "/nextTransactionID.txt");
                Scanner fr = new Scanner(f2);
                nextTransactionID = fr.nextLong();
                fr.close();
            } catch (FileNotFoundException e) {
                nextTransactionID = 10;
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

    /*creates new transaction (income or expense depending on if amount is positive). Used in conjuction w. createDate
    and printTransactionCategories. Sets a transactionID and increments the transactionID in the TransactionSystem by 10
    Id for income is set to the current transactionID of the variable in the TransactionSystem, and expense it set to
    the transactionID +1 (making IDs for income evenly divided by 2 and expense IDs not - used for determining if
    a transaction is income or expense based on id)*/
    public void newTransaction(Date date, double amount, int categoryNr) {
        if (amount != 0) {
            if (amount > 0) {
                Income income = new Income(nextTransactionID, date, amount, categoryNr);
                incomeStorage.addTransaction(income);
                System.out.println("Transaction added successfully"+ header + income);
            } else {
                Expense expense = new Expense(nextTransactionID+1, date, amount, categoryNr);
                expenseStorage.addTransaction(expense);
                System.out.println("Transaction added successfully"+header+expense);
            }
            nextTransactionID += 10;
        } else {
            System.out.println("Transaction amount cannot be zero.");
        }
    }

    //The following 3 methods uses id % 2 == 0, to determine the child class of the transaction and calls on income or expenseStorage
    public void deleteTransaction(long id) {
        Transaction transaction;
        if (id % 2 == 0) {
            transaction = incomeStorage.findTranscation(id);
            if (transaction != null) {
                incomeStorage.removeTransaction(transaction);
            }
        } else {
            transaction = expenseStorage.findTranscation(id);
            if (transaction != null) {
                expenseStorage.removeTransaction(transaction);
            }
        }
        if (transaction != null) {
            System.out.println("The following transaction has been removed:"+header+transaction);
        } else {
            System.out.println("No transaction with id "+id+" exists");
        }
    }

    /*Two methods for changing transaction w. the same name. Java calls "correct" methods based on input arguments types
    (i.e., if second argument is double or int)*/
    public void changeTransaction(long id, double amount) {
        Transaction transaction;
        if (id % 2 == 0) {
            transaction = incomeStorage.findTranscation(id);
        } else {
            transaction = expenseStorage.findTranscation(id);
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

    public void changeTransaction(long id, int categoryIndex) {
        Transaction transaction;
        if (id % 2 == 0) {
            transaction = incomeStorage.findTranscation(id);
        } else {
            transaction = expenseStorage.findTranscation(id);
        }
        if (transaction != null) {
            transaction.setCategory(categoryIndex);
            System.out.println("Transaction category has been changed"+header+transaction);
        } else {
            System.out.println("No transaction with id "+id+" exists");
        }
    }

    //prints a list of either EIncomeCategories or EExpenseCategories enums depending on if transaction amount is neg. or pos.)
    public void printTransactionCategories(double transactionAmount) {
        if (transactionAmount > 0) {
            for (EIncomeCategory enumCategory : EIncomeCategory.values()) {
                System.out.println((enumCategory.ordinal() + 1) + ". " + enumCategory.name());
            }
        } else if (transactionAmount < 0) {
            for (EExpenseCategory enumCategory : EExpenseCategory.values()) {
                System.out.println((enumCategory.ordinal() + 1) + ". " + enumCategory.name());
            }
        } else {
            System.out.println("Transaction amount cannot be zero.");
        }
    }

    //prints transactions (income, expense or both) with balance for a spec. month or for the whole year.
    public void printTransactions(int month, int choice) {
        if (month >= 0 && month <= 12 && choice >= 1 && choice <= 3) {
            System.out.println("---------------------------");
            if (month == 0) {
                printTransactionsYear(choice);
            } else {
                printTransactionsMonth(month, choice);
            }
        } else {
            System.out.println("Input choices not valid");
        }
    }

    //method for printing a month and year, respectively - called from printTransactions(), separated out for legibility
    private void printTransactionsMonth(int month, int choice) {
        double incomeSumMonth = 0;
        double expenseSumMonth = 0;
        if (choice == 1 || choice == 3) {
            incomeSumMonth = incomeStorage.printMonthReturnSum(month);
            if (incomeSumMonth != 0) {
                System.out.println("Total income "+Date.getMonthAsString(month)+": " + incomeSumMonth);
            } else {
                System.out.println("No income posts for" + Date.getMonthAsString(month));
            }
            System.out.println("---------------------------");
        }
        if (choice == 2 || choice == 3) {
            expenseSumMonth = expenseStorage.printMonthReturnSum(month);
            if (incomeSumMonth != 0) {
                System.out.println("Total expenses "+Date.getMonthAsString(month)+": " + expenseSumMonth);
            } else {
                System.out.println("No expense posts for "+Date.getMonthAsString(month));
            }
            System.out.println("---------------------------");
        }
        if (choice == 3) {
            System.out.println("TOTAL BALANCE " + Date.getMonthAsString(month).toUpperCase() + ": " + (incomeSumMonth + expenseSumMonth));
            System.out.println("---------------------------");
        }
    }

    private void printTransactionsYear(int choice) {
        double incomeSumYear = 0;
        double expenseSumYear = 0;
        if (choice == 1 || choice == 3) {
            for (int i = 1; i <= 12; i++) {
                incomeSumYear += incomeStorage.printMonthReturnSum(i);
            }
            System.out.println("TOTAL INCOME 2024: " + incomeSumYear);
            System.out.println("---------------------------");
        }
        if (choice == 2 || choice == 3) {
            for (int i = 1; i <= 12; i++) {
                expenseSumYear += expenseStorage.printMonthReturnSum(i);
            }
            System.out.println("TOTAL EXPENSES 2024: " + expenseSumYear);
            System.out.println("---------------------------");
        }
        if (choice == 3) {
            System.out.println("TOTAL BALANCE 2024: " + (incomeSumYear + expenseSumYear));
            System.out.println("---------------------------");
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
