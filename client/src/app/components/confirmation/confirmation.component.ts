import {Component, OnInit} from '@angular/core';
import {Receipt} from '../../models';

@Component({
  selector: 'app-confirmation',
  standalone: false,
  templateUrl: './confirmation.component.html',
  styleUrl: './confirmation.component.css'
})
export class ConfirmationComponent implements OnInit{

  // TODO: Task 5
  date!: Date;
  receipt!: Receipt;


  ngOnInit() {
    const receiptJson = sessionStorage.getItem('orderConfirmation');
    if (receiptJson) {
      this.receipt = JSON.parse(receiptJson);
    }
    this.date = new Date(this.receipt.timestamp);
  }


}
