package by.litelife.dao;

import by.litelife.database.DataSource;
import by.litelife.entity.Spectacle;
import by.litelife.entity.Theater;

import java.sql.*;
import java.util.ArrayList;



/**
 * Created by John on 16.04.2017.
 */
public class TheaterDAO {

    private static final String THEATER_IS_NOT_EXIST_MESSAGE = "Театра не существует.";
    private static final String THEATER_WITH_ID_IS_NOT_EXIST_MESSAGE = "Театра с таким id не существует.";
    private static final String THEATER_WAS_ADDED_MESSAGE = "Театр успешно добавлен.";
    private static final String THEATER_WAS_DELETED_MESSAGE = "Удаление успешно выполнено.";

    private static final String SELECT_THEATERS_AND_SPECTACLES_SQL = "SELECT theater.id AS id_theater, theater.name AS theater_name, spectacle.id AS id_spectacle, spectacle.name AS spectacle_name, spectacle.date, spectacle.count_of_tickets, spectacle.count_of_sold_tickets FROM theater LEFT JOIN theater_spectacle ON theater.id = theater_spectacle.id_theater LEFT JOIN spectacle ON theater_spectacle.id_spectacle = spectacle.id WHERE theater.id = ?;";
    private static final String SELECT_THEATERS_SQL = "SELECT id, name FROM theater;";
    private static final String SELECT_THEATER_SQL = "SELECT id, name FROM theater WHERE id = ?;";
    private static final String SELECT_POPULAR_THEATERS_SQL = "SELECT theater.id AS id_theater, theater.name AS theater_name, MONTH(spectacle.date) AS month, SUM(count_of_sold_tickets) AS sum_of_sold_tickets FROM theater LEFT JOIN theater_spectacle ON theater.id = theater_spectacle.id_theater LEFT JOIN spectacle ON theater_spectacle.id_spectacle = spectacle.id WHERE MONTH(spectacle.date) = ? GROUP BY theater.name ORDER BY SUM(count_of_sold_tickets) DESC LIMIT ?;";
    private static final String INSERT_INTO_THEATER_SQL = "INSERT INTO boxoffice.theater (name) VALUES (?);";
    private static final String INSERT_INTO_THEATER_SPECTACLE_SQL = "INSERT INTO `boxoffice`.`theater_spectacle` (`id_theater`, `id_spectacle`) VALUES (?, ?);";
    private static final String DELETE_THEATER_SQL = "DELETE FROM boxoffice.theater WHERE id=?;";
    private static final String DELETE_THEATER_SPECTACLE_SQL = "DELETE FROM boxoffice.theater_spectacle WHERE id_theater=?;";


