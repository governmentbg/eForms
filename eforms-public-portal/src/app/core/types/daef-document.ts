import { DocumentFile } from "./document-file";
import { Signee } from "./signee";

export interface DAEFDocument {
    businessKey: string;
    consolidating: boolean;
    fileCode: string;
    fileTitle: string;
    files: DocumentFile[];
    formAlias: string;
    signable: boolean;
    status?: { class: string, label: string }
    percentage?: number;
    signeesList?: Signee[];
    requiredSignatures?: string;
    hasDocumentSigneesError?: boolean;
    errorMessage?: string;
    hasErrorBorder?: boolean;
}

export interface DAEFDocumentError {
    daefDocument: DAEFDocument,
    error: any
}