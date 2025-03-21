package vttp.batch5.csf.assessment.server.repositories;

import jakarta.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;

// Use the following class for MySQL database
@Repository
public class RestaurantRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int checkUser(String username, String password) {

        String sql = "SELECT COUNT(*) FROM customers WHERE username = ?";
        Integer userCount = jdbcTemplate.queryForObject(sql, Integer.class, username);

        if (userCount == 0 || userCount == null) {
            // username does not exist
            return 1;
        }

        String checkPasswordSql = "SELECT COUNT(*) FROM customers WHERE username = ? AND password = SHA2(?, 224)";
        Integer passwordMatch = jdbcTemplate.queryForObject(checkPasswordSql, Integer.class, username, password);

        if (passwordMatch == null || passwordMatch == 0) {
            // Password doesn't match
            return 2;
        }

        return 0;
    }

    public void save(JsonObject jsonObject, String username){

        String INSERT_SQL = "INSERT INTO place_orders(order_id,payment_id,order_date,total,username) VALUES(?,?,?,?,?)";

        jdbcTemplate.update(INSERT_SQL, jsonObject.getString("order_id"), jsonObject.getString("payment_id"), new Date(jsonObject.getJsonNumber("timestamp").longValue()) ,jsonObject.getJsonNumber("total").doubleValue() , username);

        System.out.println("Inserted order id: " + jsonObject.getString("order_id"));
    }

}
