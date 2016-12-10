import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";
import {routing} from "./app.routing";
import {LogReportComponent} from "./logreport/logreport.component";
import {LogReportService} from "./logreport/logreport.service";
import {AppComponent} from "./app.component";
import {ReplayComponent} from "./replay/replay.component";
import {WebSocketService} from "./websocket/websocket.service";
import {UsageComponent} from "./usage/usage.component";
import {UsageService} from "./usage/usage.service";
import {LogReportWebSocketService} from "./websocket/logreport-websocket.service";
import {UsageWebSocketService} from "./websocket/usage-websocket.service";
import {ReplayStatusWebSocketService} from "./websocket/replaystatus-websocket.service";
import {PropsComponent} from "./props/props.component";
import {PropsService} from "./props/props.service";

@NgModule({
    declarations: [
        AppComponent,
        LogReportComponent,
        ReplayComponent,
        UsageComponent,
        PropsComponent
    ],
    imports: [
        BrowserModule,
        FormsModule,
        HttpModule,
        routing
    ],
    providers: [
        LogReportService, WebSocketService, UsageService, PropsService,
        LogReportWebSocketService, UsageWebSocketService, ReplayStatusWebSocketService
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
