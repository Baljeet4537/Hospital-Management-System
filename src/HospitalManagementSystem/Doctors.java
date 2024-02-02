package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctors {
    private Connection connection;
    private Scanner scanner;
    //creating constructor --
    public Doctors(Connection connection, Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }
    public void viewDoctors(){

        System.out.println("\n##############       || DOCTOR DETAILS ||      ##############\n");
        String query="SELECT * FROM DOCTORS";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet=preparedStatement.executeQuery();
            System.out.println("+--------+--------------------------+-------------------------+");
            System.out.println("    ID   |         NAME             |   SPECIALIZATION   ");
            System.out.println("+--------+--------------------------+-------------------------+");

            while(resultSet.next()){
                int id=resultSet.getInt("ID");
                String name=resultSet.getString("NAME");
                String specialization=resultSet.getString("SPECIALIZATION");
                System.out.printf("|    %-4s|    %-22s|    %-20s\n",id,name,specialization);
                System.out.println("+--------+--------------------------+-------------------------+");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public boolean getDoctorById(int id){
        String query="SELECT * FROM DOCTORS WHERE ID=?";
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
