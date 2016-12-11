export class StepForwardCommand {

    type: string;
    runUntil: number;
    numSteps: number;

    constructor( type: string, runUntil: number, steps: number ) {
        this.type = type;
        this.runUntil = runUntil;
        this.numSteps = steps;
    }
}