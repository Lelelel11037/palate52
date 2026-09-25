public class Customer extends User {
    private static final long serialVersionUID = 1L;

    private double balance;

    public Customer(String username, String password, String email, double balance) {
        super(username, password, email);
        this.balance = balance;
    }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    @Override
    public void displayRoleInfo() {
        System.out.println("покупатель | баланс: $" + balance);
    }
}