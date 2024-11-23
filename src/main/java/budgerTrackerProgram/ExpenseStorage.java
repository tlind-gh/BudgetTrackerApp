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

public class ExpenseStorage {
    private final Gson gson;
    private final String filepath;
    private final Map<Integer, List<Expense>> transactionData;

    /*constructor which takes user as argument, as the filepaths are user specific (this program only has one user
    so this feature is built for a theoretical expansion of the program with more users)*/
    public ExpenseStorage(User user) {
        gson = new Gson();
        filepath = "src/main/BudgetTrackerFileStorage/" + user.userID() + "_" + user.lastName()+ "/Expense/";
        transactionData = new HashMap<>();
        readfromFile();
    }

    /*separated from the constructor for legibility, but is only run from the constructor reads from json file storage
    and stores into hashmaps with month (integer) as key and ArrayLists of transactions as values*/
    private void readfromFile() {
        //local Type variables (necessary for ArrayLists with gson) for use with the FileReader
        Type listType = new TypeToken<ArrayList<Expense>>() {}.getType();

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

    void addTransaction(Expense expense) {
        transactionData.get(expense.getDate().getMonth()).add(expense);
    }

    void removeTransaction(Transaction transaction) {
        transactionData.get(transaction.getDate().getMonth()).remove(transaction);
    }

    //find and return a transcation based on id
    Transaction findTranscation(long id) {
        boolean isFound = false;
        Transaction transaction = null;
        for (int i = 1; i <= 12; i++) {
            for (Transaction t : transactionData.get(i)) {
                if (transaction.getId() == id) {
                    transaction = t;
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
        if (!transactionData.get(month).isEmpty()) {
            System.out.println(Date.getMonthAsString(month).toUpperCase()+" - EXPENSE");
            System.out.println("Date\t\t\tAmount\t\tCategory\t\tTransaction id");
            for (Transaction transaction : transactionData.get(month)) {
                System.out.println(transaction);
                transactionSum += transaction.getAmount();
            }
            System.out.println("---------------------------");
        }
        return transactionSum;
    }
}
