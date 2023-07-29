import { LightningElement, wire } from 'lwc';
import { refreshApex } from '@salesforce/apex';
import getDemos from '@salesforce/apex/DemoController.getDemos';

const DEMO_COLUMNS = [
    { label: 'Name', fieldName: 'name', type: 'text' },
    { label: 'Description', fieldName: 'description', type: 'text' }
];

export default class Demo_page extends LightningElement {

    demo_columns = DEMO_COLUMNS;

    @wire(getDemos)
    demos;

    get existsDemos() {
        return this.demos && this.demos.data;
    }

    handleReflesh() {
        refreshApex(this.demos);
    }
}