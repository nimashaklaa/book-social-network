import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyReturnedBooks } from './my-returned-books';

describe('MyReturnedBooks', () => {
  let component: MyReturnedBooks;
  let fixture: ComponentFixture<MyReturnedBooks>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyReturnedBooks]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyReturnedBooks);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
