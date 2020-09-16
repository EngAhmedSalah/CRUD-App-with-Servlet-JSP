package DAO;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//this class for providing CRUD operations
public class UserDAO
{
    private String jdbcurl = "jdbc:mysql://localhost/demo?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private String username = "ahmed";
    private String password = "root";

    private static final String INSERT_USERS_SQL = "INSERT INTO users" + "  (name, email, country) VALUES " +" (?, ?, ?);";
    private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id =?";
    private static final String SELECT_ALL_USERS = "select * from users";
    private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
    private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, country =? where id = ?;";

    //test connection
    protected Connection getConnection()
    {
        Connection con = null;
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(jdbcurl, username, password);
        }
        catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
        return con;
    }

    //insert user
    public void insertUser(User user)
    {
        try (Connection con = getConnection(); PreparedStatement prep = con.prepareStatement(INSERT_USERS_SQL);)
        {
            prep.setString(1 , user.getName());
            prep.setString(2 , user.getEmail());
            prep.setString(3 , user.getCountry());
            prep.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    //Select user by ID
    public User selectUser(int id)
    {
        User user  = null;
        try (Connection con = getConnection(); PreparedStatement prep = con.prepareStatement(SELECT_USER_BY_ID);)
        {
            prep.setInt(1 , id);
            ResultSet res = prep.executeQuery();
            while(res.next())
            {
                String name = res.getString("name");
                String email = res.getString("email");
                String country = res.getString("country");
                user  = new User(id , name , email , country);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return user ;
    }

    //update user
    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getCountry());
            statement.setInt(4, user.getId());

            rowUpdated = statement.executeUpdate() > 0;
        }

        return rowUpdated;
    }
    //Select All Users
    public List<User> selectAllUsers()
    {
        List<User> users = new ArrayList<>();
        try (Connection con = getConnection(); )
        {
            Statement prep = con.createStatement();
            ResultSet res = prep.executeQuery(SELECT_ALL_USERS);
            while(res.next())
            {
                int id = res.getInt("id");
                String name = res.getString("name");
                String email = res.getString("email");
                String country = res.getString("country");
                users.add(new User(id , name , email , country));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return users;
    }

    //Delete User
    public boolean deleteUser(int id)
    {
        boolean deletedUser = false;
        try (Connection con = getConnection(); PreparedStatement prep = con.prepareStatement(DELETE_USERS_SQL);)
        {
            prep.setInt(1 , id);
            deletedUser = prep.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return deletedUser;
    }
}
