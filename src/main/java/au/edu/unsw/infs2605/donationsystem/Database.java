/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package au.edu.unsw.infs2605.donationsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author clairelin
 */
public class Database {
    final private String database= "jdbc:sqlite:DonorDatabase.db";
    private static Connection conn;
    private static Database db = new Database();

    public Database() {
        try {
            setupDatabase();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static Database getInstance() {
        return db;
    }
    
    public void setupDatabase() throws SQLException {
        //connection to database
        conn = DriverManager.getConnection(database);
        Statement st = conn.createStatement();
        
        try {
            //check the table is exist
            st.execute("select 1 from appointment");
        } catch (SQLException throwables) {
         
        //create donor centre table with id, name, address, phone, donation type
        String createDonorCentreQuery = "CREATE TABLE IF NOT EXISTS donorcentre"
                                    + "("
                                    + "ID INTEGER PRIMARY KEY autoincrement, "
                                    + "NAME TEXT NOT NULL, "
                                    + "ADDRESS TEXT NOT NULL, "
                                    + "PHONE TEXT NOT NULL,"
                                    + "DONTYPE TEXT"
                                    + ");";
        st.execute(createDonorCentreQuery);
        insertDonorCentre();
        
        String createAppointmentInfoQuery = "CREATE TABLE IF NOT EXISTS appointment"
                    + "("
                    + "ID INTEGER PRIMARY KEY autoincrement, "
                    + "FIRSTNAME TEXT NOT NULL, "
                    + "LASTNAME TEXT NOT NULL, "
                    + "DONORCENTRE TEXT NOT NULL, "
                    + "DONATIONTIME TEXT NOT NULL,"
                    + "DONATIONDATE TEXT NOT NULL,"
                    + "EMAILADDRESS TEXT,"
                    + "PHONENUMBER TEXT NOT NULL,"
                    + "DONATIONTYPE TEXT, "
                    + "DONATIONSTATUS TEXT,"
                    + "NOTES TEXT"
                    + ");";
            st.execute(createAppointmentInfoQuery);
            //insert data into tables
            insertAppointmentInfo();
            System.out.println("init data success");
        }
        //close statement
        st.close();
        
    }
        
     private void insertDonorCentre() throws SQLException {
        Connection conn = DriverManager.getConnection(database);
        Statement st = conn.createStatement();
        
        PreparedStatement pSt = conn.prepareStatement(
            "INSERT OR IGNORE INTO "
                    + "donorcentre (id, name, address, phone, dontype) "
                    + "VALUES (?,?,?,?,?)"
        );

        // Data to insert
        String[] name = {"Town Hall Donor Centre", "Chatswood Donor Centre"
                , "The Shire Donor Centre"};
        String[] address = {"483 George St, Sydney NSW 2000"
                , "62, LEVEL 1/436 Victoria Ave, Chatswood NSW 2067"
                , "29 Kiora Rd, Miranda NSW 2228"};
        String[] phone = {"13 14 95", "13 14 95", "13 14 95"};
        String[] dontype = {"Blood, Plasma", "Blood", "Blood, Plasma"};

        // Loop to insert using prepared statements
        for (int i = 0; i < 3; i++) {
            pSt.setInt(1, i);
            pSt.setString(2, name[i]);
            pSt.setString(3, address[i]);
            pSt.setString(4, phone[i]);
            pSt.setString(5, dontype[i]);
            pSt.executeUpdate();
        }
        
        st.close();
        conn.close();
        
    }
      public void insertAppointmentInfo() throws SQLException {
        Statement st = conn.createStatement();
        PreparedStatement pSt = conn.prepareStatement(
                "INSERT OR IGNORE INTO "
                        + "appointment (id, firstName, lastName, donorCentre, "
                        + "donationTime, donationDate, emailAddress, "
                        + "phoneNumber, donationType, donationStatus,notes) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?)"
        );
        // Data to insert
        String[] firstName = {"John", "Margret", "Jill"};
        String[] lastName = {"Pho", "Kip", "Dance"};
        String[] donorCentre = {"Town Hall Donor Centre"
                , "Chatswood Donor Centre"
                , "The Shire Donor Centre"};
        String[] donationTime = {"12:00PM", "2:30PM", "10:30AM"};
        String[] donationDate = {"03/07/2022", "21/06/2022", "30/05/2022"};
        String[] emailAddress = {"johnpho@gmail.com", "margretkip@gmail.com"
                , "jilldance@gmail.com"};
        String[] phoneNumber = {"0465234987", "0412569276", "0417509469"};
        String[] donationType = {"Plasma", "Blood", "Blood"};
        String[] donationStatus = {"Approved", "Approved", "Approved"};
        String[] notes = {"Allergic to grass", "No allergies"
                , "Allergic to peanuts"};
        // Loop to insert using prepared statements
        for (int i = 0; i < 3; i++) {
            pSt.setInt(1, i);
            pSt.setString(2, firstName[i]);
            pSt.setString(3, lastName[i]);
            pSt.setString(4, donorCentre[i]);
            pSt.setString(5, donationTime[i]);
            pSt.setString(6, donationDate[i]);
            pSt.setString(7, emailAddress[i]);
            pSt.setString(8, phoneNumber[i]);
            pSt.setString(9, donationType[i]);
            pSt.setString(10, donationStatus[i]);
            pSt.setString(11, notes[i]);
            pSt.executeUpdate();
        }
        st.close();
    }
      /**
     *
     * @param where filter
     * @return
     */
    public ObservableList<AppointmentInfo> getAppointmentInfo(String where) {
        // Get ResultSet of all appointments that exist in the database
        ObservableList<AppointmentInfo> appointmentInfoList 
                = FXCollections.observableArrayList();
        try {
            Statement st = conn.createStatement();
            String query = "SELECT id, firstName, lastName, donorCentre"
                    + ", donationTime, donationDate, emailAddress, phoneNumber"
                    + ", donationType,donationstatus,notes FROM appointment " 
                    + where;
            System.out.println(query);
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                appointmentInfoList.add(new AppointmentInfo(rs.getInt("id")
                        , rs.getString("firstName")
                        , rs.getString("lastName")
                        , rs.getString("donorcentre")
                        , rs.getString("donationTime")
                        , rs.getString("donationDate")
                        , rs.getString("emailAddress")
                        , rs.getString("phoneNumber")
                        , rs.getString("donationType")
                        , rs.getString("donationStatus")
                        , rs.getString("notes")));
            }
            // Close
            st.close();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return appointmentInfoList;
    }

    /**
     * delete appointmentInfo by id
     * @param appointmentInfo
     */
    public void deleteAppointmentInfo(AppointmentInfo appointmentInfo) {
        try {
            Statement st = conn.createStatement();
            st.execute("DELETE FROM appointment WHERE id=" 
                    + appointmentInfo.getID());
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * insert appointmentInfo into database
     * @param appointmentInfo
     */
    public void addAppointmentInfo(AppointmentInfo appointmentInfo) {
        try {
            Statement st = conn.createStatement();
            PreparedStatement pSt = conn.prepareStatement(
                    "INSERT OR IGNORE INTO "
                            + "appointment ( firstName, lastName, donorCentre, "
                            + "donationTime, donationDate, emailAddress, "
                            + "phoneNumber, donationType, donationStatus,notes) "
                            + "VALUES (?,?,?,?,?,?,?,?,?,?)"
            );
            pSt.setString(1, appointmentInfo.getFirstName());
            pSt.setString(2, appointmentInfo.getLastName());
            pSt.setString(3, appointmentInfo.getDonorCentre());
            pSt.setString(4, appointmentInfo.getDonationTime());
            pSt.setString(5, appointmentInfo.getDonationDate());
            pSt.setString(6, appointmentInfo.getEmailAddress());
            pSt.setString(7, appointmentInfo.getPhoneNumber());
            pSt.setString(8, appointmentInfo.getDonationType());
            pSt.setString(9, appointmentInfo.getDonationStatus());
            pSt.setString(10, appointmentInfo.getNotes());
            pSt.executeUpdate();
            st.close();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * update appointmentInfo
     * @param appointmentInfo
     */
    public void UpdateAppointmentInfo(AppointmentInfo appointmentInfo) {
        try {
            Statement st = conn.createStatement();
            PreparedStatement pSt = conn.prepareStatement(
                    "UPDATE appointment SET "
                            + "firstName=?, lastName=?, donorCentre=?, "
                            + "donationTime=?, donationDate=?, emailAddress=?, " 
                            + "phoneNumber=?, donationType=?, "
                            + "donationStatus=?,notes=? where ID = ?"
            );
            pSt.setString(1, appointmentInfo.getFirstName());
            pSt.setString(2, appointmentInfo.getLastName());
            pSt.setString(3, appointmentInfo.getDonorCentre());
            pSt.setString(4, appointmentInfo.getDonationTime());
            pSt.setString(5, appointmentInfo.getDonationDate());
            pSt.setString(6, appointmentInfo.getEmailAddress());
            pSt.setString(7, appointmentInfo.getPhoneNumber());
            pSt.setString(8, appointmentInfo.getDonationType());
            pSt.setString(9, appointmentInfo.getDonationStatus());
            pSt.setString(10, appointmentInfo.getNotes());
            pSt.setInt(11, appointmentInfo.getID());
            pSt.executeUpdate();
            st.close();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

}
    
