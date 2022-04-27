import { AfterViewInit, Component, ViewChild, OnInit, Input, Output, EventEmitter, HostListener, ViewChildren, QueryList } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort, Sort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { TableColumn } from 'src/app/core/types/table-column';
import { SmartTableService } from 'src/app/core/services/smart-table.service'
import {merge, Observable, of as observableOf, Subscription} from 'rxjs';
import { catchError, map, startWith, switchMap } from 'rxjs/operators';
import { MatPaginatorIntl } from '@angular/material/paginator';
import {LangChangeEvent, TranslateService} from '@ngx-translate/core';
import { NotificationsBannerService } from 'src/app/core/services/notifications-banner.service';
import * as moment from 'moment';
import { NotificationBarType } from '../notifications-banner/notification-banner.model';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { roles } from 'src/app/core/types/roles';
import { profileTypes } from 'src/app/core/types/profileTypes';
import { enumTypes } from 'src/app/core/types/enumTypes';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-smart-table',
  templateUrl: './smart-table.component.html',
  styleUrls: ['./smart-table.component.scss']
})
export class SmartTableComponent implements OnInit, AfterViewInit {
  public tableDataSource = new MatTableDataSource([]);
  public displayedColumns: string[];
  totalElements: number;
  isRateLimitReached: boolean;
  sortString = "";
  isMobile: boolean;
  public innerWidth: any;
  scrollDistance = 2;
  totalPages: number;
  throttle = 300;
  lastElement: any;
  isLoading = false;
  scrollPosition: any;
  filterSubscription: any;
  private filterEventSubscription = new Subscription();
  privatewrapperSubscription = new Subscription();
  userProfile;
  selectedProfile;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChildren('dataCell') cell: QueryList<any>;

  @Input() isPageable = false;
  @Input() isSortable = false;
  @Input() isFilterable = false;
  @Input() tableColumns: TableColumn[];
  @Input() actionColumnName = "";
  @Input() actionButtonName = "";
  @Input() showButtonIf = "";
  @Input() buttonIcon = "";
  @Input() apiCallUrl = "";
  @Input() subData = "";
  @Input() classifier = "";
  @Input() initialSort = ""
  @Input() defaultPageSize = 5;
  @Input() parameters = "";
  @Input() allowAssignment = true;
  @Input() assigneeColumnName = "";
  @Input() assigneeColumn = "";
  @Input() filterEvent: Observable<any>;
  @Input() fetchOnInit = true;
  @Input() serviceSupplierDropdownColumn = "";
  @Input() supplierStatusColumn = "";
  @Output() rowAction: EventEmitter<any> = new EventEmitter<any>();

  columnsCount = 0;

  constructor(private smartTableService: SmartTableService,
    private paginatorIntl: MatPaginatorIntl,
    private translateService: TranslateService,
    private notificationsBannerService: NotificationsBannerService,
    private userProfileService: UserProfileService
    ) {
  }

  @HostListener('window:resize', ['$event'])
  onResize(event) {
    this.innerWidth = window.innerWidth;
    this.isMobile = this.innerWidth < 840;
    if (this.isMobile) {
      this.defaultPageSize = 5;
    } else {
      this.defaultPageSize = 25;
    }
  }

  updateTableLabels() {

    this.paginatorIntl.itemsPerPageLabel = this.translateService.instant("PAGINATOR.ITEMS_PER_PAGE");

    const originalGetRangeLabel = this.paginatorIntl.getRangeLabel;
    this.paginatorIntl.getRangeLabel = (page: number, size: number, len: number) => {

      return originalGetRangeLabel(page, size, len)
          .replace('of', this.translateService.instant("PAGINATOR.OF"));
    };

    this.paginatorIntl.changes.next();
  }
  
