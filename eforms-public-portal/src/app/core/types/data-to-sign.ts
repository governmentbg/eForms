export interface DataToSign {
    signingCertificate: string;
    certificateChain: Array<string>;
    encryptionAlgorithm: string;
    digestToSign?: string;
    documentToSign?: string;
    documentName: string;
    signingDate: Date;
    signatureValue?: string
}
