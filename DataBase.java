import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class DataBase {
    public static void main(String[] args) throws SQLException{

        init();

        try(Connection connection = getConnection()){
            statments(connection);
            resultSet(connection);
            prepare(connection);
            resultSet(connection);

        }
    }

    private static void resultSet(Connection connection) throws SQLException{
        try(Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery("SELECT * FROM userData");
            while (resultSet.next()){
                //System.out.println(resultSet.getInt("id") + resultSet.getString("name") + resultSet.getString("nick"));
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String nick = resultSet.getString("nick");
                System.out.println(id + " " + name + " " + nick);
            }
        }
    }

    private static void prepare(Connection connection) throws SQLException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


        try {
            System.out.println("Введите имя");
            String name = reader.readLine();
            System.out.println("Введите ник");
            String nick = reader.readLine();
            System.out.println("введите пароль");
            String password = reader.readLine();


            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO 'userData'('name', 'nick', 'password') VALUES (?,?,?)")) {
                preparedStatement.execute("INSERT INTO 'userData'('name', 'nick', 'password') VALUES ('" + name +"', '" + nick +"', '" + password + "')"  );
               /* preparedStatement.setString(2, nick);
                preparedStatement.setString(3, password);*/

                preparedStatement.addBatch();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void statments(Connection connection) throws SQLException{
        try(Statement statement = connection.createStatement()){
            statement.execute("CREATE TABLE if not exists 'userData'" +
                    "('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text, " +
                    "'nick' text, 'password' text)");
        }
    }

    public static void init() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:userData.s2db");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }return null;
    }
}
