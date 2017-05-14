package by.litelife.menu;

import by.litelife.dao.SpectacleDAO;
import by.litelife.dao.TheaterDAO;
import by.litelife.entity.Spectacle;
import by.litelife.entity.Theater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by John on 17.04.2017.
 */
public class SpectacleOperationMenu {

    private static final String ENTER_SPECTACLE_NAME_MESSAGE = "Введите название спектакля:";
    private static final String ENTER_SPECTACLE_ID_MESSAGE = "Введите id спектакля:";
    private static final String ENTER_SPECTACLE_DATE_MESSAGE = "Введите дату спектакля в формате HH mm dd MM yyyy";
    private static final String ENTER_TICKETS_COUNTS_MESSAGE = "Введите количество билетов:";
    private static final String ENTER_ID_THEATERS_MESSAGE = "Введите через запятую id театров, в которых будет проходить спектакль: ";
    private static final String ENTER_ID_SPECTACLE_TO_DELETE_MESSAGE = "Введите id спектакля для удаления: ";
    private static final String INCORRECT_ITEM_MESSAGE = "Нет такого пункта, попробуйте еще раз.";
    private static final String SPECTACLE_IS_NOT_EXIST_MESSAGE = "Спектакля не существует.";


    private BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    private SpectacleDAO spectacleDAO = new SpectacleDAO();
    private boolean flag = true;

    public void showMainMenu(){
        System.out.println("\nВыберите пункт:");

        System.out.println("1: Добавить спектакль.");
        System.out.println("2: Просмотреть спектакль.");
        System.out.println("3: Удалить спектакль.");
        System.out.println("4: Вернуться обратно.");
    }

    public void chooseMenuItem(){
        try {
            do {
                showMainMenu();

                int item = Integer.parseInt(bufferedReader.readLine());

                switch (item) {
                    case 1: {
                        createSpectacle();
                        break;
                    }
                    case 2: {
                        readSpectacle();
                        break;
                    }
                    case 3: {
                        deleteSpectacle();
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

    private void createSpectacle() throws IOException {
        Spectacle spectacle = new Spectacle();

        System.out.println(ENTER_SPECTACLE_NAME_MESSAGE);
        spectacle.setName(bufferedReader.readLine());
        System.out.println(ENTER_SPECTACLE_DATE_MESSAGE);
        String dateString = bufferedReader.readLine();

        SimpleDateFormat formatter = new SimpleDateFormat("HH mm dd MM yyyy");

        try {
            Date date1 = formatter.parse(dateString);
            spectacle.setDate(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        System.out.println(ENTER_TICKETS_COUNTS_MESSAGE);
        spectacle.setCountOfTickets(Integer.parseInt(bufferedReader.readLine()));

        spectacle.setCountOfSoldTickets(0);

        System.out.println(ENTER_ID_THEATERS_MESSAGE);
        String temp = bufferedReader.readLine();

        ArrayList<Theater> theatersLocal = new ArrayList<>();
        ArrayList<Theater> theatersFromDB = TheaterDAO.selectTheaters();

        for (String index : temp.split(",")) {
            for (int i = 0; i < theatersFromDB.size(); i++) {
                if (theatersFromDB.get(i).getId() == Integer.parseInt(index)) {
                    theatersLocal.add(TheaterDAO.selectTheaters().get(i));
                }
            }
        }
        spectacle.setTheaters(theatersLocal);
        spectacleDAO.create(spectacle);
    }

    private void readSpectacle() throws IOException {
        System.out.print(ENTER_SPECTACLE_ID_MESSAGE);
        int id = Integer.parseInt(bufferedReader.readLine());
        Spectacle spectacle = spectacleDAO.read(id);
        if (spectacle!=null){
            System.out.println(spectacle);
        } else {
            System.out.println(SPECTACLE_IS_NOT_EXIST_MESSAGE);
        }
    }

    private void deleteSpectacle() throws IOException {
        System.out.print(ENTER_ID_SPECTACLE_TO_DELETE_MESSAGE);
        int id = Integer.parseInt(bufferedReader.readLine());
        spectacleDAO.delete(id);
    }
}
