import {Component, inject, OnInit, signal} from '@angular/core';
import {DecimalPipe} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {Pagination} from '../../components/pagination/pagination';
import {BasePage} from '../base-page';
import {HttpClient} from '@angular/common/http';
import {ApiConfiguration} from '../../../../services/api-configuration';
import {BookResponse} from '../../../../services/models/book-response';
import {BookRequest} from '../../../../services/models/book-request';
import {PageResponseBorrowedBookResponse} from '../../../../services/models/page-response-borrowed-book-response';
import {BorrowedBookResponse} from '../../../../services/models/borrowed-book-response';

@Component({
  selector: 'app-my-returned-books',
  imports: [
    DecimalPipe,
    FormsModule,
    Pagination,
    ReactiveFormsModule
  ],
  templateUrl: './my-returned-books.html',
  styleUrl: './my-returned-books.css',
})
export class MyReturnedBooks extends BasePage implements OnInit {
  protected override loadData(): void {
      throw new Error("Method not implemented.");
  }
  private http = inject(HttpClient);
  private apiConfig = inject(ApiConfiguration);

  books = signal<Array<BorrowedBookResponse>>([]);
  errorMessage = signal<Array<string>>([]);

  ngOnInit() {
    this.loadData()
  }

  protected approveReturn(number: number) {

  }
}
