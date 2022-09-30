export interface AutoTranslation {
    externalReference: string;
    requestId: number;
    status: string;
    targetLanguageCode: string;
    translatedText: string;
    modified: string;
    externalTranslationServiceErrorMessage?: string;
    externalTranslationServiceErrorCode?: string;
}
