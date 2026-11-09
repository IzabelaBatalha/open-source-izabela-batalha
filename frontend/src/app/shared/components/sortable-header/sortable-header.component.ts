import { Component, EventEmitter, Input, Output } from '@angular/core';
import { SortColumn, SortDirection } from '../../../core/models/workation.model';

@Component({
  selector: 'app-sortable-header',
  standalone: true,
  template: `
    <button
      type="button"
      class="sortable-header"
      [class.active]="activeColumn === column"
      (click)="sort.emit(column)"
    >
      <span>{{ label }}</span>
      <span class="sort-icons" aria-hidden="true">
        <svg
          class="arrow up"
          [class.active]="activeColumn === column && direction === 'asc'"
          viewBox="0 0 10 6"
          width="10"
          height="6"
        >
          <path d="M5 0L9.33 6H0.67L5 0Z" fill="currentColor" />
        </svg>
        <svg
          class="arrow down"
          [class.active]="activeColumn === column && direction === 'desc'"
          viewBox="0 0 10 6"
          width="10"
          height="6"
        >
          <path d="M5 6L0.67 0H9.33L5 6Z" fill="currentColor" />
        </svg>
      </span>
    </button>
  `,
  styles: [
    `
      .sortable-header {
        all: unset;
        display: inline-flex;
        align-items: center;
        gap: 0.35rem;
        cursor: pointer;
        font: inherit;
        color: inherit;
        user-select: none;
      }

      .sortable-header:hover,
      .sortable-header.active {
        color: #003366;
      }

      .sort-icons {
        display: inline-flex;
        flex-direction: column;
        gap: 2px;
        color: #8aa0b5;
      }

      .arrow.active {
        color: #003366;
      }
    `,
  ],
})
export class SortableHeaderComponent {
  @Input({ required: true }) label = '';
  @Input({ required: true }) column!: SortColumn;
  @Input() activeColumn: SortColumn | null = null;
  @Input() direction: SortDirection = 'asc';

  @Output() sort = new EventEmitter<SortColumn>();
}
