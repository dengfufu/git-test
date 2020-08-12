import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-token-list',
  templateUrl: 'token-list.component.html'
})

export class TokenListComponent implements OnInit {

  validateForm: FormGroup;
  tokenList: any;
  loading = true;

  resetForm(): void {
    this.validateForm.reset();
  }

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient) {
  }

  ngOnInit(): void {
    this.loadList();
    this.validateForm = this.formBuilder.group({});
    this.validateForm.addControl('key', new FormControl());
  }

  // 初始化数据
  loadList(): void {
    const params = {};
    this.loading = true;
    this.httpClient
      .post('/api/auth/tokens/list',
        params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.tokenList = res.data;
        this.loading = false;
      });
  }

}
