import { Pipe, PipeTransform } from '@angular/core';
import { get } from 'lodash';

@Pipe({
  name: 'dataPropertyGetter'
})
export class DataPropertyGetterPipe implements PipeTransform {

  transform(object: any, keyName: string, ...args: unknown[]): unknown {
    return get(object, keyName);
  }

}