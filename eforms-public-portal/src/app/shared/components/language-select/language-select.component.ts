import { Component, Input, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { LanguageService } from 'src/app/core/services/language.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-language-select',
  templateUrl: './language-select.component.html',
  styleUrls: ['./language-select.component.scss']
})
export class LanguageSelectComponent implements OnInit {
  @Input() menuXPosition: string = 'before';

  selectedLanguage: any = { };
  languages: any[] = [ ]

  constructor(
    private translateService: TranslateService,
    private languageService: LanguageService
  ) {
    this.selectedLanguage = { };
    this.languages = [ ];
  }

  ngOnInit(): void {
    this.languageService.languages.subscribe((response) => {
      this.languages = response.map(response => response.data);

      let allowedLangs = this.languages.map(lang => lang.language);
      
      this.translateService.addLangs(allowedLangs);
      this.translateService.setDefaultLang(environment.defaultLanguage);

      if (allowedLangs.includes(localStorage.getItem('language'))) {
        let findLang = this.languages.find(lang => lang.language === localStorage.getItem('language'));
        this.translateService.use(localStorage.getItem('language'));
        this.selectedLanguage = findLang;
      } else {
        this.translateService.use(environment.defaultLanguage);
        this.selectedLanguage = this.languages.find(lang => lang.language === environment.defaultLanguage);
        localStorage.setItem('language', environment.defaultLanguage);
      }
    });
  }

  changeLanguage(language) {
    this.translateService.use(language.language);
    localStorage.setItem('language', language.language);
    this.selectedLanguage = language;
  }
}
