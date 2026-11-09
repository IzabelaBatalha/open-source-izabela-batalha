import { Routes } from '@angular/router';
import { WorkationTableComponent } from './features/workation-table/workation-table.component';

export const routes: Routes = [
  {
    path: '',
    component: WorkationTableComponent,
  },
  {
    path: '**',
    redirectTo: '',
  },
];
