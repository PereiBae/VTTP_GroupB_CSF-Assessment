import {Component, inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {MenuStoreService} from '../../menu-store.service';
import {Observable} from 'rxjs';
import {MenuItem, OrderItem} from '../../models';
import {RestaurantService} from '../../restaurant.service';

@Component({
  selector: 'app-place-order',
  standalone: false,
  templateUrl: './place-order.component.html',
  styleUrl: './place-order.component.css'
})
export class PlaceOrderComponent implements OnInit{

  // TODO: Task 3
  private fb = inject(FormBuilder)
  private router = inject(Router)
  private menuStore = inject(MenuStoreService)
  private restaurantService = inject(RestaurantService)

  protected form!: FormGroup

  orderItems: OrderItem[]=[]
  totalAmount: number = 0;

  ngOnInit(){
    this.form = this.createForm()
    this.loadOrderItems()
  }

  createForm(){
    return this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    })
  }

  loadOrderItems(): void{
    const itemsJson = sessionStorage.getItem('selectedItems');
    if (itemsJson) {
      this.orderItems = JSON.parse(itemsJson);

      // Calculate subtotals and total
      this.calculateTotals();
    } else {
      // No items selected, redirect back to menu
      this.router.navigate(['/']);
    }
  }

  calculateTotals(): void {
    // Calculate subtotal for each item
    this.orderItems.forEach(item => {
      item.subtotal = item.price * item.quantity;
    });

    // Calculate total amount
    this.totalAmount = this.orderItems.reduce((total, item) =>
      total + (item.subtotal || 0), 0);
  }

  formatPrice(price: number): string {
    return price.toFixed(2);
  }

  startOver(): void {
    // Clear session storage and navigate back to menu
    sessionStorage.removeItem('selectedItems');
    this.router.navigate(['/']);
  }

  processOrder(){
      const orderData = {
        username: this.form.value.username,
        password: this.form.value.password,
        items: this.orderItems
      };

      // Call service to place order
      this.restaurantService.placeOrder(orderData).subscribe({
        next: (response) => {
          console.info(response);
          // Store order details for confirmation page
          sessionStorage.setItem('orderConfirmation', JSON.stringify(response));

          // Navigate to confirmation page
          this.router.navigate(['/confirmation']);
        },
        error: (error) => {
          console.error('Error placing order:', error);
          // Handle error (could add an error message to the UI)
          alert("Error 400: "+ error.message);
        }
      });
    }

}
