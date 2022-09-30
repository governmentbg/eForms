import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormIoService } from 'src/app/core/services/form-io.service';

@Component({
  selector: 'app-translate-language',
  templateUrl: './translate-language.component.html',
  styleUrls: ['./translate-language.component.scss']
})
export class TranslateLanguageComponent implements OnInit {

    languages: any[] = [ ]

    constructor(
        private router: Router,
        private formIoService: FormIoService,
      ) { }
    
      ngOnInit(): void {
        this.formIoService.getFormIoResource('languages').subscribe((response) => {
          if (response instanceof Array) {
            this.languages = response.map(response => response.data);
          }
        })
      }

  back() {
    this.router.navigate(['admin-panel']);
  }

  selectLanguage(language) {
    this.router.navigate(['admin-panel/translation/' + language.language]);
  }

}
