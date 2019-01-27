import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable,of} from "rxjs";
import {Transaction} from "../classes/transaction";
import {catchError, tap} from "rxjs/operators";
const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private transactionsUrl = '//localhost:8080/transactions';
  private balanceUrl = `${this.transactionsUrl}/balance`;

  constructor(private http: HttpClient) { }

  /** GET: get total balance from the server */
  getBalance(): Observable<any> {
    return this.http.get(this.balanceUrl)
      .pipe(
        tap(_ => console.log('fetched balance')),
        catchError(this.handleError('getBalance', []))
      );
  }

  /** GET: get all transactions from the server */
  getTransactions(): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(this.transactionsUrl)
      .pipe(
        tap(_ => console.log('fetched transactions')),
        catchError(this.handleError('getTransactions', []))
      );
  }

  /** GET: get transactions from the server between two dates*/
  getBetweenDates(start: Date, end: Date): Observable<Transaction[]> {
    const url = `${this.transactionsUrl}/withdrawBetweenDates/${start}/${end}`;
    return this.http.get<Transaction[]>(url)
      .pipe(
        tap(_ => console.log('fetched transactions between dates')),
        catchError(this.handleError('getBetweenDates', []))
      );
  }

  /** GET: check if the member has transactions */
  checkForOwner(id: number): Observable<boolean> {
    const url = `${this.transactionsUrl}/checkForOwner/${id}`;
    return this.http.get<boolean>(url).pipe(
      tap(_ => console.log(`member with id = ${id} has transactions`)),
      catchError(this.handleError<boolean>('checkForOwner')));
  }

  /** POST: add a new transaction to the server */
  addTransaction (transaction: Transaction): Observable<Transaction> {
    return this.http.post<Transaction>(this.transactionsUrl, transaction, httpOptions).pipe(
      tap((transaction: Transaction) => console.log(`added transaction w/ id=${transaction.id}`)),
      catchError(this.handleError<Transaction>('addTransaction'))
    );
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      console.error(error); // log to console instead

      console.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
