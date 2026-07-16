import { Component, input, output } from '@angular/core';

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.html',
})
export class Pagination {
  totalPages = input.required<number>();
  currentPage = input.required<number>();
  isFirst = input.required<boolean>();
  isLast = input.required<boolean>();

  pageChange = output<number>();
  previous = output<void>();
  next = output<void>();

  protected pages(): number[] {
    return Array.from({ length: this.totalPages() }, (_, i) => i);
  }
}