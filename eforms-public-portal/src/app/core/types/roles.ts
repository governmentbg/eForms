export class roles {
    static readonly user = {code: 'user', label: 'User'};
    static readonly admin = {code: 'admin', label: 'Admin'};
    static readonly metadataManager = {code: 'metadataManager', label: 'Metadata Manager'};
    static readonly serviceManager = {code: 'serviceManager', label: 'Service Manager'};

    public static getRoleByCode(code: string): any {
        let property: string[] = Object.getOwnPropertyNames(roles).filter(p => p.toLowerCase() == code.toLowerCase());
        if (property.length > 0) return roles[property[0]];
        return undefined;
    }
};