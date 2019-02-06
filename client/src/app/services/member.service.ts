import { Injectable } from '@angular/core';
import {Member} from "../classes/member";
import {Observable, of} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, tap} from "rxjs/operators";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})

export class MemberService {
  private membersUrl = '//localhost:8080/members';  // URL to web api
  //private membersUrl = '//ateh.info/members';  // URL to web api

  constructor(private http: HttpClient) { }

  /** GET: get all members from the server */
  getMembers(): Observable<Member[]> {
    return this.http.get<Member[]>(this.membersUrl)
      .pipe(
        tap(_ => console.log('fetched members')),
        catchError(this.handleError('getMembers', []))
      );
  }

  /** GET: get a single member from the server */
  getMember(id: number): Observable<Member> {
    const url = `${this.membersUrl}/${id}`;
    return this.http.get<Member>(url).pipe(
      tap(_ => console.log(`fetched member id=${id}`)),
      catchError(this.handleError<Member>(`getMember id=${id}`))
    );
  }

  /** PUT: update a member on the server */
  updateMember(member: Member): Observable<any> {
    const url = `${this.membersUrl}/${member.id}`;
    return this.http.put(url, member, httpOptions).pipe(
      tap(_ => console.log(`updated member id=${member.id}`)),
      catchError(this.handleError<any>('updateMember'))
    );
  }

  /** POST: add a new member to the server */
  addMember (member: Member): Observable<Member> {
    return this.http.post<Member>(this.membersUrl, member, httpOptions).pipe(
      tap((member: Member) => console.log(`added member w/ id=${member.id}`)),
      catchError(this.handleError<Member>('addMember'))
    );
  }

  /** DELETE: delete the member from the server */
  deleteMember (member: Member | number): Observable<Member> {
    const id = typeof member === 'number' ? member : member.id;
    const url = `${this.membersUrl}/${id}`;

    return this.http.delete<Member>(url, httpOptions).pipe(
      tap(_ => console.log(`deleted member id=${id}`)),
      catchError(this.handleError<Member>('deleteMember'))
    );
  }

  /** GET: check whether the name is taken */
  isNameTaken(name: string): Observable<boolean> {
    const url = `${this.membersUrl}/isNameTaken/${name}`;
    return this.http.get<boolean>(url).pipe(
      tap(_ => console.log(`${name} checked for uniqueness`)),
      catchError(this.handleError<boolean>('isNameTaken')));
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
