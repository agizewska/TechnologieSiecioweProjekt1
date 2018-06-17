package pwr.entity;

import pwr.connection.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MovieDAO {

    private Connection connection;

    public MovieDAO() {
        connection = DatabaseConnection.getDBconnection();
    }

    public boolean create (Movie movie){
        final String sql = "INSERT INTO movies(title, director, releaseDate, coverURL) values(?, ?, ?, ?)";

        boolean result = false;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getDirector());
            preparedStatement.setString(3, movie.getReleaseDate().toString());
            preparedStatement.setString(4, movie.getCoverURL());
            preparedStatement.executeUpdate();
            System.out.printf("Record created");
            result = true;
        } catch (SQLException e) {
            System.out.printf("Something went wrong");
            e.printStackTrace();
        }

        return result;
    }

    public ArrayList <Movie> readAll() {
        final String sql = "SELECT * FROM movies";
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery(sql);
            while (resultSet.next()) {
                Movie movie = new Movie();
                movie.setId(resultSet.getLong("id"));
                movie.setTitle(resultSet.getString("title"));
                movie.setDirector(resultSet.getString("director"));
                movie.setReleaseDate(LocalDate.parse(resultSet.getString("releaseDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                movie.setCoverURL(resultSet.getString("coverURL"));
                movies.add(movie);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public Movie read(int id){
        Movie movie = null;

        final String sql = "SELECT * FROM movies WHERE id = ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                movie = new Movie();

                movie.setId(resultSet.getInt("id"));
                movie.setTitle(resultSet.getString("title"));
                movie.setDirector(resultSet.getString("director"));
                movie.setCoverURL(resultSet.getString("coverURL"));
                movie.setReleaseDate(LocalDate.parse(resultSet.getString("releaseDate"), DateTimeFormatter.ofPattern("yyyy-mm-dd")));

                return movie;

            } else {
                return null;
            }

        } catch (SQLException e){
            System.out.printf("Something went wrong");
            e.printStackTrace();
            return null;
        }
    }

    public void update(Movie movie){
        final String sql = "UPDATE movies SET title = ?, director = ?, releaseDate = ?, coverURL = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getDirector());
            preparedStatement.setString(3, movie.getReleaseDate().toString());
            preparedStatement.setString(4, movie.getCoverURL());
            preparedStatement.setLong(5, movie.getId());
            preparedStatement.executeUpdate();
            System.out.printf("Record updated");
        } catch (SQLException e) {
            System.out.printf("Something went wrong");
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        final String sql = "DELETE from movies WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.printf("Record deleted");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.printf("Something went wrong");
        }
    }

}
