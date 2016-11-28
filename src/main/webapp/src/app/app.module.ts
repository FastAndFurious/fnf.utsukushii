import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {routing} from "./app.routing";
import {LogReportComponent} from "./logreport/logreport.component";
import {LogReportService} from "./logreport/logreport.service";

import {AppComponent} from './app.component';

@NgModule({
    declarations: [
        AppComponent,
        LogReportComponent
    ],
    imports: [
        BrowserModule,
        FormsModule,
        HttpModule,
        routing
    ],
    providers: [LogReportService],
    bootstrap: [AppComponent]
})
export class AppModule {
}
