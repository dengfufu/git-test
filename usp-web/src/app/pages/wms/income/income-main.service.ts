import {Injectable} from '@angular/core';

export class IncomeMain {
  largeClassId = 10;
  largeClassName = '备件入库';
  smallClassId: number;
  smallClassName: string;
  depotId: string;
  depotName: string;
  propertyRight: string;
  propertyRightName: string;
  contId: string;
  purchaseDetailId: string;
  supplierId: string;
  supplierName: string;
}

@Injectable({
  providedIn: 'root'
})
export class IncomeMainService {

  incomeMain: IncomeMain = new IncomeMain();

  constructor() {
  }

  clear() {
    this.incomeMain = new IncomeMain();
  }

}
