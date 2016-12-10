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
            if (props.hasOwnProperty(field)) {

                if (props[field] !== null && typeof(props[field]) === 'object') {

                    let title = props[field].title;
                    let values: KVP[] = [];

                    for (let field1 in props[field]) {
                        if (props.hasOwnProperty(field)) {
                            values.push(new KVP(field1.toString(), props[field][field1]));
                        }
                    }

                    //= this.process(props[field]);
                    this.propsArray.push({title: title, values: values});


                } else {
                    console.log("Basic field");
                    console.log(field);
                    basics.push(new KVP(field.toString(), props[field]));
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
                console.log("found: " + char)
                words.push(word);
                word = char.toLowerCase();
            } else {
                word = word + char;
            }
        }
        words.push(word);
        return words.join(" ");
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
    constructor(private key: string, private value: any) {
    }
}