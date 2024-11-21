package budgerTrackerProgram;

//parent class to Expense and Income. Constructor for Transaction never used directly.
public class Transaction {
    final String id;
    final Date date;
    double amount;

    Transaction(String id, Date date, double amount) {
        this.id = id;
        this.date = date;
        this.amount = amount;
    }


    String getId() {
        return id;
    }

    double getAmount() {
        return amount;
    }

    Date getDate() {
        return date;
    }
}