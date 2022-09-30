export interface NotificationBarMessageConfig {
  [when: string]: string;
}

export const NotificationBarType = {
  Info: 'info',
  New: 'new',
  Success: 'success',
  Warn: 'attention',
  Error: 'error',
}

export interface NotificationBarMessage {
  messages?: NotificationBarMessageConfig;
}

export interface NotificationBarModel {
  action?: boolean,
  actionable?: boolean,
  actionText?: string,
  url?: string,
  openInNewTab?: boolean;
  autoHide?: boolean;
  closed?: boolean,
  closeable?: boolean;
  hideDelay?: number;
  hideOnHover?: boolean;
  id?: number,
  isHtml?: boolean;
  message?: string;
  type?: string;
  placeholders?: {};
  additionalMessage?: string;
}