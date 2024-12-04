package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;


public class Main {
    public static void main(String[] args) {


        String username = args[0];
        String password = args[1];

        String lastName = Console.PromptForString("What is the last name of an actor you like? ");

        try(BasicDataSource dataSource = new BasicDataSource();){

            dataSource.setUrl("jdbc:mysql://localhost:3306/sakila");
            dataSource.setUsername(username);
            dataSource.setPassword(password);

            //do the stuff with the datasource here...

            try(Connection connection = dataSource.getConnection();){
                try(PreparedStatement actorsByLastNameQuery = connection.prepareStatement(
                        "SELECT * FROM Actor WHERE last_name = ?");)
                {
                    actorsByLastNameQuery.setString(1, lastName);
                    try(ResultSet results = actorsByLastNameQuery.executeQuery())
                    {
                        boolean found = false;
                        while(results.next()){
                            found = true;
                            System.out.printf("%d - %s %s\n",
                                    results.getInt(1),
                                    results.getString(2),
                                    results.getString(3));
                        }
                        if(!found){
                            System.out.println("No actors with that name.");
                        }
                    }
                }

            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}