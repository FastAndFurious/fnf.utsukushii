import {Injectable, Inject} from "@angular/core";
import {Subject} from "rxjs/Subject";
import {DOCUMENT} from "@angular/platform-browser";
import {LogReport} from "./logreport";
import {Http} from "@angular/http";
import {WebSocketService} from "../websocket/websocket.service";


@Injectable()
export class LogReportService {

    private wsPath: string;
    private restUrl: string;
    private reports = new Subject<LogReport>();

    constructor(private http: Http, @Inject(DOCUMENT) document, private websocketService: WebSocketService) {
        let hostname = document.location.hostname;
        let port = document.location.port;
        this.wsPath = "/ws/logreports";
        this.restUrl = "http://" + hostname + ":" + port + "/api/logs";
        console.log("Now connecting via " + this.wsPath + " and " + this.restUrl);
    }

    getLogReport(): Subject<LogReport> {

        this.websocketService.connect(true, this.wsPath, this.reports);
        this.http.get(this.restUrl).subscribe(
            (res) => this.reports.next(res.json() as LogReport),

        );
        return this.reports;
    }


    closeWebsocket() {
        this.websocketService.close(true);
    }
}
