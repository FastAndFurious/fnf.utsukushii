import {Component, OnInit, OnDestroy} from "@angular/core";
import {RaceDataFileInfo} from "./RaceDataFileInfo_";
import {ReplayService} from "./replay.service";
import {ReplayCommand} from "./ReplayCommand";

@Component({
    selector: "replay",
    providers: [ReplayService],
    templateUrl: "./replay.component.html"
})
export class ReplayComponent implements OnInit, OnDestroy {

    private raceData : RaceDataFileInfo[];
    private frequency = 50.0;

    constructor ( private replayService: ReplayService ) {

    }

    start ( file: string ) {
        console.log("Starting: " + file + "with frequency " + this.frequency + " Hz.");
        let cmd = new ReplayCommand(file, this.frequency);
        this.replayService.start(cmd).subscribe((r)=>console.log(r), (e)=>console.log(e), ()=>console.log("completed"))
    }

    ngOnInit(): void {
        this.replayService.listAll().subscribe((list)=>{
            this.raceData = list;
            console.log("Received list: " + this.raceData[0].fileName )
        })
    }

    ngOnDestroy(): void {
    }

}