import {Component, inject, OnInit} from '@angular/core';
import {RestaurantService} from '../../restaurant.service';
import {Observable, Subscription} from 'rxjs';
import {MenuItem} from '../../models';
import {MenuStoreService} from '../../menu-store.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-menu',
  standalone: false,
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css',
  providers: [MenuStoreService]
})
export class MenuComponent implements OnInit{
  // TODO: Task 2

  private menuStore = inject(MenuStoreService)
  private router = inject(Router)

  menuItems$: Observable<MenuItem[]> = this.menuStore.menuItems$;
  totalCost$: Observable<number> = this.menuStore.totalCost$;
  totalCount$: Observable<number> = this.menuStore.totalCount$;

  totalPrice!: number
  itemCount!: number
  quantity!: number

  ngOnInit() {
    this.menuStore.loadMenuItems()
  }

  // Add an item to the order
  addItem(itemId: string): void {
    this.menuStore.addItem(itemId);
  }

  // Remove an item from the order
  removeItem(itemId: string): void {
    this.menuStore.removeItem(itemId);
  }

  // Get the quantity of a specific item
  getQuantity(itemId: string): Observable<number> {
    return this.menuStore.getQuantity(itemId);
  }

  // Format price to display with 2 decimal places
  formatPrice(price: number): string {
    return price.toFixed(2);
  }

  // Navigate to the place order page
  placeOrder(): void {
    // Get selected items with quantities
    this.menuStore.getSelectedItemsWithQuantities.subscribe(selectedItems => {
      // Store in session storage to share with place-order component
      sessionStorage.setItem('selectedItems', JSON.stringify(selectedItems));

      // Navigate to place order page
      this.router.navigate(['/place-order']);
    });
  }

}
