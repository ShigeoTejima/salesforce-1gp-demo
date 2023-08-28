import { LightningElement, wire } from "lwc";
import { ShowToastEvent } from "lightning/platformShowToastEvent";
import getContract from "@salesforce/apex/ContractController.getContract";

export default class Contract_page extends LightningElement {
  contracts_columns = [
    { label: "Id", fieldName: "id", type: "text" },
    { label: "Name", fieldName: "name", type: "text" }
  ];

  contracts;

  get existsContracts() {
    return this.contracts && this.contracts.data;
  }

  get contractsData() {
    return this.existsContracts ? [this.contracts.data] : null;
  }

  @wire(getContract)
  getContract(value) {
    this.contracts = value;
    const { data, error } = value;
    if (error) {
      this.dispatchErrorToast();
    } else if (data) {
      // nop
    }
  }

  // Helper
  dispatchErrorToast() {
    this.dispatchEvent(
      new ShowToastEvent({
        title: "Error loading Demo",
        message: "fail to fetch records of contracts",
        variant: "error"
      })
    );
  }
}
