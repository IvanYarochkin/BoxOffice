package by.litelife.dao;

import by.litelife.database.DataSource;
import by.litelife.entity.Theater;
import by.litelife.entity.Ticket;
import by.litelife.main.Main;

import java.awt.image.RescaleOp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by John on 13.05.2017.
 */
public class TicketDAO {

    private static final String CREATE_TICKET_SQL = "INSERT INTO boxoffice.ticket (id_user, id_theater, id_spectacle) VALUES (?, ?, ?);";
    private static final String SELECT_TICKETS_SQL = "SELECT id, id_user, id_theater, id_spectacle FROM boxoffice.ticket WHERE id_user = ?;";


    private static final String SUCCESS_MESSAGE = "Действие успешно выолнено.";

    public void create(Ticket ticket){
        DataSource dataSource = new DataSource();
        try (Connection connection = dataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TICKET_SQL)){
            preparedStatement.setInt(1, ticket.getUser().getId());
            preparedStatement.setInt(2, ticket.getTheater().getId());
            preparedStatement.setInt(3, ticket.getSpectacle().getId());

            preparedStatement.execute();
            System.out.println(SUCCESS_MESSAGE);

            } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Ticket> read(){
        SpectacleDAO spectacleDAO = new SpectacleDAO();
        TheaterDAO theaterDAO = new TheaterDAO();
        ArrayList<Ticket> tickets = new ArrayList<>();
        DataSource dataSource = new DataSource();

        try (Connection connection = dataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TICKETS_SQL)){
            preparedStatement.setInt(1, Main.authorizedUser.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Ticket ticket = new Ticket();
                ticket.setUser(Main.authorizedUser);
                ticket.setId(resultSet.getInt("id"));
                ticket.setSpectacle(spectacleDAO.read(resultSet.getInt("id_spectacle")));
                ticket.setTheater(theaterDAO.read(resultSet.getInt("id_theater")));
                tickets.add(ticket);
            }
            return tickets;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(){

    }

    public void delete(){

    }
}
