package hms;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HostelManagementAndAdministrationSystem {

    static Scanner sc = new Scanner(System.in);

    // ============================================================
    // USER CLASS
    // ============================================================

    static class User {
        String username;
        String password;
        String ownerName;
        String hostelName;

        User(String username, String password, String ownerName, String hostelName) {
            this.username = username;
            this.password = password;
            this.ownerName = ownerName;
            this.hostelName = hostelName;
        }
    }

    // ============================================================
    // STUDENT CLASS
    // ============================================================

    static class Student {
        int studentId;
        String name;
        int age;
        String fatherName;
        String phone;
        String fatherPhone;
        String address;
        String idProof;

        int sharingType;
        int totalFee;
        int amountPaid;
        int balance;

        LocalDate joiningDate;
        boolean active;

        Student(int studentId, String name, int age,
                String fatherName, String phone, String fatherPhone,
                String address, String idProof,
                int sharingType, int totalFee, int amountPaid) {

            this.studentId = studentId;
            this.name = name;
            this.age = age;
            this.fatherName = fatherName;
            this.phone = phone;
            this.fatherPhone = fatherPhone;
            this.address = address;
            this.idProof = idProof;

            this.sharingType = sharingType;
            this.totalFee = totalFee;
            this.amountPaid = amountPaid;
            this.balance = totalFee - amountPaid;

            this.joiningDate = LocalDate.now();
            this.active = true;
        }

        void displayDetails() {

            System.out.println("-----------------------------------------------");
            System.out.println("Student ID       : " + studentId);
            System.out.println("Name             : " + name);
            System.out.println("Age              : " + age);
            System.out.println("Father Name      : " + fatherName);
            System.out.println("Phone Number     : " + phone);
            System.out.println("Father Phone     : " + fatherPhone);
            System.out.println("Address          : " + address);
            System.out.println("ID Proof         : " + idProof);
            System.out.println("Sharing Type     : " + sharingType + " Sharing");
            System.out.println("Total Fee        : Rs." + totalFee);
            System.out.println("Amount Paid      : Rs." + amountPaid);
            System.out.println("Outstanding      : Rs." + balance);
            System.out.println("Joining Date     : " + joiningDate);
            System.out.println("Status           : " + (active ? "ACTIVE" : "CHECKED OUT"));
            System.out.println("-----------------------------------------------");
        }
    }

    // ============================================================
    // EXPENSE CLASS
    // ============================================================

    static class Expense {

        String category;
        int amount;
        String description;
        LocalDate date;

        Expense(String category, int amount, String description) {
            this.category = category;
            this.amount = amount;
            this.description = description;
            this.date = LocalDate.now();
        }

        void displayExpense() {
            System.out.println(
                    date + " | " +
                    category + " | Rs." +
                    amount + " | " +
                    description
            );
        }
    }

    // ============================================================
    // ROOM CLASS
    // ============================================================

    static class RoomType {

        int sharingType;
        int numberOfRooms;
        int capacity;
        int occupied;
        int vacancy;
        int monthlyFee;

        RoomType(int sharingType, int numberOfRooms, int monthlyFee) {

            this.sharingType = sharingType;
            this.numberOfRooms = numberOfRooms;

            this.capacity = sharingType * numberOfRooms;
            this.occupied = 0;
            this.vacancy = capacity;

            this.monthlyFee = monthlyFee;
        }

        void display() {

            System.out.println(
                    sharingType + " Sharing | " +
                    "Rooms: " + numberOfRooms +
                    " | Capacity: " + capacity +
                    " | Occupied: " + occupied +
                    " | Vacancy: " + vacancy +
                    " | Fee: Rs." + monthlyFee
            );
        }
    }

    // ============================================================
    // MAIN DATA STRUCTURES
    // ============================================================

    static User user;

    static ArrayList<Student> students = new ArrayList<>();
    static ArrayList<Expense> expenses = new ArrayList<>();

    static Map<Integer, RoomType> rooms = new HashMap<>();

    static int nextStudentId = 1001;

    static int totalIncome = 0;
    static int totalExpenditure = 0;

    // ============================================================
    // MAIN METHOD
    // ============================================================

    public static void main(String[] args) {

        System.out.println();
        System.out.println("====================================================");
        System.out.println("     HOSTEL MANAGEMENT AND ADMINISTRATION SYSTEM");
        System.out.println("====================================================");

        startApplication();
    }

    // ============================================================
    // START APPLICATION
    // ============================================================

    static void startApplication() {

        while (true) {

            System.out.println();

            System.out.println("1. Sign Up");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:

                    signUp();

                    break;

                case 2:

                    if (user == null) {

                        System.out.println();
                        System.out.println(
                                "Please create an account first."
                        );

                    } else {

                    	if (login()) {
                    	    loadRoomsFromDatabase();
                    	    loadStudentsFromDatabase();
                    	    loadExpensesFromDatabase();
                    	    dashboard();
                    	}
                    }

                    break;

                case 3:

                    System.out.println(
                            "Thank you for using the system."
                    );

                    return;

                default:

                    System.out.println(
                            "Invalid choice."
                    );
            }
        }
    }
    
    static void loadStudentsFromDatabase() {

        StudentDAO dao = new StudentDAO();

        ArrayList<Student> databaseStudents =
                dao.getAllStudents();

        // Clear existing student data
        students.clear();

        // Reset values
        totalIncome = 0;
        nextStudentId = 1001;

        // Reset room occupancy before recalculating
        for (RoomType room : rooms.values()) {

            room.occupied = 0;
            room.vacancy = room.capacity;
        }

        for (Student student : databaseStudents) {

            students.add(student);

            // Calculate total income
            totalIncome += student.amountPaid;

            // Find the highest student ID
            if (student.studentId >= nextStudentId) {

                nextStudentId =
                        student.studentId + 1;
            }

            // Update room occupancy only for active students
            if (rooms.containsKey(student.sharingType)
                    && student.active) {

                RoomType room =
                        rooms.get(student.sharingType);

                room.occupied++;
                room.vacancy--;
            }
        }

        System.out.println();

        System.out.println(
                databaseStudents.size()
                + " student(s) loaded from MySQL."
        );
    }
    
    static void loadExpensesFromDatabase() {

        StudentDAO dao = new StudentDAO();

        ArrayList<Expense> databaseExpenses =
                dao.getAllExpenses();

        expenses.clear();

        totalExpenditure = 0;

        for (Expense expense : databaseExpenses) {

            expenses.add(expense);

            totalExpenditure += expense.amount;
        }

        System.out.println();

        System.out.println(
                databaseExpenses.size()
                + " expense(s) loaded from MySQL."
        );
    }
    static void loadRoomsFromDatabase() {

        StudentDAO dao = new StudentDAO();

        ArrayList<RoomType> databaseRooms =
                dao.getAllRoomTypes();

        rooms.clear();

        for (RoomType room : databaseRooms) {

            rooms.put(
                    room.sharingType,
                    room
            );
        }

        System.out.println();

        System.out.println(
                databaseRooms.size()
                + " room type(s) loaded from MySQL."
        );
    }

    // ============================================================
    // SIGN UP
    // ============================================================

    static void signUp() {

        System.out.println();
        System.out.println("====================================================");
        System.out.println("                    SIGN UP");
        System.out.println("====================================================");

        String hostelName = readString("Enter Hostel Name: ");
        String ownerName = readString("Enter Owner Name: ");
        String username = readString("Create Username: ");

        if (user != null && user.username.equalsIgnoreCase(username)) {
            System.out.println("Username already exists.");
            return;
        }

        String password = readString("Create Password: ");
        String confirmPassword = readString("Confirm Password: ");

        if (!password.equals(confirmPassword)) {

            System.out.println("Password does not match.");
            return;
        }

        user = new User(username, password, ownerName, hostelName);

        System.out.println();
        System.out.println("Account created successfully.");

        configureHostel();
    }

    // ============================================================
    // HOSTEL CONFIGURATION
    // ============================================================

    static void configureHostel() {

        System.out.println();
        System.out.println("====================================================");
        System.out.println("               HOSTEL CONFIGURATION");
        System.out.println("====================================================");

        rooms.clear();

        System.out.println();
        System.out.println("Enter number of rooms and monthly fee");
        System.out.println("for each sharing type.");
        System.out.println();

        configureRoomType(4);
        configureRoomType(3);
        configureRoomType(2);
        configureRoomType(1);

        System.out.println();
        System.out.println("Hostel configuration completed successfully.");
    }

    static void configureRoomType(int sharingType) {

        int numberOfRooms =
                readNonNegativeInt(
                        "Number of " + sharingType + "-sharing rooms: "
                );

        int fee =
                readNonNegativeInt(
                        "Monthly fee for " + sharingType + "-sharing: Rs."
                );

        RoomType room = new RoomType(
                sharingType,
                numberOfRooms,
                fee
        );

        StudentDAO dao = new StudentDAO();

        boolean saved =
                dao.saveRoomType(
                        sharingType,
                        numberOfRooms,
                        fee
                );

        if (!saved) {
            System.out.println(
                    "Room configuration could not be saved."
            );
            return;
        }

        rooms.put(sharingType, room);

        System.out.println(
                sharingType + "-sharing room configuration saved."
        );
    }
    // ============================================================
    // LOGIN
    // ============================================================

    static boolean login() {

        System.out.println();
        System.out.println("====================================================");
        System.out.println("                     LOGIN");
        System.out.println("====================================================");

        String username = readString("Username: ");
        String password = readString("Password: ");

        if (user.username.equals(username)
                && user.password.equals(password)) {

            System.out.println();
            System.out.println("Login successful.");
            return true;
        }

        System.out.println();
        System.out.println("Invalid username or password.");
        return false;
    }

    // ============================================================
    // DASHBOARD
    // ============================================================

    static void dashboard() {

        while (true) {

            System.out.println();
            System.out.println("====================================================");
            System.out.println("                    DASHBOARD");
            System.out.println("====================================================");

            displayDashboardSummary();

            System.out.println();
            System.out.println("1. New Student Joining");
            System.out.println("2. View Vacancies");
            System.out.println("3. View Student Details");
            System.out.println("4. Fee Payment");
            System.out.println("5. Outstanding Amounts");
            System.out.println("6. Income Details");
            System.out.println("7. Expenditure Details");
            System.out.println("8. Student Checkout");
            System.out.println("9. View Room Details");
            System.out.println("10. Logout");

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    newStudentJoining();
                    break;

                case 2:
                    displayVacancies();
                    break;

                case 3:
                    viewStudentDetails();
                    break;

                case 4:
                    feePayment();
                    break;

                case 5:
                    outstandingAmounts();
                    break;

                case 6:
                    displayIncome();
                    break;

                case 7:
                    expenditureMenu();
                    break;

                case 8:
                    checkoutStudent();
                    break;

                case 9:
                    displayRoomDetails();
                    break;

                case 10:
                    System.out.println("Logged out successfully.");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // ============================================================
    // DASHBOARD SUMMARY
    // ============================================================

    static void displayDashboardSummary() {

        int activeStudents = getActiveStudentCount();

        int totalCapacity = getTotalCapacity();

        int totalVacancy = getTotalVacancy();

        System.out.println(
                "Hostel Name      : " + user.hostelName
        );

        System.out.println(
                "Owner Name       : " + user.ownerName
        );

        System.out.println(
                "-----------------------------------------------"
        );

        System.out.println(
                "Total Capacity   : " + totalCapacity
        );

        System.out.println(
                "Existing Students: " + activeStudents
        );

        System.out.println(
                "Total Vacancies  : " + totalVacancy
        );

        System.out.println(
                "Total Income     : Rs." + totalIncome
        );

        System.out.println(
                "Total Expenditure: Rs." + totalExpenditure
        );

        System.out.println(
                "Net Balance      : Rs."
                + (totalIncome - totalExpenditure)
        );

        System.out.println(
                "Outstanding      : Rs." + getTotalOutstanding()
        );
    }

    // ============================================================
    // NEW STUDENT JOINING
    // ============================================================

    static void newStudentJoining() {

        System.out.println();
        System.out.println("====================================================");
        System.out.println("                NEW STUDENT JOINING");
        System.out.println("====================================================");

        displayVacancies();

        int sharingType =
                readInt("Choose sharing type (1/2/3/4): ");

        if (!rooms.containsKey(sharingType)) {

            System.out.println("Invalid sharing type.");
            return;
        }

        RoomType room = rooms.get(sharingType);

        if (room.vacancy <= 0) {

            System.out.println();
            System.out.println(
                    "No vacancy available for "
                    + sharingType
                    + "-sharing."
            );

            return;
        }

        System.out.println();
        System.out.println("Room available.");
        System.out.println("Monthly Fee: Rs." + room.monthlyFee);

        String name = readString("Student Name: ");

        int age = readInt("Age: ");

        String fatherName =
                readString("Father Name: ");

        String phone =
                readString("Student Phone Number: ");

        if (findStudentByPhone(phone) != null) {

            System.out.println(
                    "A student with this phone number already exists."
            );

            return;
        }

        String fatherPhone =
                readString("Father Phone Number: ");

        String address =
                readString("Address: ");

        String idProof =
                readString("ID Proof Number: ");

        int amountPaid =
                readNonNegativeInt("Amount Paid: Rs.");

        if (amountPaid > room.monthlyFee) {

            System.out.println(
                    "Amount paid cannot be greater than the total fee."
            );

            return;
        }

        int balance =
                room.monthlyFee - amountPaid;

        Student student =
                new Student(
                        nextStudentId,
                        name,
                        age,
                        fatherName,
                        phone,
                        fatherPhone,
                        address,
                        idProof,
                        sharingType,
                        room.monthlyFee,
                        amountPaid
                );

        // ==========================================
        // SAVE STUDENT TO MYSQL
        // ==========================================

        StudentDAO dao = new StudentDAO();

        boolean databaseSaved = dao.addStudent(student);

        if (!databaseSaved) {

            System.out.println();
            System.out.println(
                    "Student registration failed because "
                    + "the database could not save the record."
            );

            return;
        }

        // ==========================================
        // UPDATE APPLICATION DATA
        // ==========================================

        students.add(student);

        nextStudentId++;

        room.occupied++;
        room.vacancy--;

        totalIncome += amountPaid;

        // ==========================================
        // SUCCESS MESSAGE
        // ==========================================

        System.out.println();

        System.out.println("====================================================");
        System.out.println("       STUDENT REGISTRATION SUCCESSFUL");
        System.out.println("====================================================");

        System.out.println(
                "Student ID       : " + student.studentId
        );

        System.out.println(
                "Name             : " + student.name
        );

        System.out.println(
                "Sharing          : " + sharingType + " Sharing"
        );

        System.out.println(
                "Total Fee        : Rs." + room.monthlyFee
        );

        System.out.println(
                "Amount Paid      : Rs." + amountPaid
        );

        System.out.println(
                "Outstanding      : Rs." + balance
        );

        System.out.println(
                "Database Status  : SAVED"
        );

        System.out.println();
    }

    // ============================================================
    // DISPLAY VACANCIES
    // ============================================================

    static void displayVacancies() {

        System.out.println();
        System.out.println("====================================================");
        System.out.println("                 VACANCY DETAILS");
        System.out.println("====================================================");

        for (int type = 4; type >= 1; type--) {

            RoomType room = rooms.get(type);

            if (room != null) {

                System.out.println(
                        type + "-Sharing : "
                        + room.vacancy
                        + " vacancies"
                );
            }
        }

        System.out.println("-----------------------------------------------");
        System.out.println(
                "Total Vacancies: "
                + getTotalVacancy()
        );
    }

    // ============================================================
    // VIEW STUDENT DETAILS
    // ============================================================

    static void viewStudentDetails() {

        System.out.println();
        System.out.println("====================================================");
        System.out.println("                STUDENT DETAILS");
        System.out.println("====================================================");

        System.out.println("1. View All Students");
        System.out.println("2. Search by Phone");
        System.out.println("3. Search by Student ID");

        int choice = readInt("Enter choice: ");

        if (choice == 1) {

            if (students.isEmpty()) {

                System.out.println("No students registered.");
                return;
            }

            for (Student student : students) {
                student.displayDetails();
            }

        } else if (choice == 2) {

            String phone =
                    readString("Enter phone number: ");

            Student student =
                    findStudentByPhone(phone);

            if (student != null) {
                student.displayDetails();
            } else {
                System.out.println("Student not found.");
            }

        } else if (choice == 3) {

            int id =
                    readInt("Enter Student ID: ");

            Student student =
                    findStudentById(id);

            if (student != null) {
                student.displayDetails();
            } else {
                System.out.println("Student not found.");
            }

        } else {

            System.out.println("Invalid choice.");
        }
    }

    // ============================================================
    // FEE PAYMENT
    // ============================================================

    static void feePayment() {

        System.out.println();
        System.out.println("====================================================");
        System.out.println("                   FEE PAYMENT");
        System.out.println("====================================================");

        String phone =
                readString("Enter Student Phone Number: ");

        Student student =
                findStudentByPhone(phone);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        if (!student.active) {
            System.out.println(
                    "This student has already checked out."
            );
            return;
        }

        System.out.println("Student Name : " + student.name);
        System.out.println("Total Fee    : Rs." + student.totalFee);
        System.out.println("Paid         : Rs." + student.amountPaid);
        System.out.println("Outstanding  : Rs." + student.balance);

        if (student.balance == 0) {
            System.out.println("No outstanding amount.");
            return;
        }

        int amount =
                readNonNegativeInt(
                        "Enter payment amount: Rs."
                );

        if (amount <= 0) {
            System.out.println("Invalid payment amount.");
            return;
        }

        if (amount > student.balance) {
            System.out.println(
                    "Payment cannot be greater than outstanding amount."
            );
            return;
        }

        // Create DAO object
        StudentDAO dao = new StudentDAO();

        // Save payment to MySQL
        boolean paymentSaved =
                dao.recordPayment(student.studentId, amount);

        if (!paymentSaved) {
            System.out.println(
                    "Payment failed. Database was not updated."
            );
            return;
        }

        // Update in-memory object only after database success
        student.amountPaid += amount;
        student.balance -= amount;

        totalIncome += amount;

        System.out.println();
        System.out.println("====================================================");
        System.out.println("             PAYMENT SUCCESSFUL");
        System.out.println("====================================================");
        System.out.println("Student Name      : " + student.name);
        System.out.println("Payment Amount    : Rs." + amount);
        System.out.println("Total Paid        : Rs." + student.amountPaid);
        System.out.println("Remaining Balance : Rs." + student.balance);
        System.out.println("Database Status   : SAVED");
        System.out.println("====================================================");
    }

    // ============================================================
    // OUTSTANDING AMOUNTS
    // ============================================================

    static void outstandingAmounts() {

        System.out.println();
        System.out.println("====================================================");
        System.out.println("              OUTSTANDING AMOUNTS");
        System.out.println("====================================================");

        boolean found = false;

        for (Student student : students) {

            if (student.active && student.balance > 0) {

                found = true;

                System.out.println(
                        "ID: " + student.studentId
                        + " | Name: " + student.name
                        + " | Phone: " + student.phone
                        + " | Balance: Rs." + student.balance
                );
            }
        }

        if (!found) {

            System.out.println(
                    "No outstanding amounts."
            );
        }

        System.out.println("-----------------------------------------------");
        System.out.println(
                "Total Outstanding: Rs."
                + getTotalOutstanding()
        );
    }

    // ============================================================
    // INCOME
    // ============================================================

    static void displayIncome() {

        System.out.println();
        System.out.println("====================================================");
        System.out.println("                  INCOME DETAILS");
        System.out.println("====================================================");

        System.out.println(
                "Total Income: Rs."
                + totalIncome
        );
    }

    // ============================================================
    // EXPENDITURE MENU
    // ============================================================

    static void expenditureMenu() {

        while (true) {

            System.out.println();
            System.out.println("====================================================");
            System.out.println("               EXPENDITURE MANAGEMENT");
            System.out.println("====================================================");

            System.out.println("1. Add Expenditure");
            System.out.println("2. View Expenditures");
            System.out.println("3. Return to Dashboard");

            int choice =
                    readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    addExpense();
                    break;

                case 2:
                    displayExpenses();
                    break;

                case 3:
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // ============================================================
    // ADD EXPENSE
    // ============================================================

    static void addExpense() {

        System.out.println();

        System.out.println("Select Expense Category:");

        System.out.println("1. Groceries");
        System.out.println("2. Vegetables");
        System.out.println("3. Electricity Bill");
        System.out.println("4. Water Bill");
        System.out.println("5. Maintenance");
        System.out.println("6. Salaries");
        System.out.println("7. Others");

        int choice =
                readInt("Enter category: ");

        String category;

        switch (choice) {

            case 1:
                category = "Groceries";
                break;

            case 2:
                category = "Vegetables";
                break;

            case 3:
                category = "Electricity Bill";
                break;

            case 4:
                category = "Water Bill";
                break;

            case 5:
                category = "Maintenance";
                break;

            case 6:
                category = "Salaries";
                break;

            case 7:
                category = "Others";
                break;

            default:
                System.out.println("Invalid category.");
                return;
        }

        int amount =
                readNonNegativeInt(
                        "Enter amount: Rs."
                );

        if (amount <= 0) {
            System.out.println(
                    "Expense amount must be greater than zero."
            );
            return;
        }

        String description =
                readString("Enter description: ");

        // Create DAO
        StudentDAO dao = new StudentDAO();

        // Save expense to MySQL
        boolean expenseSaved =
                dao.addExpense(
                        category,
                        amount,
                        description
                );

        if (!expenseSaved) {

            System.out.println(
                    "Expense could not be saved."
            );

            return;
        }

        // Update Java data only after database success
        Expense expense =
                new Expense(
                        category,
                        amount,
                        description
                );

        expenses.add(expense);

        totalExpenditure += amount;

        System.out.println();
        System.out.println(
                "Expenditure added successfully."
        );

        System.out.println(
                "Database Status: SAVED"
        );
    }
    // ============================================================
    // DISPLAY EXPENSES
    // ============================================================

    static void displayExpenses() {

        System.out.println();
        System.out.println("====================================================");
        System.out.println("                EXPENDITURE DETAILS");
        System.out.println("====================================================");

        if (expenses.isEmpty()) {

            System.out.println(
                    "No expenditure records available."
            );

            return;
        }

        for (Expense expense : expenses) {

            expense.displayExpense();
        }

        System.out.println("-----------------------------------------------");

        System.out.println(
                "Total Expenditure: Rs."
                + totalExpenditure
        );
    }

    // ============================================================
    // CHECKOUT
    // ============================================================

    static void checkoutStudent() {

        System.out.println();
        System.out.println("====================================================");
        System.out.println("                  STUDENT CHECKOUT");
        System.out.println("====================================================");

        String phone =
                readString("Enter Student Phone Number: ");

        Student student =
                findStudentByPhone(phone);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        if (!student.active) {
            System.out.println(
                    "Student has already checked out."
            );
            return;
        }

        System.out.println(
                "Student Name: "
                + student.name
        );

        System.out.println(
                "Outstanding Amount: Rs."
                + student.balance
        );

        if (student.balance > 0) {

            System.out.println();
            System.out.println(
                    "Checkout cannot be completed."
            );

            System.out.println(
                    "Please clear the outstanding amount first."
            );

            return;
        }

        System.out.println();
        System.out.println(
                "No outstanding balance."
        );

        String confirmation =
                readString(
                        "Confirm checkout? (yes/no): "
                );

        if (confirmation.equalsIgnoreCase("yes")) {

            // Create DAO object
            StudentDAO dao = new StudentDAO();

            // Update checkout status in MySQL
            boolean checkoutSaved =
                    dao.checkoutStudent(student.studentId);

            if (!checkoutSaved) {

                System.out.println();
                System.out.println(
                        "Checkout failed. Database was not updated."
                );

                return;
            }

            // Update Java object
            student.active = false;

            // Restore room vacancy
            RoomType room =
                    rooms.get(student.sharingType);

            if (room != null) {

                if (room.occupied > 0) {
                    room.occupied--;
                }

                room.vacancy++;
            }

            System.out.println();
            System.out.println(
                    "===================================================="
            );

            System.out.println(
                    "       STUDENT CHECKOUT COMPLETED"
            );

            System.out.println(
                    "===================================================="
            );

            System.out.println(
                    "Student Name   : " + student.name
            );

            System.out.println(
                    "Student ID     : " + student.studentId
            );

            System.out.println(
                    "Room Type      : "
                    + student.sharingType
                    + " Sharing"
            );

            System.out.println(
                    "Database Status: UPDATED"
            );

            System.out.println(
                    "Room Vacancy   : RESTORED"
            );

            System.out.println(
                    "===================================================="
            );

        } else {

            System.out.println(
                    "Checkout cancelled."
            );
        }
    }

    // ============================================================
    // ROOM DETAILS
    // ============================================================

    static void displayRoomDetails() {

        System.out.println();
        System.out.println("====================================================");
        System.out.println("                   ROOM DETAILS");
        System.out.println("====================================================");

        for (int type = 4; type >= 1; type--) {

            RoomType room = rooms.get(type);

            if (room != null) {

                room.display();
            }
        }

        System.out.println("-----------------------------------------------");

        System.out.println(
                "Total Capacity: "
                + getTotalCapacity()
        );

        System.out.println(
                "Total Occupied: "
                + getActiveStudentCount()
        );

        System.out.println(
                "Total Vacancy: "
                + getTotalVacancy()
        );
    }

    // ============================================================
    // FIND STUDENT BY PHONE
    // ============================================================

    static Student findStudentByPhone(String phone) {

        for (Student student : students) {

            if (student.phone.equals(phone)) {

                return student;
            }
        }

        return null;
    }

    // ============================================================
    // FIND STUDENT BY ID
    // ============================================================

    static Student findStudentById(int id) {

        for (Student student : students) {

            if (student.studentId == id) {

                return student;
            }
        }

        return null;
    }

    // ============================================================
    // ACTIVE STUDENT COUNT
    // ============================================================

    static int getActiveStudentCount() {

        int count = 0;

        for (Student student : students) {

            if (student.active) {

                count++;
            }
        }

        return count;
    }

    // ============================================================
    // TOTAL CAPACITY
    // ============================================================

    static int getTotalCapacity() {

        int capacity = 0;

        for (RoomType room : rooms.values()) {

            capacity += room.capacity;
        }

        return capacity;
    }

    // ============================================================
    // TOTAL VACANCY
    // ============================================================

    static int getTotalVacancy() {

        int vacancy = 0;

        for (RoomType room : rooms.values()) {

            vacancy += room.vacancy;
        }

        return vacancy;
    }

    // ============================================================
    // TOTAL OUTSTANDING
    // ============================================================

    static int getTotalOutstanding() {

        int outstanding = 0;

        for (Student student : students) {

            if (student.active) {

                outstanding += student.balance;
            }
        }

        return outstanding;
    }

    // ============================================================
    // INPUT METHODS
    // ============================================================

    static String readString(String message) {

        System.out.print(message);

        return sc.nextLine().trim();
    }

    static int readInt(String message) {

        while (true) {

            try {

                System.out.print(message);

                String input = sc.nextLine().trim();

                return Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }

    static int readNonNegativeInt(String message) {

        while (true) {

            int value = readInt(message);

            if (value >= 0) {

                return value;
            }

            System.out.println(
                    "Value cannot be negative."
            );
        }
    }
}