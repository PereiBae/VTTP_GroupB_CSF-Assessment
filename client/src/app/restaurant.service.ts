import {HttpClient} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import {MenuItem} from './models';

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
}
