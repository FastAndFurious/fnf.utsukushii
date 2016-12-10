import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {Observable, Subject} from "rxjs";
@Injectable()
export class PropsService {
    constructor(private http: Http) {
    }

    private observable = new Subject<any>();

    getProps(): Observable<any> {
        this.http.get("/api/props").subscribe((r) => this.observable.next(r.json()), (e) => PropsService.handleError(e));
        return this.observable;
    }

    saveProps(props: any) {

        console.log("now posting " + props);
        this.http.post("/api/props", props).subscribe(
            (r) => {
                console.log("Success: " + r);
            },
            (e) => {
                console.log("Error: " + e );
            },
            () => {
                console.log("Closed.");
            });
    }

    static handleError(e): void {
        console.error(e);
    }
}