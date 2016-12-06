import {Injectable, Inject} from "@angular/core";
import {WebSocketService} from "./websocket.service";
import {Subject} from "rxjs";
import {DOCUMENT} from "@angular/platform-browser";
@Injectable()
export class ReplayStatusWebSocketService extends WebSocketService {

    constructor(@Inject(DOCUMENT) document) {
        super();
        this.initWsUrl(document);
    }

    connect(force: boolean = false, subject: Subject<any>): void {

        this.connectToPath(force, "/ws/replaystatus", subject);
    }
}