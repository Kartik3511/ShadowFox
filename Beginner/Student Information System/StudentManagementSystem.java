import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class StudentManagementSystem extends JFrame {

    private JTextField nameField, rollField, deptField;
    private JButton addButton, updateButton, deleteButton;
    private JTable table;
    private DefaultTableModel model;

    private ArrayList<Student> students = new ArrayList<>();

    public StudentManagementSystem() {

        setTitle("Student Information System");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel for input fields
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Roll No:"));
        rollField = new JTextField();
        panel.add(rollField);

        panel.add(new JLabel("Department:"));
        deptField = new JTextField();
        panel.add(deptField);

        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        panel.add(addButton);
        panel.add(updateButton);

        // Table
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Name", "Roll No", "Department"});

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Layout
        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(deleteButton, BorderLayout.SOUTH);

        // Event Handling
        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                nameField.setText(model.getValueAt(selectedRow, 0).toString());
                rollField.setText(model.getValueAt(selectedRow, 1).toString());
                deptField.setText(model.getValueAt(selectedRow, 2).toString());
            }
        });
    }

    private void addStudent() {
        String name = nameField.getText();
        String roll = rollField.getText();
        String dept = deptField.getText();

        if (name.isEmpty() || roll.isEmpty() || dept.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields required!");
            return;
        }

        Student student = new Student(name, roll, dept);
        students.add(student);
        model.addRow(new Object[]{name, roll, dept});

        clearFields();
    }

    private void updateStudent() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to update!");
            return;
        }

        String name = nameField.getText();
        String dept = deptField.getText();

        students.get(selectedRow).setName(name);
        students.get(selectedRow).setDepartment(dept);

        model.setValueAt(name, selectedRow, 0);
        model.setValueAt(dept, selectedRow, 2);

        clearFields();
    }

    private void deleteStudent() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to delete!");
            return;
        }

        students.remove(selectedRow);
        model.removeRow(selectedRow);

        clearFields();
    }

    private void clearFields() {
        nameField.setText("");
        rollField.setText("");
        deptField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentManagementSystem().setVisible(true);
        });
    }
}
