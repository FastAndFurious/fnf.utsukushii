import {Component, OnInit, OnDestroy} from '@angular/core';
import {LogReport, LogEntryCounter} from './logreport';
import {LogReportService} from "./logreport.service";
import {Router} from '@angular/router';
import {Observable} from "rxjs";
import {WebSocketService} from "../websocket/websocket.service";

@Component({
    selector: 'log-report',
    styleUrls: ['./logreport.component.css'],
    templateUrl: './logreport.component.html',
    providers: [LogReportService, WebSocketService]
})
export class LogReportComponent implements OnInit, OnDestroy {

    report: LogReport = new LogReport();

    constructor(private logReportService: LogReportService) {
    }

    getReport(): void {
        this.logReportService.getLogReport().subscribe(
            data => this.report = data,
            error => console.log(error)
        );
    }

    ngOnInit(): void {
        this.getReport();
    }

    ngOnDestroy(): void {

    }

    displayReport(report: LogReport): void {
        this.report = report;
    }


}














