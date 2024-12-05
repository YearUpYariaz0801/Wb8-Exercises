package com.pluralsight;

import com.pluralsight.models.*;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SakilaDataManager {

    private final BasicDataSource dataSource;

    public SakilaDataManager(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Actor> actorsByLastName(String lastName){

        ArrayList<Actor> actors = new ArrayList<>();


        //do the stuff with the datasource here...

        try(Connection connection = dataSource.getConnection();){
            try(PreparedStatement actorsByLastNameQuery = connection.prepareStatement(
                    "SELECT * FROM Actor WHERE last_name = ?");)
            {
                actorsByLastNameQuery.setString(1, lastName);
                try(ResultSet results = actorsByLastNameQuery.executeQuery())
                {
                    while(results.next()){
                        actors.add( new Actor(
                                results.getInt(1),
                                results.getString(2),
                                results.getString(3)
                        ));
                    }
                }
            }

            return actors;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Film> filmsByActorId(int actorId){
        ArrayList<Film> films = new ArrayList<>();

        try(Connection connection = dataSource.getConnection();){
            try(PreparedStatement filmsByActorId = connection.prepareStatement(
                    """
                            SELECT film.film_id, title, description, release_year, length FROM film
                            JOIN film_actor ON film.film_id = film_actor.film_id
                            WHERE actor_id = ?
                            """);)
            {
                filmsByActorId.setInt(1, actorId);

                try(ResultSet results = filmsByActorId.executeQuery())
                {
                    while(results.next()){
                        films.add( new Film(
                                results.getInt(1),
                                results.getString(2),
                                results.getString(3),
                                results.getInt(4),
                                results.getInt(5)
                        ));
                    }
                }
            }

            return films;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}