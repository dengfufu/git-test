import { Component } from '@angular/core';
import { NzIconService } from 'ng-zorro-antd/icon';

const ngZorroIconLiteral =
'<svg width="51px" height="50px" viewBox="0 0 51 50" version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">\n' +
  '    <!-- Generator: Sketch 59 (86127) - https://sketch.com -->\n' +
  '    <title>编组 3</title>\n' +
  '    <desc>Created with Sketch.</desc>\n' +
  '    <g id="-" stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">\n' +
  '        <g id="备件管理-备件入库-查看详情" transform="translate(-795.000000, -232.000000)">\n' +
  '            <g id="编组-2" transform="translate(795.000000, 232.000000)">\n' +
  '                <g id="编组-3">\n' +
  '                    <ellipse id="椭圆形备份-17" fill-opacity="0.26" fill="#20BF8E" transform="translate(25.500000, 25.000000) rotate(-270.000000) translate(-25.500000, -25.000000) " cx="25.5" cy="25" rx="24.5" ry="25"></ellipse>\n' +
  '                    <ellipse id="椭圆形备份-22" fill-opacity="0.6" fill="#20BF8E" transform="translate(25.500000, 25.000000) rotate(-270.000000) translate(-25.500000, -25.000000) " cx="25.5" cy="25" rx="14.5" ry="16"></ellipse>\n' +
  '                    <g id="gouxuan" transform="translate(19.000000, 19.000000)"></g>\n' +
  '                    <path d="M24.5267391,30.6509929 C24.38903,30.8569963 24.1879559,30.9829144 23.9716055,30.9989423 C23.9561307,31 23.9408296,31 23.9254044,31 C23.725299,31 23.5321486,30.9056496 23.3867641,30.7345224 L19.2400908,25.8496975 C18.9295498,25.4846354 18.9187944,24.8775816 19.2163444,24.4959763 C19.5138448,24.1136929 20.0067311,24.1011364 20.3174459,24.4667138 L23.8576677,28.6362491 L30.6200438,19.3497418 C30.8932513,18.9405825 31.3841505,18.8822203 31.7161526,19.2181014 C32.0481548,19.554579 32.0957717,20.1581886 31.8226884,20.5667783 L24.5266895,30.6509658 L24.5267391,30.6509929 Z" id="路径" fill="#FFFFFF" fill-rule="nonzero"></path>\n' +
  '                </g>\n' +
  '            </g>\n' +
  '        </g>\n' +
  '    </g>\n' +
  '</svg'
@Component({
  selector: 'green-round-icon',
  templateUrl: 'green-round-icon.component.html',
  styleUrls: ['green-round-icon.component.less'],
})
export class GreenRoundIconComponent {
  constructor(private iconService: NzIconService) {
    this.iconService.addIconLiteral('ng-zorro:antd', ngZorroIconLiteral);
  }
}
