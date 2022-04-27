import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-mobile-navigation',
  templateUrl: './mobile-navigation.component.html',
  styleUrls: ['./mobile-navigation.component.scss']
})
export class MobileNavigationComponent implements OnInit {
  @Input() mainStepNumber;
  @Input() subStepNumber;
  @Input() stepPercentage;
  @Input() totalLanes: number;
  @Input() currentLaneIndex: number;
  @Input() currentLaneName: string;
  @Input() currentSubStepName: string;
  @Input() isCollapsed: boolean;

  constructor() { }

  ngOnInit(): void {
  }

}
