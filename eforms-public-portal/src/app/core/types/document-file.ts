export interface DocumentFile {
    filelabel: string,
    filecode: string,
    filename: string,
    content: string,
    contentType: string,
    location: string,
    fileExtension?: string,
    storage?: string,
    size?: Number,
    blob?: Blob,
    isSigned?: boolean,
    bucket?: string,
    key?: string
}