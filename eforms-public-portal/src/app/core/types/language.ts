import { CountryFlagIcon } from "./country-flag-icon";

export interface Language {
    language: string,
    languageLong: string,
    countryFlagIcon: CountryFlagIcon[],
    status: string,
    iconUrl: string
}
