// https://help.form.io/developers/form-renderer#form-renderer-options

export interface FormIoRenderOptions {
    readOnly?: boolean;
    noDefaults?: boolean;
    language?: string;
    i18n?: any;
    viewAsHtml?: boolean;
    renderMode?: any;
    highlightErrors?: boolean;
    componentErrorClass?: string;
    template?: string;
    templates?: any;
    iconset?: any;
    buttonSettings?: any;
    components?: any;
    disabled?: any;
    showHiddenFields?: boolean;
    hide?: any;
    show?: any;
    formio?: any;
    decimalSeparator?: string;
    thousandsSeparator?: string;
    fileService?: any;
    hooks?: any;
    languageOverride?: any;
    prefix?: string;
    suffix?: string;
}