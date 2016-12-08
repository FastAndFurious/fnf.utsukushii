import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {Observable, Subject} from "rxjs";
@Injectable()
export class PropsService {
    constructor ( private http: Http) {}

    private observable = new Subject<any>();

    getProps () : Observable<any> {
        this.http.get("/api/props").subscribe((r)=>this.observable.next(r.json()), (e)=>PropsService.handleError(e));
        return this.observable;
    }

    static handleError (e): void {
        console.error(e);
    }
}