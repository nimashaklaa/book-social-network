import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {Main} from './pages/main/main';
import {BookList} from './pages/book-list/book-list';
import {MyBookList} from './pages/my-book-list/my-book-list';
import {MyBorrowedBooks} from './pages/my-borrowed-books/my-borrowed-books';
import {MyReturnedBooks} from './pages/my-returned-books/my-returned-books';
import {MyWaitingList} from './pages/my-waiting-list/my-waiting-list';

const routes: Routes = [
  {
    path:'',
    component:Main,
    children:[
      {path:'',component:BookList},
      {path:'my-books', component:MyBookList},
      {path:'my-waiting-list',component:MyWaitingList},
      {path:'my-returned-books',component:MyReturnedBooks},
      {path:'my-borrowed-books',component:MyBorrowedBooks},
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BookRoutingModule { }
