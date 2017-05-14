package by.litelife.dao;

import by.litelife.database.DataSource;
import by.litelife.entity.Spectacle;
import by.litelife.entity.Theater;

import java.sql.*;
import java.util.*;

/**
 * Created by John on 16.04.2017.
 */
public class SpectacleDAO {

    private static final String SPECTACLE_IS_NOT_EXIST_MESSAGE = "Спектакля не существует.";
    private static final String SPECTACLE_WITH_ID_IS_NOT_EXIST_MESSAGE = "Спектакля с таким id не существует.";
    private static final String SPECTACLE_WAS_ADDED_MESSAGE = "Спектакль успешно добавлен.";
    private static final String SPECTACLE_WAS_DELETED_MESSAGE = "Удаление спектакля успешно выполнено.";

    private static final String SELECT_THEATERS_AND_SPECTACLES_SQL = "SELECT theater.id AS id_theater, theater.name AS theater_name, spectacle.id AS id_spectacle, spectacle.name AS spectacle_name, spectacle.date, spectacle.count_of_tickets, spectacle.count_of_sold_tickets FROM theater LEFT JOIN theater_spectacle ON theater.id = theater_spectacle.id_theater LEFT JOIN spectacle ON theater_spectacle.id_spectacle = spectacle.id WHERE spectacle.id = ?;";
    private static final String SELECT_SPECTACLES_SQL = "SELECT id, name, date, count_of_tickets, count_of_sold_tickets FROM spectacle;";
    private static final String SELECT_POPULAR_SPECTACLES_SQL = "SELECT * FROM spectacle WHERE MONTH(spectacle.date) = ? ORDER BY spectacle.count_of_sold_tickets DESC LIMIT ?;";
    private static final String INSERT_INTO_SPECTACLE_SQL = "INSERT INTO boxoffice.spectacle (name, date, count_of_tickets, count_of_sold_tickets) VALUES (?, ?, ?, ?);";
    private static final String INSERT_INTO_THEATER_SPECTACLE_SQL = "INSERT INTO `boxoffice`.`theater_spectacle` (`id_theater`, `id_spectacle`) VALUES (?, ?);";
    private static final String DELETE_SPECTACLE_SQL = "DELETE FROM boxoffice.spectacle WHERE id=?;";
    private static final String DELETE_THEATER_SPECTACLE_SQL = "DELETE FROM boxoffice.theater_spectacle WHERE id_spectacle=?;";
    private static final String UPDATE_SPECTACLE_SQL = "UPDATE boxoffice.spectacle SET count_of_sold_tickets=? WHERE id=?;";


    public static ArrayList<Spectacle> selectSpectacles(){

        DataSource dataSource = new DataSource();

        try (Connection connection = dataSource.createConnection();
             Statement statement = connection.createStatement();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_THEATERS_AND_SPECTACLES_SQL)){
            ResultSet resultSet = statement.executeQuery(SELECT_SPECTACLES_SQL);

            ArrayList<Spectacle> listOfSpectacles = new ArrayList<>();

            while (resultSet.next()){
                Spectacle spectacle = new Spectacle();
                spectacle.setId(resultSet.getInt(1));
                spectacle.setName(resultSet.getString(2));
                spectacle.setDate(resultSet.getDate(3));
                spectacle.setCountOfTickets(resultSet.getInt(4));
                spectacle.setCountOfSoldTickets(resultSet.getInt(5));


                preparedStatement.setInt(1,spectacle.getId());
                ResultSet resultSet2 = preparedStatement.executeQuery();
                ArrayList<Theater> theaters = new ArrayList<>();

                while (resultSet2.next()){
                    Theater theater = new Theater();
                    theater.setId(resultSet2.getInt("id_theater"));
                    theater.setName(resultSet2.getString("theater_name"));

                    theaters.add(theater);
                }
                spectacle.setTheaters(theaters);
                listOfSpectacles.add(spectacle);
            }

            return listOfSpectacles;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void create(Spectacle spectacle){

        DataSource dataSource = new DataSource();
        try (Connection connection = dataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_SPECTACLE_SQL);
             PreparedStatement preparedStatement3 = connection.prepareStatement(INSERT_INTO_THEATER_SPECTACLE_SQL)){

            java.util.Date utilDate = spectacle.getDate();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());


            preparedStatement.setString(1,spectacle.getName());
            preparedStatement.setDate(2, sqlDate);

            preparedStatement.setInt(3,spectacle.getCountOfTickets());
            preparedStatement.setInt(4,spectacle.getCountOfSoldTickets());
            preparedStatement.execute();

            boolean isExist = false;

            for (Spectacle localSpectacle : SpectacleDAO.selectSpectacles()){
                if (localSpectacle.getName().equals(spectacle.getName())){
                    spectacle.setId(localSpectacle.getId());
                    for (int i = 0 ; i < spectacle.getTheaters().size() ; i++){
                        preparedStatement3.setInt(1, spectacle.getTheaters().get(i).getId());
                        preparedStatement3.setInt(2, spectacle.getId());
                        preparedStatement3.execute();
                        isExist = true;
                    }
                }
            }
            if (!isExist){
                System.out.println(SPECTACLE_IS_NOT_EXIST_MESSAGE);
            } else{
                System.out.println(SPECTACLE_WAS_ADDED_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Spectacle read(int id){

        for (Spectacle spectacle : SpectacleDAO.selectSpectacles()){
            if (spectacle.getId()==id){
                return spectacle;
            }
        }
        return null;
    }

    public void update(Spectacle spectacle){

        DataSource dataSource = new DataSource();
        try (Connection connection = dataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SPECTACLE_SQL)){
            preparedStatement.setInt(1, spectacle.getCountOfSoldTickets()+1);
            preparedStatement.setInt(2, spectacle.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id){
        boolean isExist = false;

        for (Spectacle spectacle : SpectacleDAO.selectSpectacles()){
            if (spectacle.getId()==id){
                isExist = true;
            }
        }
        if (isExist){
            DataSource dataSource = new DataSource();
            try (Connection connection = dataSource.createConnection();
                 PreparedStatement preparedStatement1 = connection.prepareStatement(DELETE_SPECTACLE_SQL);
                 PreparedStatement preparedStatement = connection.prepareStatement(DELETE_THEATER_SPECTACLE_SQL)){

                preparedStatement1.setInt(1,id);
                preparedStatement.setInt(1,id);

                preparedStatement.execute();
                preparedStatement1.execute();

                System.out.println(SPECTACLE_WAS_DELETED_MESSAGE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println(SPECTACLE_WITH_ID_IS_NOT_EXIST_MESSAGE);
        }
    }


    public void showSpectaclesByCountOfSoldTickets(int month, int limit){

        DataSource dataSource = new DataSource();
        try (Connection connection = dataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_POPULAR_SPECTACLES_SQL)){
            preparedStatement.setInt(1, month);
            preparedStatement.setInt(2, limit);

            ResultSet resultSet = preparedStatement.executeQuery();



            if(resultSet.first()){

                System.out.println("\nСамые популярные спектакли на "+month+" месяц:");
                System.out.println("id    Название спектакля");
                System.out.println(resultSet.getString("id")+"     "+resultSet.getString("name")+ " ("+resultSet.getString("count_of_sold_tickets")+" билетов продано)");
                while (resultSet.next()){
                    System.out.println(resultSet.getString("id")+"     "+resultSet.getString("name")+ " ("+resultSet.getString("count_of_sold_tickets")+" билетов продано)");
                }
                System.out.println("");

            } else {
                System.out.println("Нет спектаклей в театрах в "+month+" месяце.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
