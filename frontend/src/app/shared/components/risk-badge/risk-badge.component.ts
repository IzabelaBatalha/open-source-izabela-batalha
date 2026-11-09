import { Component, Input } from '@angular/core';
import { RiskLevel } from '../../../core/models/workation.model';

interface RiskDisplay {
  label: string;
  icon: string;
  className: string;
}

@Component({
  selector: 'app-risk-badge',
  standalone: true,
  template: `
    <span class="risk-badge" [class]="display.className">
      <img [src]="display.icon" [alt]="display.label" width="20" height="20" />
      <span>{{ display.label }}</span>
    </span>
  `,
  styles: [
    `
      .risk-badge {
        display: inline-flex;
        align-items: center;
        gap: 0.5rem;
        font-size: 0.875rem;
        font-weight: 500;
      }

      .risk-high {
        color: #db4446;
      }

      .risk-low {
        color: #ff8b00;
      }

      .risk-no {
        color: #00826e;
      }
    `,
  ],
})
export class RiskBadgeComponent {
  @Input({ required: true }) set risk(value: RiskLevel) {
    this.display = RISK_DISPLAY[value];
  }

  display: RiskDisplay = RISK_DISPLAY['HIGH'];
}

const RISK_DISPLAY: Record<RiskLevel, RiskDisplay> = {
  HIGH: {
    label: 'High risk',
    icon: 'assets/icons/red-risk.svg',
    className: 'risk-high',
  },
  LOW: {
    label: 'No risk',
    icon: 'assets/icons/yellow-risk.svg',
    className: 'risk-low',
  },
  NO: {
    label: 'No risk',
    icon: 'assets/icons/green-risk.svg',
    className: 'risk-no',
  },
};
