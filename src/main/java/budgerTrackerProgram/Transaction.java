package budgerTrackerProgram;

//parent class to Expense and Income.
public class Transaction {
    final String id;
    final Date date;
    double amount;

    //constructor for Transaction is never used, only used as super() in child classes
    Transaction(String id, Date date, double amount) {
        this.id = id;
        this.date = date;
        this.amount = amount;
    }

    double getAmount() {
        return amount;
    }
}