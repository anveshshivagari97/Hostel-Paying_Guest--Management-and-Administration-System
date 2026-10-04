package hms;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class StudentDAO {

	public boolean addStudent(
	        HostelManagementAndAdministrationSystem.Student student) {
        String sql = "INSERT INTO students "
                + "(student_id, name, age, father_name, phone, father_phone, "
                + "address, id_proof, sharing_type, total_fee, amount_paid, "
                + "balance, joining_date, active) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, student.studentId);
            ps.setString(2, student.name);
            ps.setInt(3, student.age);
            ps.setString(4, student.fatherName);
            ps.setString(5, student.phone);
            ps.setString(6, student.fatherPhone);
            ps.setString(7, student.address);
            ps.setString(8, student.idProof);
            ps.setInt(9, student.sharingType);
            ps.setInt(10, student.totalFee);
            ps.setInt(11, student.amountPaid);
            ps.setInt(12, student.balance);
            ps.setDate(13, java.sql.Date.valueOf(student.joiningDate));
            ps.setBoolean(14, student.active);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student added to database successfully!");
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
	public java.util.ArrayList<HostelManagementAndAdministrationSystem.Student> getAllStudents() {

	    java.util.ArrayList<HostelManagementAndAdministrationSystem.Student> studentList =
	            new java.util.ArrayList<>();

	    String sql = "SELECT * FROM students";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         java.sql.ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {

	            HostelManagementAndAdministrationSystem.Student student =
	                    new HostelManagementAndAdministrationSystem.Student(
	                            rs.getInt("student_id"),
	                            rs.getString("name"),
	                            rs.getInt("age"),
	                            rs.getString("father_name"),
	                            rs.getString("phone"),
	                            rs.getString("father_phone"),
	                            rs.getString("address"),
	                            rs.getString("id_proof"),
	                            rs.getInt("sharing_type"),
	                            rs.getInt("total_fee"),
	                            rs.getInt("amount_paid")
	                    );

	            // Use the actual values stored in MySQL
	            student.balance = rs.getInt("balance");
	            student.joiningDate =
	                    rs.getDate("joining_date").toLocalDate();
	            student.active =
	                    rs.getBoolean("active");

	            studentList.add(student);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return studentList;
	}
	public boolean recordPayment(int studentId, int amount) {

	    String insertPayment =
	            "INSERT INTO payments (student_id, amount, payment_date) "
	            + "VALUES (?, ?, ?)";

	    String updateStudent =
	            "UPDATE students "
	            + "SET amount_paid = amount_paid + ?, "
	            + "balance = balance - ? "
	            + "WHERE student_id = ?";

	    Connection con = null;

	    try {
	        con = DBConnection.getConnection();

	        // Start transaction
	        con.setAutoCommit(false);

	        // 1. Insert payment history
	        try (PreparedStatement ps1 =
	                     con.prepareStatement(insertPayment)) {

	            ps1.setInt(1, studentId);
	            ps1.setInt(2, amount);
	            ps1.setDate(3,
	                    java.sql.Date.valueOf(
	                            java.time.LocalDate.now()
	                    ));

	            ps1.executeUpdate();
	        }

	        // 2. Update student's paid amount and balance
	        try (PreparedStatement ps2 =
	                     con.prepareStatement(updateStudent)) {

	            ps2.setInt(1, amount);
	            ps2.setInt(2, amount);
	            ps2.setInt(3, studentId);

	            ps2.executeUpdate();
	        }

	        // Save both operations
	        con.commit();

	        System.out.println("Payment saved to MySQL successfully!");

	        return true;

	    } catch (Exception e) {

	        // Undo changes if anything goes wrong
	        if (con != null) {
	            try {
	                con.rollback();
	            } catch (Exception rollbackException) {
	                rollbackException.printStackTrace();
	            }
	        }

	        e.printStackTrace();
	        return false;

	    } finally {

	        if (con != null) {
	            try {
	                con.close();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
	public boolean checkoutStudent(int studentId) {

	    String sql =
	            "UPDATE students "
	            + "SET active = false "
	            + "WHERE student_id = ?";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, studentId);

	        int rows = ps.executeUpdate();

	        if (rows > 0) {
	            System.out.println(
	                    "Student checkout status saved to MySQL."
	            );
	            return true;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return false;
	}
	public boolean addExpense(String category, int amount, String description) {

	    String sql =
	            "INSERT INTO expenses "
	            + "(category, amount, description, expense_date) "
	            + "VALUES (?, ?, ?, ?)";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, category);
	        ps.setInt(2, amount);
	        ps.setString(3, description);
	        ps.setDate(
	                4,
	                java.sql.Date.valueOf(
	                        java.time.LocalDate.now()
	                )
	        );

	        int rows = ps.executeUpdate();

	        if (rows > 0) {
	            System.out.println(
	                    "Expense saved to MySQL successfully!"
	            );
	            return true;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return false;
	}
	public java.util.ArrayList<
    HostelManagementAndAdministrationSystem.Expense>
    getAllExpenses() {

java.util.ArrayList<
        HostelManagementAndAdministrationSystem.Expense> expenseList =
        new java.util.ArrayList<>();

String sql = "SELECT * FROM expenses";

try (Connection con = DBConnection.getConnection();
     PreparedStatement ps = con.prepareStatement(sql);
     java.sql.ResultSet rs = ps.executeQuery()) {

    while (rs.next()) {

        HostelManagementAndAdministrationSystem.Expense expense =
                new HostelManagementAndAdministrationSystem.Expense(
                        rs.getString("category"),
                        rs.getInt("amount"),
                        rs.getString("description")
                );

        expense.date =
                rs.getDate("expense_date").toLocalDate();

        expenseList.add(expense);
    }

} catch (Exception e) {
    e.printStackTrace();
}

return expenseList;
}
	public boolean saveRoomType(
	        int sharingType,
	        int numberOfRooms,
	        int monthlyFee) {

	    String sql =
	            "INSERT INTO rooms "
	            + "(sharing_type, number_of_rooms, capacity, occupied, vacancy, monthly_fee) "
	            + "VALUES (?, ?, ?, ?, ?, ?) "
	            + "ON DUPLICATE KEY UPDATE "
	            + "number_of_rooms = VALUES(number_of_rooms), "
	            + "capacity = VALUES(capacity), "
	            + "monthly_fee = VALUES(monthly_fee), "
	            + "vacancy = VALUES(vacancy)";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        int capacity =
	                numberOfRooms * sharingType;

	        int occupied = 0;

	        int vacancy = capacity;

	        ps.setInt(1, sharingType);
	        ps.setInt(2, numberOfRooms);
	        ps.setInt(3, capacity);
	        ps.setInt(4, occupied);
	        ps.setInt(5, vacancy);
	        ps.setInt(6, monthlyFee);

	        int rows = ps.executeUpdate();

	        if (rows > 0) {
	            System.out.println(
	                    "Room configuration saved to MySQL."
	            );
	            return true;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return false;
	}
	public java.util.ArrayList<
    HostelManagementAndAdministrationSystem.RoomType>
    getAllRoomTypes() {

java.util.ArrayList<
        HostelManagementAndAdministrationSystem.RoomType>
        roomList =
        new java.util.ArrayList<>();

String sql = "SELECT * FROM rooms";

try (Connection con = DBConnection.getConnection();
     PreparedStatement ps = con.prepareStatement(sql);
     java.sql.ResultSet rs = ps.executeQuery()) {

    while (rs.next()) {

        HostelManagementAndAdministrationSystem.RoomType room =
                new HostelManagementAndAdministrationSystem.RoomType(
                        rs.getInt("sharing_type"),
                        rs.getInt("number_of_rooms"),
                        rs.getInt("monthly_fee")
                );

        room.capacity =
                rs.getInt("capacity");

        room.occupied =
                rs.getInt("occupied");

        room.vacancy =
                rs.getInt("vacancy");

        roomList.add(room);
    }

} catch (Exception e) {
    e.printStackTrace();
}

return roomList;
}
	
}
