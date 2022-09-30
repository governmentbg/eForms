import { Directive, ElementRef, HostListener, Input, Renderer2 } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { TooltipPossitions } from 'src/app/core/types/tooltip-possitions';

@Directive({
  selector: '[appTooltip]'
})
export class TooltipDirective {
  
  @Input('appTooltip') tooltipBody: string;
  @Input() tooltipTitle: string;
  @Input() type: string ='notice';
  @Input() placement: string = "top-center";
  tooltip: HTMLElement;
  offset = 8;

  @HostListener('mouseenter') onMouseEnter() {
    if (!this.tooltip) { this.show(); }
  }

  @HostListener('mouseleave') onMouseLeave() {
    if (this.tooltip) { this.hide(); }
  }

  constructor(private el: ElementRef, private renderer: Renderer2, private translateService: TranslateService) { }
  show() {
    this.create();
    this.renderer.addClass(this.tooltip, 'tooltip-show');
    this.renderer.addClass(this.tooltip, this.type);
    this.setPosition();

  }

  hide() {
    this.renderer.removeClass(this.tooltip, 'tooltip-show');
    this.renderer.removeChild(document.body, this.tooltip);
    this.tooltip = null;
  }

  create() {
    this.tooltip = this.renderer.createElement('span');

    let tooltipTitle = this.renderer.createElement('span');
    if(this.tooltipTitle) {
      this.renderer.addClass(tooltipTitle, 'tooltip-title');
    }

    let tooltipBody = this.renderer.createElement('span');    
    this.renderer.addClass(tooltipBody, 'tooltip-body');

    let arrow = this.renderer.createElement('span');
    this.renderer.addClass(arrow, 'tooltip-arrow');
    this.renderer.addClass(arrow, this.placement);
    this.renderer.addClass(arrow, this.placement.split('-')[0]);

    let arrowInner = this.renderer.createElement('span');
    this.renderer.addClass(arrowInner, 'tooltip-arrow-inner');
    this.renderer.addClass(arrowInner, this.placement);    
    this.renderer.addClass(arrowInner, this.placement.split('-')[0]);

    this.renderer.appendChild(
      tooltipTitle,
      this.renderer.createText(this.tooltipTitle ? this.translateService.instant(this.tooltipTitle) : '')
    );
    if(this.tooltipTitle) {
      this.renderer.appendChild(
        tooltipTitle,
        this.renderer.createElement('br')
      );
    } 

    this.renderer.appendChild(
      tooltipBody,
      this.renderer.createText(this.tooltipBody ? this.translateService.instant(this.tooltipBody) : '')
    );

    this.renderer.appendChild(
      this.tooltip,
      tooltipTitle 
    );

    this.renderer.appendChild(
      this.tooltip,
      tooltipBody 
    );
    this.renderer.appendChild(
      this.tooltip,
      arrowInner 
    );
    this.renderer.appendChild(
      this.tooltip,
      arrow 
    );

    this.renderer.appendChild(this.el.nativeElement, this.tooltip);

  }

  setPosition() {
    const hostPos = this.el.nativeElement.getBoundingClientRect();
    const tooltipPos = this.tooltip.getBoundingClientRect();
    switch(TooltipPossitions[this.placement]) {
      case 0  : {
        this.renderer.setStyle(this.tooltip, 'top', `${0 - tooltipPos.height  - this.offset}px` );
        this.renderer.setStyle(this.tooltip, 'right', `0`);
        break;
      }
      case 1  : {
        this.renderer.setStyle(this.tooltip, 'top', `${0 - tooltipPos.height  - this.offset}px` );
        this.renderer.setStyle(this.tooltip, 'left', `${(hostPos.width -tooltipPos.width) / 2}px`);
        break;
      }
      case 2 : {
        this.renderer.setStyle(this.tooltip, 'top', `${0 - tooltipPos.height  - this.offset}px` );
        this.renderer.setStyle(this.tooltip, 'left', `0`);
        break;
      }
      case 3  : {
        this.renderer.setStyle(this.tooltip, 'top', `${(hostPos.height + this.offset)}px` );
        this.renderer.setStyle(this.tooltip, 'right', `0`);
        break;
      }      
      case 4 : {
        this.renderer.setStyle(this.tooltip, 'top', `${(hostPos.height + this.offset)}px` );
        this.renderer.setStyle(this.tooltip, 'right', `${(hostPos.width -tooltipPos.width) / 2}px`);
        break;
      }      
      case 5  : {
        this.renderer.setStyle(this.tooltip, 'top', `${(hostPos.height + this.offset)}px` );
        this.renderer.setStyle(this.tooltip, 'left', `0`);
        break;
      }
      case 6  : {
        this.renderer.setStyle(this.tooltip, 'top', `0` );
        this.renderer.setStyle(this.tooltip, 'left', `${0 - tooltipPos.width - this.offset}px`);
        break;
      }
      case 7  : {
        this.renderer.setStyle(this.tooltip, 'top', `${(hostPos.height  - tooltipPos.height) / 2}px` );
        this.renderer.setStyle(this.tooltip, 'left', `${0 - tooltipPos.width - this.offset}px`);
        break;
      }
      case 8  : {
        this.renderer.setStyle(this.tooltip, 'bottom', `0` );
        this.renderer.setStyle(this.tooltip, 'left', `${0 - tooltipPos.width - this.offset}px`);
        break;
      }
      case 9  : {
        this.renderer.setStyle(this.tooltip, 'top', `0` );
        this.renderer.setStyle(this.tooltip, 'right', `${0 - tooltipPos.width - this.offset}px`);
        break;
      }
      case 10  : {
        this.renderer.setStyle(this.tooltip, 'top', `${(hostPos.height  - tooltipPos.height) / 2}px` );
        this.renderer.setStyle(this.tooltip, 'right', `${0 - tooltipPos.width - this.offset}px`);
        break;
      }
      case 11  : {
        this.renderer.setStyle(this.tooltip, 'bottom', `0` );
        this.renderer.setStyle(this.tooltip, 'right', `${0 - tooltipPos.width - this.offset}px`);
        break;
      }
      default : {
        //default is top-center
        this.renderer.setStyle(this.tooltip, 'top', `${0 - tooltipPos.height  - this.offset}px` );
        this.renderer.setStyle(this.tooltip, 'left', `${(hostPos.width -tooltipPos.width) / 2}px`);
        break;
      }
    }
  }
}
