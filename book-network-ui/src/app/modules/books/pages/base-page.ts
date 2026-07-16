import { signal } from '@angular/core';

export abstract class BasePage {
  page = 0;
  size = 10;
  totalPages = 0;
  isFirst = signal<boolean>(true);
  isLast = signal<boolean>(false);

  protected abstract loadData(): void;

  goToNextPage() {
    if (!this.isLast()) {
      this.page++;
      this.loadData();
    }
  }

  goToPreviousPage() {
    if (!this.isFirst()) {
      this.page--;
      this.loadData();
    }
  }

  goToPage(pageIndex: number) {
    this.page = pageIndex;
    this.loadData();
  }

  protected setPageData(first: boolean, last: boolean, totalPages: number) {
    this.isFirst.set(first);
    this.isLast.set(last);
    this.totalPages = totalPages;
  }
}
