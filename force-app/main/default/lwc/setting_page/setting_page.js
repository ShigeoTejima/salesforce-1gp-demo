import { LightningElement } from "lwc";
import { ShowToastEvent } from "lightning/platformShowToastEvent";
import testConnect from "@salesforce/apex/SettingController.testConnect";
import saveApiKey from "@salesforce/apex/SettingController.saveApiKey";

export default class Setting_page extends LightningElement {
  apiKey;

  get apiKeyNotInputed() {
    return !this.apiKey || this.apiKey === "";
  }

  handleChangeApiKey(e) {
    this.apiKey = e.target.value;
  }

  handleTestConnect() {
    if (this.apiKeyNotInputed) {
      return;
    }

    testConnect({ apiKey: this.apiKey })
      .then((result) => {
        console.log(result);
        if (result.result === "SUCCESS") {
          this.dispatchToast({
            title: "Test connect",
            message: "success",
            variant: "success"
          });
        } else if (result.result === "FAILURE") {
          this.dispatchToast({
            title: "Test connect",
            message: "failure",
            variant: "warning"
          });
        } else {
          this.dispatchToast({
            title: "Test connect",
            message: "unexpected error occured",
            variant: "error"
          });
        }
      })
      .catch((error) => {
        console.error(error);
        this.dispatchToast({
          title: "Test connect",
          message: "unexpected error occured",
          variant: "error"
        });
      });
  }

  handleSave() {
    if (this.apiKeyNotInputed) {
      return;
    }

    saveApiKey({ apiKey: this.apiKey })
      .then((result) => {
        console.log(result);
        if (result.result === "SUCCESS") {
          this.dispatchToast({
            title: "Save",
            message: "success",
            variant: "success"
          });
        } else if (result.result === "FAILURE") {
          this.dispatchToast({
            title: "Save",
            message: "failure",
            variant: "error"
          });
        } else {
          this.dispatchToast({
            title: "Save",
            message: "unexpected error occured",
            variant: "error"
          });
        }
      })
      .catch((error) => {
        console.error(error);
        this.dispatchToast({
          title: "Save",
          message: "unexpected error occured",
          variant: "error"
        });
      });
  }

  // Helper
  dispatchToast(detail) {
    this.dispatchEvent(
      new ShowToastEvent({
        title: detail.title,
        message: detail.message,
        variant: detail.variant
      })
    );
  }
}
