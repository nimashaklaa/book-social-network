import { Component, inject, signal, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApiConfiguration } from '../../../../services/api-configuration';
import { BookResponse } from '../../../../services/models/book-response';
import { PageResponseBookResponse } from '../../../../services/models/page-response-book-response';
import {BookCard} from '../../components/book-card/book-card';

@Component({
  selector: 'app-book-list',
  imports: [
    BookCard
  ],
  templateUrl: './book-list.html',
  styleUrl: './book-list.scss',
})
export class BookList implements OnInit {
  private http = inject(HttpClient);
  private apiConfig = inject(ApiConfiguration);

  books = signal<Array<BookResponse>>([]);
  errorMessage = signal<Array<string>>([]);
  isFirst = signal<boolean>(true);
  isLast = signal<boolean>(false);

  page = 0;
  size = 4;
  totalPages = 0;

  ngOnInit() {
    this.findAllBooks();
  }

  private findAllBooks() {
    this.http.get<PageResponseBookResponse>(
      `${this.apiConfig.rootUrl}/books`,
      { params: { page: this.page, size: this.size } }
    ).subscribe({
      next: (response) => {
        this.books.set(response.content ?? []);
        this.totalPages = response.totalPages ?? 0;
        this.isFirst.set(response.first ?? true);
        this.isLast.set(response.last ?? false);
      },
      error: (err) => {
        const body = typeof err.error === 'string' ? JSON.parse(err.error) : err.error;
        this.errorMessage.set(body?.validationErrors ?? (body?.error ? [body.error] : ['An error occurred']));
      }
    });
  }

  goToNextPage() {
    if (!this.isLast()) {
      this.page++;
      this.findAllBooks();
    }
  }

  goToPreviousPage() {
    if (!this.isFirst()) {
      this.page--;
      this.findAllBooks();
    }
  }

  goToPage(pageIndex: number) {
    this.page = pageIndex;
    this.findAllBooks();
  }
}
