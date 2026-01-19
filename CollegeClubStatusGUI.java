import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CollegeClubStatusGUI extends JFrame {

    private HashMap<String, Student> students = new HashMap<>();
    private JTextField nameField;
    private JLabel clubLabel, duesLabel, gradeLabel, warningLabel;
    private JTextArea tasksArea;

    public CollegeClubStatusGUI() {

        setTitle("🎓 College Club Status Portal");
        setSize(650, 560);
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
        JPanel center = new JPanel(new GridLayout(8, 1, 12, 12));
        center.setBorder(new EmptyBorder(20, 50, 20, 50));
        center.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel("Enter Student Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        nameField = new JTextField();
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JButton button = new JButton("Check Status");
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setBackground(new Color(76, 175, 80));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);

        button.addActionListener(e -> checkStatus());

        clubLabel = new JLabel("Club: ");
        duesLabel = new JLabel("Dues: ");
        gradeLabel = new JLabel("Grade: ");

        Font infoFont = new Font("Segoe UI", Font.PLAIN, 15);
        clubLabel.setFont(infoFont);
        duesLabel.setFont(infoFont);
        gradeLabel.setFont(infoFont);

        warningLabel = new JLabel("", JLabel.CENTER);
        warningLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        center.add(nameLabel);
        center.add(nameField);
        center.add(button);
        center.add(clubLabel);
        center.add(duesLabel);
        center.add(gradeLabel);
        center.add(warningLabel);

        add(center, BorderLayout.CENTER);

        /* ===== TASKS AREA ===== */
        tasksArea = new JTextArea();
        tasksArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tasksArea.setEditable(false);
        tasksArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        tasksArea.setBackground(new Color(245, 245, 245));

        JScrollPane scrollPane = new JScrollPane(tasksArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("📌 Pending Tasks"));
        add(scrollPane, BorderLayout.SOUTH);

        setVisible(true);
    }

    /* ===== DATA ===== */
    private void seedData() {

        ArrayList<String> nehaTasks = new ArrayList<>();
        nehaTasks.add("Complete coding assignment");
        nehaTasks.add("Submit project report");
        students.put("neha",
                new Student("Neha", "Coding Club", true, "₹500 pending", nehaTasks, 92));

        ArrayList<String> samTasks = new ArrayList<>();
        samTasks.add("Prepare AI presentation");
        samTasks.add("Attend AI workshop");
        students.put("sam",
                new Student("Sam", "AI Club", false, "No dues", samTasks, 88));

        ArrayList<String> timTasks = new ArrayList<>();
        timTasks.add("Submit Java lab record");
        students.put("tim",
                new Student("Tim", "Java Club", true, "Assignment pending", timTasks, 81));

        ArrayList<String> jonnyTasks = new ArrayList<>();
        jonnyTasks.add("Finalize robotics model");
        jonnyTasks.add("Test robot performance");
        students.put("jonny",
                new Student("Jonny", "Robotics Club", false, "No dues", jonnyTasks, 96));

        ArrayList<String> samanthaTasks = new ArrayList<>();
        samanthaTasks.add("Complete design presentation");
        samanthaTasks.add("Submit design sketches");
        students.put("samantha",
                new Student("Samantha", "Design Club", true, "₹300 pending", samanthaTasks, 90));
    }

    /* ===== MAIN LOGIC ===== */
    private void checkStatus() {

        String name = nameField.getText().trim().toLowerCase();

        if (!students.containsKey(name)) {
            JOptionPane.showMessageDialog(this, "❌ Student not found");
            warningLabel.setText("");
            tasksArea.setText("");
            speak("Student record not found");
            return;
        }

        Student s = students.get(name);

        clubLabel.setText("Club: " + s.club);
        duesLabel.setText("Dues: " + s.duesMessage);
        gradeLabel.setText("Grade: " + s.numericGrade);

        tasksArea.setText("");
        for (String task : s.pendingTasks) {
            tasksArea.append("• " + task + "\n");
        }

        String voiceMessage = "Student " + s.name +
                ". Club " + s.club +
                ". Grade " + s.numericGrade + ".";

        if (s.hasDues) {
            warningLabel.setText("⚠ ACTION REQUIRED");
            warningLabel.setForeground(Color.RED);
            voiceMessage += " Action required. Pending dues.";
        } else {
            warningLabel.setText("✔ ALL CLEAR");
            warningLabel.setForeground(new Color(0, 150, 0));
            voiceMessage += " All clear. No pending dues.";
        }

        speak(voiceMessage);
    }

    /* ===== VOICE OUTPUT (ANTIGRAVITY SAFE) ===== */
    private void speak(String text) {
        try {
            String command = "PowerShell -Command \"Add-Type –AssemblyName System.Speech; " +
                    "$speak = New-Object System.Speech.Synthesis.SpeechSynthesizer; " +
                    "$speak.Speak('" + text + "');\"";
            Runtime.getRuntime().exec(command);
        } catch (Exception e) {
            System.out.println("Voice output error");
        }
    }

    public static void main(String[] args) {
        new CollegeClubStatusGUI();
    }
}

/* ===== STUDENT CLASS (UNCHANGED) ===== */
class Student {
    String name, club, duesMessage;
    boolean hasDues;
    ArrayList<String> pendingTasks;
    int numericGrade;

    Student(String n, String c, boolean d, String m,
            ArrayList<String> t, int g) {
        name = n;
        club = c;
        hasDues = d;
        duesMessage = m;
        pendingTasks = t;
        numericGrade = g;
    }
}
