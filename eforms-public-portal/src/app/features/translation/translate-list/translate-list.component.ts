import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { TableColumn } from 'src/app/core/types/table-column';
import { FormIoService } from 'src/app/core/services/form-io.service';
import { HttpClient } from '@angular/common/http';
import { NotificationsBannerService } from 'src/app/core/services/notifications-banner.service';
import { NotificationBarType } from 'src/app/shared/components/notifications-banner/notification-banner.model';
import { Formio } from 'formiojs';
import { Subject, Subscription } from 'rxjs';
import { DAEFService } from 'src/app/core/services/daef-service.service';
import { SmartTableComponent } from 'src/app/shared/components/smart-table/smart-table.component';
import { MatDialog } from '@angular/material/dialog';
import { DialogComponent } from 'src/app/shared/components/dialog/dialog.component';

@Component({
  selector: 'app-translate-list',
  templateUrl: './translate-list.component.html',
  styleUrls: ['./translate-list.component.scss']
})
export class TranslateListComponent implements OnInit {

    private wrapperSubscription = new Subscription();
    public targetLanguage
    public targetLanguageCode
    public tableColumns: TableColumn[]
    public apiCallUrl
    public subData
    public formSrc
    public form
    public filterEventSubject: Subject<any> = new Subject<any>();
    public parameters: string = ""
    public allLanguagesList = []
    public targetLanguageSelect
    public tableElementsLoaded = []

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private formIoService: FormIoService,
        private http: HttpClient,
        private notificationsBannerService: NotificationsBannerService,
        private daefService: DAEFService,
        public dialog: MatDialog
    ) { }

    ngOnInit(): void {
        this.route.params.subscribe((params: Params) => {
            this.targetLanguageCode = params['targetLanguageCode'];

            this.apiCallUrl = `/api/admin/projects/${environment.formioBaseProject}/translation/${this.targetLanguageCode}?`
            this.initializeColumns();
            this.filterTable();
        });
        
        this.setLanguageDetailsByIsoCode();

        this.wrapperSubscription.add(
            this.formIoService.getFormByAlias('common/component/common-translation-filter', environment.formioBaseProject).subscribe(result => {
                this.formSrc = result;
                Formio.setBaseUrl(`${environment.apiUrl}`);
            })
        )
    }

    ngOnDestroy(): void {
        this.wrapperSubscription.unsubscribe();
    }

    back() {
        this.router.navigate(['admin-panel/translation']);
    }

    initializeColumns(): void {

        this.tableColumns = [
            {
                name: "TRANSLATION.BULGARIAN_TEXT",
                dataKey: 'data.translation',
                position: 'left',
                isSortable: false
            },
            {
                name: "TRANSLATION.STATUS",
                dataKey: 'data.targetTranslation.status',
                position: 'left',
                isSortable: false,
                doTranslate: true,
                translationPath: 'TRANSLATION.'
            },
        ];

        if(this.targetLanguageCode !== environment.defaultLanguage) {
            this.tableColumns.splice(1, 0, {
                name: "TRANSLATION.SELECTED_LANGUAGE",
                dataKey: 'data.targetTranslation.translation',
                position: 'left',
                isSortable: false
            });
        }
    }

    handleRowActionEvent(event) {
        this.router.navigate(['admin-panel/translation/' + this.targetLanguageCode + '/' + event.data.key]);
    }

    setLanguageDetailsByIsoCode() 
    {
        this.formIoService.getFormIoResource('languages').subscribe((response) => {
            if (response instanceof Array) {
              this.allLanguagesList = response;

              this.allLanguagesList.forEach((languageData) => {
                if (languageData.data.language == this.targetLanguageCode) {
                    this.targetLanguageSelect = languageData.data;
                }
              })
            }
        })
    }

    publishAll() {

        const dialogRef = this.dialog.open(DialogComponent, { data: { title: 'IMPORTANT', body: 'TRANSLATION.PUBLISH_ALL_TRANSLATION_QUESTION', canProceed: true } });

        dialogRef.afterClosed().subscribe((isConfirmed) => {
            if (isConfirmed) {
                this.daefService.postChangeTranslationStatus(this.targetLanguageCode).subscribe(response => {
                    this.notificationsBannerService.show({message: "TRANSLATION.PUBLISH_ALL_SUCCESS", type: NotificationBarType.Success })
                    this.filterTable();
                },
                (error) => {});
            }
          })
        
    }

    handleFormReady(form: any) {
        this.form = form;
        this.filterTable();
    }
    
    handleFilterEvent(event) {
        if (event.type === 'filter') {
          let translationFilter = event.data.translationFilter.trim();
          let targetTranslationFilter = (event.data.targetTranslationFilter)? event.data.targetTranslationFilter.trim() : "";
          let targetStatusFilter = event.data.targetStatusFilter.trim();
    
          this.parameters = this.replaceAll(`&translationFilter=${translationFilter}&targetStatusFilter=${targetStatusFilter}&targetTranslationFilter=${targetTranslationFilter}`);
          this.filterTable();
        }
    }

    replaceAll(url){
        while(url.indexOf('{') !== -1 || url.indexOf('}') !== -1){
          url = url.replace('{','%7B').replace('}', '%7D')
        }
        return url
      }
    
    filterTable() {
        this.filterEventSubject.next(this.parameters);
    }

    handleTargetLanguageCodeChange(event) {
        this.router.navigateByUrl('/', {skipLocationChange: true})
            .then(()=>this.router.navigate(['admin-panel/translation/' + event.value.language]));
    }

    handlePostTableLoad(event) {
        this.tableElementsLoaded = event;
    }

}
