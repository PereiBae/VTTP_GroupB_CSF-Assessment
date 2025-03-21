package vttp.batch5.csf.assessment.server.services;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import vttp.batch5.csf.assessment.server.models.MenuItem;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;
import vttp.batch5.csf.assessment.server.repositories.RestaurantRepository;

import java.io.StringReader;
import java.util.List;
import java.util.UUID;

@Service
public class RestaurantService {

  @Autowired
  private OrdersRepository ordersRepo;

  @Autowired
  private RestaurantRepository restaurantRepo;

  private final String PAYMENT_API_URL = "https://payment-service-production-a75a.up.railway.app/api/payment";

  // TODO: Task 2.2
  // You may change the method's signature
  public List<MenuItem> getMenu() {

    return ordersRepo.getMenu();

  }
  
  // TODO: Task 4
  public JsonObject postFoodOrder(JsonObject payload) {

    try{
      String username = payload.getString("username");
      String password = payload.getString("password");
      int userExists = restaurantRepo.checkUser(username, password);
      if (userExists == 1) {
        System.out.println("User " + username + " does not exist");
        return Json.createObjectBuilder()
                .add("message", "Invalid username")
                .build();
      } else if (userExists == 2) {
        System.out.println("User " + username + " has entered the wrong password");
        return Json.createObjectBuilder()
                .add("message", "Invalid password")
                .build();
      }

      String orderId = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
      JsonArray jsonArray = payload.getJsonArray("items");
      int payment = 0;
      for (int i = 0; i < jsonArray.size(); i++) {
        JsonObject item = jsonArray.getJsonObject(i);
        int quantity = item.getInt("quantity");
        double price = item.getJsonNumber("price").doubleValue();
        payment += quantity * price;
      }
      System.out.println("Total amount to be paid: " + payment);

      RestTemplate restTemplate = new RestTemplate();
      JsonObject requestPayload = Json.createObjectBuilder()
              .add("order_id", orderId)
              .add("payer", username)
              .add("payee", username)
              .add("payment", payment)
              .build();

      String url = UriComponentsBuilder.fromUriString(PAYMENT_API_URL)
              .build()
              .toUriString();
      System.out.println("URL BUILT: " + url);

      RequestEntity<String> request = RequestEntity
              .post(url)
              .accept(MediaType.APPLICATION_JSON)
              .contentType(MediaType.APPLICATION_JSON)
              .header("X-Authenticate", username)
              .body(requestPayload.toString());

      ResponseEntity<String> response = restTemplate.exchange(request, String.class);
      System.out.println("Response received: " + response.getBody());

      if (response.getStatusCode().equals(HttpStatus.OK)) {
        System.out.println("Payment successful");
        String responsePayload = response.getBody();
        JsonReader jsonReader = Json.createReader(new StringReader(responsePayload));
        JsonObject jsonObject = jsonReader.readObject();
        try{
          ordersRepo.save(orderId, payload, jsonObject);
          System.out.println("Mongo Completed, moving to SQL");
          restaurantRepo.save(jsonObject, username);
          System.out.println("MySQL Completed, building JsonObject receipt");

          return Json.createObjectBuilder()
                  .add("order_id", orderId)
                  .add("paymentId", jsonObject.getString("payment_id"))
                  .add("total", jsonObject.getJsonNumber("total"))
                  .add("timestamp", jsonObject.getJsonNumber("timestamp"))
                  .build();
        } catch (Exception e){
          e.printStackTrace();
          return Json.createObjectBuilder()
                  .add("message", "Order processing failed. Please try again.")
                  .build();
        }
      } else{

        System.out.println("Payment failed, " + response.getStatusCode() + " " + response.getBody());
        String responsePayload = response.getBody();
        JsonReader jsonReader = Json.createReader(new StringReader(responsePayload));
        JsonObject jsonObject = jsonReader.readObject();

        return Json.createObjectBuilder()
                .add("message", jsonObject.getString("error"))
                .build();
      }
    } catch (Exception e) {
      e.printStackTrace();
      return Json.createObjectBuilder()
              .add("message", "An unexpected error occurred. Please try again.")
              .build();
    }

  }

}
