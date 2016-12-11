import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import {RaceDataFileInfo} from "./RaceDataFileInfo_";
import {Subject, Observable} from "rxjs";
import {ReplayCommand} from "./ReplayCommand";
import {ReplayStatus} from "./ReplayStatus";
import {ReplayStatusWebSocketService} from "../websocket/replaystatus-websocket.service";
import {StepForwardCommand} from "./StepForwardCommand";
import {AddBreakPointCommand} from "./AddBreakPointCommand";

@Injectable ()
export class ReplayService {

    private fileInfoObservable = new Subject<RaceDataFileInfo[]>();
    private replayStatusObservable = new Subject<ReplayStatus>();

    constructor ( private http: Http, private websocketService: ReplayStatusWebSocketService) {}

    listAll () : Subject<RaceDataFileInfo []> {

        this.http.get("api/replay").subscribe((data)=>{
            this.fileInfoObservable.next(data.json() as RaceDataFileInfo[]);
        });
        return this.fileInfoObservable;

    }

    getReplayStatus () : Observable<ReplayStatus> {

        this.websocketService.connect(true, this.replayStatusObservable);
        return this.replayStatusObservable;
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

    step  (cmd: StepForwardCommand ) : Observable<Response> {
        return this.http.post("api/replay/step", cmd );
    }

    addBreakPoint(breakPoint: number) : Observable<Response> {
        return this.http.post("api/replay/breakpoint", new AddBreakPointCommand(breakPoint));
    }

    closeWebsocket() {
        this.websocketService.close(true);
    }

}