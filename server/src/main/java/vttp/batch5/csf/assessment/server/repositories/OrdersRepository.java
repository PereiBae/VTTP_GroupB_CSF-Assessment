package vttp.batch5.csf.assessment.server.repositories;

import jakarta.json.JsonObject;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vttp.batch5.csf.assessment.server.models.MenuItem;

import java.util.Date;
import java.util.List;


@Repository
public class OrdersRepository {

  @Autowired
  private MongoTemplate mongoTemplate;

  // TODO: Task 2.2
  // You may change the method's signature
  // Write the native MongoDB query in the comment below
  //
  //  Native MongoDB query here
  //
  public List<MenuItem> getMenu() {

    // Create a query with a sort on the name field
    Query query = new Query();
    query.with(Sort.by(Sort.Direction.ASC, "name"));

    // Find all documents and apply the sort
    return mongoTemplate.find(query, MenuItem.class, "menus");

  }

  // TODO: Task 4
  // Write the native MongoDB query for your access methods in the comment below
  //
  //  Native MongoDB query here

  public void save(String orderId, JsonObject fromAng, JsonObject fromPayment) {

    Document doc = new Document();
    doc.put("_id", orderId);
    doc.put("order_id", orderId);
    doc.put("payment_id", fromPayment.get("payment_id"));
    doc.put("username", fromAng.get("username"));
    doc.put("total", fromPayment.get("total"));
    doc.put("timestamp", new Date(fromPayment.getJsonNumber("timestamp").longValue()) );
    doc.put("items", fromAng.get("items"));
    mongoTemplate.save(doc, "orders");

    System.out.println("Document saved in orders: " + doc);
  }
}
