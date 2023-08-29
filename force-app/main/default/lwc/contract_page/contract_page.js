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
    return this.existsContracts ? [this.contracts.data] : [];
  }

  get noContract() {
    return this.contractsData.length === 0;
  }

  @wire(getContract)
  getContract(value) {
    this.contracts = value;
    const { data, error } = value;
    if (error) {
      if (
        error.body &&
        error.body.exceptionType === "demo_aho.UnauthorizedException"
      ) {
        this.dispatchErrorToast(
          "failed to retrieve the contract due to an authorization error"
        );
      } else {
        this.dispatchErrorToast();
      }
    } else if (data) {
      // nop
    }
  }

  // Helper
  dispatchErrorToast(
    message = "failed to retrieve contract due to unexpected error"
  ) {
    this.dispatchEvent(
      new ShowToastEvent({
        title: "Error loading Contract",
        message: message,
        variant: "error"
      })
    );
  }
}
