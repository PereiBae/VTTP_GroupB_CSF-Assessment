import {Component, inject} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Router} from '@angular/router';
import {MenuStoreService} from '../../menu-store.service';

@Component({
  selector: 'app-place-order',
  standalone: false,
  templateUrl: './place-order.component.html',
  styleUrl: './place-order.component.css'
})
export class PlaceOrderComponent {

  // TODO: Task 3
  private fb = inject(FormBuilder)
  private router = inject(Router)
  private menuStore = inject(MenuStoreService)

  protected form!: FormGroup



}
