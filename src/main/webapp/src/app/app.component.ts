import {Component, OnInit} from '@angular/core';
import {LogReportService} from "./logreport/logreport.service";


@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css'],
    providers: [ LogReportService]
})
export class AppComponent implements OnInit{
    title = 'app works!';
    logreportService: LogReportService;

    constructor ( logreportService: LogReportService ) {
        this.logreportService = logreportService;
    }

    ngOnInit () {
        this.title = "Log Reports";
    }

}
