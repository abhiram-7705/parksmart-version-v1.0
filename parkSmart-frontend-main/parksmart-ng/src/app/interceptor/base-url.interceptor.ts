import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environments';

@Injectable()
export class BaseUrlInterceptor implements HttpInterceptor {

  private readonly BASE_URL = environment.apiUrl;

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {

    if (req.url.startsWith('http')) {
      return next.handle(req);
    }

    const apiReq = req.clone({
      url: this.BASE_URL + req.url
    });

    return next.handle(apiReq);
  }
}