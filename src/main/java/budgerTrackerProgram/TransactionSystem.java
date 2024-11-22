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
        Transaction transaction = searchTransactionByID(id, true);
        if (transaction != null) {
            transactionStorage.getIncomeDataMonth(transaction.getDate().getMonth()).remove(transaction);
            System.out.println("Transcation deleted");
        } else {
            transaction = searchTransactionByID(id, false);
            if (transaction != null) {
                transactionStorage.getExpenseDataMonth(transaction.getDate().getMonth()).remove(transaction);
                System.out.println("Transcation deleted");
            } else {
                System.out.println("Not a valid id");
            }
        }
    }

    private Transaction searchTransactionByID(long id, boolean isIncome) {
        Transaction transaction = null;
        boolean isFound = false;
        //List<? extends Transaction> transactionList = (id.charAt(0) == 'I' || id.charAt(0) =='i') ?
        for (int i = 1; i <= 12; i++) {
            if (isFound){
                break;
            }
            List<? extends Transaction> transactionList = isIncome ?
                    transactionStorage.getIncomeDataMonth(i) :
                    transactionStorage.getExpenseDataMonth(i);
            for (Transaction t : transactionList) {
                if (t.getId() == id) {
                    transaction = t;
                    isFound = true;
                    break;
                }
            }
        }
        if (isFound) {
            System.out.println("Transaction found:\n" + transaction);
        } else {
            System.out.println("No transaction with that ID");
        }
        return transaction;
    }

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
                transactionStorage.getIncomeDataMonth(date.getMonth()).add(income);
                System.out.println("Transaction added successfully\n"+ income);
            } else {
                Expense expense = new Expense(nextTransactionID, date, amount, categoryNr);
                transactionStorage.getExpenseDataMonth(date.getMonth()).add(expense);
                System.out.println("Transaction added successfully\n"+ expense);
            }
            nextTransactionID += 1;
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
                    incomeSumMonth = printMonthReturnSum(month, transactionStorage.getIncomeDataMonth(month),"income");
                }
                if (choice == 2 || choice == 3) {
                    expenseSumMonth = printMonthReturnSum(month, transactionStorage.getExpenseDataMonth(month),"expense");
                }
                if (choice == 3) {
                    System.out.println("TOTAL BALANCE" + Date.getMonthAsString(month).toUpperCase() + ": " + (incomeSumMonth - expenseSumMonth));
                }
            }
        } else {
            System.out.println("Input choices not valid");
        }
    }

    public void printTransactions(Date date) {
        int month = date.getMonth();
        boolean isFound = false;
        for (Transaction t : transactionStorage.getIncomeDataMonth(month)) {
            if (date.getDay() == t.getDate().getDay()) {
                System.out.println(t);
                isFound = true;
            }
        }
        for (Transaction t : transactionStorage.getExpenseDataMonth(month)) {
            if (date.getDay() == t.getDate().getDay()) {
                System.out.println(t);
                isFound = true;
            }
        }
    }

    private void printTransactionsYear(int choice) {
        double incomeSumYear = 0;
        double expenseSumYear = 0;
        if (choice == 1 || choice == 3) {
            for (int i = 1; i <= 12; i++) {
                incomeSumYear += printMonthReturnSum(i, transactionStorage.getIncomeDataMonth(i),"income");
            }
            System.out.println("TOTAL INCOME 2024: " + incomeSumYear);
            System.out.println("-------------");
        }
        if (choice == 2 || choice == 3) {
            for (int i = 1; i <= 12; i++) {
                expenseSumYear += printMonthReturnSum(i, transactionStorage.getIncomeDataMonth(i),"expense");
                System.out.println("-------------");
            }
            System.out.println("TOTAL INCOME 2024: " + expenseSumYear);
            System.out.println("-------------");
        }
        if (choice == 3) {
            System.out.println("TOTAL BALANCE 2024" + (incomeSumYear - expenseSumYear));
        }
    }

    private double printMonthReturnSum(int month, List<? extends Transaction> transactionList, String type) {
        double transactionSum = 0;
        System.out.println(Date.getMonthAsString(month).toUpperCase()+" - "+type.toUpperCase());
        if (!transactionList.isEmpty()) {
            System.out.println("Date\t\t\tAmount\t\tCategory\t\tTransaction id");
            for (Transaction transaction : transactionList) {
                System.out.println(transaction);
                transactionSum += transaction.getAmount();
            }
            System.out.println(Date.getMonthAsString(month) + "total "+ type +": "+ transactionSum);
        } else {
            System.out.println("No transactions");
        }
        System.out.println("-------------");
        return transactionSum;
    }

    public void closeSystem() {
        transactionStorage.writeToFile();
    }
}