    public static ArrayList<Theater> selectTheaters(){

        DataSource dataSource = new DataSource();
        try (Connection connection = dataSource.createConnection();
             Statement statement = connection.createStatement();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_THEATERS_AND_SPECTACLES_SQL)){
            ResultSet resultSet = statement.executeQuery(SELECT_THEATERS_SQL);

            ArrayList<Theater> theaters = new ArrayList<>();

            while (resultSet.next()){
                Theater theater = new Theater();
                theater.setId(resultSet.getInt(1));
                theater.setName(resultSet.getString(2));

                preparedStatement.setInt(1,theater.getId());
                ResultSet resultSet2 = preparedStatement.executeQuery();
                ArrayList<Spectacle> spectacles = new ArrayList<>();

                while (resultSet2.next()){
                    Spectacle spectacle = new Spectacle();
                    spectacle.setId(resultSet2.getInt("id_spectacle"));
                    spectacle.setName(resultSet2.getString("spectacle_name"));
                    spectacle.setDate(resultSet2.getDate("date"));
                    spectacle.setCountOfTickets(resultSet2.getInt("count_of_tickets"));
                    spectacle.setCountOfSoldTickets(resultSet2.getInt("count_of_sold_tickets"));
                    spectacles.add(spectacle);
                }
                theater.setSpectacles(spectacles);
                theaters.add(theater);
            }

            return theaters;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void create(Theater theater){

        DataSource dataSource = new DataSource();
        try (Connection connection = dataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_THEATER_SQL);
             PreparedStatement preparedStatement3 = connection.prepareStatement(INSERT_INTO_THEATER_SPECTACLE_SQL)){

            preparedStatement.setString(1,theater.getName());
            preparedStatement.execute();

            boolean isExist = false;

            for (Theater localTheater : selectTheaters()){
                if (localTheater.getName().equals(theater.getName())){
                    theater.setId(localTheater.getId());
                    for (int i = 0 ; i < theater.getSpectacles().size() ; i++){
                        preparedStatement3.setInt(1, theater.getId());
                        preparedStatement3.setInt(2, theater.getSpectacles().get(i).getId());
                        preparedStatement3.execute();
                        isExist = true;
                    }
                }
            }
            if (!isExist){
                System.out.println(THEATER_IS_NOT_EXIST_MESSAGE);
            } else {
                System.out.println(THEATER_WAS_ADDED_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Theater read(int idTheater){


        DataSource dataSource = new DataSource();
        try (Connection connection = dataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_THEATERS_AND_SPECTACLES_SQL);
             PreparedStatement preparedStatement1 = connection.prepareStatement(SELECT_THEATER_SQL)){

            preparedStatement1.setInt(1, idTheater);
            ResultSet resultSet = preparedStatement1.executeQuery();

            if (resultSet.first()) {

                Theater theater = new Theater();

                theater.setId(resultSet.getInt("id"));
                theater.setName(resultSet.getString("name"));

                preparedStatement.setInt(1, idTheater);
                ResultSet resultSet2 = preparedStatement.executeQuery();

                ArrayList<Spectacle> spectacles = new ArrayList<>();

                while (resultSet2.next()) {
                    Spectacle spectacle = new Spectacle();
                    spectacle.setId(resultSet2.getInt("id_spectacle"));
                    spectacle.setDate(resultSet2.getDate("date"));
                    spectacle.setCountOfSoldTickets(resultSet2.getInt("count_of_sold_tickets"));
                    spectacle.setCountOfTickets(resultSet2.getInt("count_of_tickets"));
                    spectacle.setName(resultSet2.getString("spectacle_name"));

                    spectacles.add(spectacle);
                }

                theater.getSpectacles().addAll(spectacles);
                return theater;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;


    }

    public void update(){
    }

    public void delete(int id){

        boolean isExist = false;

        for (Theater theater : TheaterDAO.selectTheaters()){
            if (theater.getId()==id){
                isExist = true;
            }
        }
        if (isExist){
            DataSource dataSource = new DataSource();
            try (Connection connection = dataSource.createConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(DELETE_THEATER_SPECTACLE_SQL);
                 PreparedStatement preparedStatement2 = connection.prepareStatement(DELETE_THEATER_SQL)){

                preparedStatement.setInt(1,id);
                preparedStatement.executeQuery();

                preparedStatement2.setInt(1,id);
                preparedStatement2.executeQuery();

                System.out.println(THEATER_WAS_DELETED_MESSAGE);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println(THEATER_WITH_ID_IS_NOT_EXIST_MESSAGE);
        }

    }

    public void showTheatersByCountOfSoldTickets(int month, int limit){

        DataSource dataSource = new DataSource();
        try (Connection connection = dataSource.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_POPULAR_THEATERS_SQL)){
            preparedStatement.setInt(1, month);
            preparedStatement.setInt(2, limit);

            ResultSet resultSet = preparedStatement.executeQuery();



            if(resultSet.first()){
                System.out.println("\nСамые популярные театры в "+month+" месяц:");
                System.out.println("id    Название театра");
                System.out.println(resultSet.getString(1)+"     "+resultSet.getString(2)+ " ("+resultSet.getString(4)+" билетов продано)");
                while (resultSet.next()){
                    System.out.println(resultSet.getString(1)+"     "+resultSet.getString(2)+ " ("+resultSet.getString(4)+" билетов продано)");
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
