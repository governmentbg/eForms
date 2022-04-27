export interface TableColumn {
    name: string;
    dataKey: string;
    position?: 'right' | 'left';
    isSortable?: boolean;
    isDate?: boolean;
    isEnum?: boolean;
    enumeration?: string;
    translationPath?: string;
}