package by.litelife.dao;

import by.litelife.database.DataSource;
import by.litelife.entity.User;
import by.litelife.main.Main;
import by.litelife.menu.AuthorizationMenu;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by John on 16.04.2017.
 */
public class UserDAO {

    private static final String USER_WAS_DELETED_MESSAGE = "Пользователь успешно удален.";
    private static final String USER_IS_NOT_EXIST_MESSAGE = "Нет такого пользователя.";

    private static final String INSERT_USER_SQL = "INSERT INTO boxoffice.user (name, login, password, status) VALUES (?, ?, ?, ?);";
    private static final String DELETE_USER_SQL = "DELETE FROM boxoffice.user WHERE login = ?;\n";
    private static final String SELECT_USERS_SQL = "SELECT * FROM boxoffice.user;";

    public void create(User user){

        DataSource dataSource = new DataSource();
        try (Connection connection = dataSource.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)){

            preparedStatement.setString(1,user.getName());
            preparedStatement.setString(2,user.getLogin());
            preparedStatement.setString(3,user.getPassword());
            preparedStatement.setByte(4,user.getStatus());

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User read(String login){

        boolean isEmpty = true;

        for (User user : selectUsers()){
            if (user.getName().equals(login)){
                return new User(user.getId(),user.getName(),user.getLogin(),user.getPassword(),user.getStatus());
            }
        }

        return null;
    }


    public void update(){
    }

    public void delete(String deleteLogin){

            DataSource dataSource = new DataSource();
            try (Connection connection = dataSource.createConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_SQL)){
                preparedStatement.setString(1, deleteLogin);

                boolean isDeleted = false;

                for (User user : selectUsers()){
                    if (user.getLogin().equals(deleteLogin)){
                        preparedStatement.execute();
                        isDeleted = true;
                    }
                }

                if (isDeleted){
                    System.out.println(USER_WAS_DELETED_MESSAGE);
                } else {
                    System.out.println(USER_IS_NOT_EXIST_MESSAGE);
                }

                if (Main.authorizedUser.getLogin().equals(deleteLogin)){
                    Main.authorizedUser = null;
                    AuthorizationMenu authorizationMenu = new AuthorizationMenu();
                    authorizationMenu.signIn();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public ArrayList<User> selectUsers(){
        DataSource dataSource = new DataSource();
        try (Connection connection = dataSource.createConnection();
             Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(SELECT_USERS_SQL);

            ArrayList<User> listOfUsers = new ArrayList<>();
            while (resultSet.next()){
                User user = new User();
                user.setId(resultSet.getInt(1));
                user.setName(resultSet.getString(2));
                user.setLogin(resultSet.getString(3));
                user.setPassword(resultSet.getString(4));
                user.setStatus(resultSet.getByte(5));
                listOfUsers.add(user);
            }
            return listOfUsers;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
