import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyWaitingList } from './my-waiting-list';

describe('MyWaitingList', () => {
  let component: MyWaitingList;
  let fixture: ComponentFixture<MyWaitingList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyWaitingList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyWaitingList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
