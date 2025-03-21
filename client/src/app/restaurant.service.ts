import {HttpClient} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import {ConfirmOrder, MenuItem, OrderItem} from './models';

@Injectable({
  providedIn: 'root'
})
export class RestaurantService {

  private http = inject(HttpClient)

  // TODO: Task 2.2
  // You change the method's signature but not the name
  getMenuItems() {

    return this.http.get<MenuItem[]>('/api/menu')

  }

  // TODO: Task 3.2

  placeOrder(orderData:{
    username: string,
    password: string,
    items: OrderItem[]
  }) {
    const order: ConfirmOrder={
      username: orderData.username,
      password:orderData.password,
      items: orderData.items.map(item=>({
        id: item.id,
        price: item.price,
        quantity: item.quantity
      }))
    }
    return this.http.post<any>('/api/food_order', order)
  }

}
