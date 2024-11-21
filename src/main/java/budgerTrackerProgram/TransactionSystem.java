package budgerTrackerProgram;

import java.util.List;

public class TransactionSystem {

    private long nextTransactionID;
    private final TransactionStorage transactionStorage;

    public TransactionSystem(User user) {
        transactionStorage = new TransactionStorage(user);

        //change, should be read from file.
        nextTransactionID = 1;
    }


    public void newTransaction(int month, int day, double amount, int categoryNr) {
        Date date = new Date(month, day);
        String id = String.format("%02d%02d", month, nextTransactionID);
        if (amount > 0) {
            Income income = new Income(("I"+id), date, amount, categoryNr);
            transactionStorage.addTransaction(income, month);
            nextTransactionID +=1;
            System.out.println("Transaction added successfully\n"+income);
        } else if (amount < 0) {
            Expense expense = new Expense(("E"+id), date, amount, categoryNr);
            transactionStorage.addTransaction(expense, month);
            nextTransactionID +=1;
            System.out.println("Transaction added successfully\n"+expense);
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
        if(month >= 0 && month <= 12 && choice >= 1 && choice <= 3) {
            if (month == 0) {
                printTransactionsYear(choice);
            } else {
                double incomeSumMonth = 0;
                double expenseSumMonth = 0;
                if (choice == 1 || choice == 3) {
                    incomeSumMonth = printIncomeMonthReturnSum(month);
                }
                if (choice == 2 || choice == 3) {
                    expenseSumMonth = printExpenseMonthReturnSum(month);
                }
                if (choice == 3) {
                    System.out.println("TOTAL BALANCE" + Date.getMonthAsString(month).toUpperCase() + ": " + (incomeSumMonth - expenseSumMonth));
                }
            }
        } else {
            System.out.println("Input choices not valid");
        }
    }

    private void printTransactionsYear(int choice) {
        double incomeSumYear = 0;
        double expenseSumYear = 0;
        if (choice == 1 || choice == 3) {
            for (int i = 1; i <= 12; i++) {
                incomeSumYear += printIncomeMonthReturnSum(i);
            }
            System.out.println("TOTAL INCOME 2024: " + incomeSumYear);
            System.out.println("-------------");
        }
        if (choice == 2 || choice == 3) {
            for (int i = 1; i <= 12; i++) {
                expenseSumYear += printExpenseMonthReturnSum(i);
                System.out.println("-------------");
            }
            System.out.println("TOTAL INCOME 2024: " + expenseSumYear);
            System.out.println("-------------");
        }
        if (choice == 3) {
            System.out.println("TOTAL BALANCE 2024" + +(incomeSumYear - expenseSumYear));
        }
    }

    private double printIncomeMonthReturnSum(int month) {
        List<Income> incomeList = transactionStorage.getIncomeDataMonth(month);
        double incomeSum = 0;
        System.out.println(Date.getMonthAsString(month).toUpperCase()+" - INCOME");
        if (!incomeList.isEmpty()) {
            System.out.println("Date\t\t\tAmount\t\tCategory\t\tTransaction id");
            for (Transaction transaction : incomeList) {
                System.out.println(transaction);
                incomeSum += transaction.getAmount();
            }
            System.out.println(Date.getMonthAsString(month) +"total income: "+incomeSum);
        } else {
            System.out.println("No transactions");
        }
        System.out.println("-------------");
        return incomeSum;
    }

    private double printExpenseMonthReturnSum(int month) {
        List<Expense> expenseList = transactionStorage.getExpenseDataMonth(month);
        double expenseSum = 0;
        System.out.println(Date.getMonthAsString(month).toUpperCase()+" - EXPENSE");
        if (!expenseList.isEmpty()) {
            System.out.println("Date\t\t\tAmount\t\tCategory\t\tTransaction id");
            for (Transaction transaction : expenseList) {
                System.out.println(transaction);
                expenseSum += transaction.getAmount();
            }
            System.out.println(Date.getMonthAsString(month) + "total expenses: "+ expenseSum);
        } else {
            System.out.println("No transactions");
        }
        System.out.println("-------------");
        return expenseSum;
    }

    public void closeSystem() {
        transactionStorage.writeToFile();
    }

}


