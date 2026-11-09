import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

import { RiskBadgeComponent } from './risk-badge.component';

describe('RiskBadgeComponent', () => {
  let fixture: ComponentFixture<RiskBadgeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RiskBadgeComponent],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    fixture = TestBed.createComponent(RiskBadgeComponent);
  });

  it('should display high risk label', () => {
    fixture.componentRef.setInput('risk', 'HIGH');
    fixture.detectChanges();

    const element: HTMLElement = fixture.nativeElement;
    expect(element.textContent).toContain('High risk');
  });

  it('should display no risk label for LOW and NO', () => {
    fixture.componentRef.setInput('risk', 'LOW');
    fixture.detectChanges();
    expect(fixture.nativeElement.textContent).toContain('No risk');

    fixture.componentRef.setInput('risk', 'NO');
    fixture.detectChanges();
    expect(fixture.nativeElement.textContent).toContain('No risk');
  });
});
