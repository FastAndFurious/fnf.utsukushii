import {Component, OnInit} from "@angular/core";
import {PropsService} from "./props.service";

@Component({
    templateUrl: "props.component.html",
    providers: [PropsService],
    styleUrls: ["props.component.css"]
})
export class PropsComponent implements OnInit {

    constructor(private propsService: PropsService) {
    }

    selected: string = "Basics";
    private props: any;
    propsArray: { title: string, values: KVP[] }[] = [];

    process(props: any) {

        let basics: KVP[] = [];
        this.propsArray.push({title: "Basics", values: basics});

        for (let field in props) {
            //console.log("Field: " + field.toString());
            if (props.hasOwnProperty(field)) {

                if (props[field] !== null && typeof(props[field]) === 'object') {

                    let title = props[field].title;
                    let values: KVP[] = [];

                    let origProp = field.toString();
                    values.push(new KVP("origProp", origProp, false));

                    for (let field1 in props[field]) {
                        if (props[field].hasOwnProperty(field1)) {
                            let display: boolean = (field1.toString() !== "title");
                            //console.log (field1.toString() + ": " + display);
                            values.push(new KVP(field1.toString(), props[field][field1], display));
                        }
                    }

                    //= this.process(props[field]);
                    this.propsArray.push({title: title, values: values});

                } else {
                    //console.log("Basic field");
                    //console.log(field);
                    let display: boolean = (field.toString() !== "title" && field.toString() !== "id");
                    basics.push(new KVP(field.toString(), props[field], display));
                }
            }
        }
    }


    unCamel(camel: String): string {

        let words = [];
        let arr = camel.split('');
        let word = arr[0].toUpperCase();
        for (let n in arr) {
            if ( n == '0' ) continue;
            let char = arr[n];
            if (char.toUpperCase() === char) {
                words.push(word);
                word = char.toLowerCase();
            } else {
                word = word + char;
            }
        }
        words.push(word);
        return words.join(" ");
    }

    save() {
        let props = {};
        this.propsArray.forEach((entry)=>{
            if (entry.title==="Basics") {
                entry.values.forEach((kvp: KVP) => {
                    props[kvp.key] = kvp.value;
                });
            } else {
                let subprops = {};
                entry.values.forEach((kvp: KVP) => {
                    if ( kvp.key === "origProp" ) {
                        props[kvp.value] = subprops;
                    } else {
                        subprops[kvp.key] = kvp.value;

                    }
                });
            }
        });
        console.log(props);
        this.propsService.saveProps(props);
    }

    select(title: string) {
        this.selected = title;
        console.log("selected: " + title);
    }

    ngOnInit(): void {
        this.propsService.getProps().subscribe((p) => {
            this.process(p);
            console.log(p)
        });
    }

}

class KVP {

    key: string;
    value: any;
    display: boolean = true;
    constructor( key: string, value: any, display: boolean = true) {
        this.key = key;
        this.value = value;
        this.display = display;
    }
}