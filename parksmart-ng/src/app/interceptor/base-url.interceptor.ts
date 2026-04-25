import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class BaseUrlInterceptor implements HttpInterceptor {

  private readonly BASE_URL = 'http://localhost:8081';

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {

    // If the request URL is already absolute, do nothing
    if (req.url.startsWith('http')) {
      return next.handle(req);
    }

    // Otherwise, add the base URL
    const apiReq = req.clone({
      url: this.BASE_URL + req.url
    });

    return next.handle(apiReq);
  }
}