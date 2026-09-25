public class Employee extends User {
    private static final long serialVersionUID = 1L;

    private String position;

    public Employee(String username, String password, String email, String position) {
        super(username, password, email);
        this.position = position;
    }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    @Override
    public void displayRoleInfo() {
        System.out.println("сотрудник | должность: " + position);
    }
}