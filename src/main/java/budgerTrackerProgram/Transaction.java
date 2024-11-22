package budgerTrackerProgram;

//parent class to Expense and Income.
public abstract class Transaction {
    final long id;
    Date date;
    double amount;

    //constructor for Transaction is never used, only used as super() in child classes
    Transaction(long id, Date date, double amount) {
        this.id = id;
        this.date = date;
        this.amount = amount;
    }

    abstract void setCategory(int categoryIndex);

    long getId() {
        return id;
    }

    Date getDate() {
        return date;
    }

    void setDate(Date date) {
        this.date = date;
    }

    double getAmount() {
        return amount;
    }

    void setAmount(double amount) {
        this.amount = amount;
    }
}