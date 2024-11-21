package budgerTrackerProgram;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*TransactionStorage handles reading and writing to file and also holds the income and expense data (in HashMaps)
an instance of the transactionStorage class is handled via the TransactionSystem class*/
public class TransactionStorage {
    private final Gson gson;
    private final String filepath_root;
    private final String filepath_income;
    private final String filepath_expense;
    private final Map<Integer, List<Income>> incomeData;
    private final Map<Integer, List<Expense>> expenseData;

    /*constructor which takes user as argument, as the filepaths are user specific (this program only has one user
    so this feature is built for a theoretical expansion of the program with more users)*/
    public TransactionStorage(User user) {
        gson = new Gson();
        filepath_root = "src/main/BudgetTrackerFileStorage/" + user.userID() + "_" + user.lastName();
        filepath_income = filepath_root + "/Income";
        filepath_expense = filepath_root + "/Expense";
        incomeData = new HashMap<>();
        expenseData = new HashMap<>();
        readfromFile();
    }

    /*separated from the constructor for legibility, but is only run from the constructor reads from json file storage
    and stores into hashmaps with month (integer) as key and ArraLists of Income or Expense as value*/
    private void readfromFile() {
        //check if there is user folder. Otherwise adds user folder and two subfolders for income and expense files, respectivley
        File f = new File(filepath_root);
        if (f.mkdir()) {
            new File(filepath_income).mkdir();
            new File(filepath_expense).mkdir();
        }
        //local Type variables (necessary for ArraLists with gson) for use with the FileReader
        Type listTypeIncome = new TypeToken<ArrayList<Income>>() {}.getType();
        Type listTypeExpense = new TypeToken<ArrayList<Expense>>() {}.getType();

        /*creates 12 ArrayLists (for the 12 months of 2024) in each map with the months (1-12) as keys
        data is loaded from a file if a file for that month exists, otherwise an empty arraylist is created for that month*/
        for (int i = 1; i <= 12; i++) {
            try {
                incomeData.put(i, gson.fromJson(new FileReader(filepath_income + "/" + i + ".json"), listTypeIncome));
            } catch (IOException e) {
                incomeData.put(i, new ArrayList<>());
            }
            try {
                expenseData.put(i, gson.fromJson(new FileReader(filepath_expense + "/" + i + ".json"), listTypeExpense));
            } catch (IOException e) {
                expenseData.put(i, new ArrayList<>());
            }
        }
    }

    /*method which is called by the TransactionSystem() when the user closes the program and which stores
    all non-empty ArrayLists (in the HashMaps) to separate json files*/
    void writeToFile() {
        for (int i = 1; i <= 12; i++) {
            if (!incomeData.get(i).isEmpty()) {
                try {
                    FileWriter fw = new FileWriter(filepath_income + "/" + i + ".json");
                    gson.toJson(incomeData.get(i), fw);
                    fw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (!expenseData.get(i).isEmpty()) {
                try {
                    FileWriter fw = new FileWriter(filepath_expense + "/" + i + ".json");
                    gson.toJson(expenseData.get(i), fw);
                    fw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /*adding a new Transaction to the storage, depending on if an Income or an Expense object is provided as an argument
    it is added to the incomeData or the expenseData by the following two methods. Java chooses the "correct method
    based on input arguments, this is an example of polymophism*/
    void addTransaction(Income income, int month) {
        incomeData.get(month).add(income);
    }

    void addTransaction(Expense expense, int month) {
        expenseData.get(month).add(expense);
    }

    List<Income> getIncomeDataMonth(int month) {
        return incomeData.get(month);
    }

    List<Expense> getExpenseDataMonth(int month) {
        return expenseData.get(month);
    }

}