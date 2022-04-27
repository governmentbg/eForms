import { IdentificationType } from "./identification-type";

export interface CamundaProcess {
    id: string,
    taskDefinitionKey: string,
    name: string,
    assignee: string,
    owner: string,
    formKey: string,
    due: string,
    status: string,
    order: number,
    formio?: any,
    userTasks?: any,
    properties?: any
}

export interface StartProcessRequest {
    variables
}
export interface ProcessInfo {
    id: string,
    businessKey: string,
}
export interface ProcessResponse {
    businessKey: string,
    lanes: CamundaProcess[],
}

export interface LocalProccessVariable {
    modifications: Object
}