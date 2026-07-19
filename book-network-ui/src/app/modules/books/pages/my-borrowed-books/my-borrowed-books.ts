import {Component, inject, OnInit, signal} from '@angular/core';
import {DatePipe, DecimalPipe} from "@angular/common";
import {Pagination} from "../../components/pagination/pagination";
import {BasePage} from '../base-page';
import {HttpClient} from '@angular/common/http';
import {ApiConfiguration} from '../../../../services/api-configuration';
import {BorrowedBookResponse} from '../../../../services/models/borrowed-book-response';
import {findAllBorrowedBooks} from '../../../../services/fn/books/find-all-borrowed-books';
import {PageResponseBookResponse} from '../../../../services/models/page-response-book-response';

@Component({
  selector: 'app-my-borrowed-books',
    imports: [
        DatePipe,
        DecimalPipe,
        Pagination
    ],
  templateUrl: './my-borrowed-books.html',
  styleUrl: './my-borrowed-books.css',
})
export class MyBorrowedBooks extends BasePage implements OnInit {

  private httpClient = inject(HttpClient);
  private apiConfig = inject(ApiConfiguration);

  isDrawerOpen = signal<boolean>(false);
  errorMessage = signal<Array<string>>([]);

  books = signal<Array<BorrowedBookResponse>>([]);
  selectedBook = signal<BorrowedBookResponse|undefined>(undefined);

  ngOnInit() {
    this.loadData();
  }

  protected override loadData(): void {
    findAllBorrowedBooks(this.httpClient, this.apiConfig.rootUrl, { page: this.page, size: this.size })
      .subscribe({
        next: (response) => {
          const pageData = response.body as PageResponseBookResponse;
          this.books.set(pageData?.content ?? []);
          console.log('borrowed books', pageData?.content);
          this.setPageData(pageData?.first ?? true, pageData?.last ?? false, pageData?.totalPages ?? 0);
        },
        error: (err) => {
          this.handleError(err)
        }
      })
  }

  protected closeDrawer() {

  }

  protected openDetails(book: any) {

  }

  protected returnBook(book:number) {

  }

  private handleError(err:any) {
    const body = typeof err.error === 'string' ? JSON.parse(err.error) : err.error;
    this.errorMessage.set(body?.validationErrors ?? (body?.error ? [body.error] : ['An error occurred']));
  }

  protected getDueDate(borrowedDate: Date |string) {
    if(!borrowedDate) return null;
    const borrowedDateObj = new Date(borrowedDate);
    borrowedDateObj.setDate(borrowedDateObj.getDate() + 14);
    return borrowedDateObj;
  }

  private getDaysUntilDue(borrowedDate: Date | string): number | null {
    const dueDate = this.getDueDate(borrowedDate);
    if (!dueDate) return null;

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const toReturnDateObj = new Date(dueDate);
    toReturnDateObj.setHours(0, 0, 0, 0);

    const timeDifference = toReturnDateObj.getTime() - today.getTime();
    return Math.round(timeDifference / (1000 * 3600 * 24));
  }

  protected ifReturnedDayIsNear(borrowedDate: Date |string) {
    const daysDifference = this.getDaysUntilDue(borrowedDate);
    if(!daysDifference) return false;
    return daysDifference>0 && daysDifference <= 3 ;
  }

  protected ifLateDueDates(borrowedDate: Date |string) {
    const daysDifference = this.getDaysUntilDue(borrowedDate);
    if(!daysDifference) return false;
    return daysDifference <= 0;
  }
}
