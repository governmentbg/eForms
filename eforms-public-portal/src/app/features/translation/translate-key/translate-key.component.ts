import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { FormIoService } from 'src/app/core/services/form-io.service';
import { environment } from 'src/environments/environment';
import { NotificationsBannerService } from 'src/app/core/services/notifications-banner.service';
import { NotificationBarType } from 'src/app/shared/components/notifications-banner/notification-banner.model';
import { AutoTranslation } from 'src/app/core/types/autoTranslation';
import { Translation } from 'src/app/core/types/translation';
import { DAEFService } from 'src/app/core/services/daef-service.service';
import { combineLatest, forkJoin } from 'rxjs';

@Component({
  selector: 'app-translate-key',
  templateUrl: './translate-key.component.html',
  styleUrls: ['./translate-key.component.scss']
})
export class TranslateKeyComponent implements OnInit {

    public environment
    public manualTranslationValue = ""
    public targetLanguage
    public targetLanguageCode
    public key
    public translationData: Translation = {
        hasTranslation: false,
        identifier: "",
        key: "",
        language: "",
        status: "",
        targetTranslation: {},
        translation: ""
    }
    public lastIssuedAutoTranslation: AutoTranslation = {
        externalReference: '',
        requestId: 0,
        status: "",
        targetLanguageCode: "",
        translatedText: "",
        modified: ""
    }
    public lastSuccessfulAutoTranslation: AutoTranslation = {
        externalReference: '',
        requestId: 0,
        status: "",
        targetLanguageCode: "",
        translatedText: "",
        modified: ""
    }

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private formIoService: FormIoService,
        private notificationsBannerService: NotificationsBannerService,
        private daefService: DAEFService
    ) { }
    
    ngOnInit(): void {
        this.environment = environment;

        this.route.params.subscribe((params: Params) => {
            this.targetLanguageCode = params['targetLanguageCode'];
            this.key = params['key'];
        });

        this.setLanguageDetailsByIsoCode(this.targetLanguageCode)
        this.setTranslationData()
        this.setAutoTranslationData();
    }

    back() {
        this.router.navigate(['admin-panel/translation/' + this.targetLanguageCode]);
    }

    setLanguageDetailsByIsoCode(languageIsoCode) 
    {
        this.formIoService.getFormIoResource('languages').subscribe((response) => {
            if (response instanceof Array) {
              let languages = response.map(response => response.data);
              languages.forEach((languageData) => {
                if (languageData.language == this.targetLanguageCode) {
                    this.targetLanguage = languageData;
                }
              })
            }
        })
    }

    setTranslationData()
    {
        this.daefService.getTranslationData(this.targetLanguageCode, this.key).subscribe(response => {
            this.translationData = response['data'];
            if (!this.translationData.targetTranslation) {
                this.translationData.targetTranslation = {}
            }
        })
    }

    setAutoTranslationData()
    {
        
        forkJoin(this.daefService.getAutoTranslationData(this.targetLanguageCode, this.key),
            this.daefService.getAutoTranslationData(this.targetLanguageCode, this.key, 'RECEIVED')
        ).subscribe(([lastIssuedRequest, lastSuccessfulRequest]) => {

            if(lastIssuedRequest) this.lastIssuedAutoTranslation = { ...lastIssuedRequest.data, modified: lastIssuedRequest.modified};
            if(lastSuccessfulRequest) this.lastSuccessfulAutoTranslation = { ...lastSuccessfulRequest.data, modified: lastSuccessfulRequest.modified};

            if(this.lastIssuedAutoTranslation.status === 'RECEIVED') {
                this.lastSuccessfulAutoTranslation = this.lastIssuedAutoTranslation;
            } else if(this.lastIssuedAutoTranslation.status === 'ERROR') {
                const errMessage = this.lastIssuedAutoTranslation.externalTranslationServiceErrorCode + " " + this.lastIssuedAutoTranslation.externalTranslationServiceErrorMessage;
                this.notificationsBannerService.hideAll();
                this.notificationsBannerService.show({message: "ERROR.INTEGRATIONS.ETRANSLATION", type: NotificationBarType.Error, additionalMessage: errMessage})
            }
        });
    }

    requestAutoTranslation()
    {
        this.daefService.postRequestAutoTranslation(this.targetLanguageCode, this.key).subscribe(response => {
            if (response && response.data.status == "SENT") {
                this.notificationsBannerService.hideAll();
                this.notificationsBannerService.show({message: "TRANSLATION.AUTO_TRANSLATION_REQUEST_SENT", type: NotificationBarType.Success })
            }
            this.setAutoTranslationData();
        })
    }

    useAutoTranslation()
    {
        if (this.lastSuccessfulAutoTranslation.translatedText) {
            this.translationData.targetTranslation.translation = this.lastSuccessfulAutoTranslation.translatedText
        }
    }

    setTranslationStatus(status: string) {
        this.daefService.postChangeTranslationStatus(this.targetLanguageCode, status, this.key).subscribe(response => {
            this.notificationsBannerService.hideAll();
            this.notificationsBannerService.show({message: "TRANSLATION.TRANSLATION_STATUS_CHANGED", type: NotificationBarType.Success })
            this.translationData.targetTranslation.status = status
        })
    }

    editTranslation()
    {
        this.daefService.postEditTranslation(this.targetLanguageCode, this.key, this.translationData.targetTranslation.translation).subscribe(response => {
            this.notificationsBannerService.hideAll();
            this.notificationsBannerService.show({message: "TRANSLATION.SAVED", type: NotificationBarType.Success })
            this.setTranslationData()
        })
    }

}
