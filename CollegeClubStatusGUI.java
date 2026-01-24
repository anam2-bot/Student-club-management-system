import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class CollegeClubStatusGUI extends JFrame {

    private HashMap<String, Student> students = new HashMap<>();

    private JComboBox<String> usnDropdown;
    private JLabel nameLabelDisplay, clubLabel, duesLabel, gradeLabel, usnLabel,
            paymentLabel, emailLabel, warningLabel;
    private JTextArea tasksArea;
    private JButton emailButton;

    public CollegeClubStatusGUI() {

        setTitle("🎓 College Club Status Portal");
        setSize(900, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        seedData();

        /* ===== HEADER ===== */
        JLabel header = new JLabel("🎓 College Club Status Portal", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        header.setBackground(new Color(33, 150, 243));
        header.setBorder(new EmptyBorder(15, 10, 15, 10));
        add(header, BorderLayout.NORTH);

        /* ===== CENTER PANEL ===== */
        JPanel center = new JPanel(new GridLayout(0, 1, 12, 12));
        center.setBorder(new EmptyBorder(20, 50, 20, 50));
        center.setBackground(Color.WHITE);

        JLabel selectUSNLabel = new JLabel("Select Student USN:");
        selectUSNLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        usnDropdown = new JComboBox<>();
        for (Student s : students.values())
            usnDropdown.addItem(s.usn);

        usnDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        usnDropdown.addActionListener(e -> checkStatus());

        nameLabelDisplay = new JLabel("Name: ");
        usnLabel = new JLabel("USN: ");
        clubLabel = new JLabel("Club: ");
        duesLabel = new JLabel("Dues: ");
        gradeLabel = new JLabel("Grade: ");
        paymentLabel = new JLabel("Payment Status: ");
        emailLabel = new JLabel("Email Status: ");

        Font infoFont = new Font("Segoe UI", Font.PLAIN, 15);
        nameLabelDisplay.setFont(infoFont);
        usnLabel.setFont(infoFont);
        clubLabel.setFont(infoFont);
        duesLabel.setFont(infoFont);
        gradeLabel.setFont(infoFont);
        paymentLabel.setFont(infoFont);
        emailLabel.setFont(infoFont);

        warningLabel = new JLabel("", JLabel.CENTER);
        warningLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        emailButton = new JButton("Send Email Reminder");
        emailButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        emailButton.setBackground(new Color(255, 152, 0));
        emailButton.setForeground(Color.WHITE);
        emailButton.setVisible(false);
        emailButton.addActionListener(e -> sendEmail());

        /* ===== PAID / UNPAID BUTTONS ===== */
        JButton paidBtn = new JButton("View Paid Students");
        JButton unpaidBtn = new JButton("View Unpaid Students");

        paidBtn.addActionListener(e -> showPaymentList(true));
        unpaidBtn.addActionListener(e -> showPaymentList(false));

        center.add(selectUSNLabel);
        center.add(usnDropdown);
        center.add(nameLabelDisplay);
        center.add(usnLabel);
        center.add(clubLabel);
        center.add(duesLabel);
        center.add(gradeLabel);
        center.add(paymentLabel);
        center.add(emailLabel);
        center.add(warningLabel);
        center.add(emailButton);
        center.add(paidBtn);
        center.add(unpaidBtn);

        add(center, BorderLayout.CENTER);

        /* ===== TASKS AREA ===== */
        tasksArea = new JTextArea();
        tasksArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tasksArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(tasksArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("📌 Pending Tasks"));
        add(scrollPane, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void seedData() {
        students.put("1NH22CS001", new Student("Neha", "Coding Club", true, "₹500 pending",
                new String[] { "Complete coding assignment", "Submit project report" }, 92, "1NH22CS001"));

        students.put("1NH22AI015", new Student("Sam", "AI Club", false, "No dues",
                new String[] { "Prepare AI presentation", "Attend AI workshop" }, 88, "1NH22AI015"));

        students.put("1NH22CS042", new Student("Tim", "Java Club", true, "Assignment pending",
                new String[] { "Submit Java lab record" }, 81, "1NH22CS042"));

        students.put("1NH22ME011", new Student("Jonny", "Robotics Club", false, "No dues",
                new String[] { "Finalize robotics model", "Test robot performance" }, 96, "1NH22ME011"));

        students.put("1NH22DS008", new Student("Samantha", "Design Club", true, "₹300 pending",
                new String[] { "Complete design presentation", "Submit design sketches" }, 90, "1NH22DS008"));
    }

    private void checkStatus() {
        Student s = students.get(usnDropdown.getSelectedItem());

        nameLabelDisplay.setText("Name: " + s.name);
        usnLabel.setText("USN: " + s.usn);
        clubLabel.setText("Club: " + s.club);
        duesLabel.setText("Dues: " + s.duesMessage);
        gradeLabel.setText("Grade: " + s.numericGrade);
        paymentLabel.setText("Payment Status: " + s.paymentStatus);
        emailLabel.setText("Email Status: " + s.emailStatus);

        tasksArea.setText("");
        for (String t : s.tasks)
            tasksArea.append("• " + t + "\n");

        if (s.hasDues) {
            warningLabel.setText("⚠ ACTION REQUIRED");
            warningLabel.setForeground(Color.RED);
            emailButton.setVisible(true);
        } else {
            warningLabel.setText("✔ ALL CLEAR");
            warningLabel.setForeground(new Color(0, 150, 0));
            emailButton.setVisible(false);
        }
    }

    private void sendEmail() {
        Student s = students.get(usnDropdown.getSelectedItem());
        s.emailStatus = "Email Sent";
        emailLabel.setText("Email Status: Email Sent");
        JOptionPane.showMessageDialog(this, "Email sent to " + s.name);
    }

    /* ===== SHOW PAID / UNPAID LIST ===== */
    private void showPaymentList(boolean paid) {
        JFrame frame = new JFrame(paid ? "Paid Students" : "Unpaid Students");
        frame.setSize(600, 400);

        String[] cols = { "Name", "USN", "Club", "Status" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        for (Student s : students.values()) {
            if (paid && !s.hasDues || !paid && s.hasDues) {
                model.addRow(new Object[] { s.name, s.usn, s.club, s.paymentStatus });
            }
        }

        frame.add(new JScrollPane(new JTable(model)));
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new CollegeClubStatusGUI();
    }
}

class Student {
    String name, club, duesMessage, usn, paymentStatus, emailStatus;
    boolean hasDues;
    int numericGrade;
    String[] tasks;

    Student(String n, String c, boolean d, String m, String[] t, int g, String u) {
        name = n;
        club = c;
        hasDues = d;
        duesMessage = m;
        tasks = t;
        numericGrade = g;
        usn = u;
        paymentStatus = d ? "Not Paid" : "Paid";
        emailStatus = d ? "Email Pending" : "No Action Needed";
    }
}
