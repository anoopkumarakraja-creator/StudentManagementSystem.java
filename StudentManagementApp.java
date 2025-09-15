/******************************************************************************

                            Online Java Compiler.
                Code, Compile, Run and Debug java program online.
Write your code in this editor and press "Run" button to execute it.

*******************************************************************************/

import java.io.*;
import java.util.*;

class Student implements Serializable {
    private String name;
    private int rollNumber;
    private String grade;

    public Student(String name, int rollNumber, String grade) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public String getGrade() {
        return grade;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Roll No: " + rollNumber + " | Name: " + name + " | Grade: " + grade;
    }
}

class StudentManagementSystem {
    private List<Student> students;
    private static final String FILE_NAME = "students.dat";

    public StudentManagementSystem() {
        students = new ArrayList<>();
        loadStudents();
    }

    public void addStudent(Student student) {
        students.add(student);
        saveStudents();
    }

    public void removeStudent(int rollNumber) {
        students.removeIf(s -> s.getRollNumber() == rollNumber);
        saveStudents();
    }

    public Student searchStudent(int rollNumber) {
        for (Student s : students) {
            if (s.getRollNumber() == rollNumber) {
                return s;
            }
        }
        return null;
    }

    public void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            for (Student s : students) {
                System.out.println(s);
            }
        }
    }

    private void saveStudents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(students);
        } catch (IOException e) {
            System.out.println("Error saving students: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadStudents() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            students = (List<Student>) ois.readObject();
        } catch (FileNotFoundException e) {
            students = new ArrayList<>(); 
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
    }
}

public class StudentManagementApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentManagementSystem sms = new StudentManagementSystem();

    public static void main(String[] args) {
        int choice;
        do {
            showMenu();
            choice = getValidInteger("Enter your choice: ");
            switch (choice) {
                case 1 -> addStudent();
                case 2 -> editStudent();
                case 3 -> removeStudent();
                case 4 -> searchStudent();
                case 5 -> sms.displayAllStudents();
                case 6 -> System.out.println("Exiting... Goodbye!");
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 6);
    }

    private static void showMenu() {
        System.out.println("\n--- Student Management System ---");
        System.out.println("1. Add Student");
        System.out.println("2. Edit Student");
        System.out.println("3. Remove Student");
        System.out.println("4. Search Student");
        System.out.println("5. Display All Students");
        System.out.println("6. Exit");
    }

    private static void addStudent() {
        String name = getValidString("Enter student name: ");
        int rollNumber = getValidInteger("Enter roll number: ");
        String grade = getValidString("Enter grade: ");

        Student existing = sms.searchStudent(rollNumber);
        if (existing != null) {
            System.out.println("Student with this roll number already exists!");
            return;
        }
        sms.addStudent(new Student(name, rollNumber, grade));
        System.out.println("Student added successfully.");
    }

    private static void editStudent() {
        int rollNumber = getValidInteger("Enter roll number of student to edit: ");
        Student student = sms.searchStudent(rollNumber);
        if (student != null) {
            String newName = getValidString("Enter new name: ");
            String newGrade = getValidString("Enter new grade: ");
            student.setName(newName);
            student.setGrade(newGrade);
            System.out.println("Student updated successfully.");
        } else {
            System.out.println("Student not found.");
        }
    }

    private static void removeStudent() {
        int rollNumber = getValidInteger("Enter roll number to remove: ");
        Student student = sms.searchStudent(rollNumber);
        if (student != null) {
            sms.removeStudent(rollNumber);
            System.out.println("Student removed successfully.");
        } else {
            System.out.println("Student not found.");
        }
    }

    private static void searchStudent() {
        int rollNumber = getValidInteger("Enter roll number to search: ");
        Student student = sms.searchStudent(rollNumber);
        if (student != null) {
            System.out.println(student);
        } else {
            System.out.println("Student not found.");
        }
    }

    private static int getValidInteger(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    private static String getValidString(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("This field cannot be empty.");
            }
        } while (input.isEmpty());
        return input;
    }
}
