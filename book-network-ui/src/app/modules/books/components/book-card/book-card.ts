import {Component, inject, Input, signal} from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { BookResponse } from '../../../../services/models/book-response';
import {borrowBook} from '../../../../services/fn/books/borrow-book';
import {HttpClient} from '@angular/common/http';
import {ApiConfiguration} from '../../../../services/api-configuration';

@Component({
  selector: 'app-book-card',
  imports: [DecimalPipe],
  templateUrl: './book-card.html',
  styleUrl: './book-card.scss',
})
export class BookCard {

  private http = inject(HttpClient);
  private apiConfig = inject(ApiConfiguration);

  errorMessage = signal<Array<string>>([]);

  get book():BookResponse{
    return this._book;
  }
  @Input()
  set book(book:BookResponse){
    this._book = book;
  }

  private _book: BookResponse = {} as BookResponse

  protected borrow() {
    borrowBook(this.http, this.apiConfig.rootUrl,{
      'book-id':this._book.id!
    }).subscribe({
      next: () => {},
      error: (err) => {
        this.handleError(err)
      }
    });
  }

  private handleError(err: any) {
    const body = typeof err.error === 'string' ? JSON.parse(err.error) : err.error;
    this.errorMessage.set(body?.validationErrors ?? (body?.error ? [body.error] : ['An error occurred']));
  }
}
