package budgerTrackerProgram;

//Expense class, inherits from Transaction
public class Expense extends Transaction{
    private EExpenseCategory category;

    //constructor called by newTransaction() in the TransactionSystem class
    Expense(String id, Date date, double amount, int categoryIndex) {
        super(id, date, amount);
        /*does not allow amount to be 0 or positive for Expense objects
        (should not be allowed to occur by the newTransaction() method, but this is an extra safe-guard)*/
        if (amount >= 0) {
            throw new IllegalArgumentException("Expense cannot have amount >= 0");
        }
        setCategory(categoryIndex);
    }

    /*set category by using index input used in conjunction with printTransactionCategories()
    in TransactionSystem class which prints a numbered list of the enums in EExpenseCategory class*/
    void setCategory(int categoryIndex) {
        try {
            category = EExpenseCategory.values()[categoryIndex-1];
        } catch (ArrayIndexOutOfBoundsException e) {
            category = EExpenseCategory.OTHER;
            System.out.println("Not a valid choice, category set to other");
        }
    }

    @Override
    void setAmount(double amount) {
        if (amount >= 0) {
            throw new IllegalArgumentException("Expense cannot have amount >= 0");
        } else {
            this.amount = amount;
        }
    }

    @Override
    public String toString() {
        return String.format("%-20s %-20.1f %-20s %-20s", date, amount, category.name(), id);
    }
}
