import {Component, OnInit} from "@angular/core";
import {PropsService} from "./props.service";

@Component({
    templateUrl: "props.component.html",
    providers: [PropsService]
    })
export class PropsComponent implements OnInit {

    constructor(private propsService: PropsService) {}

    selected: string = "Basics";
    private props: any;
    propsArray: { title: string, values: KVP[] }[] = [];

    process (props: any ) {

        let basics : KVP[] = [];
        this.propsArray.push({ title: "Basics", values: basics});

        for ( let field in props ) {
            if ( props.hasOwnProperty(field)) {

                if (props[field] !== null && typeof(props[field]) === 'object') {

                    let title = field.toString();
                    let values: KVP[] = [];

                    for ( let field1 in props[field] ) {
                        if ( props.hasOwnProperty(field)) {
                            values.push(new KVP(field1.toString(), props[field][field1]));
                        }
                    }

                        //= this.process(props[field]);
                    this.propsArray.push( {title: title, values: values });


                } else {
                    console.log("Basic field");
                    console.log(field);
                    basics.push(new KVP(field.toString(), props[field]));
                }
            }
        }
    }

    select ( title: string ) {
        this.selected = title;
    }

    ngOnInit(): void {
        this.propsService.getProps().subscribe( (p) => {
            this.process(p);
            console.log(p)
        });
    }

}

class KVP {
    constructor ( private key: string, private value: any) {}
}