export class serviceButtonEnum {
    static readonly '17,14,13,16,15,11' = {label: 'SERVICES.SERVICES_IN_PROGRESS', icon: 'edit', classifier: 'serviceInApplication'}
    static readonly '91,18' = {label: 'SERVICES.REQUESTED_SERVICES', icon: 'visibility', classifier: 'serviceInRequest'}
    static readonly '97,82,99,98,81' = {label: 'SERVICES.COMPLETED_SERVICES', icon: 'visibility', classifier: 'serviceInCompletion'}

    public static getDisplayByCode(code: string): any {
        let property: string[] = Object.getOwnPropertyNames(serviceButtonEnum).filter(p => p.split(',').indexOf(code.toLowerCase()) > -1);
        if (property.length > 0) return serviceButtonEnum[property[0]];
        return undefined;
    }
}