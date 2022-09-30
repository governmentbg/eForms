import { AfterViewInit, Component, EventEmitter, HostListener, Input, OnInit, Output, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatPaginator, MatPaginatorIntl } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { LangChangeEvent, TranslateService } from '@ngx-translate/core';
import { merge, Observable, of as observableOf, Subscription } from 'rxjs';
import { catchError, map, startWith, switchMap } from 'rxjs/operators';
import { NotificationsBannerService } from 'src/app/core/services/notifications-banner.service';
import { SmartTableService } from 'src/app/core/services/smart-table.service';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { enumTypes } from 'src/app/core/types/enumTypes';
import { profileTypes } from 'src/app/core/types/profileTypes';
import { roles } from 'src/app/core/types/roles';
import { TableColumn } from 'src/app/core/types/table-column';
import { NotificationBarType } from '../notifications-banner/notification-banner.model';
import { get, set } from 'lodash';

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
  langChangeSubscription: Subscription;
  noDataMessage: string;

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
  @Input() isAdmin = false;
  @Input() serviceSupplierDropdownColumn = "";
  @Input() supplierStatusColumn = "";
  @Output() rowAction: EventEmitter<any> = new EventEmitter<any>();
  @Output() tableElementsLoaded: EventEmitter<any> = new EventEmitter<any>();

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
    
    this.langChangeSubscription = this.translateService.onLangChange.subscribe((langChanged: LangChangeEvent) => {
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

    if (this.langChangeSubscription) {
      this.langChangeSubscription.unsubscribe();
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

           // emit table elements loaded
           this.tableElementsLoaded.emit(this.tableDataSource.data);
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
          this.setPIN(data);
          this.isRateLimitReached = data === null;
          if (data.totalElements === 0) {
            if(this.userProfileService.checkUserRolesAndType([roles.admin, roles.serviceManager],[profileTypes.administration])) {
              this.noDataMessage = "SERVICES.ERRORS.SERVICE_NOT_FOUND";
            } else {
            switch (this.classifier) {
              case "?":
                this.noDataMessage = "SERVICES.ERRORS.SERVICE_NOT_FOUND";
                break;
              case "?classifier=serviceInApplication&":
                this.noDataMessage = "SERVICES.NO_SERVICES_IN_APPLICATION";
                break;
              case "?classifier=serviceInRequest&":
                this.noDataMessage = "SERVICES.NO_REQUESTED_SERVICES";
                break;
              case "?classifier=serviceInCompletion&":
                this.noDataMessage = "SERVICES.NO_COMPLETED_SERVICES";
                break;
              }
            }

            this.notificationsBannerService.show({message: this.noDataMessage, type: NotificationBarType.Info});
          }

          if (data === null) {
            return [];
          }
          this.totalPages = data.totalPages;
          return data;
        })
      )
        .subscribe((data: any) => {

          this.setTableDataSource(data)

          this.updateTableLabels()
    });

    if (this.filterEvent) {
     this.filterSubscription = filterSubscr
    }
  }

  setTableDataSource(data: any, updateTotalElements: boolean = true) {
    if(!data.elements){
      data.elements = data;
    };    
    this.totalElements = (updateTotalElements)? data.totalElements : this.totalElements;

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
      
      // emit table elements loaded
      this.tableElementsLoaded.emit(this.tableDataSource.data);

      this.isLoading = false;
  }

  applyFilter(event: Event) {
  }

  emitRowAction(rowElement: any, index: any) {

    rowElement.smartTableIndex = index;

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

    for (let row of data) {
      for (let prop in row) {
        if (prop === 'data') {
          for (let propInner in row[prop]) {

            row[prop][propInner] = this.getTranslationLabelIfEnum(row[prop][propInner], propInner)
          }
        } else {

          row[prop] = this.getTranslationLabelIfEnum(row[prop], prop)
        }
      }
    }

  }

  private getTranslationLabelIfEnum(simpleValue, propertyKey){
    let columnDetail = this.tableColumns.find(obj => obj.translateKey === propertyKey);
    if(columnDetail && columnDetail.isEnum){

      let label = enumTypes[columnDetail.enumeration].getDisplayByCode(simpleValue).label;
      return label;
    }
    return simpleValue;
  }

  setRolesEnumerationLabel(dataInput) {
    let data;
    if(dataInput.elements){
      data = dataInput.elements

    } else {
      data = dataInput
    }
    // Case for user profile roles : Transform enum to string values
    if(data.length > 0 && (data[0].roles || data[0]?.data?.roles)) {  
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

  formatLabelAsCssClass(label: string){
    return label.replace(/\./g, '-').replace(/\_/g, '-').toLowerCase();
  }

  setPIN(dataInput) {
    let data;
    if(dataInput.elements){
      data = dataInput.elements

    } else {
      data = dataInput
    }
    
    this.tableColumns.forEach(column => {
      if(column.isPIN){
        data.forEach(element => {
          let value = get(element, column.dataKey);
          if(value.split('-').length) {
            set(element, column.dataKey, value.split('-')[1] )
          }
        })
      }
    })
  }
  
}
