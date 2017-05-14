package by.litelife.menu;

import by.litelife.dao.SpectacleDAO;
import by.litelife.dao.TheaterDAO;
import by.litelife.dao.TicketDAO;
import by.litelife.entity.Spectacle;
import by.litelife.entity.Ticket;
import by.litelife.exception.TicketsLargerThanNumberException;
import by.litelife.main.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by John on 16.04.2017.
 */
class MainMenuOfUser {

    private boolean flag = true;
    private static final String MONTH_MESSAGE = "Введите месяц, по которому произойдет отбор:";
    private static final String MAX_THEATER = "Введите максимальнок количество выведенных театров:";
    private static final String MAX_SPECTACLE = "Введите максимальнок количество выведенных спектаклей:";
    private static final String INCORRECT_ITEM_MESSAGE = "Нет такого пункта, попробуйте еще раз.";
    private static final String ID_THEATER_MESSAGE = "Введите id театра, в котором вы хотите заказать билет: ";
    private static final String ID_SPECTACLE_MESSAGE = "Введите id спектакля, в котором вы хотите заказать билет: ";
    private static final String INCORRECT_SPECTACLE_MESSAGE = "Вы ввели id спектакля, который не идет в данном театре или спектакля с таким id не существует!";
    private static final String THEATER_IS_EMPTY_MESSAGE = "Вы ввели id театра, которого не существует: ";
    private static final String TICKETS_IS_EMPTY_MESSAGE = "У этого пользователя нет билетов.";

    private BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public void showMainMenu(){
        System.out.println("\nВыберите пункт:");
        System.out.println("1: Самые популярные театры.");
        System.out.println("2: Самые популярные спектакли.");
        System.out.println("3: Заказать билет.");
        System.out.println("4: Посмотреть все билеты.");
        System.out.println("5: Выход из программы.");
    }

    public void chooseMenuItem(){
        try {
            do {
                showMainMenu();

                int item = Integer.parseInt(bufferedReader.readLine());

                switch (item) {
                    case 1: {
                        showPopularTheaters();
                        break;
                    }
                    case 2: {
                        showPopularSpectacles();
                        break;
                    }
                    case 3: {
                        try {
                            orderTicket();
                        } catch (TicketsLargerThanNumberException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    }
                    case 4: {
                        showTickets();
                        break;
                    }
                    case 5: {
                        System.exit(0);
                        break;
                    }
                    default: {
                        System.out.println(INCORRECT_ITEM_MESSAGE);
                    }
                }
            } while (flag) ;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     void showPopularTheaters() throws IOException {
        TheaterDAO theaterDAO = new TheaterDAO();
        System.out.print(MONTH_MESSAGE);
        int month = Integer.parseInt(bufferedReader.readLine());
        System.out.print(MAX_THEATER);
        int limit = Integer.parseInt(bufferedReader.readLine());
        theaterDAO.showTheatersByCountOfSoldTickets(month,limit);
    }

     void showPopularSpectacles() throws IOException {
        SpectacleDAO spectacleDAO = new SpectacleDAO();
        System.out.print(MONTH_MESSAGE);
        int month = Integer.parseInt(bufferedReader.readLine());
        System.out.print(MAX_SPECTACLE);
        int limit = Integer.parseInt(bufferedReader.readLine());
        spectacleDAO.showSpectaclesByCountOfSoldTickets(month, limit);
    }
    
    void orderTicket() throws IOException, TicketsLargerThanNumberException {
        SpectacleDAO spectacleDAO = new SpectacleDAO();
        TicketDAO ticketDAO = new TicketDAO();
        Ticket ticket = new Ticket();
        TheaterDAO theaterDAO = new TheaterDAO();

        ticket.setUser(Main.authorizedUser);
        
        System.out.print(ID_THEATER_MESSAGE);
        int idTheater = Integer.parseInt(bufferedReader.readLine());
        ticket.setTheater(theaterDAO.read(idTheater));
        
        if (ticket.getTheater()!=null){
            ticket.setSpectacle(checkSpectacle(ticket));
            if (ticket.getSpectacle()!=null){
                if (ticket.getSpectacle().getCountOfTickets()>ticket.getSpectacle().getCountOfSoldTickets()){
                    ticketDAO.create(ticket);
                    spectacleDAO.update(ticket.getSpectacle());

                } else {
                    throw new TicketsLargerThanNumberException("Нет свободных билетов на этот сеанс.");
                }

            } else {
                System.out.println(INCORRECT_SPECTACLE_MESSAGE);
            }
        } else {
            System.out.println(THEATER_IS_EMPTY_MESSAGE);
        }

    }
    
    private Spectacle checkSpectacle(Ticket ticket) throws IOException {
        SpectacleDAO spectacleDAO = new SpectacleDAO();
            System.out.println(ID_SPECTACLE_MESSAGE);
            int idSpectacle = Integer.parseInt(bufferedReader.readLine());
            for (Spectacle spectacle: ticket.getTheater().getSpectacles()) {
                if(spectacle.getId()==idSpectacle) {
                    return spectacleDAO.read(idSpectacle);
                }
            }
        return null;
    }

    void showTickets(){
        TicketDAO ticketDAO = new TicketDAO();
        if (ticketDAO.read().size()==1){
            for (Ticket ticket: ticketDAO.read()){
                System.out.println(ticket);
            }
        } else {
            System.out.println(TICKETS_IS_EMPTY_MESSAGE);
        }

    }

}
