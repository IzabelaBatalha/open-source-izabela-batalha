import { Component, Input } from '@angular/core';
import { getFlagUrl } from '../../utils/country.util';

@Component({
  selector: 'app-country-cell',
  standalone: true,
  template: `
    <span class="country-cell">
      <img
        class="flag"
        [src]="flagUrl"
        [alt]="countryName + ' flag'"
        width="20"
        height="15"
        loading="lazy"
      />
      <span>{{ countryName }}</span>
    </span>
  `,
  styles: [
    `
      .country-cell {
        display: inline-flex;
        align-items: center;
        gap: 0.5rem;
      }

      .flag {
        border: 1px solid #d9e2ea;
        object-fit: cover;
      }
    `,
  ],
})
export class CountryCellComponent {
  flagUrl = '';

  @Input({ required: true })
  set country(value: string) {
    this.flagUrl = getFlagUrl(value);
    this.countryName = value;
  }

  countryName = '';
}
