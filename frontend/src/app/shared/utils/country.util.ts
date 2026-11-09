const COUNTRY_CODE_MAP: Record<string, string> = {
  Germany: 'de',
  'United States': 'us',
  Ukraine: 'ua',
  Belgium: 'be',
  Spain: 'es',
  Greece: 'gr',
  India: 'in',
};

export function getCountryCode(countryName: string): string {
  return COUNTRY_CODE_MAP[countryName] ?? countryName.slice(0, 2).toLowerCase();
}

export function getFlagUrl(countryName: string): string {
  const code = getCountryCode(countryName);
  return `https://flagcdn.com/w20/${code}.png`;
}
