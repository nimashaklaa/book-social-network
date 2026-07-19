import {Component, inject, OnInit, signal} from '@angular/core';
import {DatePipe, DecimalPipe} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {Pagination} from '../../components/pagination/pagination';
import {BasePage} from '../base-page';
import {HttpClient} from '@angular/common/http';
import {ApiConfiguration} from '../../../../services/api-configuration';
import {BorrowedBookResponse} from '../../../../services/models/borrowed-book-response';
import {findAllReturnedBooks} from '../../../../services/fn/books/find-all-returned-books';
import {PageResponseBookResponse} from '../../../../services/models/page-response-book-response';
import {approveReturnBook} from '../../../../services/fn/books/approve-return-book';

@Component({
  selector: 'app-my-returned-books',
  imports: [
    DecimalPipe,
    FormsModule,
    Pagination,
    ReactiveFormsModule,
    DatePipe
  ],
  templateUrl: './my-returned-books.html',
  styleUrl: './my-returned-books.css',
})
export class MyReturnedBooks extends BasePage implements OnInit {

  private httpClient = inject(HttpClient);
  private apiConfig = inject(ApiConfiguration);

  books = signal<Array<BorrowedBookResponse>>([]);
  errorMessage = signal<Array<string>>([]);
  isDrawerOpen = signal<boolean>(false);
  selectedBook = signal<BorrowedBookResponse|undefined>(undefined);

  ngOnInit() {
    this.loadData()
  }

  protected override loadData(): void {
    findAllReturnedBooks(this.httpClient, this.apiConfig.rootUrl, { page: this.page, size: this.size })
      .subscribe({
      next: (response) => {
        const pageData = response.body as PageResponseBookResponse;
        this.books.set(pageData?.content ?? []);
        console.log('returned books', pageData?.content);
        this.setPageData(pageData?.first ?? true, pageData?.last ?? false, pageData?.totalPages ?? 0);
      },
      error: (err) => {
        this.handleError(err)
      }
    });
  }

  protected approveReturn(bookId: number) {
    approveReturnBook(this.httpClient, this.apiConfig.rootUrl, {
      'book-id': bookId
    }).subscribe({
      next:(response) => {
        const returnedNumber = response.body;
        console.log('Returned value:', returnedNumber);
        console.log('HTTP Status:', response.status);
      },
      error: (err) => {
        this.handleError(err);
      }
    });
  }

  private handleError(err: any) {
    const body = typeof err.error === 'string' ? JSON.parse(err.error) : err.error;
    this.errorMessage.set(body?.validationErrors ?? (body?.error ? [body.error] : ['An error occurred']));
  }

  protected openDetails(book: BorrowedBookResponse) {
    console.log('selected book', book);
    this.selectedBook.set(book);
    this.isDrawerOpen.set(true);
  }

  protected closeDrawer() {
    this.isDrawerOpen.set(false);
    this.selectedBook.set(undefined);
  }
}
