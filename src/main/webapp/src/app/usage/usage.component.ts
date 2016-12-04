import {Component, OnInit, OnDestroy} from "@angular/core";
import {UsageService} from "./usage.service";
import {Observable} from "rxjs";
import {UsageReport} from "./UsageReport";

@Component({
    templateUrl: "usage.component.html",
    providers: [UsageService]
})
export class UsageComponent implements OnInit, OnDestroy {

    constructor(private usageService: UsageService) {
    }

    report = new UsageReport();
    prefix = "progress-bar-";
    latest: number = new Date().getTime();

    typeOf(percentage: number): string {
        if (percentage > 80) return this.prefix + "danger";
        if (percentage > 50) return this.prefix + "warning";
        return this.prefix + "success";
    }

    ngOnInit(): void {
        console.log("onInit: subscribing to usage records");
        this.usageService.getReport().subscribe((r) => {
                this.report = r;
                this.latest = new Date().getTime();
            },
            (e) => {
                console.log("error retrieving usage record: " + e);
            },
            () => {
                console.log("completed usage records");
            })
    }

    ngOnDestroy(): void {
        console.log("onDestroy: closing service");
        this.usageService.close();
    }

}