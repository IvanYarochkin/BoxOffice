package by.litelife.menu;

import by.litelife.dao.SpectacleDAO;
import by.litelife.dao.TheaterDAO;
import by.litelife.entity.Spectacle;
import by.litelife.entity.Theater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by John on 17.04.2017.
 */
public class TheaterOperationMenu {


    private static final String THEATER_IS_NOT_EXIST_MESSAGE ="Театра с данным id не существует.";
    private static final String ENTER_ID_SPECTACLE_MESSAGE ="Введите через запятую id спектаклей, которые будут проходить в театре: ";
    private static final String ENTER_THEATER_NAME_MESSAGE ="Введите название театра: ";
    private static final String ENTER_ID_THEATER_MESSAGE ="Введите id театра: ";
    private static final String ENTER_ID_THEATER_TO_DELETE_MESSAGE = "Введите id театра для удаления: ";
    private static final String INCORRECT_ITEM_MESSAGE = "Нет такого пункта, попробуйте еще раз.";

    private TheaterDAO theaterDAO = new TheaterDAO();
    private boolean flag = true;
    private  BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public void showMainMenu(){
        System.out.println("\nВыберите пункт:");

        System.out.println("1: Добавить театр.");
        System.out.println("2: Просмотреть театр.");
        System.out.println("3: Удалить театр.");
        System.out.println("4: Вернуться обратно.");
    }

    public void chooseMenuItem(){
        try {
            do {
                showMainMenu();

                int item = Integer.parseInt(bufferedReader.readLine());

                switch (item){
                    case 1 : {
                        createTheater();
                        break;
                    }
                    case 2: {
                        readTheater();
                        break;
                    }
                    case 3 :{
                        deleteTheater();
                        break;
                    }
                    case 4 :{
                        flag = false;
                        break;
                    }
                    default : {
                        System.out.println(INCORRECT_ITEM_MESSAGE);
                    }
                }
            } while (flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createTheater() throws IOException {
        Theater theater = new Theater();

        System.out.println(ENTER_THEATER_NAME_MESSAGE);
        theater.setName(bufferedReader.readLine());

        System.out.println(ENTER_ID_SPECTACLE_MESSAGE);
        String temp = bufferedReader.readLine();

        ArrayList<Spectacle> spectaclesLocal = new ArrayList<>();
        ArrayList<Spectacle> spectaclesFromDB = SpectacleDAO.selectSpectacles();

        for (String index : temp.split(",")) {
            for (int i = 0 ; i < spectaclesFromDB.size(); i++){
                if (spectaclesFromDB.get(i).getId()== Integer.parseInt(index)){
                    spectaclesLocal.add(SpectacleDAO.selectSpectacles().get(i));
                }
            }
        }
        theater.setSpectacles(spectaclesLocal);

        theaterDAO.create(theater);
    }

    private void readTheater() throws IOException {
        System.out.print(ENTER_ID_THEATER_MESSAGE);
        int id = Integer.parseInt(bufferedReader.readLine());

        Theater theater = theaterDAO.read(id);

        if (theater!=null){
            System.out.println(theater);
        } else {
            System.out.println(THEATER_IS_NOT_EXIST_MESSAGE);
        }
    }

    private void deleteTheater() throws IOException {
        System.out.print(ENTER_ID_THEATER_TO_DELETE_MESSAGE);
        int id = Integer.parseInt(bufferedReader.readLine());
        theaterDAO.delete(id);
    }

}
