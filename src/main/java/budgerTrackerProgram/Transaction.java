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

    void setAmount(double amount) {
        this.amount = amount;
    }

    /*abstract method, i.e., no method body (because Transaction class does not have a variable for category) '
    only present to be able to call the "same" method in the child classes*/
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

    /*Static method for getting a String with column headers corresponding to the information in the string
    returned with the toString() method in the child classes. Used by other classes in methods printing transaction info*/
    static String getTransactionHeader() {
        return String.format("%-20s %-20s %-20s %-20s", "Date","Amount","Category","Transaction ID");
    }
}