import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-orn',
  templateUrl: './orn.component.html',
  styleUrls: ['./orn.component.scss']
})
export class OrnComponent implements OnInit {
  @Input() ornNumber: string;
  orn: string;
  timestamp: string;
  constructor() { }

  ngOnInit(): void {
    var [orn, timestamp] = this.ornNumber.split(' ');
    this.orn = orn;
    this.timestamp = timestamp;
  }

}
