package budgerTrackerProgram;

public class Income extends Transaction {
    private EIncomeCategory category;

    //constructor called by newTransaction() in the TransactionSystem class
    Income(String id, Date date, double amount, int categoryIndex) {
        super(id, date, amount);
        /*does not allow amount to be 0 or negative for Income objects
        (should not be allowed to occur by the newTransaction() method, but this is an extra safe-guard)*/
        if (amount <= 0) {
            throw new IllegalArgumentException("Income cannot have amount <= 0");
        }
        setCategory(categoryIndex);
    }

    /*set category by using index input used in conjunction with printTransactionCategories()
    in TransactionSystem class which prints a numbered list of the enums in EIncomeCategory class*/
    void setCategory(int categoryIndex) {
        try {
            category = EIncomeCategory.values()[categoryIndex-1];
        } catch (ArrayIndexOutOfBoundsException e) {
            category = EIncomeCategory.OTHER;
            System.out.println("Not a valid choice, category set to other");
        }

    }

    /* @Override-methods: NB! example of polymorphism, this method-call on an instance
    of a child class of Transaction, does different things depending on the child class*/

    /*overrides the setAmount() for Transaction and includes the include specific if-clause
    not allowing income amount to be zero or negative*/
    @Override
    void setAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Income cannot have amount <= 0");
        } else {
            this.amount = amount;
        }
    }

    @Override
    public String toString() {
        return String.format("%-20s %-20.1f %-20s %-20s", date, amount, category.name(), id);
    }
}

