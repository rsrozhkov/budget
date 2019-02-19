import { Component, OnInit } from '@angular/core';
import {Chart} from 'chart.js';
import {Transaction} from "../../classes/transaction";
import {TransactionService} from "../../services/transaction.service";

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css']
})
export class ChartComponent implements OnInit {
  lineChart = [];
  private transactions: Transaction[];
  private labels = [];
  private data = [];

  constructor(private transactionService: TransactionService) { }

  /** Получение данных с сервера при инициализации компонента */
  ngOnInit() {
    this.transactionService.getTransactions()
      .subscribe(transactions => {
        this.transactions = transactions;
        this.initChartData();
        this.drawChart();
      });
  }

  /** Подготовка данных для отрисовки */
  private initChartData() {
    for(let i =0, total=0, len = this.transactions.length, last = len-1; i < len; i++) {
      let date = this.transactions[i].date;
      let amount = this.transactions[i].amount/100;
      if (i<last && this.transactions[i+1].date == date) {
        total = amount +total;
      } else {
        this.labels.push(date);
        total = amount +total;
        this.data.push(total);
      }
    }
  }

  /** Отрисовка графика */
  private drawChart() {
    this.lineChart = new Chart('lineChart', {
      type: 'line',
      data: {
        labels: this.labels,
        datasets: [{
          label: 'Средств  в бюджете',
          data: this.data,
          fill:true,
          lineTension:0.2,
          borderColor:"red",
          borderWidth: 1
        }]
      },
      options: {
        scales: {
          xAxes: [{
            type: 'time',
            time: {
              unit: 'day',
              displayFormats: {
                'day': 'DD.MM.YYYY'
              }
            },
            ticks: {
              beginAtZero:true
            }
          }],
          yAxes: [{
            ticks: {
              beginAtZero:true
            }
          }]
        }
      }
    });
  }
}
