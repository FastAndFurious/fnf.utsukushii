import {Component, OnInit, OnDestroy} from "@angular/core";
import {RaceDataFileInfo} from "./RaceDataFileInfo_";
import {ReplayService} from "./replay.service";
import {ReplayCommand} from "./ReplayCommand";
import {ReplayStatus} from "./ReplayStatus";
import {StepForwardCommand} from "./StepForwardCommand";

@Component({
    selector: "replay",
    providers: [ReplayService],
    templateUrl: "./replay.component.html"
})
export class ReplayComponent implements OnInit, OnDestroy {

    private raceData : RaceDataFileInfo[];
    private frequency = 50.0;
    private breakPoint: number;
    private status: ReplayStatus = new ReplayStatus();

    constructor ( private replayService: ReplayService ) {
        this.replayService.getReplayStatus().subscribe((status)=>{
            this.status = status;
        })
    }

    start ( file: string ) {
        console.log("Starting: " + file + "with frequency " + this.frequency + " Hz.");
        let cmd = new ReplayCommand(file, this.frequency);
        this.status.status = "PLAYING";
        this.status.fileName = file;
        this.replayService.start(cmd).subscribe((r)=>console.log(r), (e)=>console.log(e), ()=>{})
    }

    suspend () {
        this.status.status = "SUSPENDED";
        console.log("Suspending...");
        this.replayService.suspend().subscribe(()=>{}, (e)=>console.log(e), ()=>{});
    }

    resume () {
        this.status.status = "PLAYING";
        this.status.fileName = null;
        console.log("Resuming...");
        this.replayService.resume().subscribe(()=>{}, (e)=>console.log(e), ()=>{});
    }

    stop () {
        this.status.status = "OFF";
        this.status.fileName = null;
        console.log("stopping...");
        this.replayService.stop().subscribe(()=>{}, (e)=>console.log(e), ()=>{});
    }

    step () {
        console.log("stepping...");
        this.replayService.step(new StepForwardCommand("Steps", 0, 1)).subscribe(()=>{}, (e)=>console.log(e), ()=>{});
    }

    addBreakPoint () {
        this.replayService.addBreakPoint ( this.breakPoint).subscribe(()=>{}, (e)=>console.log(e), ()=>{});
    }

    ngOnInit(): void {
        this.replayService.listAll().subscribe((list)=>{
            this.raceData = list;
            console.log("Received list: " + this.raceData[0].fileName )
        })
    }

    ngOnDestroy(): void {
        console.log("Closing socket to replay");
        this.replayService.closeWebsocket();
    }

    canStart () : boolean {
        return this.status.status === "OFF";
    }

    canSuspend ( file: string ) : boolean {
        return this.status.status === "PLAYING" && this.status.fileName === file;
    }

    canResume ( file: string ) : boolean {
        return this.status.status === "SUSPENDED" && this.status.fileName === file;
    }

    canStep ( file: string ) : boolean {
        return this.canResume( file);
    }

    canStop ( file: string ) : boolean {
        return this.status.status !== "OFF" && this.status.fileName === file;
    }


}