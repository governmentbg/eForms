import {Component, ElementRef, Input, OnInit, QueryList, ViewChildren} from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MatTable } from '@angular/material/table';
import { TranslateService } from '@ngx-translate/core';
import {Observable, Subject} from "rxjs";
import { NotificationsBannerService } from 'src/app/core/services/notifications-banner.service';

@Component({
    selector: 'app-service-supplier-eas',
    templateUrl: './service-supplier-eas.component.html',
    styleUrls: ['./service-supplier-eas.component.scss']
})
export class ServiceSupplierEasComponent implements OnInit {
    @Input() metadata;
    @Input() availableChannelTaxList;
    @Input() availableAdministrativeUnitsList;
    @Input() updateChannelEvent: Observable<object>;
    @Input() deleteChannelEvent: Observable<object>;
    @ViewChildren(MatTable) tables;
    hasAdministrativeUnits: boolean = true;
    addChannelsSubscription;
    deleteChannelsSubscription;


    eventOpenAllCollapsable: Subject<void> = new Subject<void>();
    eventCloseAllCollapsable: Subject<void> = new Subject<void>();
    easForm : FormGroup;
    displayedColumns: string[] = ['actions','typeChannel', 'typeTerm', 'term', 'amount'];
    availableDeadlines = [
        {
          "label": "Нормална услуга",
          "key": "normalService",
          "code": "0006-000083"
        },
        {
          "label": "Бърза услуга",
          "key": "fastService",
          "code": "0006-000084"
        },
        {
          "label": "Експресна услуга",
          "key": "expressService",
          "code": "0006-000085"
        },
        {
          "label": "Няма нормативно указан срок",
          "key": "noDefinitionTermService",
          "code": "1006-120001"
        }
      ]
    

    constructor(private formBuilder: FormBuilder,
        private translateService: TranslateService,
        private notificationsBannerService: NotificationsBannerService,) {
        this.easForm = this.formBuilder.group({
            hasAdministrativeUnits: [''],
            administrativeUnits: this.formBuilder.array([]),
            searchBar: ['']
        })
    }

    ngOnInit(): void {
        this.addChannelsSubscription = this.updateChannelEvent.subscribe((channel) => {
            this.addChannels(channel)
        });
        this.deleteChannelsSubscription = this.deleteChannelEvent.subscribe((channel) => {
            this.deleteChannel(channel)
        });
        if(this.availableAdministrativeUnitsList.length) {
            this.availableAdministrativeUnitsList.forEach(element => {
                let selectedIndex = this.metadata.administrativeUnitsList ? this.metadata.administrativeUnitsList.findIndex(au => au.administrationUnit === element.administrationUnit) : -1
                if(this.easForm.value.administrativeUnits.findIndex(a => a.unit.administrationUnit === element.administrationUnit) === -1) {
                    const auForm = this.formBuilder.group({
                        unit: [element], 
                        selected: [selectedIndex !== -1],
                        visible: [true],
                        channelsAndTermsList: this.formBuilder.array([])
                    });
                    if(this.availableChannelTaxList?.length) {
                        let hasSelectedChannels = false
                        let selectedUnit
                        if (selectedIndex != -1) {
                            selectedUnit = this.metadata.administrativeUnitsList[selectedIndex]
                            hasSelectedChannels = selectedUnit?.channelsAndTermsList.length > 0
                        }
                        this.availableChannelTaxList.forEach(channel => {
                            let channelIndex = -1
                            if (selectedIndex != -1 && hasSelectedChannels) {
                                channelIndex = selectedUnit.channelsAndTermsList.findIndex(c => (c.channel.channelType === channel.channel.channelType && c.deadlineType === channel.deadlineType && c.validFrom == channel.validFrom && c.validTo == channel.validTo))
                            }
                            let selected = !hasSelectedChannels;
                            if(channelIndex != - 1) {
                                selected = true
                            }
                            const channelForm = this.formBuilder.group({
                                channelsAndTerms: [channel], 
                                selected: [selected]
                            });
                            (<FormArray>auForm.controls.channelsAndTermsList).push(channelForm)
                        })
                    } 
                }
                this.easForm.value.administrativeUnits.findIndex(a => a.unit.administrationUnit === element.administrationUnit)
                const auForm = this.formBuilder.group({
                    unit: [element], 
                    selected: [selectedIndex !== -1],
                    visible: [true],
                    channelsAndTermsList: this.formBuilder.array([])
                });
                if(this.availableChannelTaxList?.length) {
                    let hasSelectedChannels = false
                    let selectedUnit
                    if (selectedIndex != -1) {
                        selectedUnit = this.metadata.administrativeUnitsList[selectedIndex]
                        hasSelectedChannels = selectedUnit?.channelsAndTermsList.length > 0
                    }
                    this.availableChannelTaxList.forEach(channel => {
                        let channelIndex = -1
                        if (selectedIndex != -1 && hasSelectedChannels) {
                            channelIndex = selectedUnit.channelsAndTermsList.findIndex(c => (c.channel.channelType === channel.channel.channelType && c.deadlineType === channel.deadlineType && c.validFrom == channel.validFrom && c.validTo == channel.validTo))
                        }
                        let selected = !hasSelectedChannels;
                        if(channelIndex != - 1) {
                            selected = true
                        }
                        const channelForm = this.formBuilder.group({
                            channelsAndTerms: [channel], 
                            selected: [selected]
                        });
                        (<FormArray>auForm.controls.channelsAndTermsList).push(channelForm)
                    })
                }
                (<FormArray>this.easForm.controls.administrativeUnits).push(auForm);
            });
            this.easForm.controls.hasAdministrativeUnits.setValue(this.metadata?.hasAdministrativeUnits)
        } else {
            this.easForm.controls.hasAdministrativeUnits.setValue('no')            
            this.easForm.controls.hasAdministrativeUnits.disable()
        }
    }

