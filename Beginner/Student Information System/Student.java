public class Student {

    private String name;
    private String rollNo;
    private String department;

    public Student(String name, String rollNo, String department) {
        this.name = name;
        this.rollNo = rollNo;
        this.department = department;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getRollNo() {
        return rollNo;
    }

    public String getDepartment() {
        return department;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}