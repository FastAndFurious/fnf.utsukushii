import {Component, OnInit, OnDestroy} from '@angular/core';
import {LogReport, LogEntryCounter} from './logreport';
import {LogReportService} from "./logreport.service";
import {Router} from '@angular/router';
import {Observable} from "rxjs";

@Component({
    selector: 'log-report',
    styleUrls: ['./logreport.component.css'],
    templateUrl: './logreport.component.html',
    providers: [LogReportService]
})
export class LogReportComponent implements OnInit, OnDestroy {

    report: LogReport = new LogReport();
    selectedEntry: LogEntryCounter;

    constructor(private logReportService: LogReportService, private router: Router) {
    }

    getReport(): void {
        this.logReportService.getLogReport().subscribe(
            data => this.report = data,
            error => console.log(error)
        );
    }

    wsConnect(): void {

        this.logReportService.connect();
        console.log("trying to subscribe to ws");
        //this.logReportService.send("Hello");
        this.logReportService.getLogReport().subscribe(
            res => {
                //var report: LogReport = JSON.parse(res.data);
                console.log('Component got report: ' + res);
                console.log('Got Report: ' + res.entries + " entries.");
            },
            function (e) {
                console.log('Error: ' + e.message);
            },
            function () {
                console.log('Completed');
            }
        );
    }


    ngOnInit(): void {
        this.getReport();
        this.wsConnect();
    }

    ngOnDestroy(): void {

    }

    displayReport(report: LogReport): void {
        this.report = report;
    }


}














