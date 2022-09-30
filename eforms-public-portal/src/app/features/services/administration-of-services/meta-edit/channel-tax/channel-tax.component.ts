import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import { AbstractControl, FormBuilder, NgForm, ValidatorFn, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import * as moment from 'moment';
import { Subscription } from 'rxjs';
import { metadataResources } from 'src/app/core/types/metadata-resources';
import { AdvancedDialogComponent } from 'src/app/shared/components/advanced-dialog/advanced.dialog.component';

@Component({
    selector: 'app-channel-tax',
    templateUrl: './channel-tax.component.html',
    styleUrls: ['./channel-tax.component.scss']
})
export class ChannelTaxComponent implements OnInit {
    private eventsSubscription = new Subscription();
    @Input() termsTaxesData;
    @Input() isGrey;
    @Input() availableChannels = [];
    @Input() eventPaymentChange ;
    @Output() updateChannelEvent: EventEmitter<any> = new EventEmitter();
    @Output() deleteChannelEvent: EventEmitter<any> = new EventEmitter();
    @Output() isEdditingChannel: EventEmitter<any> = new EventEmitter();
    channelTaxList;
    displayedColumns: string[] = ['typeChannel', 'typeTerm', 'term', 'amount', 'actions'];
    editChannels = false;
    editChannelsIndex = -1;
    availableDeadlines = metadataResources.deadlines ;
    availableDeadlineUnits = metadataResources.deadlineUnits
    editChannelFormGroup
    hasPayment : boolean;
    channelTemplate = {
        channel: ['',[Validators.required, this.channelAndTaxAreUniqueForPeriodValidator().bind(this)]],
        validFrom: ['',[Validators.required]],
        validTo: ['', [this.validToMustBeBiggerThanValidFromValidator().bind(this)]],
        deadlineType: ['',[Validators.required, this.channelAndTaxAreUniqueForPeriodValidator().bind(this)]],
        deadlineTerm: ['',[this.noDefinitionTermServiceValidator().bind(this)]],
        deadlineUnit: ['',[this.noDefinitionTermServiceValidator().bind(this)]],
        deadlineTypeLabel: [''],
        hasPayment: [false],
        taxAmount: ['',[this.taxAmountRequiredWithHasPaymentValidator().bind(this)]],
        currency: ['BGN'],
        id: ['']
      }
    constructor(
        private formBuilder: FormBuilder,
        private dialog: MatDialog,
        private translateService: TranslateService
    ) {
        this.editChannelFormGroup = this.formBuilder.group(this.channelTemplate)
    }

    getDeadlineTrans(key) {
        let transKey = this.availableDeadlines.find(d => d.key === key).transKey;
        return this.translateService.instant(transKey)
    }
    getDeadlineUnitLabel(deadlineUnit, deadlineTerm, capitalize = false) {
        let transKey = 'METADATA_REVIEW.CHAN_TAX.DEADLINE_UNIT.' + (deadlineTerm > 1 ? 'MULTI.' : 'SINGLE.') + deadlineUnit.key.toUpperCase()
        if (capitalize) {
            return this.translateService.instant(transKey)
        }
        return this.translateService.instant(transKey).toLowerCase()
    }

    ngOnInit(): void {
        if (this.eventPaymentChange) {
            this.eventsSubscription = this.eventPaymentChange.subscribe((result) => {
                this.hasPayment = result
                this.handleHasPaymentChangeFromService()
            });
        }
        if(this.termsTaxesData) {
            this.channelTaxList = this.termsTaxesData?.channelsAndTermsList.map(c => {
                if(!c.id){
                    c.id = Math.random()
                }
                return c
            })
        }
        else {
            this.channelTaxList = [] 
        }
    }

    addChannel(){
        this.editChannelsIndex = -1
        this.editChannels = true
        this.isEdditingChannel.emit(true)
        this.editChannelFormGroup = this.formBuilder.group(this.channelTemplate)
        this.handleHasPaymentChangeFromService()
    }

    declineChannel() {
        this.editChannels = false
        this.isEdditingChannel.emit(false)
    }

    saveChannelData() {
        Object.keys(this.editChannelFormGroup.controls).forEach(key => {
            this.editChannelFormGroup.controls[key].updateValueAndValidity()
          });

        if(this.editChannelFormGroup.valid) {
            if(this.editChannelFormGroup.value.deadlineUnit) {
                let deadlineUnit = this.editChannelFormGroup.value.deadlineUnit
                deadlineUnit.label = this.getDeadlineUnitLabel(this.editChannelFormGroup.value.deadlineUnit, this.editChannelFormGroup.value.deadlineTerm, true)
                this.editChannelFormGroup.controls.deadlineUnit.setValue(deadlineUnit)
            }
            let deadlineTypeLabel = this.availableDeadlines.find(d => d.key === this.editChannelFormGroup.value.deadlineType).label
            this.editChannelFormGroup.controls.deadlineTypeLabel.setValue(deadlineTypeLabel)
            if(this.editChannelsIndex === -1) {
                this.editChannelFormGroup.controls.id.setValue(Math.random())
                this.channelTaxList.push(this.editChannelFormGroup.value)
                this.channelTaxList = [...this.channelTaxList]
            }
            else {
                this.channelTaxList[this.editChannelsIndex] = this.editChannelFormGroup.value
                this.channelTaxList = [...this.channelTaxList]
            }
            this.editChannels = false
            this.isEdditingChannel.emit(false)
            this.updateChannelEvent.emit(this.editChannelFormGroup.value)
        }
    }

    editChannel(index) {
        this.editChannelsIndex = index   
        const channel = this.channelTaxList[this.editChannelsIndex];
        const selectedChannel = this.availableChannels.find( c => c.channelType === channel.channel.channelType)
        let selectedDeadlineUnit
        if(channel.deadlineType !== "noDefinitionTermService"){
            selectedDeadlineUnit = this.availableDeadlineUnits.find( d => d.key === channel.deadlineUnit.key)
        }
        this.editChannelFormGroup.setValue({
            channel: selectedChannel ? selectedChannel : '',
            validFrom: channel.validFrom ? channel.validFrom : '',
            validTo: channel.validTo ? channel.validTo : '',
            deadlineType: channel.deadlineType ? channel.deadlineType : '',
            deadlineTerm: channel.deadlineTerm ? channel.deadlineTerm : '',
            deadlineUnit: selectedDeadlineUnit ? selectedDeadlineUnit : '',
            deadlineTypeLabel:channel.deadlineTypeLabel ? channel.deadlineTypeLabel : '',
            hasPayment:channel.hasPayment ? channel.hasPayment : '',
            taxAmount: channel.taxAmount ? channel.taxAmount : '',
            currency: channel.currency ? channel.currency : 'BGN',
            id: channel.id
        });
        this.handleDeadLineChange()
        this.handleHasPaymentChangeFromService()
        this.editChannels = true  
        this.isEdditingChannel.emit(true)
    }

    deleteChannel(index){
        let deleteSigneeModalData = {
          headerTitle: "REMOVE_CHANNEL_MODAL_HEADER_TITLE",
          bodyIcon: "info",
          mainTitle: "REMOVE_CHANNEL_MODAL_MAIN_TITLE",
          bodyMessage : "",
          canGoBack: true,
          canGoBackMessage: "GO_BACK",
          canGoForward: true,
          canGoForwardMessage: "REMOVE"
        }
        
        let deleteSigneeModalRef = this.dialog.open(AdvancedDialogComponent, { data: deleteSigneeModalData });
    
        deleteSigneeModalRef.componentInstance.confirmed.subscribe(() => {
            this.deleteChannelEvent.emit(this.channelTaxList.splice(index,1)[0] )
            this.channelTaxList = [...this.channelTaxList]
        })
    
    }
    handleChannelChange(){
        this.editChannelFormGroup.controls.deadlineType.updateValueAndValidity()
        this.editChannelFormGroup.controls.channel.updateValueAndValidity()
    }

    handleDeadLineChange(){
        if(this.editChannelFormGroup.controls.deadlineType.value === 'noDefinitionTermService') {
            this.editChannelFormGroup.controls.deadlineTerm.setValue('')
            this.editChannelFormGroup.controls.deadlineTerm.disable()
            this.editChannelFormGroup.controls.deadlineUnit.setValue('')
            this.editChannelFormGroup.controls.deadlineUnit.disable()
        } else {
            this.editChannelFormGroup.controls.deadlineTerm.enable()
            this.editChannelFormGroup.controls.deadlineUnit.enable()
        }
        this.editChannelFormGroup.controls.deadlineType.updateValueAndValidity()
        this.editChannelFormGroup.controls.channel.updateValueAndValidity()
    }

    handleDateChange(){
        this.editChannelFormGroup.controls.validTo.setValue(moment(this.editChannelFormGroup.value.validTo).toISOString(), {emitEvent: true})
        this.editChannelFormGroup.controls.validFrom.setValue(moment(this.editChannelFormGroup.value.validFrom).toISOString(), {emitEvent: true})
        this.editChannelFormGroup.controls.validTo.updateValueAndValidity()
        this.editChannelFormGroup.controls.validFrom.updateValueAndValidity()
        this.editChannelFormGroup.controls.deadlineType.updateValueAndValidity()
        this.editChannelFormGroup.controls.channel.updateValueAndValidity()
    }

    handleHasPaymentChange(){
        this.editChannelFormGroup.controls.taxAmount.setValue('')
        this.editChannelFormGroup.controls.taxAmount.setErrors(null)
        this.editChannelFormGroup.controls.taxAmount.updateValueAndValidity();
    }

    handleHasPaymentChangeFromService() {
        if(!this.hasPayment){
            this.editChannelFormGroup.controls.hasPayment.setValue(false)
            this.editChannelFormGroup.controls.hasPayment.disable()
        } else {
            if (this.editChannelsIndex === - 1) {
                this.editChannelFormGroup.controls.hasPayment.setValue(true)
            }
            this.editChannelFormGroup.controls.hasPayment.enable() 
        }
    }
      
    validToMustBeBiggerThanValidFromValidator(): ValidatorFn {  
        return (control: AbstractControl): { [key: string]: any } | null => {
            if(this.editChannelFormGroup) {
                this.editChannelFormGroup.controls.validTo.errors = {}
                this.editChannelFormGroup.controls.validFrom.errors = {}
                if(!this.editChannelFormGroup.controls.validTo.value){   
                    return null;
                } else if(moment(this.editChannelFormGroup.controls.validTo.value).isAfter(this.editChannelFormGroup.controls.validFrom.value)) {
                    return null;
                } else {
                    return {validToMustBeBiggerThanValidFrom: true};
                }
            }
            return null;
        }            
    }

    taxAmountRequiredWithHasPaymentValidator(): ValidatorFn {  
        return (control: AbstractControl): { [key: string]: any } | null => {
            if(this.editChannelFormGroup) {
                if(!this.editChannelFormGroup.controls.hasPayment.value){
                    return null;
                } else if(this.editChannelFormGroup.controls.taxAmount.value === '' || this.editChannelFormGroup.controls.taxAmount.value === null || this.editChannelFormGroup.controls.taxAmount.value === undefined) {
                    return {taxAmountRequiredWithHasPayment: true};
                } else {
                    return null;
                }
            }
            return null;
        }            
    }

    noDefinitionTermServiceValidator(): ValidatorFn {  
        return (control: AbstractControl): { [key: string]: any } | null => {
            if(this.editChannelFormGroup) {
                if(this.editChannelFormGroup.controls.deadlineType.value === 'noDefinitionTermService'){   
                    return null;
                } else if(!control.value) {
                    return {required: true};
                }
            }
            return null;
        }            
    }

    channelAndTaxAreUniqueForPeriodValidator() : ValidatorFn {
        return (control: AbstractControl): { [key: string]: any } | null => {
            if(this.editChannelFormGroup) {
                let newChannel = this.editChannelFormGroup.value
                let isChannelUnique = true;
                this.channelTaxList.forEach(channel => {
                    if(channel.id !== newChannel.id ) {
                        if(channel.channel.channelType === newChannel.channel.channelType && channel.deadlineType === newChannel.deadlineType) {
                            if(moment(newChannel.validFrom).isSameOrAfter(channel.validFrom) && (!channel.validTo || moment(channel.validTo).isSameOrAfter(newChannel.validFrom))) {
                                isChannelUnique = false
                            }
                            if(moment(channel.validFrom).isSameOrAfter(newChannel.validFrom) && (!channel.validTo || !newChannel.validTo || moment(newChannel.validTo).isSameOrAfter(channel.validFrom))) {
                                isChannelUnique = false
                            }
                        }
                    }
                });
                return isChannelUnique ? null : {channelAndTaxMustBeUnique: true};
            }
            return null;
        }
    }
}
