import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {routing} from "./app.routing";
import {LogReportComponent} from "./logreport/logreport.component";
import {LogReportService} from "./logreport/logreport.service";

import {AppComponent} from './app.component';
import {ReplayComponent} from "./replay/replay.component";
import {WebSocketService} from "./websocket/websocket.service";

@NgModule({
    declarations: [
        AppComponent,
        LogReportComponent,
        ReplayComponent
    ],
    imports: [
        BrowserModule,
        FormsModule,
        HttpModule,
        routing
    ],
    providers: [LogReportService, WebSocketService],
    bootstrap: [AppComponent]
})
export class AppModule {
}
