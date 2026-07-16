import { Component, Input } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { BookResponse } from '../../../../services/models/book-response';

@Component({
  selector: 'app-book-card',
  imports: [DecimalPipe],
  templateUrl: './book-card.html',
  styleUrl: './book-card.scss',
})
export class BookCard {

  get book():BookResponse{
    return this._book;
  }
  @Input()
  set book(book:BookResponse){
    this._book = book;
  }

  private _book:BookResponse ={}
}
