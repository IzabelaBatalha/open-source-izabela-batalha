import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

import { WorkationService } from './workation.service';
import { Workation } from '../models/workation.model';

describe('WorkationService', () => {
  let service: WorkationService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [WorkationService, provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(WorkationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should fetch workations with sort parameters', () => {
    const mockData: Workation[] = [
      {
        workationId: 'w1',
        employee: 'Steffen Jacobs',
        origin: 'Germany',
        destination: 'United States',
        start: '02/01/2024',
        end: '31/12/2024',
        workingDays: 65,
        risk: 'HIGH',
      },
    ];

    service.getWorkations('employee', 'asc').subscribe((data) => {
      expect(data).toEqual(mockData);
    });

    const request = httpMock.expectOne(
      (req) =>
        req.url === 'http://localhost:8080/workflex/workation' &&
        req.params.get('sort') === 'employee' &&
        req.params.get('direction') === 'asc',
    );

    expect(request.request.method).toBe('GET');
    request.flush(mockData);
  });
});
