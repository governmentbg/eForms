import { Component, OnInit } from '@angular/core';
import { TableColumn } from 'src/app/core/types/table-column';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { CamundaProcessService } from 'src/app/core/services/camunda-process.service';
import { UserProfileService } from 'src/app/core/services/user-profile.service';
import { UserProfile } from 'src/app/core/types/user-profile';
import { DAEFService } from 'src/app/core/services/daef-service.service';
import { DAEFServiceProvider } from 'src/app/core/types/daefservice';


@Component({
  selector: 'app-administration-of-services',
  templateUrl: './administration-of-services.component.html',
  styleUrls: ['./administration-of-services.component.scss']
})
export class AdministrationOfServicesComponent implements OnInit {
  tableData
  tableColumns: TableColumn[];
  apiCallUrl: string
  userProfile: UserProfile;


  constructor(
    private router: Router,
    private camundaProcessService: CamundaProcessService,
    public userProfileService: UserProfileService,
    private daefService: DAEFService,
    ) { }

  ngOnInit(): void {
    this.apiCallUrl = `/api/admin/projects/${environment.formioBaseProject}/eas`
    this.initializeColumns();
    this.userProfileService
    .subscribe((userProfile) => {
      this.userProfile = userProfile
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
      }
    ];
  }

  back() {
    this.router.navigate(['home']);
  }

  handleRowActionEvent(event) {

    const getCircularReplacer = () => {
      const seen = new WeakSet();
      return (key, value) => {
        if (typeof value === "object" && value !== null) {
          if (seen.has(value)) {
            return;
          }
          seen.add(value);
        }
        return value;
      };
    };

    this.daefService.getFullDAEFService(event.data.arId, event.data.supplierEAS)
    .subscribe(response => {      
      let easVariables = {
        service: JSON.parse(JSON.stringify(event, getCircularReplacer())),
        serviceSupplier: JSON.parse(JSON.stringify(response, getCircularReplacer())),
        userProfile: this.userProfile,
        formioBaseProject: environment.formioBaseProject,
        serviceSupplierAdministrativeUnit: "",
      }
      let variables = {};
      variables['context'] = {
        value: easVariables
      }
      this.daefService.setService(event.data.arId);
      this.camundaProcessService.startMetadataProcess({ variables });
    })
  }

}
