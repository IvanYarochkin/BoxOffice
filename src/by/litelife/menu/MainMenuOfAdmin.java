package by.litelife.menu;

import by.litelife.dao.SpectacleDAO;
import by.litelife.dao.TheaterDAO;
import by.litelife.exception.TicketsLargerThanNumberException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by John on 17.04.2017.
 */
public class MainMenuOfAdmin {

    private static final String INCORRECT_ITEM_MESSAGE = "Нет такого пункта, попробуйте еще раз.";


    private boolean flag = true;
    private BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public void showMainMenu(){
        System.out.println("\nВыберите пункт:");
        System.out.println("1: Самые популярные театры.");
        System.out.println("2: Самые популярные спектакли.");
        System.out.println("3: Заказать билет.");
        System.out.println("4: Просмотреть билеты.");
        System.out.println("5: Операции с пользователем.");
        System.out.println("6: Операции с театром.");
        System.out.println("7: Операции со спектаклем.");
        System.out.println("8: Выход из программы.");
    }

    public void chooseMenuItem() {
        try {
            MainMenuOfUser mainMenuOfUser = new MainMenuOfUser();
            do {
                showMainMenu();

                int item = Integer.parseInt(bufferedReader.readLine());

                switch (item) {
                    case 1: {
                        mainMenuOfUser.showPopularTheaters();
                        break;
                    }
                    case 2: {
                        mainMenuOfUser.showPopularSpectacles();
                        break;
                    }
                    case 3: {
                        try {
                            mainMenuOfUser.orderTicket();
                        } catch (TicketsLargerThanNumberException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    }
                    case 4: {
                        mainMenuOfUser.showTickets();
                        break;
                    }
                    case 5: {
                        UserOperationMenu userOperationMenu = new UserOperationMenu();
                        userOperationMenu.chooseMenuItem();
                        break;
                    }
                    case 6: {
                        TheaterOperationMenu theaterOperationMenu = new TheaterOperationMenu();
                        theaterOperationMenu.chooseMenuItem();
                        break;
                    }
                    case 7: {
                        SpectacleOperationMenu spectacleOperationMenu = new SpectacleOperationMenu();
                        spectacleOperationMenu.chooseMenuItem();
                        break;
                    }
                    case 8: {
                        System.exit(0);
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


}
