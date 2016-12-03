import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import {RaceDataFileInfo} from "./RaceDataFileInfo_";
import {Subject, Observable} from "rxjs";
import {ReplayCommand} from "./ReplayCommand";

@Injectable ()
export class ReplayService {

    private observable = new Subject<RaceDataFileInfo[]>();

    constructor ( private http: Http ) {}

    listAll () : Subject<RaceDataFileInfo []> {

        this.http.get("api/replay").subscribe((data)=>{
            this.observable.next(data.json() as RaceDataFileInfo[]);
        });
        return this.observable;

    }

    start ( cmd: ReplayCommand ) : Observable<Response> {
        return this.http.post("api/replay", cmd );
    }

    suspend () : Observable<Response> {
        return this.http.post("api/replay/suspend", null);
    }

    resume () : Observable<Response> {
        return this.http.post("api/replay/resume", null );
    }

    stop () : Observable<Response> {
        return this.http.post("api/replay/stop", null );
    }

}