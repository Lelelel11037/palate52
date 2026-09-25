public class Employee {
    private String name;
    private String department;
    private double salary;
    private String hireDate;
    private double performanceRating;

    public Employee(String name, String department, double salary, String hireDate, double performanceRating) {
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.hireDate = hireDate;
        this.performanceRating = performanceRating;
    }

    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getSalary() { return salary; }
    public String getHireDate() { return hireDate; }
    public double getPerformanceRating() { return performanceRating; }

    public void setName(String name) { this.name = name; }
    public void setDepartment(String department) { this.department = department; }
    public void setSalary(double salary) { this.salary = salary; }
    public void setHireDate(String hireDate) { this.hireDate = hireDate; }
    public void setPerformanceRating(double performanceRating) { this.performanceRating = performanceRating; }

    @Override
    public String toString() {
        return name + " | " + department + " | зарплата: " + salary + " | дата найма: " + hireDate + " | рейтинг: " + performanceRating;
    }
}