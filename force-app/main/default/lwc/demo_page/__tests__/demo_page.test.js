import { createElement } from 'lwc';
import { refreshApex } from '@salesforce/apex';
import { ShowToastEventName } from 'lightning/platformShowToastEvent';
import Demo_page from 'c/demo_page';
import getDemos from '@salesforce/apex/DemoController.getDemos';

jest.mock(
    '@salesforce/apex/DemoController.getDemos',
    () => {
        const {
            createApexTestWireAdapter
        } = require('@salesforce/sfdx-lwc-jest');
        return {
            default: createApexTestWireAdapter(jest.fn())
        };
    },
    { virtual: true }
);

// Mock refreshApex module
jest.mock(
    '@salesforce/apex',
    () => {
        return {
            refreshApex: jest.fn(() => Promise.resolve())
        };
    },
    { virtual: true }
);

describe('c-demo-page', () => {
    afterEach(() => {
        // The jsdom instance is shared across test cases in a single file so reset the DOM
        while (document.body.firstChild) {
            document.body.removeChild(document.body.firstChild);
        }

        jest.clearAllMocks();
    });

    // Helper function to wait until the microtask queue is empty.
    async function flushPromises() {
        return Promise.resolve();
    }

    describe('list of demos', () => {
        it('when data exists', async () => {
            const element = createElement('c-demo-page', {
                is: Demo_page
            });
    
            // Act
            document.body.appendChild(element);

            const mockGetDemos = [
                { "id": 1, "name": "Foo", "description": "Foo Description" },
                { "id": 2, "name": "Bar" }
            ];
            getDemos.emit(mockGetDemos);

            await flushPromises();

            const datatableElement = element.shadowRoot.querySelector('lightning-datatable');

            expect(datatableElement.data.length).toBe(2);
            expect(datatableElement.data).toEqual([
                { "id": 1, "name": "Foo", "description": "Foo Description" },
                { "id": 2, "name": "Bar" }
            ])
        });

        it('when data is empty', async () => {
            const element = createElement('c-demo-page', {
                is: Demo_page
            });
    
            // Act
            document.body.appendChild(element);

            getDemos.emit([]);

            await flushPromises();

            const datatableElement = element.shadowRoot.querySelector('lightning-datatable');

            expect(datatableElement.data.length).toBe(0);

        });

        it('when returned error to get data', async () => {
            const element = createElement('c-demo-page', {
                is: Demo_page
            });
            document.body.appendChild(element);

            // Mock handler for toast event
            const toastHandler = jest.fn();
            element.addEventListener(ShowToastEventName, toastHandler);

            getDemos.error();

            await flushPromises();

            const datatableElement = element.shadowRoot.querySelector('lightning-datatable');

            expect(datatableElement).toBe(null);

            expect(toastHandler).toHaveBeenCalled();
            expect(toastHandler).toHaveBeenCalledTimes(1);
        });
    });

    describe('refresh button', () => {
        it('refresh called', async () => {
            const element = createElement('c-demo-page', {
                is: Demo_page
            });
    
            // Act
            document.body.appendChild(element);

            getDemos.emit([]);

            // show initial page
            await flushPromises();

            const datatableElement = element.shadowRoot.querySelector('lightning-datatable');
            expect(datatableElement.data.length).toBe(0);

            // click reflesh button
            const refleshButtonElement = element.shadowRoot.querySelector('lightning-button.reflesh');
            refleshButtonElement.click();

            getDemos.emit([
                { "id": 1, "name": "Foo" }
            ]);

            await flushPromises();

            expect(refreshApex).toHaveBeenCalled();
            expect(refreshApex).toHaveBeenCalledTimes(1);

            const reflesheddatatableElement = element.shadowRoot.querySelector('lightning-datatable');
            expect(reflesheddatatableElement.data.length).toBe(1);

        });

        it('when returned error to get data', async () => {
            const element = createElement('c-demo-page', {
                is: Demo_page
            });
            document.body.appendChild(element);

            // Mock handler for toast event
            const toastHandler = jest.fn();
            element.addEventListener(ShowToastEventName, toastHandler);

            getDemos.emit([]);

            // show initial page
            await flushPromises();

            const datatableElement = element.shadowRoot.querySelector('lightning-datatable');
            expect(datatableElement.data.length).toBe(0);

            expect(toastHandler).toHaveBeenCalledTimes(0);

            // click reflesh button
            const refleshButtonElement = element.shadowRoot.querySelector('lightning-button.reflesh');
            refleshButtonElement.click();

            getDemos.error();

            await flushPromises();

            expect(refreshApex).toHaveBeenCalled();
            expect(refreshApex).toHaveBeenCalledTimes(1);

            const reflesheddatatableElement = element.shadowRoot.querySelector('lightning-datatable');
            expect(reflesheddatatableElement).toBe(null);

            /*
             * NOTE: 2023/7/30) refleshApexがエラーの場合に、ShowToastEventをdispatchしてなくても
             * このjestではなぜかtoastHandlerが呼ばれてるように見えた
             * 実際に画面で確認するとToastは表示されない
             */
            expect(toastHandler).toHaveBeenCalled();
            expect(toastHandler).toHaveBeenCalledTimes(1);
        });
    });
});