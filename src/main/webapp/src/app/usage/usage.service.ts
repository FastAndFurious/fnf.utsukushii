import {Injectable} from "@angular/core";
import {Subject} from "rxjs";
import {UsageReport} from "./UsageReport";
import {WebSocketService} from "../websocket/websocket.service";

@Injectable()
export class UsageService {

    private observable: Subject<UsageReport> = new Subject<UsageReport>();

    constructor ( private websocketService : WebSocketService ) {

    }

    getReport () : Subject<UsageReport> {

        this.websocketService.connect(true, "/ws/usage", this.observable);
        return this.observable;
    }

    close() {
        this.websocketService.close(true);
        console.log("closing websocket");
    }
}
