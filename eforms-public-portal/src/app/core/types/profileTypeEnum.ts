export class profileTypeEnum {
  static readonly '1' = {code: '1', label: 'PROFILE_ROLES.TYPE_FL'};
  static readonly '2' = {code: '2', label: 'PROFILE_ROLES.TYPE_UL'};
  static readonly '3' = {code: '3', label: 'PROFILE_ROLES.TYPE_DA'};

  public static getDisplayByCode(code: string): any {
      let property: string[] = Object.getOwnPropertyNames(profileTypeEnum).filter(p => p.toLowerCase() == code.toLowerCase());
      if (property.length > 0) return profileTypeEnum[property[0]];
      return undefined;
  }
}