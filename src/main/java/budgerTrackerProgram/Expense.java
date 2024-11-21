package budgerTrackerProgram;

public class Expense extends Transaction{
    private EExpenseCategory category;

    //constructor called by newTransaction() in the TransactionSystem class
    Expense(String id, Date date, double amount, int categoryIndex) {
        super(id, date, amount);
        //does not allow amount to be 0 or negative for Expense objects
        if (amount >= 0) {
            throw new IllegalArgumentException("Expense cannot have amount >= 0");
        }
        setCategory(categoryIndex);
    }

    //separated from constructor for legibility
    void setCategory(int categoryIndex) {
        try {
            category = EExpenseCategory.values()[categoryIndex-1];
        } catch (ArrayIndexOutOfBoundsException e) {
            category = EExpenseCategory.OTHER;
            System.out.println("Not a valid choice, category set to other");
        }

    }

    EExpenseCategory getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return date +"\t\t"+amount+"\t\t"+category.name()+"\t\t\t"+id;
    }
}
