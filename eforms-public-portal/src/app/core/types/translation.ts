export interface Translation {
    hasTranslation: boolean,
    identifier: string,
    key: string,
    language: string,
    status: string,
    targetTranslation?: any,
    translation: string
}
