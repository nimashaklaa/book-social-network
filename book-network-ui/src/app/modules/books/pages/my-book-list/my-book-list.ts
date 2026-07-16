import { Component, inject, OnInit, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
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
  imports: [DecimalPipe, Pagination, FormsModule],
  templateUrl: './my-book-list.html',
  styleUrl: './my-book-list.scss',
})
export class MyBookList extends BasePage implements OnInit {
  private http = inject(HttpClient);
  private apiConfig = inject(ApiConfiguration);

  books = signal<Array<BookResponse>>([]);
  errorMessage = signal<Array<string>>([]);
  isDrawerOpen = signal<boolean>(false);

  newBook: BookRequest ={
    title: '',
    authorName: '',
    isbn: '',
    synopsis: '',
    shareable: true
  }

  ngOnInit() {
    this.loadData();
  }

  protected loadData() {
    this.http.get<PageResponseBookResponse>(
      `${this.apiConfig.rootUrl}/books/owner`,
      { params: { page: this.page, size: this.size } }
    ).subscribe({
      next: (response) => {
        this.books.set(response.content ?? []);
        this.setPageData(response.first ?? true, response.last ?? false, response.totalPages ?? 0);
      },
      error: (err) => {
        this.handleError(err)
      }
    });
  }
  protected addNewBook() {
    this.resetForm();
    this.isDrawerOpen.set(true);
  }
  protected closeDrawer() {
    this.isDrawerOpen.set(false);
  }

  protected submitNewBook() {
    this.http.post<number>(
      `${this.apiConfig.rootUrl}/books`,
      this.newBook
    ).subscribe({
      next: () => {
        this.closeDrawer();
        this.resetForm();
        this.loadData();
      },
      error: (err) => {
        this.handleError(err)
      }
    });
  }

  protected updateBookDetails() {}
  protected updateBookShareableStatus() {}
  protected updateBookArchivedStatus() {}
  protected updateCoverPicture() {}


  private resetForm() {
    this.newBook = {
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
