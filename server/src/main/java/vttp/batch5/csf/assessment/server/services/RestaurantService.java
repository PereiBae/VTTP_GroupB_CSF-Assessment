package vttp.batch5.csf.assessment.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vttp.batch5.csf.assessment.server.models.MenuItem;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;

import java.util.List;

@Service
public class RestaurantService {

  @Autowired
  private OrdersRepository ordersRepo;

  // TODO: Task 2.2
  // You may change the method's signature
  public List<MenuItem> getMenu() {

    return ordersRepo.getMenu();

  }
  
  // TODO: Task 4


}
