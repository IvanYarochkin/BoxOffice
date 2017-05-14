package by.litelife.menu;

import by.litelife.dao.UserDAO;
import by.litelife.entity.User;
import by.litelife.hash.Md5Hash;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by John on 17.04.2017.
 */
public class UserOperationMenu {

    private static final String ENTER_USER_NAME_MESSAGE = "Введите имя пользователя: ";
    private static final String ENTER_USER_LOGIN_MESSAGE = "Введите логин пользователя: ";
    private static final String ENTER_USER_PASSWORD_MESSAGE = "Введите пароль пользователя: ";
    private static final String ENTER_USER_STATUS_MESSAGE = "Введите статус пользователя(1 - админ, 0 - пользователь): ";
    private static final String ENTER_USER_LOGIN_TO_DELETE_MESSAGE = "Введите логин пользователя которого вы хотите удалить: ";
    private static final String INCORRECT_VALUE_MESSAGE = "Вы ввели иное значение.";
    private static final String INCORRECT_ITEM_MESSAGE = "Нет такого пункта, попробуйте еще раз.";
    private static final String USER_IS_NOT_EXIST_MESSAGE = "Такого пользователя не существует.";



    private BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    private UserDAO userDAO = new UserDAO();

    private boolean flag = true;

    public void showMainMenu(){
        System.out.println("\nВыберите пункт:");

        System.out.println("1: Добавить пользователя.");
        System.out.println("2: Просмотреть пользователя.");
        System.out.println("3: Удалить пользователя.");
        System.out.println("4: Вернуться обратно.");
    }

    public void chooseMenuItem(){
        try {

            do {
                showMainMenu();

                int item = Integer.parseInt(bufferedReader.readLine());

                switch (item) {
                    case 1: {
                        createUser();
                        break;
                    }
                    case 2: {
                        readUser();
                        break;
                    }
                    case 3: {
                        deleteUser();
                        break;
                    }
                    case 4: {
                        flag = false;
                        break;
                    }

                    default: {
                        System.out.println(INCORRECT_ITEM_MESSAGE);
                    }
                }
            } while (flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void createUser() throws IOException {
        User user = new User();

        System.out.println(ENTER_USER_NAME_MESSAGE);

        user.setName(bufferedReader.readLine());

        System.out.println(ENTER_USER_LOGIN_MESSAGE);
        user.setLogin(bufferedReader.readLine());

        System.out.println(ENTER_USER_PASSWORD_MESSAGE);
        user.setPassword(Md5Hash.md5Custom(bufferedReader.readLine()));

        boolean isNotBoolean = true;
        do {
            System.out.println(ENTER_USER_STATUS_MESSAGE);
            user.setStatus(Byte.parseByte(bufferedReader.readLine()));
            if ((user.getStatus() == 1) || (user.getStatus() == 0)) {
                isNotBoolean = false;
            } else {
                System.out.println(INCORRECT_VALUE_MESSAGE);
            }
        } while (isNotBoolean);
        userDAO.create(user);
    }


    private void readUser() throws IOException {
        System.out.print(ENTER_USER_LOGIN_MESSAGE);

        String login = bufferedReader.readLine();

        User user = userDAO.read(login);
        if (user!=null){
            System.out.println(user);
        } else {
            System.out.println(USER_IS_NOT_EXIST_MESSAGE);
        }
    }

    private void deleteUser() throws IOException {
        System.out.print(ENTER_USER_LOGIN_TO_DELETE_MESSAGE);
        String deleteLogin = bufferedReader.readLine();

        userDAO.delete(deleteLogin);
    }
}
