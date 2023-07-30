import { LightningElement, wire } from 'lwc';
import { refreshApex } from '@salesforce/apex';
import { ShowToastEvent } from 'lightning/platformShowToastEvent';
import getDemos from '@salesforce/apex/DemoController.getDemos';

const DEMO_COLUMNS = [
    { label: 'Name', fieldName: 'name', type: 'text' },
    { label: 'Description', fieldName: 'description', type: 'text' }
];

export default class Demo_page extends LightningElement {

    demo_columns = DEMO_COLUMNS;

    demos;

    get existsDemos() {
        return this.demos && this.demos.data;
    }

    get demoData() {
        return this.existsDemos ? this.demos.data : null;
    }

    @wire(getDemos)
    getDemos(value) {
        this.demos = value;
        const { data, error } = value;
        if (error) {
            this.dispatchEvent(
                new ShowToastEvent({
                    title: 'Error loading Demo',
                    message: 'fail to fetch records of demo',
                    variant: 'error'
                })
            );
        } else if (data) {
            // nop
        }
    }

    handleReflesh() {
        refreshApex(this.demos);
    }
}