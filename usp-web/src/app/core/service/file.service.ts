import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {Result} from '@core/interceptor/result';
import {environment} from '@env/environment';

/**
 * TODO
 */
@Injectable({
  providedIn: 'root'
})
export class FileService {

  static BASE_URL = environment.server_url + '/api/file/showFile?fileId=';

  constructor(private httpClient: HttpClient) {

  }

  // 上传文件   返回文件id
  upload(file): Observable<string> {
    const formData = new FormData();
    formData.append('file', file);
    return this.httpClient.post('/api/file/uploadFile', formData)
      .pipe(
        map((result: Result) => {
          return result.data && result.data.fileId;
        }),
        catchError((error: Error) => {
          alert(error.message);
          return throwError(`I caught: ${error}`);
        })
      );
  }

  // 上传头像文件   返回两个文件id
  uploadFaceImg(file): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.httpClient.post('/api/file/uploadFaceImg', formData)
      .pipe(
        map((result: Result) => {
          return result.data;
        }),
        catchError((error: Error) => {
          alert(error.message);
          return throwError(`上传出错！: ${error}`);
        })
      );
  }

  // 上传图片文件   返回两个文件id，后一个为缩略图
  uploadImg(file, thumbWidth, thumbHeight): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('thumbWidth', thumbWidth);
    formData.append('thumbHeight', thumbHeight);
    return this.httpClient.post('/api/file/uploadImg', formData)
      .pipe(
        map((result: Result) => {
          return result.data;
        }),
        catchError((error: Error) => {
          alert(error.message);
          return throwError(`上传出错！: ${error}`);
        })
      );
  }

  delete(fileId: string): Observable<string> {
    const payload = new HttpParams()
      .set('fileId', fileId);
    return this.httpClient.post('/api/file/delFile', payload)
      .pipe(
        map((result: Result) => {
          return result.data && result.data;
        })
      );
  }
}
