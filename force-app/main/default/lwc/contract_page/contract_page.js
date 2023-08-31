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
      console.error(error);
      const errorMessage = this.errorMessageFromError(error);
      this.dispatchErrorToast(errorMessage);
    } else if (data) {
      // nop
    }
  }

  errorMessageFromError(error) {
    const exceptionType = error.body && error.body.exceptionType;
    switch (exceptionType) {
      case "demo_aho.UnauthorizedException":
        return "failed to retrieve the contract due to an authorization error";
      case "demo_aho.UnauthorizedException.NotSetApiKeyException":
        return "failed to retrieve the contract due to apikey is not set";
      default:
        return "failed to retrieve contract due to unexpected error";
    }
  }

  // Helper
  dispatchErrorToast(message) {
    this.dispatchEvent(
      new ShowToastEvent({
        title: "Error loading Contract",
        message: message,
        variant: "error"
      })
    );
  }
}
