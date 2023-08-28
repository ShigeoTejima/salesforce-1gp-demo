import { createElement } from "lwc";
import { ShowToastEventName } from "lightning/platformShowToastEvent";
import Contract_page from "c/contract_page";
import getContract from "@salesforce/apex/ContractController.getContract";

jest.mock(
  "@salesforce/apex/ContractController.getContract",
  () => {
    const { createApexTestWireAdapter } = require("@salesforce/sfdx-lwc-jest");
    return {
      default: createApexTestWireAdapter(jest.fn())
    };
  },
  { virtual: true }
);

describe("c-contract-page", () => {
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

  describe("show contract", () => {
    it("when data exists", async () => {
      const element = createElement("c-contract-page", {
        is: Contract_page
      });
      document.body.appendChild(element);

      const mockGetContract = { id: 1, name: "Foo" };
      getContract.emit(mockGetContract);

      await flushPromises();

      const datatableElement = element.shadowRoot.querySelector(
        "lightning-datatable"
      );

      expect(datatableElement.data.length).toBe(1);
      expect(datatableElement.data).toEqual([{ id: 1, name: "Foo" }]);
    });

    it("when data is empty", async () => {
      const element = createElement("c-contract-page", {
        is: Contract_page
      });
      document.body.appendChild(element);

      getContract.emit(null);

      await flushPromises();

      const datatableElement = element.shadowRoot.querySelector(
        "lightning-datatable"
      );

      expect(datatableElement).toBe(null);

      // TODO
      throw new Error("not yet impl. display message for no contracts.");
    });
    it("when returned unauthorizded error to get data", async () => {
      // TODO
      throw new Error("not yet impl. display message for no contracts.");
    });

    it("when returned error to get data", async () => {
      const element = createElement("c-contract-page", {
        is: Contract_page
      });
      document.body.appendChild(element);

      // Mock handler for toast event
      const toastHandler = jest.fn();
      element.addEventListener(ShowToastEventName, toastHandler);

      getContract.error();

      await flushPromises();

      const datatableElement = element.shadowRoot.querySelector(
        "lightning-datatable"
      );

      expect(datatableElement).toBe(null);

      expect(toastHandler).toHaveBeenCalled();
      expect(toastHandler).toHaveBeenCalledTimes(1);
    });
  });
});
