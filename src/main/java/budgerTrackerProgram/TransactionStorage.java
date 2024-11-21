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

public class TransactionStorage {
    private final Gson gson;
    private final String filepath_root;
    private final String filepath_income;
    private final String filepath_expense;
    private final Map<Integer, List<Income>> incomeData;
    private final Map<Integer, List<Expense>> expenseData;

    public TransactionStorage(User user) {
        gson = new Gson();
        filepath_root = "src/main/BudgetTrackerFileStorage/" + user.userID() + "_" + user.lastName();
        filepath_income = filepath_root + "/Income";
        filepath_expense = filepath_root + "/Expense";
        incomeData = new HashMap<>();
        expenseData = new HashMap<>();
        readfromFile();
    }

    private void readfromFile() {
        //check if there is user file - otherwise add folders
        long id = 0;
        Type listTypeIncome = new TypeToken<ArrayList<Income>>() {}.getType();
        Type listTypeExpense = new TypeToken<ArrayList<Expense>>() {}.getType();
        File f = new File(filepath_root);
        if (f.mkdir()) {
            new File(filepath_income).mkdir();
            new File(filepath_expense).mkdir();
        }
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

    Map<Integer, List<Income>> getAllIncomeData() {
        return incomeData;
    }

    Map<Integer, List<Expense>> getAllExpenseData() {
        return expenseData;
    }

}