    openAllCollapsable() {
        this.eventOpenAllCollapsable.next();
    }

    closeAllCollapsable() {
        this.eventCloseAllCollapsable.next();
    }

    getDeadlineLabel(key) {
        return this.availableDeadlines.find(d => d.key === key).label
    }
    getDeadlineUnitLabel(deadlineUnit, deadlineTerm) {
        let transKey = 'METADATA_REVIEW.CHAN_TAX.DEADLINE_UNIT.' + (deadlineTerm > 1 ? 'MULTI.' : 'SINGLE.') + deadlineUnit.key.toUpperCase()
        return this.translateService.instant(transKey).toLowerCase()
    }
    addChannels(channel) {
        (<FormArray>this.easForm.controls.administrativeUnits).controls.forEach((eas : FormGroup) => {
            let isNew = true;
            (<FormArray>eas.controls.channelsAndTermsList).controls.forEach((c: FormGroup)=> {
                if(c.value.channelsAndTerms.id == channel.id) {
                    isNew = false
                    c.setValue({channelsAndTerms: channel, selected: c.value.selected}, {emitEvent: true})
                }
            })
            if(isNew){
                let selecteed = eas.value.channelsAndTermsList.filter(c => c.selected === true).length === eas.value.channelsAndTermsList.length
                const channelForm = this.formBuilder.group({
                    channelsAndTerms: [channel], 
                    selected: [selecteed]
                });
                (<FormArray>eas.controls.channelsAndTermsList).push(channelForm)
            }
        })
        this.tables.forEach(table => {
            table.renderRows()
        })
    }
    deleteChannel(channel) {
        (<FormArray>this.easForm.controls.administrativeUnits).controls.forEach((eas : FormGroup) => {
            (<FormArray>eas.controls.channelsAndTermsList).controls.forEach((c: FormGroup, index)=> {
                if(c.value.channelsAndTerms.id == channel.id) {
                    (<FormArray>eas.controls.channelsAndTermsList).controls.splice(index, 1)
                }
            })
        })
        this.tables.forEach(table => {
            table.renderRows()
        })
    }
    filter(){
        (<FormArray>this.easForm.controls.administrativeUnits).controls.forEach( eas => {
           (<FormGroup>eas).controls.visible.setValue(eas.value.unit.administrationUnit.toLowerCase().includes(this.easForm.value.searchBar.toLowerCase()))
        })
    }
    clearFilter(){
        this.easForm.controls.searchBar.setValue('')
        this.filter()
    }
    toggleAll(isSelected) {
        (<FormArray>this.easForm.controls.administrativeUnits).controls.forEach( eas => {
            if(eas.value.visible) {
                (<FormGroup>eas).controls.selected.setValue(isSelected)
            }
         })
    }
    isUnitValid(unit) {
        if(unit.value.selected){
            this.notificationsBannerService.hideAllErrors()
        }
        return !(unit.value.selected && unit.value.channelsAndTermsList.filter(c => {return c.selected}).length === 0 )
    }
    handleHasAdministrativeUnitsChange(value) {
        if(value?.value === 'no') {
            this.notificationsBannerService.hideAllErrors()
        }
    }
}
