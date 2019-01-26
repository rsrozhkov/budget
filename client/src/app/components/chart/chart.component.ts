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
    this.labels.push("");
    this.data.push(0);
    for(let i =0, total=0, len = this.transactions.length; i < len; i++) {
      this.labels.push(this.transactions[i].date);
      total = this.transactions[i].amount/100 +total;
      this.data.push(total);
    }
  }

  /** Отрисовка графика */
  private drawChart() {
    this.lineChart = new Chart('lineChart', {
      type: 'line',
      data: {
        labels: this.labels,
        datasets: [{
          label: 'Средств в бюджете',
          data: this.data,
          fill:false,
          lineTension:0.2,
          borderColor:"red",
          borderWidth: 1
        }]
      },
      options: {
        title:{
          text:"График бюджета",
          display:true
        },
        scales: {
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
