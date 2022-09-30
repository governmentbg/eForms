export class serviceWithSuppliersStatusEnum {

  static readonly 'active' = {code: 'active', label: 'SERVICE_WITH_SUPPLIERS.STATUS_ACTIVE'};
  static readonly 'inactive' = {code: 'inactive', label: 'SERVICE_WITH_SUPPLIERS.STATUS_INACTIVE'};
    static readonly 'draft' = {code: 'draft', label: 'SERVICE_WITH_SUPPLIERS.STATUS_DRAFT'};
    static readonly 'published' = {code: 'published', label: 'SERVICE_WITH_SUPPLIERS.PUBLISHED'};

  public static getDisplayByCode(code: string): any {
      let property: string[] = Object.getOwnPropertyNames(serviceWithSuppliersStatusEnum).filter(p => p.toLowerCase() == code.toLowerCase());
      if (property.length > 0) return serviceWithSuppliersStatusEnum[property[0]];
      return undefined;
  }
};