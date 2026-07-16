import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyBookList } from './my-book-list';

describe('MyBookList', () => {
  let component: MyBookList;
  let fixture: ComponentFixture<MyBookList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyBookList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyBookList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