  ngOnInit() {

    this.innerWidth = window.innerWidth;
    this.isMobile = this.innerWidth < 840;
    this.userProfile = this.userProfileService.currentUser
    this.selectedProfile = this.userProfileService.selectedProfile

    this.translateService.getTranslation('bg').subscribe((translatedLabel: string) => {
      // forced update when translateService loads translations from server
      this.updateTableLabels()
    });

    this.translateService.onLangChange.subscribe((langChanged: LangChangeEvent) => {
      // forced update when translateService changes language
      this.updateTableLabels()
    });

    const columnNames = this.tableColumns.map((tableColumn: TableColumn) => tableColumn.name);
    if (this.assigneeColumnName) {
      columnNames.push(this.assigneeColumnName);
    }
    if (this.serviceSupplierDropdownColumn) {
      columnNames.splice(2, 0, this.serviceSupplierDropdownColumn);
    }
    if (this.supplierStatusColumn) {
      columnNames.splice(3, 0, this.supplierStatusColumn);
    }
    if (this.actionColumnName) {
      this.displayedColumns = [...columnNames, this.actionColumnName]
    } else {
      this.displayedColumns = columnNames;
    }
    this.columnsCount = 100/(this.tableColumns.length + Number(!!this.actionColumnName) + Number(!!this.assigneeColumnName) + Number(!!this.serviceSupplierDropdownColumn) + Number(!!this.supplierStatusColumn));
    if (this.filterEvent) {
      this.filterEventSubscription.add(this.filterEvent.subscribe((filtersData) => {                  
            if(this.filterSubscription){
              this.filterSubscription.unsubscribe()
            }            
            this.paginator.pageIndex = 0;
            this.showData(filtersData)
         }
      ));
    }
  }

  ngOnDestroy() {
    if (this.filterEvent) {
      if(this.filterSubscription){
        this.filterSubscription.unsubscribe()
      }      
      this.filterEventSubscription.unsubscribe();
    }
  }

  ngAfterViewInit(): void {
    if (this.isPageable) {
      this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
      this.sort.active = this.initialSort;
      if (this.isMobile) {
        this.defaultPageSize = 5;
      } else {
        this.defaultPageSize = 25;
      }
      if(this.fetchOnInit) {
        this.showData();
      }
    } else {
      if(this.fetchOnInit) {
        this.showNonPageableData();
      }
    }
  }

  showNonPageableData() {
    let filterSubscr;
    filterSubscr = this.smartTableService.getNonPageableData(this.apiCallUrl)
        .subscribe((data: any) => {
           if(this.subData){
             data = data[this.subData]
           }
           this.setEnumerationLabel(data);
           this.setRolesEnumerationLabel(data);
           this.tableDataSource = new MatTableDataSource<any>(data)      
        });
    if (this.filterEvent) {
      this.filterSubscription = filterSubscr
    }

  }

  showData(filtersData = "") {
    this.isLoading = true;
    let filterSubscr;
    filterSubscr = merge(this.sort.sortChange, this.paginator.page)
    .pipe(
      startWith({}),
      switchMap(() => {
        if (this.sort.active.length > 0) {
          this.sortString = this.tableColumns.find(column => column.name === this.sort.active).dataKey;
          if (this.sort.direction === "desc") {
            this.sortString = '-' + this.sortString;
          }
        }
          return this.smartTableService.getData(this.apiCallUrl, this.classifier, this.sortString, this.paginator.pageIndex, this.defaultPageSize, filtersData)
            .pipe(catchError(() => observableOf(null)));
        }),
        map(data => {
          this.setEnumerationLabel(data);
          this.setRolesEnumerationLabel(data);
          this.isRateLimitReached = data === null;
          if (data.totalElements === 0) {
            if(this.userProfileService.checkUserRolesAndType([roles.serviceManager],[profileTypes.administration])) {
              this.notificationsBannerService.show({message: "SERVICES.ERRORS.SERVICE_NOT_FOUND", type: NotificationBarType.Info});
            } else {
            switch (this.classifier) {
              case "?":
                this.notificationsBannerService.show({message: "SERVICES.ERRORS.SERVICE_NOT_FOUND", type: NotificationBarType.Info});
                break;
              case "?classifier=serviceInApplication&":
                this.notificationsBannerService.show({message: "SERVICES.NO_SERVICES_IN_APPLICATION", type: NotificationBarType.Info});
                break;
              case "?classifier=serviceInRequest&":
                this.notificationsBannerService.show({message: "SERVICES.NO_REQUESTED_SERVICES", type: NotificationBarType.Info});
                break;
              case "?classifier=serviceInCompletion&":
                this.notificationsBannerService.show({message: "SERVICES.NO_COMPLETED_SERVICES", type: NotificationBarType.Info});
                break;
              }
            }
          }

          if (data === null) {
            return [];
          }
          this.totalPages = data.totalPages;
          return data;
        })
      ).subscribe(data => this.setTableDataSource(data));

    if (this.filterEvent) {
     this.filterSubscription = filterSubscr
    }
  }

