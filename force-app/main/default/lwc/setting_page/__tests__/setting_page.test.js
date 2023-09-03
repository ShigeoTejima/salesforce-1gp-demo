import { createElement } from "lwc";
import { ShowToastEventName } from "lightning/platformShowToastEvent";
import Setting_page from "c/setting_page";
import testConnect from "@salesforce/apex/SettingController.testConnect";
import saveApiKey from "@salesforce/apex/SettingController.saveApiKey";

jest.mock(
  "@salesforce/apex/SettingController.testConnect",
  () => ({
    default: jest.fn()
  }),
  { virtual: true }
);
jest.mock(
  "@salesforce/apex/SettingController.saveApiKey",
  () => ({
    default: jest.fn()
  }),
  { virtual: true }
);

describe("c-setting-page", () => {
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

  describe("test connect", () => {
    it("success", async () => {
      const element = createElement("c-setting-page", {
        is: Setting_page
      });
      document.body.appendChild(element);

      // step 1:
      // step 1 - when:
      const testConnectElement =
        element.shadowRoot.querySelector(".test-connect");

      // step 1 - then:
      expect(testConnectElement.disabled).toBeTruthy();

      // step 2:
      // step 2 - given:
      const apiKeyElement = element.shadowRoot.querySelector(".api-key");
      apiKeyElement.value = "api-key-for-test";

      // step 2 - when:
      apiKeyElement.dispatchEvent(new CustomEvent("change"));

      await flushPromises();

      // step 2 - then:
      expect(testConnectElement.disabled).toBeFalsy();

      // step 3
      // step 3 - given:

      // Mock handler for toast event
      const toastHandler = jest.fn();
      element.addEventListener(ShowToastEventName, toastHandler);

      const mockTestConnect = { result: "SUCCESS" };
      testConnect.mockResolvedValue(mockTestConnect);

      // step 3 - when:
      testConnectElement.click();

      await flushPromises();

      // step 3 - then:
      expect(testConnect.mock.calls.length).toBe(1);
      expect(testConnect.mock.calls[0][0]).toEqual({
        apiKey: "api-key-for-test"
      });

      expect(toastHandler).toHaveBeenCalled();
      expect(toastHandler).toHaveBeenCalledTimes(1);
      expect(toastHandler.mock.calls[0][0].detail).toEqual({
        title: "Test connect",
        message: "success",
        variant: "success"
      });
    });

    it("failure", async () => {
      const element = createElement("c-setting-page", {
        is: Setting_page
      });
      document.body.appendChild(element);

      // step 1:
      // step 1 - when:
      const testConnectElement =
        element.shadowRoot.querySelector(".test-connect");

      // step 1 - then:
      expect(testConnectElement.disabled).toBeTruthy();

      // step 2:
      // step 2 - given:
      const apiKeyElement = element.shadowRoot.querySelector(".api-key");
      apiKeyElement.value = "api-key-for-test";

      // step 2 - when:
      apiKeyElement.dispatchEvent(new CustomEvent("change"));

      await flushPromises();

      // step 2 - then:
      expect(testConnectElement.disabled).toBeFalsy();

      // step 3
      // step 3 - given:

      // Mock handler for toast event
      const toastHandler = jest.fn();
      element.addEventListener(ShowToastEventName, toastHandler);

      const mockTestConnect = { result: "FAILURE" };
      testConnect.mockResolvedValue(mockTestConnect);

      // step 3 - when:
      testConnectElement.click();

      await flushPromises();

      // step 3 - then:
      expect(testConnect.mock.calls.length).toBe(1);
      expect(testConnect.mock.calls[0][0]).toEqual({
        apiKey: "api-key-for-test"
      });

      expect(toastHandler).toHaveBeenCalled();
      expect(toastHandler).toHaveBeenCalledTimes(1);
      expect(toastHandler.mock.calls[0][0].detail).toEqual({
        title: "Test connect",
        message: "failure",
        variant: "warning"
      });
    });

    it("result is unexpected", async () => {
      const element = createElement("c-setting-page", {
        is: Setting_page
      });
      document.body.appendChild(element);

      // step 1:
      // step 1 - when:
      const testConnectElement =
        element.shadowRoot.querySelector(".test-connect");

      // step 1 - then:
      expect(testConnectElement.disabled).toBeTruthy();

      // step 2:
      // step 2 - given:
      const apiKeyElement = element.shadowRoot.querySelector(".api-key");
      apiKeyElement.value = "api-key-for-test";

      // step 2 - when:
      apiKeyElement.dispatchEvent(new CustomEvent("change"));

      await flushPromises();

      // step 2 - then:
      expect(testConnectElement.disabled).toBeFalsy();

      // step 3
      // step 3 - given:

      // Mock handler for toast event
      const toastHandler = jest.fn();
      element.addEventListener(ShowToastEventName, toastHandler);

      const mockTestConnect = { result: "UNEXPECTED" };
      testConnect.mockResolvedValue(mockTestConnect);

      // step 3 - when:
      testConnectElement.click();

      await flushPromises();

      // step 3 - then:
      expect(testConnect.mock.calls.length).toBe(1);
      expect(testConnect.mock.calls[0][0]).toEqual({
        apiKey: "api-key-for-test"
      });

      expect(toastHandler).toHaveBeenCalled();
      expect(toastHandler).toHaveBeenCalledTimes(1);
      expect(toastHandler.mock.calls[0][0].detail).toEqual({
        title: "Test connect",
        message: "unexpected error occured",
        variant: "error"
      });
    });

    it("unexpected error", async () => {
      const element = createElement("c-setting-page", {
        is: Setting_page
      });
      document.body.appendChild(element);

      // step 1:
      // step 1 - when:
      const testConnectElement =
        element.shadowRoot.querySelector(".test-connect");

      // step 1 - then:
      expect(testConnectElement.disabled).toBeTruthy();

      // step 2:
      // step 2 - given:
      const apiKeyElement = element.shadowRoot.querySelector(".api-key");
      apiKeyElement.value = "api-key-for-test";

      // step 2 - when:
      apiKeyElement.dispatchEvent(new CustomEvent("change"));

      await flushPromises();

      // step 2 - then:
      expect(testConnectElement.disabled).toBeFalsy();

      // step 3
      // step 3 - given:

      // Mock handler for toast event
      const toastHandler = jest.fn();
      element.addEventListener(ShowToastEventName, toastHandler);

      const mockTestConnect = {
        body: { message: "An internal server error has occurred" },
        ok: false,
        status: 400,
        statusText: "Bad Request"
      };
      testConnect.mockRejectedValue(mockTestConnect);

      // step 3 - when:
      testConnectElement.click();

      await flushPromises();

      // step 3 - then:
      expect(testConnect.mock.calls.length).toBe(1);
      expect(testConnect.mock.calls[0][0]).toEqual({
        apiKey: "api-key-for-test"
      });

      expect(toastHandler).toHaveBeenCalled();
      expect(toastHandler).toHaveBeenCalledTimes(1);
      expect(toastHandler.mock.calls[0][0].detail).toEqual({
        title: "Test connect",
        message: "unexpected error occured",
        variant: "error"
      });
    });
  });

  describe("save api-key", () => {
    it("success", async () => {
      const element = createElement("c-setting-page", {
        is: Setting_page
      });
      document.body.appendChild(element);

      // step 1:
      // step 1 - when:
      const saveElement = element.shadowRoot.querySelector(".save");

      // step 1 - then:
      expect(saveElement.disabled).toBeTruthy();

      // step 2:
      // step 2 - given:
      const apiKeyElement = element.shadowRoot.querySelector(".api-key");
      apiKeyElement.value = "api-key-for-test";

      // step 2 - when:
      apiKeyElement.dispatchEvent(new CustomEvent("change"));

      await flushPromises();

      // step 2 - then:
      expect(saveElement.disabled).toBeFalsy();

      // step 3
      // step 3 - given:

      // Mock handler for toast event
      const toastHandler = jest.fn();
      element.addEventListener(ShowToastEventName, toastHandler);

      const mockSaveApiKey = { result: "SUCCESS" };
      saveApiKey.mockResolvedValue(mockSaveApiKey);

      // step 3 - when:
      saveElement.click();

      await flushPromises();

      // step 3 - then:
      expect(saveApiKey.mock.calls.length).toBe(1);
      expect(saveApiKey.mock.calls[0][0]).toEqual({
        apiKey: "api-key-for-test"
      });

      expect(toastHandler).toHaveBeenCalled();
      expect(toastHandler).toHaveBeenCalledTimes(1);
      expect(toastHandler.mock.calls[0][0].detail).toEqual({
        title: "Save",
        message: "success",
        variant: "success"
      });
    });

    it("failure", async () => {
      const element = createElement("c-setting-page", {
        is: Setting_page
      });
      document.body.appendChild(element);

      // step 1:
      // step 1 - when:
      const saveElement = element.shadowRoot.querySelector(".save");

      // step 1 - then:
      expect(saveElement.disabled).toBeTruthy();

      // step 2:
      // step 2 - given:
      const apiKeyElement = element.shadowRoot.querySelector(".api-key");
      apiKeyElement.value = "api-key-for-test";

      // step 2 - when:
      apiKeyElement.dispatchEvent(new CustomEvent("change"));

      await flushPromises();

      // step 2 - then:
      expect(saveElement.disabled).toBeFalsy();

      // step 3
      // step 3 - given:

      // Mock handler for toast event
      const toastHandler = jest.fn();
      element.addEventListener(ShowToastEventName, toastHandler);

      const mockSaveApiKey = { result: "FAILURE" };
      saveApiKey.mockResolvedValue(mockSaveApiKey);

      // step 3 - when:
      saveElement.click();

      await flushPromises();

      // step 3 - then:
      expect(saveApiKey.mock.calls.length).toBe(1);
      expect(saveApiKey.mock.calls[0][0]).toEqual({
        apiKey: "api-key-for-test"
      });

      expect(toastHandler).toHaveBeenCalled();
      expect(toastHandler).toHaveBeenCalledTimes(1);
      expect(toastHandler.mock.calls[0][0].detail).toEqual({
        title: "Save",
        message: "failure",
        variant: "error"
      });
    });

    it("result is unexpected", async () => {
      const element = createElement("c-setting-page", {
        is: Setting_page
      });
      document.body.appendChild(element);

      // step 1:
      // step 1 - when:
      const saveElement = element.shadowRoot.querySelector(".save");

      // step 1 - then:
      expect(saveElement.disabled).toBeTruthy();

      // step 2:
      // step 2 - given:
      const apiKeyElement = element.shadowRoot.querySelector(".api-key");
      apiKeyElement.value = "api-key-for-test";

      // step 2 - when:
      apiKeyElement.dispatchEvent(new CustomEvent("change"));

      await flushPromises();

      // step 2 - then:
      expect(saveElement.disabled).toBeFalsy();

      // step 3
      // step 3 - given:

      // Mock handler for toast event
      const toastHandler = jest.fn();
      element.addEventListener(ShowToastEventName, toastHandler);

      const mockSaveApiKey = { result: "UNEXPECTED" };
      saveApiKey.mockResolvedValue(mockSaveApiKey);

      // step 3 - when:
      saveElement.click();

      await flushPromises();

      // step 3 - then:
      expect(saveApiKey.mock.calls.length).toBe(1);
      expect(saveApiKey.mock.calls[0][0]).toEqual({
        apiKey: "api-key-for-test"
      });

      expect(toastHandler).toHaveBeenCalled();
      expect(toastHandler).toHaveBeenCalledTimes(1);
      expect(toastHandler.mock.calls[0][0].detail).toEqual({
        title: "Save",
        message: "unexpected error occured",
        variant: "error"
      });
    });

    it("unexpected error occured", async () => {
      const element = createElement("c-setting-page", {
        is: Setting_page
      });
      document.body.appendChild(element);

      // step 1:
      // step 1 - when:
      const saveElement = element.shadowRoot.querySelector(".save");

      // step 1 - then:
      expect(saveElement.disabled).toBeTruthy();

      // step 2:
      // step 2 - given:
      const apiKeyElement = element.shadowRoot.querySelector(".api-key");
      apiKeyElement.value = "api-key-for-test";

      // step 2 - when:
      apiKeyElement.dispatchEvent(new CustomEvent("change"));

      await flushPromises();

      // step 2 - then:
      expect(saveElement.disabled).toBeFalsy();

      // step 3
      // step 3 - given:

      // Mock handler for toast event
      const toastHandler = jest.fn();
      element.addEventListener(ShowToastEventName, toastHandler);

      const mockSaveApiKey = {
        body: { message: "An internal server error has occurred" },
        ok: false,
        status: 400,
        statusText: "Bad Request"
      };
      saveApiKey.mockRejectedValue(mockSaveApiKey);

      // step 3 - when:
      saveElement.click();

      await flushPromises();

      // step 3 - then:
      expect(saveApiKey.mock.calls.length).toBe(1);
      expect(saveApiKey.mock.calls[0][0]).toEqual({
        apiKey: "api-key-for-test"
      });

      expect(toastHandler).toHaveBeenCalled();
      expect(toastHandler).toHaveBeenCalledTimes(1);
      expect(toastHandler.mock.calls[0][0].detail).toEqual({
        title: "Save",
        message: "unexpected error occured",
        variant: "error"
      });
    });
  });
});
