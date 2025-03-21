package vttp.batch5.csf.assessment.server.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vttp.batch5.csf.assessment.server.models.MenuItem;

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
  
}
