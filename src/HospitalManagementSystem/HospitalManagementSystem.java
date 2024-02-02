package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url="jdbc:mysql://localhost:3306/hospital";
    private static final String username="root";
    private final static String password="Ajeet@072";

    public static void main(String[] args) {

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner=new Scanner(System.in);

        try{

            Connection connection= DriverManager.getConnection(url,username,password);
            Doctors doctors=new Doctors(connection, scanner);
            Patients patients=new Patients(connection, scanner);
            boolean b=true;
            while (b){

                System.out.println("\n############## HOSPITAL - MANAGEMENT - SYSTEM ###############\n");
                System.out.println("               [ 1 ] -> Add Patients ");
                System.out.println("               [ 2 ] -> View Patients");
                System.out.println("               [ 3 ] -> View Doctors");
                System.out.println("               [ 4 ] -> Book Appointments");
                System.out.println("               [ 5 ] -> Show Appointments");
                System.out.println("               [ 6 ] -> Exit\n");

                System.out.print("               Enter your choice(1/6): ");
                int ch=scanner.nextInt();
                switch (ch){
                    case 1:
                        while (true){
                            patients.addPatient();
                            System.out.print("Do you want Add more patients (Y/N): ");
                            String choice=scanner.next().toUpperCase();
                            if (choice.equals("N")){
                                break;
                            }
                        }
                        break;
                    case 2:
                        patients.viewPatient();
                        break;
                    case 3:
                        doctors.viewDoctors();
                        break;
                    case 4:
                        bookAppointment(connection,scanner,patients,doctors);
                        break;
                    case 5:
                        showAppointments(connection);
                        break;
                    case 6:
                        b=false;
                        break;
                    default:
                        System.out.println("Invalid input\n");
                }
                System.out.print("Do you want to do more Operations(Y/N): ");
                String choice=scanner.next().toUpperCase();
                if(choice.equals("N")){
                    System.out.println("THANK YOU FOR USING HOSPITAL MANAGEMENT SYSTEM.\n");
                    b=false;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void bookAppointment(Connection connection, Scanner scanner, Patients patients, Doctors doctors){

        System.out.println("\n##############     || BOOK - APPOINTMENTS ||   ##############\n");
        System.out.print("               Enter Patient Id: ");
        int patient_id=scanner.nextInt();
        System.out.print("               Enter Doctor Id: ");
        int doctor_id=scanner.nextInt();
        System.out.print("               Enter appointment Date(YYYY/MM/DATE): ");
        String date=scanner.next();
        if(patients.getPatientById(patient_id) && doctors.getDoctorById(doctor_id)){
            if(doctorAvailability(doctor_id,date, connection )){
                String query="INSERT INTO APPOINTMENTS(PATIENT_ID,DOCTOR_ID,APPOINTMENTS_DATE) VALUES(?,?,?)";
                try {
                    PreparedStatement preparedStatement=connection.prepareStatement(query);
                    preparedStatement.setInt(1,patient_id);
                    preparedStatement.setInt(2,doctor_id);
                    preparedStatement.setString(3,date);
                    int resultSet= preparedStatement.executeUpdate();
                    if(resultSet>0){
                        System.out.println("               Appointment Booked.");
                    }
                    else{
                        System.out.println("Failed To Book Appointment..!!");
                    }
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            else {
                System.out.println("Doctor is not available on this date.");
            }
        }
        else{
            System.out.println("Either Doctor or Patient Not Available.");
        }
    }
    public static boolean doctorAvailability(int doctor_id, String date, Connection connection ){
        String query="SELECT COUNT(*) FROM APPOINTMENTS WHERE ID=? AND APPOINTMENTS_DATE=?";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,doctor_id);
            preparedStatement.setString(2,date);
            ResultSet resultSet= preparedStatement.executeQuery();
            if(resultSet.next()){
                int count=resultSet.getInt(1);
                if(count==0){
                    return true;
                }
                else {
                    return false;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public static void showAppointments(Connection connection){
        System.out.println("\n##############  || APPOINTMENTS - DETAILS ||  ################\n");
        String query="SELECT * FROM APPOINTMENTS";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet=preparedStatement.executeQuery();
            System.out.println("+--------------+-------------------+----------------+------------------+");
            System.out.println("   ID                PATIENT_ID          DOCTOR_ID    APPOINTMENTS_DATE ");
            System.out.println("+--------------+-------------------+----------------+------------------+");

            while(resultSet.next()){
                int id=resultSet.getInt("ID");
                int patient_id=resultSet.getInt("PATIENT_ID");
                int doctor_id =resultSet.getInt("DOCTOR_ID");
                String date=resultSet.getString("APPOINTMENTS_DATE");

                System.out.printf("|    %-10s|    %-15s|     %-10s |  %-10s\n",id,patient_id,doctor_id,date);
                System.out.println("+--------------+-------------------+----------------+------------------+");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
