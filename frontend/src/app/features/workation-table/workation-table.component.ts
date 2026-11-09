import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

import { WorkationService } from '../../core/services/workation.service';
import {
  SortColumn,
  SortDirection,
  SortState,
  Workation,
} from '../../core/models/workation.model';
import { SortableHeaderComponent } from '../../shared/components/sortable-header/sortable-header.component';
import { CountryCellComponent } from '../../shared/components/country-cell/country-cell.component';
import { RiskBadgeComponent } from '../../shared/components/risk-badge/risk-badge.component';

interface TableColumn {
  key: SortColumn;
  label: string;
}

@Component({
  selector: 'app-workation-table',
  standalone: true,
  imports: [
    CommonModule,
    SortableHeaderComponent,
    CountryCellComponent,
    RiskBadgeComponent,
  ],
  templateUrl: './workation-table.component.html',
  styleUrl: './workation-table.component.scss',
})
export class WorkationTableComponent implements OnInit {
  private readonly workationService = inject(WorkationService);

  readonly columns: TableColumn[] = [
    { key: 'employee', label: 'Employee' },
    { key: 'origin', label: 'Origin' },
    { key: 'destination', label: 'Destination' },
    { key: 'start', label: 'Start' },
    { key: 'end', label: 'End' },
    { key: 'workingDays', label: 'Working days' },
    { key: 'risk', label: 'Risk' },
  ];

  readonly workations = signal<Workation[]>([]);
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);
  readonly sortState = signal<SortState>({ column: 'employee', direction: 'asc' });

  ngOnInit(): void {
    this.loadWorkations();
  }

  onSort(column: SortColumn): void {
    const current = this.sortState();
    const direction: SortDirection =
      current.column === column && current.direction === 'asc' ? 'desc' : 'asc';

    this.sortState.set({ column, direction });
    this.loadWorkations();
  }

  private loadWorkations(): void {
    const { column, direction } = this.sortState();
    this.loading.set(true);
    this.error.set(null);

    this.workationService.getWorkations(column, direction).subscribe({
      next: (workations) => {
        this.workations.set(workations);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Unable to load workations. Please ensure the backend is running.');
        this.loading.set(false);
      },
    });
  }
}
