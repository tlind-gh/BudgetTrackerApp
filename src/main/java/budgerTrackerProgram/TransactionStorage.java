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

    /*constructor which takes a user specific filepath from the TransactionSystem (this program only has one user
    so this feature is built for a theoretical expansion of the program with more users) and a boolean to
    determine the "role" of the storage (Income or Expense)*/
    public TransactionStorage(String filepath_user, boolean isIncome) {
        this.isIncome = isIncome;
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
        /*local Type variables (necessary for ArrayLists with gson) for use with the FileReader, is set to ArraList<Income> or
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
    }

    void removeTransaction(Transaction transaction) {
        transactionData.get(transaction.getDate().getMonth()).remove(transaction);
    }

    //find and return a transaction based on id by looping all lists in the hashmap
    Transaction findTranscation(long id) {
        boolean isFound = false;
        Transaction transaction = null;
        for (List<Transaction> transactionArrayList : transactionData.values()) {
            for (Transaction t : transactionArrayList) {
                if (t.getId() == id) {
                    transaction = t;
                    isFound = true;
                    break;
                }
            }
            if (isFound) {
                break;
            }
        }
        return transaction;
    }

    //print all transaction for a month and returns the sum
    double printMonthReturnSum(int month) {
        double transactionSum = 0;
        String transactionType = isIncome ? " - INCOME" : " - EXPENSES";
        if (!transactionData.get(month).isEmpty()) {
            System.out.println(Date.getMonthAsString(month).toUpperCase()+transactionType);
            System.out.printf("%-20s %-20s %-20s %-20s\n", "Date","Amount","Category","ID");
            for (Transaction transaction : transactionData.get(month)) {
                System.out.println(transaction);
                transactionSum += transaction.getAmount();
            }
            System.out.println("---------------------------");
        }
        return transactionSum;
    }
}
