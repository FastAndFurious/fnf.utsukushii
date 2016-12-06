import {Injectable} from "@angular/core";
import {Subject} from "rxjs";
import {UsageReport} from "./UsageReport";
import {UsageWebSocketService} from "../websocket/usage-websocket.service";

@Injectable()
export class UsageService {

    private observable: Subject<UsageReport> = new Subject<UsageReport>();

    constructor ( private websocketService : UsageWebSocketService ) {

    }

    getReport () : Subject<UsageReport> {

        this.websocketService.connect(true, this.observable);
        return this.observable;
    }

    close() {
        this.websocketService.close(true);
        console.log("closing websocket");
    }
}
