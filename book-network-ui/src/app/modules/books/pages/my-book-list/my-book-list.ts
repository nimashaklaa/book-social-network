import { Component, inject, OnInit, signal } from '@angular/core';
import {DecimalPipe, NgClass} from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ApiConfiguration } from '../../../../services/api-configuration';
import { BookResponse } from '../../../../services/models/book-response';
import { PageResponseBookResponse } from '../../../../services/models/page-response-book-response';
import { Pagination } from '../../components/pagination/pagination';
import { BasePage } from '../base-page';
import {BookRequest} from '../../../../services/models/book-request';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-my-book-list',
  imports: [DecimalPipe, Pagination, FormsModule, NgClass],
  templateUrl: './my-book-list.html',
  styleUrl: './my-book-list.scss',
})
export class MyBookList extends BasePage implements OnInit {
  private http = inject(HttpClient);
  private apiConfig = inject(ApiConfiguration);

  books = signal<Array<BookResponse>>([]);
  errorMessage = signal<Array<string>>([]);
  isDrawerOpen = signal<boolean>(false);
  isEditModeEnable = signal<boolean>(false);

  newBook: BookRequest = this.getEmptyBook()

  ngOnInit() {
    this.loadData();
  }

  protected loadData() {
    this.http.get<PageResponseBookResponse>(
      `${this.apiConfig.rootUrl}/books/owner`,
      { params: { page: this.page, size: this.size } }
    ).subscribe({
      next: (response) => {
        console.log("response", response);
        this.books.set(response.content ?? []);
        this.setPageData(response.first ?? true, response.last ?? false, response.totalPages ?? 0);
      },
      error: (err) => {
        this.handleError(err)
      }
    });
  }
  protected addNewBook() {
    if(this.isEditModeEnable()) {
      this.isEditModeEnable.set(false);
    }
    this.newBook = this.getEmptyBook();
    this.isDrawerOpen.set(true);
  }
  protected closeDrawer() {
    this.isDrawerOpen.set(false);
  }

  protected saveBook() {
    if (this.isEditModeEnable()) {
      this.http.put(`${this.apiConfig.rootUrl}/books/update/${this.newBook.id}`, this.newBook
      ).subscribe({
        next: () => {
          this.closeDrawer();
          this.newBook = this.getEmptyBook();
          this.loadData();
        },
        error: (err) => {
          this.handleError(err)
        }
    })}
    else{
      this.http.post<number>(
        `${this.apiConfig.rootUrl}/books`,
        this.newBook
      ).subscribe({
        next: () => {
          this.closeDrawer();
          this.newBook = this.getEmptyBook();
          this.loadData();
        },
        error: (err) => {
          this.handleError(err)
        }
      });
    }

  }

  protected updateBookDetails(book: BookResponse) {
    this.isEditModeEnable.set(true);
    this.newBook={
      id: book.id,
      title: book.title ||'',
      authorName: book.authorName || '',
      isbn: book.isbn || '',
      synopsis: book.synopsis || '',
      shareable: book.shareable ?? true,
    };
    this.isDrawerOpen.set(true);
  }

  protected updateBookShareableStatus() {}
  protected updateBookArchivedStatus() {}
  protected updateCoverPicture() {}



  private getEmptyBook():BookRequest{
    return{
      title: '',
      authorName: '',
      isbn: '',
      synopsis: '',
      shareable: true,
    }
  }

  private handleError(err: any) {
    const body = typeof err.error === 'string' ? JSON.parse(err.error) : err.error;
    this.errorMessage.set(body?.validationErrors ?? (body?.error ? [body.error] : ['An error occurred']));
  }


}
