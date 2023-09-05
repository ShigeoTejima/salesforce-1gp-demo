import { LightningElement } from "lwc";
import { ShowToastEvent } from "lightning/platformShowToastEvent";
import testConnect from "@salesforce/apex/SettingController.testConnect";
import saveApiKey from "@salesforce/apex/SettingController.saveApiKey";
import getApiKey from "@salesforce/apex/SettingController.getApiKey";

export default class Setting_page extends LightningElement {
  edit;
  apiKey;
  editApiKey;

  get apiKeyLabel() {
    if (this.edit) {
      return "Enter api-key:";
    }
    return "Current api-key:";
  }

  get apiKeyPlaceholder() {
    if (this.edit) {
      return "type your api-key...";
    }
    return "";
  }

  get notEdit() {
    return !this.edit;
  }

  get apiKeyNotInputed() {
    if (this.edit) {
      return !this.editApiKey || this.editApiKey === "";
    }
    return !this.apiKey || this.apiKey === "";
  }

  connectedCallback() {
    getApiKey()
      .then((result) => {
        console.log(result);
        this.apiKey = result.value ? result.value : "";
        this.edit = !this.apiKey || this.apiKey === "" ? true : false;
      })
      .catch((error) => {
        console.error(error);
      });
  }

  handleChangeApiKey(e) {
    if (this.edit) {
      this.editApiKey = e.target.value;
    }
  }

  handleTestConnect() {
    if (this.apiKeyNotInputed) {
      return;
    }

    testConnect({ apiKey: this.getTargetApiKey() })
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

  handleEdit() {
    this.edit = true;
    this.editApiKey = this.apiKey;
  }

  handleEditCancel() {
    this.editCancel();
  }

  handleSave() {
    if (this.apiKeyNotInputed) {
      this.editCancel();
      return;
    }

    saveApiKey({ apiKey: this.editApiKey })
      .then((result) => {
        console.log(result);
        if (result.result === "SUCCESS") {
          this.dispatchToast({
            title: "Save",
            message: "success",
            variant: "success"
          });

          this.edit = false;
          this.apiKey = this.editApiKey;
          this.editApiKey = null;
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

  editCancel() {
    this.edit = false;
    this.template.querySelector(".api-key").value = this.apiKey;
    this.editApiKey = null;
  }

  getTargetApiKey() {
    if (this.edit) {
      return this.editApiKey;
    }
    return this.apiKey;
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
