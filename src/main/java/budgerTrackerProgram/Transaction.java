package budgerTrackerProgram;

//parent class to Expense and Income. Abstract class.
public abstract class Transaction {
    final String id;
    Date date;
    double amount;

    //constructor for Transaction - never used directly, only used as super() in child classes
    Transaction(String id, Date date, double amount) {
        this.id = id;
        this.date = date;
        this.amount = amount;
    }

    //abstract method, i.e., no method body, only present to be able to call the "same" method in the child classes
    abstract void setCategory(int categoryIndex);

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