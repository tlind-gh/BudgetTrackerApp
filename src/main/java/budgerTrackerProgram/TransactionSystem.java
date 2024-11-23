package budgerTrackerProgram;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class TransactionSystem {
    private final String filepath_user;
    private final IncomeStorage incomeStorage;
    private final ExpenseStorage expenseStorage;
    private long nextTransactionID;

    public TransactionSystem(User user) {
        filepath_user = "src/main/BudgetTrackerFileStorage/" + user.userID() + "_" + user.lastName();
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
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        incomeStorage = new IncomeStorage(user);
        expenseStorage = new ExpenseStorage(user);
    }
    /*
    public void changeTransaction(long id, Date date) {
        Transaction transaction = searchTransactionByID(id, true);
        if (transaction == null) {
            transaction = searchTransactionByID(id, false);
        }
        if (transaction != null) {
            transaction.setDate(date);
            System.out.println("Date of transaction changed:\n"+transaction);
        } else {
            System.out.println("Not a valid id");
        }
    }

    public void changeTransaction(long id, double amount) {
        Transaction transaction = searchTransactionByID(id, true);
        if (transaction == null) {
            transaction = searchTransactionByID(id, false);
        }
        if (transaction != null) {
            try {
                transaction.setAmount(amount);
                System.out.println("Amount of transaction changed:\n" + transaction);
            } catch (IllegalArgumentException e) {
                System.out.println("Not a valid amount for the transaction type");
            }
        } else {
            System.out.println("Not a valid id");
        }
    }

    public void deleteTransaction(long id) {
        if (id )
    }
    */
    public Date createDate(int month, int day) {
        Date date = null;
        try {
            date = new Date(month, day);
        } catch (IllegalArgumentException e) {
            System.out.println("Not a valid date");
        }
        return date;
    }

    public void newTransaction(Date date, double amount, int categoryNr) {
        if (amount != 0) {
            if (amount > 0) {
                Income income = new Income(nextTransactionID, date, amount, categoryNr);
                incomeStorage.addTransaction(income);
                System.out.println("Transaction added successfully\n"+ income);
            } else {
                Expense expense = new Expense(nextTransactionID+1, date, amount, categoryNr);
                expenseStorage.addTransaction(expense);
                System.out.println("Transaction added successfully\n"+ expense);
            }
            nextTransactionID += 10;
        } else {
            System.out.println("Transaction amount cannot be zero.");
        }
    }

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

    public void printTransactions(int month, int choice) {
        if (month >= 1 && month <= 12 && choice >= 1 && choice <= 3) {
            printTransactionsMonth(month, choice);
        } else if (month == 0 && choice >= 1 && choice <= 3) {
            printTransactionsYear(choice);
        } else {
            System.out.println("Input choices not valid");
        }
    }

    private void printTransactionsMonth(int month, int choice) {
        double incomeSumMonth = 0;
        double expenseSumMonth = 0;
        if (choice == 1 || choice == 3) {
            incomeSumMonth = incomeStorage.printMonthReturnSum(month);
            if (incomeSumMonth != 0) {
                System.out.println("Total income "+Date.getMonthAsString(month).toLowerCase()+": " + incomeSumMonth);
            } else {
                System.out.println("No income post in "+Date.getMonthAsString(month).toLowerCase());
            }
            System.out.println("---------------------------");
        }
        if (choice == 2 || choice == 3) {
            if (incomeSumMonth != 0) {
                expenseSumMonth = expenseStorage.printMonthReturnSum(month);
                System.out.println("Total expenses "+Date.getMonthAsString(month).toLowerCase()+": " + expenseSumMonth);
            } else {
                System.out.println("No expense posts in "+Date.getMonthAsString(month).toLowerCase());
            }
            System.out.println("---------------------------");
        }
        if (choice == 3) {
            System.out.println("TOTAL BALANCE" + Date.getMonthAsString(month).toUpperCase() + ": " + (incomeSumMonth - expenseSumMonth));
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
            System.out.println("TOTAL BALANCE 2024: " + (incomeSumYear - expenseSumYear));
            System.out.println("---------------------------");
        }
    }

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
