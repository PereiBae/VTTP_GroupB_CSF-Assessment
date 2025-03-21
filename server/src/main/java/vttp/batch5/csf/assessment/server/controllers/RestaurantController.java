package vttp.batch5.csf.assessment.server.controllers;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vttp.batch5.csf.assessment.server.models.MenuItem;
import vttp.batch5.csf.assessment.server.services.RestaurantService;

import java.io.StringReader;
import java.util.List;

@Controller
@RequestMapping("/api")
public class RestaurantController {

  @Autowired
  private RestaurantService restaurantService;

  // TODO: Task 2.2
  // You may change the method's signature
  @GetMapping(value = "/menu", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<List<MenuItem>> getMenus() {

    System.out.println("getMenus called");
    List<MenuItem> menuItems = restaurantService.getMenu();

    return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(menuItems);
  }

  // TODO: Task 4
  // Do not change the method's signature
  @PostMapping(value = "/food_order", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<String> postFoodOrder(@RequestBody String payload) {
    System.out.println(payload);

    JsonObject jsonPayload = Json.createReader(new StringReader(payload)).readObject();

    JsonObject response = restaurantService.postFoodOrder(jsonPayload);
    if (response.containsKey("message")) {
      return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(response.toString());
    }

    return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(response.toString());
  }
}