  setTableDataSource(data: any) {
    if(!data.elements){
      data.elements = data;
    };    
    this.totalElements = data.totalElements;
      if (this.isMobile) {
        if (data.totalPages >= this.paginator.pageIndex && data.totalPages>0 ) {
          this.paginator.pageIndex ++;         
          this.tableDataSource.data = this.tableDataSource.data.concat(data.elements);
        }
        else {
          this.tableDataSource = new MatTableDataSource<any>(data.elements);
        }
      } else {
        this.tableDataSource = new MatTableDataSource<any>(data.elements);
      }
      if (this.serviceSupplierDropdownColumn.length) {
        this.tableDataSource.data.forEach(eas => {
          if (eas.data.serviceSupplierList?.length === 1) {
            eas['selectedSupplier'] = new FormControl(eas.data.serviceSupplierList[0]);
          } else {
            eas['selectedSupplier'] = new FormControl('');
          }
        })
      }
      this.isLoading = false;
  }

  applyFilter(event: Event) {
  }

  emitRowAction(rowElement: any) {
    this.rowAction.emit(rowElement);
  }

  onScrollDown($event) {
    if (this.totalPages > this.paginator.pageIndex && !this.isLoading) {
      this.showData();
    }
  }

  evalShowButtonIf(element, arg) {
    return eval(arg)
  }

  setEnumerationLabel(dataInput){
    let data;
    if(dataInput.elements){
      data = dataInput.elements
    } else {
      data = dataInput
    }
    if(!Array.isArray(data)) {
      return
    }
    for (var row of data) {
      for (let key in row) {
        let columnDetail = this.tableColumns.find(obj => obj.dataKey === key);
        if(columnDetail && columnDetail.isEnum){               
          let label = enumTypes[columnDetail.enumeration].getDisplayByCode(row[key]).label;
          row[key] = this.translateService.instant(label);
        }
      }
     }
  }

  setRolesEnumerationLabel(dataInput) {
    let data;
    if(dataInput.elements){
      data = dataInput.elements

    } else {
      data = dataInput
    }
    // Case for user profile roles : Transform enum to string values
    if(data.length > 0 && (data[0].roles || data[0]?.data.roles)) {  
      let  dataLength = Object.keys(data).length;          
      for (let d = 0; d < dataLength; d++) { 
        let dataRoles = data[d].data ? data[d].data.roles : data[d].roles 
        let  rolesLength = Object.keys(dataRoles).length;
        let rolesDisp =[] ; 
        for (let r = 0; r < rolesLength; r++) {
          let code = dataRoles[r];
          let role = roles.getRoleByCode(code);
          let label = role ? role.label : code
          if(r !== 0){
            label = ' '+ label
          }
          rolesDisp.push(label);
        };
        dataRoles.code = dataRoles ;
        dataRoles.display = rolesDisp ;
      };
    };
    // End
  }

  getDisplayByCode(enumeration: string, status: string){
    let enumObject = enumTypes[enumeration].getDisplayByCode(status);
    if (enumObject) {
      return enumObject.label
    }
  }

  getIconByCode(enumeration: string, status: string){
    let enumObject = enumTypes[enumeration].getDisplayByCode(status);
    if (enumObject) {
      return enumObject.icon
    }
  }
  
}
