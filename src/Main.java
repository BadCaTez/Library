import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.server.ExportException;
import java.sql.*;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
       GraphicMenu.menu();
    }
    static class GraphicMenu extends JFrame {
        public static final String BD_URL = "jdbc:mysql://127.0.0.1:3306/mydb";
        public static String LOGIN;
        public static String PASSWORD;
        JPanel tabelPanel = new JPanel();
        JPanel auth = new Authorization();

        public static void menu(){
            new GraphicMenu();
        }

        GraphicMenu(){
            super("Авторизация");
            setSize(600,450);
            tabelPanel.setBounds(0,100,600,350);
            tabelPanel.add(auth);
            auth.setBounds(0,100,600,350);
            setContentPane(tabelPanel);
            setLayout(null);
            setVisible(true);
        }

        class Authorization extends JPanel {
            JTextField loginText = new JTextField("root");
            JTextField passwordText = new JTextField("101002Сщеуя");
            JButton authoButton = new JButton("Авторизация");
            JTextArea textLogin = new JTextArea("Логин");
            JTextArea textPassword = new JTextArea("Пароль");

            public Authorization(){
                textLogin.setBounds(150,0,75,25);
                add(textLogin);
                loginText.setBounds(200,0,125,25);
                add(loginText);

                textPassword.setBounds(150,50,75,25);
                add(textPassword);
                passwordText.setBounds(200,50,125,25);
                add(passwordText);

                authoButton.setBounds(225,100,100,25);
                add(authoButton);
                authoButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        try{
                            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
                            try(Connection conn = DriverManager.getConnection(BD_URL, loginText.getText(), passwordText.getText())){
                                System.out.println("Подключение произошло успешно");
                                LOGIN = loginText.getText();
                                PASSWORD = passwordText.getText();
                                new Tables();
                                dispose();
                                setVisible(false);
                            }
                            catch (Exception e){System.out.println("Неверный логин или пароль " + e);}
                        } catch(Exception e){System.out.println("Ошибка подключения: " + e);}
                    }
                });
            }
        }
    }

    static class Tables extends JFrame{
        JButton butBooks = new JButton("Книги");
        JButton butReaders = new JButton("Читатели");
        JButton butUserBooks = new JButton("Взятые Книги");

        JPanel tables = new JPanel(new CardLayout());

        JPanel panelBooks = new PanelBooks();
        JPanel panelReaders = new PanelReaders();
        JPanel panelUserBooks = new PanelUserBooks();

        LinkedList<Book> listBook = new LinkedList<Book>();
        LinkedList<Reader> listReader = new LinkedList<Reader>();
        LinkedList<UserBook> listUserBooks = new LinkedList<UserBook>();
        PanelBooks pb = new PanelBooks();
        PanelReaders pr = new PanelReaders();
        PanelUserBooks pub = new PanelUserBooks();

        Tables(){
            super("Библиотека");
            setSize(800,450);
            setLayout(null);

            CardLayout layout = (CardLayout)(tables.getLayout());
            add(tables);
            tables.setBounds(0,75,800,375);
            tables.add(panelBooks, "Книги");
            tables.add(panelReaders,"Читатели");
            tables.add(panelUserBooks,"Взятые Книги");

            add(butBooks);
            butBooks.setBounds(0,0,125,25);
            butBooks.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                        try(Connection conn = DriverManager.getConnection(GraphicMenu.BD_URL, GraphicMenu.LOGIN, GraphicMenu.PASSWORD)){
                            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
                            Statement statement = conn.createStatement();
                            ResultSet resultSet = statement.executeQuery("SELECT * FROM book");

                            while (resultSet.next()) {
                                listBook.add(new Book(
                                        resultSet.getInt("idBook"), resultSet.getString("RealseDate"),
                                        resultSet.getString("Name"), resultSet.getString("Author"),
                                        resultSet.getInt("Pages"), resultSet.getInt("idLibrary")
                                        ));
                            }
                        }
                        catch (Exception e){System.out.println("Ошибка подключения: " + e);}
                    pb.createPanel(listBook);
                    layout.show(tables, "Книги");
                }
            });

            add(butReaders);
            butReaders.setBounds(135,0,125,25);
            butReaders.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    try(Connection conn = DriverManager.getConnection(GraphicMenu.BD_URL, GraphicMenu.LOGIN, GraphicMenu.PASSWORD)){
                        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
                        Statement statement = conn.createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM reader");

                        while (resultSet.next()) {
                            listReader.add(new Reader(
                                    resultSet.getInt("idReader"), resultSet.getString("FIO"),
                                    resultSet.getString("DataBirthday"), resultSet.getDouble("Rating"),
                                    resultSet.getString("PlaceResident"), resultSet.getString("MobileNumber"),
                                    resultSet.getInt("idLibrary")
                            ));
                        }
                    }
                    catch (Exception e){System.out.println("Ошибка подключения: " + e);}
                    pr.createPanel(listReader);
                    layout.show(tables, "Читатели");
                }
            });

            add(butUserBooks);
            butUserBooks.setBounds(270,0,125,25);
            butUserBooks.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    try(Connection conn = DriverManager.getConnection(GraphicMenu.BD_URL, GraphicMenu.LOGIN, GraphicMenu.PASSWORD)){
                        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
                        Statement statement = conn.createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM reader");

                        while (resultSet.next()) {
                            listReader.add(new Reader(
                                    resultSet.getInt("idReader"), resultSet.getString("FIO"),
                                    resultSet.getString("DataBirthday"), resultSet.getDouble("Rating"),
                                    resultSet.getString("PlaceResident"), resultSet.getString("MobileNumber"),
                                    resultSet.getInt("idLibrary")
                            ));
                        }
                    }
                    catch (Exception e){System.out.println("Ошибка подключения: " + e);}
                    pub.createPanel(listReader);
                    layout.show(tables, "Взятые Книги");
                }
            });

            setVisible(true);
        }

        class PanelBooks extends JPanel{
            public void createPanel(LinkedList<Book> list){
                String[] columnName = new String[]{"Id","Дата Релиза","Название","Автор","Кол-во Страниц"};
                String[][] date = new String[list.size()][columnName.length];
                for(int i = 0; i < list.size(); i++){
                    date[i][0] = String.valueOf(listBook.get(i).idBook);
                    date[i][1] = list.get(i).realseDate;
                    date[i][2] = list.get(i).name;
                    date[i][3] = list.get(i).author;
                    date[i][4] = String.valueOf(list.get(i).pagers);
                }
                JTable table = new JTable(date, columnName);
                JScrollPane scrollPane = new JScrollPane(table);
                panelBooks.add(scrollPane);
                scrollPane.setBounds(0,0,800,375);
                panelBooks.setLayout(null);
            }
        }

        class PanelReaders extends  JPanel{
            public void createPanel(LinkedList<Reader> list){
                String[] columnName = new String[]{
                        "Id","ФИО","День Рождения","Рейтинг","Место жительства",
                        "Номер Телефона"
                };
                String[][] date = new String[list.size()][columnName.length];
                for(int i = 0; i < list.size(); i++){
                    date[i][0] = String.valueOf(listReader.get(i).idReader);
                    date[i][1] = listReader.get(i).FIO;
                    date[i][2] = listReader.get(i).DataBirthday;
                    date[i][3] = String.valueOf(listReader.get(i).Rating);
                    date[i][4] = listReader.get(i).PlaceResident;
                    date[i][5] = listReader.get(i).MobileNumber;
                }
                JTable table = new JTable(date, columnName);
                JScrollPane scrollPane = new JScrollPane(table);
                panelReaders.add(scrollPane);
                scrollPane.setBounds(0,0,800,375);
                panelReaders.setLayout(null);
            }
        }

        class PanelUserBooks extends JPanel{
            JTextField findUserText = new JTextField();
            JButton butSearch = new JButton("Поиск");
            JButton butAddUserBook = new JButton("Добавать");
            String[] columnName = new String[]{
                    "Id","Название","Автор"
            };
            LinkedList<UserBook> listUserBook;
            String[][] date = new String[25][3];
            JTable table;
            public void createPanel(LinkedList<Reader> listReaders){
                butSearch.setBounds(0,0,100,25);
                butAddUserBook.setBounds(300,0,100,25);
                findUserText.setBounds(110,0,100,25);
                panelUserBooks.add(butSearch);
                panelUserBooks.add(butAddUserBook);
                panelUserBooks.add(findUserText);
                table = new JTable(date, columnName);
                JScrollPane scrollPane = new JScrollPane(table);
                panelUserBooks.add(scrollPane);
                scrollPane.setBounds(50, 65, 700, 250);

                for(int i = 0; i < date.length; i++){
                    date[i][0] = " ";
                    date[i][1] = " ";
                    date[i][2] = " ";
                }

                butSearch.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        searchUser(listReaders);
                    }
                });

                butAddUserBook.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        new WindowAddUserBook();
                    }
                });

                panelUserBooks.setLayout(null);
            }

            public void searchUser(LinkedList<Reader> list){
                boolean isSearch = false;
                for(int i = 0; i < list.size(); i++){
                    if(findUserText.getText().equals(list.get(i).FIO)){
                        listUserBook =
                                getDateForRows(list.get(i).idReader);
                        isSearch = true;
                        if(isSearch){
                            createRow(listUserBook);
                        }
                        break;
                    }
                }
            }

            public void createRow (LinkedList<UserBook> list){
                date = new String[list.size()][columnName.length];
                for (int i = 0; i < list.size(); i++) {
                    date[i][0] = String.valueOf(list.get(i).idUserBook);
                    date[i][1] = list.get(i).name;
                    date[i][2] = list.get(i).author;
                    table.setValueAt(date[i][0], i, 0);
                    table.setValueAt(date[i][1], i, 1);
                    table.setValueAt(date[i][2], i, 2);
                }
            }

            public LinkedList<UserBook> getDateForRows(int idUser){
                LinkedList<UserBook> list = new LinkedList<UserBook>();
                try(Connection conn = DriverManager.getConnection(GraphicMenu.BD_URL, GraphicMenu.LOGIN, GraphicMenu.PASSWORD)){
                    Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM userbook, book " +
                            "WHERE userbook.idUserBook = book.idBook AND userbook.idUser = " + idUser);
                    while (resultSet.next()) {
                        list.add(new UserBook(resultSet.getInt("userbook.idUserBook"),
                                resultSet.getString("book.Name"),resultSet.getString("book.Author")));
                    }
                }
                catch (Exception e){System.out.println("Ошибка подключения: " + e);}
            return list;
            }

            public LinkedList<Book> getBooks(){
                LinkedList<Book> list = new LinkedList<Book>();
                try(Connection conn = DriverManager.getConnection(GraphicMenu.BD_URL, GraphicMenu.LOGIN, GraphicMenu.PASSWORD)){
                    Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM book");

                    while (resultSet.next()) {
                        list.add(new Book(
                                resultSet.getInt("idBook"), resultSet.getString("RealseDate"),
                                resultSet.getString("Name"), resultSet.getString("Author"),
                                resultSet.getInt("Pages"), resultSet.getInt("idLibrary")
                        ));
                    }
                }
                catch (Exception e){System.out.println("Ошибка подключения: " + e);}
                return list;
            }

            public class WindowAddUserBook extends JFrame{
                JTextField userText = new JTextField();
                JTextField bookNameText = new JTextField();
                JButton butAdd = new JButton("Добавить");
                int idB;
                int idU;
                public WindowAddUserBook(){
                    super();
                    setLocation(500,500);
                    setSize(450,450);
                    userText.setBounds(75,0,200,25);
                    bookNameText.setBounds(75,50,200,25);
                    butAdd.setBounds(75,100,100,25);
                    add(userText);
                    add(bookNameText);
                    add(butAdd);
                    butAdd.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            LinkedList<Book> listBook = getBooks();
                            for(int i = 0; i < listBook.size(); i++){
                                if(listBook.get(i).name.equals(bookNameText)){
                                    idB = listBook.get(i).idBook;
                                    break;
                                }
                            }

                            for(int i = 9; i < listReader.size(); i++){
                                if(listReader.get(i).FIO.equals(userText)){
                                    idU = listReader.get(i).idReader;
                                    break;
                                }

                                addUserBook(idU, idB);
                            }
                        }
                    });
                    setLayout(null);
                    setVisible(true);
                }
            }

            public void addUserBook(int idUser, int idBooks){
                LinkedList<UserBook> listUserBook = getDateForRows(idUser);
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
                    try (Connection conn = DriverManager.getConnection(GraphicMenu.BD_URL, GraphicMenu.LOGIN, GraphicMenu.PASSWORD)) {

                        Statement statement = conn.createStatement();
                        int row = statement.executeUpdate("INSERT INTO userbook(idUserBook, idBook, idUser) VALUES (4, 1, 1)");
                        System.out.println(row);
                        listUserBook = getDateForRows(idUser);
                        createRow(listUserBook);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                } catch (Exception e){System.out.println(e);}
            }
        }
    }
}

class Book {
    int idBook;
    String realseDate;
    String name;
    String author;
    int pagers;
    int idLibary;

    Book(int idBook, String realseDate, String name, String author, int pagers, int idLibary){
        this.idBook = idBook;
        this.realseDate = realseDate;
        this.name = name;
        this.author = author;
        this.pagers = pagers;
        this.idLibary = idLibary;
    }
}

class Reader{
    int idReader;
    String FIO;
    String DataBirthday;
    double Rating;
    String PlaceResident;
    String MobileNumber;
    int idLibrary;

    Reader(int idReader, String FIO, String DataBirthday, double Rating, String PlaceResident,
           String MobileNumber, int idLibrary){
        this.idReader = idReader;
        this.FIO = FIO;
        this.DataBirthday = DataBirthday;
        this.Rating = Rating;
        this.PlaceResident = PlaceResident;
        this.MobileNumber = MobileNumber;
        this.idLibrary = idLibrary;
    }
}

class UserBook{
    int idUserBook;
    String name;
    String author;
    UserBook(int idUserBook, String name, String author){
        this.idUserBook = idUserBook;
        this.name = name;
        this.author = author;
    }
}