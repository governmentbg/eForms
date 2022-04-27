export interface FileToSign {
    name: string
    originalName: string
    size: number
    storage: string
    type: string
    url: string
    bucket?: string
    key?: string
}
