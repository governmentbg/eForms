import {AfterViewChecked, Component, OnInit} from '@angular/core';
import { TableColumn } from 'src/app/core/types/table-column';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { CamundaProcessService } from 'src/app/core/services/camunda-process.service';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { UserProfile } from 'src/app/core/types/user-profile';
import { DAEFService } from 'src/app/core/services/daef-service.service';
import { DAEFServiceProvider } from 'src/app/core/types/daefservice';
import {Formio} from "formiojs";
import {FormIoService} from "../../../core/services/form-io.service";
import {Subject} from "rxjs";
import {FormioComponent} from "@formio/angular";
import {LangChangeEvent, TranslateService} from "@ngx-translate/core";
import { NotificationsBannerComponent } from 'src/app/shared/components/notifications-banner/notifications-banner.component';
import { NotificationsBannerService } from 'src/app/core/services/notifications-banner.service';
import { NotificationBarType } from 'src/app/shared/components/notifications-banner/notification-banner.model';


@Component({
  selector: 'app-administration-of-services',
  templateUrl: './administration-of-services.component.html',
  styleUrls: ['./administration-of-services.component.scss']
})
export class AdministrationOfServicesComponent implements OnInit, AfterViewChecked {
  tableData
  tableColumns: TableColumn[];
  apiCallUrl: string
  userProfile: UserProfile;
  formSrc;
  serviceStatuses = 'serviceStatuses=active,draft,published&';
  filterEventSubject: Subject<any> = new Subject<any>();
  shouldFilter = false;
  form: FormioComponent;


  constructor(
    private router: Router,
    private camundaProcessService: CamundaProcessService,
    public userProfileService: UserProfileService,
    private notificationsBannerService: NotificationsBannerService,
    private formioService: FormIoService,
    private translateService: TranslateService
    ) { }

  ngOnInit(): void {
    this.formioService.getFormByAlias('common/component/metadata-filter', environment.formioBaseProject).subscribe(result => {
      this.formSrc = result;
    })

    this.apiCallUrl = `/api/admin/projects/${environment.formioBaseProject}/eas`
    this.initializeColumns();
    this.userProfileService
    .subscribe((userProfile) => {
      this.userProfile = userProfile
      let showSubmissionSuccess = localStorage.getItem('showMetaDataSubmissionSuccess');
      if (showSubmissionSuccess) {
        this.notificationsBannerService.show({message: "SUCCESSFUL_METADATA_SUBMISSION", type: NotificationBarType.Success, placeholders: { service: showSubmissionSuccess}});
        localStorage.removeItem('showMetaDataSubmissionSuccess');
      }
    });
    
  }

  initializeColumns(): void {
    this.tableColumns = [
      {
        name: "SERVICES.SERVICE_ID",
        dataKey: 'data.arId',
        position: 'left',
        isSortable: true
      },
      {
        name: 'SERVICES.NAME',
        dataKey: 'data.serviceTitle',
        position: 'left',
        isSortable: false
      },
      {
        name: 'SERVICES.STATUS',
        dataKey: 'data.status',
        position: 'left',
        isEnum : true,
        enumeration: "serviceWithSuppliersStatusEnum",
        translateKey: 'status',
        isSortable: false,
        doTranslate: true
      }
    ];
  }

  back() {
    this.router.navigate(['home']);
  }

  public ngAfterViewChecked(): void {

    if(this.shouldFilter){
      this.shouldFilter=false
      this.filterTable()
    }
  }

  private changeFormIoLanguage(currentLang: string, translations: any) {
    this.form.formio.addLanguage(currentLang, translations);
    this.form.formio.language = currentLang;
    this.form.formio.redraw();
  }

  private initFormIoLanguage() {
    this.translateService.getTranslation(this.translateService.currentLang).subscribe((translations) => {
      for (const key in translations) {
        if (Object.prototype.hasOwnProperty.call(translations, key)) {
          if (key.includes('FORMIO.')) {
            const newKey = key.replace(/FORMIO./g, '');
            translations[newKey] = translations[key];
          }
        }
      }

      this.changeFormIoLanguage(this.translateService.currentLang, translations);
    });
  }

  async handleFormReady(form: FormioComponent) {
    this.form = form;

    this.initFormIoLanguage();

    this.translateService.onLangChange.subscribe((langChanged: LangChangeEvent) => {
      this.changeFormIoLanguage(langChanged.lang, langChanged.translations);
    });

  }

  handleFilterEvent(event){

    if(event.type === 'applyFilterEvent') {
      this.serviceStatuses = `serviceStatuses=${event.data.select.toString()}&`
      this.shouldFilter=true
    }
  }

  filterTable() {
    this.filterEventSubject.next();
  }
  handleRowActionEvent(event) {
    let arId = event.data.arId
    let supplierEAS = event.data.supplierEAS
    this.router.navigate(['administration-of-services/edit',arId,supplierEAS]);
  }


}
