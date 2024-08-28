

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;

public class App extends JFrame {
    private JTextField studentNameField;
    private JTextField studentIDField;
    private JTextArea attendanceArea;
    private HashMap<String, String> attendanceRecords;
    private static final String FILE_NAME = "attendance_records.txt";

    public App() {
        // Initialize attendance records
        attendanceRecords = new HashMap<>();
        loadAttendanceRecords();

        // Set up the frame
        setTitle("Attendance Management System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Create UI components
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Attendance Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        studentNameField = new JTextField(20);
        studentIDField = new JTextField(20);
        JButton markAttendanceButton = new JButton("Mark Attendance");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        inputPanel.add(titleLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        inputPanel.add(new JLabel("Student Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        inputPanel.add(studentNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Student ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        inputPanel.add(studentIDField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        inputPanel.add(markAttendanceButton, gbc);

        attendanceArea = new JTextArea(15, 50);
        attendanceArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        attendanceArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(attendanceArea);

        // Add components to the frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Add action listener to the button
        markAttendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                markAttendance();
            }
        });

        // Display existing attendance records
        displayAttendanceRecords();

        // Make the frame visible
        setVisible(true);
    }

    private void markAttendance() {
        String studentName = studentNameField.getText();
        String studentID = studentIDField.getText();

        if (studentName.isEmpty() || studentID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both student name and ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Record attendance
        attendanceRecords.put(studentID, studentName);
        attendanceArea.append("Student Name: " + studentName + ", Student ID: " + studentID + " - Present\n");

        // Save the updated records
        saveAttendanceRecords();

        // Clear input fields
        studentNameField.setText("");
        studentIDField.setText("");
    }

    private void saveAttendanceRecords() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String studentID : attendanceRecords.keySet()) {
                writer.write(studentID + "," + attendanceRecords.get(studentID));
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving attendance records", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAttendanceRecords() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    attendanceRecords.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            // If file not found, it's the first run
            if (!(e instanceof FileNotFoundException)) {
                JOptionPane.showMessageDialog(this, "Error loading attendance records", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void displayAttendanceRecords() {
        for (String studentID : attendanceRecords.keySet()) {
            String studentName = attendanceRecords.get(studentID);
            attendanceArea.append("Student Name: " + studentName + ", Student ID: " + studentID + " - Present\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new App();
            }
        });
    }
}
