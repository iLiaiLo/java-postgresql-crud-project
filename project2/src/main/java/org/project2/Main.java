package org.project2;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    private static Properties loadDbProperties() {
        Properties properties = new Properties();
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("application.properties")){
            properties.load(input);
        } catch(IOException ex){
            System.out.println("Error loading properties file: " + ex.getMessage());
        }
        return properties;
    }

    private static void printPersons (Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery( "SELECT id, name FROM PERSON");
        while (resultSet.next()) {

        int id = resultSet.getInt( "id");
            String name = resultSet.getString( "name");
            System.out.println("ID: "+ id + " Name:" + name);
        }
            System.out.println("---------------------");
    }


    private static void addPerson(Statement statement){
        try {
            Scanner scanner=new Scanner(System.in);
            System.out.println("enter id and name of person to insert");
            long id=scanner.nextLong();
            String name=scanner.next();

            statement.executeUpdate("INSERT INTO PERSON (ID, NAME) VALUES" +"(" + id + ",'" + name + "')");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void updatePerson(Statement statement){
        try{
            Scanner scanner=new Scanner(System.in);
            System.out.println("enter id and name of person to update");
            long id=scanner.nextLong();
            String name=scanner.next();
            statement.executeUpdate("UPDATE PERSON SET NAME = '"+name+"' WHERE ID = "+id);
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private static void deletePerson(Statement statement){
        try {
            Scanner scanner=new Scanner(System.in);
            System.out.println("enter id  of person to delete");
            long id=scanner.nextLong();
            statement.executeUpdate("DELETE FROM PERSON WHERE ID = "+id);
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }



    public static void main(String[] args) {
        Properties dbProperties = loadDbProperties();

        String url = dbProperties.getProperty("db.url");
        String username= dbProperties.getProperty("db.username");
        String password = dbProperties.getProperty("db.password");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement=connection.createStatement();
            Scanner scanner=new Scanner(System.in);

            int input=-1;

            while (input!=0){
                System.out.println("please select option");
                System.out.println("1.select all persons");
                System.out.println("2.add person");
                System.out.println("3.update person");
                System.out.println("4.delete person");
                System.out.println("0.exit");

                input=scanner.nextInt();

                switch (input){
                    case(1):
                        printPersons(statement);
                        break;
                    case(2):
                        addPerson(statement);
                        break;
                    case(3):
                        updatePerson(statement);
                        break;
                    case(4):
                        deletePerson(statement);
                        break;
                    case(0):
                        System.out.println("exit successfully");
                        break;
                    default:
                        System.out.println("invalid input try again");
                        break;
                }
            }
            scanner.close();

        } catch (SQLException e) {
            System.out.println("Database error: "+ e.getMessage());
        }

    }
}