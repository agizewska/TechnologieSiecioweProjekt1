package pwr.mainApp;

import pwr.connection.DatabaseConnection;
import pwr.entity.Movie;
import pwr.entity.MovieDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;

@WebServlet(name = "/MovieController", urlPatterns = {"/MovieController"})
public class MovieController extends HttpServlet {

    private String title, director, coverURL;
    private int day, month, year;
    private LocalDate date;
    private MovieDAO movieDAO;
    private Movie movie;
    private ArrayList <Movie> movies;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        title = request.getParameter("title");
        director = request.getParameter("director");
        String sDay = request.getParameter("day");
        String sMonth = request.getParameter("month");
        String sYear = request.getParameter("year");

        if (title.isEmpty() && director.isEmpty() && sDay.isEmpty()
                && sMonth.isEmpty() && sYear.isEmpty()) {
            response.sendRedirect("error.jsp");
        } else {
            try {
                day = Integer.parseInt(sDay);
                month = Integer.parseInt(sMonth);
                year = Integer.parseInt(sYear);
                date = LocalDate.of(year, month, day);
            } catch (DateTimeException e){
                e.printStackTrace();
                response.sendRedirect("error.jsp");
            }
        }

        coverURL = request.getParameter("coverURL");

        movieDAO = new MovieDAO();

        movie = new Movie(title, director, date, coverURL);

        boolean added = movieDAO.create(movie);

        if (added == true){
            response.sendRedirect("/tableGetter.jsp");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        movies = movieDAO.readAll();
        if (!movies.isEmpty()){
            request.setAttribute("movies", movies);
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/table.jsp");
            requestDispatcher.forward(request, response);
        }
    }


}
