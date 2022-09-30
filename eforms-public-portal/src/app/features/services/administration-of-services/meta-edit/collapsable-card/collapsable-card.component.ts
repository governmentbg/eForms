import {Component, EventEmitter, Input, OnInit} from '@angular/core';
import {Observable, Subject, Subscription} from "rxjs";

@Component({
    selector: 'app-collapsable-card',
    templateUrl: './collapsable-card.component.html',
    styleUrls: ['./collapsable-card.component.scss']
})
export class CollapsableCardComponent implements OnInit {
    private eventsSubscription = new Subscription();
    @Input() initialVisibility: boolean;
    @Input() title?: string;
    @Input() isGrey: boolean;
    @Input() forceOpen: Observable<void>;
    @Input() forceClose: Observable<void>;
    @Input() toggleOnlyOnIcon = false;
    @Input() hasError = false;

    visible: boolean

    constructor() {
    }

    ngOnInit(): void {
        this.visible = this.initialVisibility
        if (this.forceOpen) {
            this.eventsSubscription = this.forceOpen.subscribe(() => {
                this.visible = true;
            });
        }
        if (this.forceClose) {
            this.eventsSubscription = this.forceClose.subscribe(() => {
                this.visible = false;
            });
        }
    }

    toggle() {
        this.visible = !this.visible
    }

    ngOnDestroy() {
        this.eventsSubscription.unsubscribe();
    }
}
