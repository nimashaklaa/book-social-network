import { Component, inject, signal, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApiConfiguration } from '../../../../services/api-configuration';
import { BookResponse } from '../../../../services/models/book-response';
import { PageResponseBookResponse } from '../../../../services/models/page-response-book-response';
import { BookCard } from '../../components/book-card/book-card';
import { Pagination } from '../../components/pagination/pagination';
import { BasePage } from '../base-page';

@Component({
  selector: 'app-book-list',
  imports: [BookCard, Pagination],
  templateUrl: './book-list.html',
  styleUrl: './book-list.scss',
})
export class BookList extends BasePage implements OnInit {
  private http = inject(HttpClient);
  private apiConfig = inject(ApiConfiguration);

  books = signal<Array<BookResponse>>([]);
  errorMessage = signal<Array<string>>([]);

  ngOnInit() {
    this.loadData();
  }

  protected loadData() {
    this.http.get<PageResponseBookResponse>(
      `${this.apiConfig.rootUrl}/books`,
      { params: { page: this.page, size: this.size } }
    ).subscribe({
      next: (response) => {
        this.books.set(response.content ?? []);
        this.setPageData(response.first ?? true, response.last ?? false, response.totalPages ?? 0);
      },
      error: (err) => {
        const body = typeof err.error === 'string' ? JSON.parse(err.error) : err.error;
        this.errorMessage.set(body?.validationErrors ?? (body?.error ? [body.error] : ['An error occurred']));
      }
    });
  }
}