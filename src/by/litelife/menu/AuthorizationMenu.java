package by.litelife.menu;

import by.litelife.dao.UserDAO;
import by.litelife.entity.User;
import by.litelife.hash.Md5Hash;
import by.litelife.main.Main;

import java.util.Scanner;

/**
 * Created by John on 16.04.2017.
 */
public class AuthorizationMenu {

    private static final String LOGIN_MESSAGE = "Введите логин: ";
    private static final String PASSWORD_MESSAGE = "Введите пароль: ";
    private static final String AUTHORIZATION_MESSAGE = "Авторизация:\n";
    private static final String LOGIN_IS_NOT_EXIST_MESSAGE = "Такого логина не существует, попробуйте еще раз.";
    private static final String INCORRECT_PASSWORD_MESSAGE = "Вы ввели неверный пароль, попробуйте еще раз.";

    private String login;
    private String password;
    private boolean flagOfName = true;
    private boolean flagOfPassword = true;


    public void showLoginMenu(){
        System.out.print(LOGIN_MESSAGE);
    }

    public void showPasswordMenu(){
        System.out.print(PASSWORD_MESSAGE);
    }

    public void signIn() {



        UserDAO userDAO = new UserDAO();
        System.out.println(AUTHORIZATION_MESSAGE);

        int j = 0;

        do {
            if (j>0){
                System.out.println(LOGIN_IS_NOT_EXIST_MESSAGE);
            }
            showLoginMenu();

            Scanner scanner = new Scanner(System.in);
            login = scanner.nextLine();

            for (User user : userDAO.selectUsers()){
                if(user.getName().equals(login)){
                    flagOfName = false;
                }
            }

            j++;
        } while (flagOfName);


        int i = 0;

        do{
            if (i>0){
                System.out.println(INCORRECT_PASSWORD_MESSAGE);
            }
            showPasswordMenu();

            Scanner scanner2 = new Scanner(System.in);
            password = scanner2.nextLine();

            password = Md5Hash.md5Custom(password);

            for (User user : userDAO.selectUsers()){
                if(user.getName().equals(login)){
                    if (password.equals(user.getPassword())){
                        flagOfPassword=false;
                    }
                }
            }
            i++;
        } while (flagOfPassword);


        for (User user : userDAO.selectUsers()){
            if(user.getName().equals(login)){
                Main.authorizedUser = user;
            }
        }

        System.out.println("");

        if (Main.authorizedUser.getStatus()==1){
            MainMenuOfAdmin mainMenuOfAdmin = new MainMenuOfAdmin();
            mainMenuOfAdmin.chooseMenuItem();
        } else {
            MainMenuOfUser mainMenuOfUser = new MainMenuOfUser();
            mainMenuOfUser.chooseMenuItem();
        }



    }
}
