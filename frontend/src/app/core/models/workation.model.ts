export type RiskLevel = 'HIGH' | 'LOW' | 'NO';

export interface Workation {
  workationId: string;
  employee: string;
  origin: string;
  destination: string;
  start: string;
  end: string;
  workingDays: number;
  risk: RiskLevel;
}

export type SortColumn =
  | 'employee'
  | 'origin'
  | 'destination'
  | 'start'
  | 'end'
  | 'workingDays'
  | 'risk';

export type SortDirection = 'asc' | 'desc';

export interface SortState {
  column: SortColumn;
  direction: SortDirection;
}
