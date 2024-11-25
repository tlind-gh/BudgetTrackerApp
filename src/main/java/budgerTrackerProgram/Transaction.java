package budgerTrackerProgram;

//parent class to Expense and Income.
public abstract class Transaction {
    final String id;
    Date date;
    double amount;

    //constructor for Transaction is never used, only used as super() in child classes
    Transaction(String id, Date date, double amount) {
        this.id = id;
        this.date = date;
        this.amount = amount;
    }

    void setCategory(int categoryIndex) {
    }

    String getId() {
        return id;
    }

    Date getDate() {
        return date;
    }

    double getAmount() {
        return amount;
    }

    void setAmount(double amount) {
        this.amount = amount;
    }
}