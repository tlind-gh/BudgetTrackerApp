package budgerTrackerProgram;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*Class for writing/reading transaction data from/to file and also add, search, remove and print transactions.
Can either be of type Income or Expense (set by boolean) and handle one of the two storages.
Methods from the class are handled via the TransactionSystem class which coordinates the methods for use
from the Main class*/
public class TransactionStorage {
    private final boolean isIncome;
    private final Gson gson;
    private final String filepath;
    private final Map<Integer, List<Transaction>> transactionData;
    private final String printTransactionHeader;

    /*constructor which takes a user specific filepath from the TransactionSystem (this program only has one user
    so this feature is built for a theoretical expansion of the program with more users) and a boolean to
    determine the "role" of the storage (Income or Expense)*/
    public TransactionStorage(String filepath_user, boolean isIncome) {
        this.isIncome = isIncome;
        printTransactionHeader = String.format("\n%-20s %-20s %-20s %-20s\n", "Date","Amount","Category","Transaction ID");
        gson = new Gson();
        /*income and expense files are stored in different subfolders, filepath is set depending on the "role" set
        by the isIncome boolean*/
        filepath = isIncome ?
                filepath_user + "/Income/":
                filepath_user + "/Expense/";
        transactionData = new HashMap<>();
        readfromFile();
    }

    /*separated from the constructor for legibility, but is only run from the constructor reads from json file storage
    and stores into hashmaps with month (integer) as key and ArrayLists of transactions as values*/
    private void readfromFile() {
        /*local Type variables (necessary for ArrayLists with gson) for use with the FileReader, is set to ArrayList<Income> or
        ArrayList<Expense> depending on isIncome boolean*/
        Type listType = isIncome ?
                new TypeToken<ArrayList<Income>>() {}.getType():
                new TypeToken<ArrayList<Expense>>() {}.getType();

        /*creates 12 ArrayLists (for the 12 months of 2024) in each map with the months (1-12) as keys
        data is loaded from a file if a file for that month exists, otherwise an empty arraylist is created for that month*/
        for (int i = 1; i <= 12; i++) {
            try {
                transactionData.put(i, gson.fromJson(new FileReader(filepath + i + ".json"), listType));
            } catch (IOException e) {
                transactionData.put(i, new ArrayList<>());
            }
        }
    }

    /*method which is called by the TransactionSystem() when the user closes the program and which stores
    all non-empty ArrayLists (in the HashMaps) to separate json files*/
    void writeToFile() {
        for (int i = 1; i <= 12; i++) {
            if (!transactionData.get(i).isEmpty()) {
                try {
                    FileWriter fw = new FileWriter(filepath + i + ".json");
                    gson.toJson(transactionData.get(i), fw);
                    fw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    void addTransaction(Transaction transaction) {
        transactionData.get(transaction.getDate().getMonth()).add(transaction);
        System.out.println("Transaction added successfully"+ printTransactionHeader + transaction);
    }

    void removeTransaction(String id) {
        Transaction transaction = findTransaction(id);
        if (transaction != null) {
            transactionData.get(transaction.getDate().getMonth()).remove(transaction);
            System.out.println("The following transaction has been removed:"+printTransactionHeader+transaction);
        }
    }

    //find and return a transaction based on id by extracting the month from the id (second and third last digit)
    Transaction findTransaction(String id) {
        Transaction transaction = null;
        int month = Integer.parseInt(id.substring(1,3));
        for (Transaction t : transactionData.get(month)) {
            if (t.getId().equals(id)) {
                transaction = t;
                break;
            }
        }
        return transaction;
    }

    void printByDate(Date date) {
        ArrayList<Transaction> transactionArrayList = new ArrayList<>();
        for (Transaction transaction : transactionData.get(date.getMonth())) {
            if (transaction.getDate().getDay() == date.getDay()) {
                transactionArrayList.add(transaction);
            }
        }
        if (!transactionArrayList.isEmpty()) {
            System.out.printf("%-20s %-20s %-20s %-20s\n", "Date", "Amount", "Category", "Transaction ID");
            for (Transaction transaction : transactionArrayList) {
                System.out.println(transaction);
            }
        } else {
            System.out.println("No posts for "+date);
        }
        System.out.println("---------------------------");
    }

    /*print all transaction for a month and returns the sum. Also print sum if boolean input argument is true*/
    double printMonthReturnSum(int month, boolean printMonthTotal) {
        double transactionSumMonth = 0;
        String transactionType = isIncome ? "INCOME" : "EXPENSES";
        if (!transactionData.get(month).isEmpty()) {
            System.out.println(Date.getMonthAsString(month).toUpperCase() + " - " + transactionType);
            System.out.printf("%-20s %-20s %-20s %-20s\n", "Date", "Amount", "Category", "Transaction ID");
            for (Transaction transaction : transactionData.get(month)) {
                System.out.println(transaction);
                transactionSumMonth += transaction.getAmount();
            }
        } else if (printMonthTotal) {
            System.out.println("No "+transactionType+" posts for " + Date.getMonthAsString(month));
        }
        System.out.println("---------------------------");
        if (printMonthTotal) {
            System.out.println("Total "+transactionType+" for "+ Date.getMonthAsString(month)+": "+transactionSumMonth);
            System.out.println("---------------------------");
        }
        return transactionSumMonth;
    }

    //prints all transactions from the hashmap, calls on the month method w. specification to not print the sum per month
    double printYearReturnSum() {
        double transactionSumYear = 0;
        String transactionType = isIncome ? "INCOME" : "EXPENSES";
        for (int i = 1; i <= 12; i++) {
            transactionSumYear += printMonthReturnSum(i, false);
        }
        System.out.println("TOTAL "+transactionType+" 2024: " + transactionSumYear);
        System.out.println("---------------------------");
        return transactionSumYear;
    }
}
