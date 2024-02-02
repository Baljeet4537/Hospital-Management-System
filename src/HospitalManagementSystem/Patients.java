package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class Patients {
    private Connection connection;
    private Scanner scanner;

    //creating constructor --
    public Patients(Connection connection, Scanner scanner){

        this.connection=connection;
        this.scanner=scanner;
    }

    public void addPatient(){

        System.out.println("\n##############       || ADD PATIENTS ||       ###############\n");

        System.out.print("               Enter patient Name: ");
        String name=scanner.next();
        System.out.print("               Enter patient Age: ");
        int age =scanner.nextInt();
        System.out.print("               Enter patient gender: ");
        String gender=scanner.next();

        try{
            String query="INSERT INTO PATIENTS(NAME, AGE, GENDER) VALUES(?,?,?)";
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);

            int rowsAffected=preparedStatement.executeUpdate();
            if(rowsAffected>0){
                System.out.println("Patient Added Successfully...");
            }
            else{
                System.out.println("Failed to add Patient !");
            }

        }catch(SQLException e) {
            e.printStackTrace();
        }

    }
    public void viewPatient(){


        System.out.println("\n################     || PATIENTS DETAIL ||   ################\n");
        String query="SELECT * FROM PATIENTS";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet=preparedStatement.executeQuery();
            System.out.println("+------------+---------------------+----------------+------------+");
            System.out.println("   ID                NAME               AGE             GENDER  ");
            System.out.println("+------------+---------------------+----------------+------------+");

            while(resultSet.next()){
                int id=resultSet.getInt("ID");
                String name=resultSet.getString("NAME");
                int age=resultSet.getInt("AGE");
                String gender=resultSet.getString("GENDER");
                System.out.printf("|    %-10s|    %-15s|     %-10s |   %-9s|\n",id,name,age,gender);
                System.out.println("+------------+---------------------+----------------+------------+");

            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public boolean getPatientById(int id){
        String query="SELECT * FROM PATIENTS WHERE ID=?";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;}
            else{
                return false;
            }

            }catch (SQLException e){
            e.printStackTrace();
        }

        return  false;
    }
}
