package by.litelife.main;

import by.litelife.entity.User;
import by.litelife.menu.AuthorizationMenu;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by John on 16.04.2017.
 */
public class Main {

    public static User authorizedUser;

    public static void main(String[] args) throws IOException, SQLException {

        AuthorizationMenu authorizationMenu = new AuthorizationMenu();
        authorizationMenu.signIn();



    }
}
