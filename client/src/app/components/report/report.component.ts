import { Component, OnInit } from '@angular/core';
import {Transaction} from "../../classes/transaction";
import {TransactionService} from "../../services/transaction.service";
import {forEach} from "@angular/router/src/utils/collection";

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent implements OnInit {
  startDate: Date;
  endDate: Date;
  transactions: Transaction[];

  constructor(private transactionService: TransactionService) { }

  ngOnInit() {
  }

  /** Получает с сервера все отрицательные транзакции между датами */
  load(startDate: Date, endDate: Date){
    this.transactionService.getBetweenDates(startDate,endDate)
      .subscribe(transactions => this.transactions = transactions);
  }
}
