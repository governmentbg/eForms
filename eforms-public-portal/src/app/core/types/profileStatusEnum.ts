export class profileStatusEnum {

  static readonly 'active' = {code: 'active', label: 'PROFILE_ROLES.STATUS_ACTIVE'};
  static readonly 'inactive' = {code: 'inactive', label: 'PROFILE_ROLES.STATUS_INACTIVE'};
  static readonly 'draft' = {code: 'draft', label: 'PROFILE_ROLES.STATUS_DRAFT'};

  public static getDisplayByCode(code: string): any {
      let property: string[] = Object.getOwnPropertyNames(profileStatusEnum).filter(p => p.toLowerCase() == code.toLowerCase());
      if (property.length > 0) return profileStatusEnum[property[0]];
      return undefined;
  }
};