import { createElement } from "lwc";
import { ShowToastEventName } from "lightning/platformShowToastEvent";
import Setting_page from "c/setting_page";
import testConnect from "@salesforce/apex/SettingController.testConnect";
import saveApiKey from "@salesforce/apex/SettingController.saveApiKey";
import getApiKey from "@salesforce/apex/SettingController.getApiKey";

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
jest.mock(
  "@salesforce/apex/SettingController.getApiKey",
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

  describe("display page", () => {
    it("api-key does not set yet", async () => {
      // given:
      //   - because call in connect Callback
      const mockGetApiKey = { value: null };
      getApiKey.mockResolvedValue(mockGetApiKey);

      const element = createElement("c-setting-page", {
        is: Setting_page
      });
      document.body.appendChild(element);

      // when:
      await flushPromises();

      // then:
      const apiKeyElement = element.shadowRoot.querySelector(".api-key");
      expect(apiKeyElement.label).toBe("Enter api-key:");
      expect(apiKeyElement.placeholder).toBe("type your api-key...");
      expect(apiKeyElement.value).toBe("");
      expect(apiKeyElement.disabled).toBeFalsy();
      expect(apiKeyElement.required).toBeTruthy();

      const testConnectElement =
        element.shadowRoot.querySelector(".test-connect");
      expect(testConnectElement.disabled).toBeTruthy();

      const editElement = element.shadowRoot.querySelector(".edit");
      expect(editElement).toBeNull();

      const editCancelElement =
        element.shadowRoot.querySelector(".edit-cancel");
      expect(editCancelElement).toBeNull();

      const saveConnectElement = element.shadowRoot.querySelector(".save");
      expect(saveConnectElement.disabled).toBeTruthy();
    });

    it("api-key does set already", async () => {
      // given:
      //   - because call in connect Callback
      const mockGetApiKey = { value: "api-key-for-test" };
      getApiKey.mockResolvedValue(mockGetApiKey);

      const element = createElement("c-setting-page", {
        is: Setting_page
      });
      document.body.appendChild(element);

      // when:
      await flushPromises();

      // then:
      const apiKeyElement = element.shadowRoot.querySelector(".api-key");
      expect(apiKeyElement.label).toBe("Current api-key:");
      expect(apiKeyElement.placeholder).toBe("");
      expect(apiKeyElement.value).toBe("api-key-for-test");
      expect(apiKeyElement.disabled).toBeTruthy();
      expect(apiKeyElement.required).toBeFalsy();

      const testConnectElement =
        element.shadowRoot.querySelector(".test-connect");
      expect(testConnectElement.disabled).toBeFalsy();

      const editElement = element.shadowRoot.querySelector(".edit");
      expect(editElement).not.toBeNull();
      expect(editElement.disabled).toBeFalsy();

      const editCancelElement =
        element.shadowRoot.querySelector(".edit-cancel");
      expect(editCancelElement).toBeNull();

      const saveElement = element.shadowRoot.querySelector(".save");
      expect(saveElement).toBeNull();
    });
  });

  describe("show mode", () => {
    describe("test connect", () => {
      it("success", async () => {
        // given:
        //   - because call in connect Callback
        const mockGetApiKey = { value: "api-key-for-test" };
        getApiKey.mockResolvedValue(mockGetApiKey);

        const element = createElement("c-setting-page", {
          is: Setting_page
        });
        document.body.appendChild(element);

        // when:
        await flushPromises();

        // step 1:
        // step 1 - then:
        const apiKeyElement = element.shadowRoot.querySelector(".api-key");
        expect(apiKeyElement.value).toBe("api-key-for-test");

        const testConnectElement =
          element.shadowRoot.querySelector(".test-connect");
        expect(testConnectElement.disabled).toBeFalsy();

        // step 2
        // step 2 - given:

        // Mock handler for toast event
        const toastHandler = jest.fn();
        element.addEventListener(ShowToastEventName, toastHandler);

        const mockTestConnect = { result: "SUCCESS" };
        testConnect.mockResolvedValue(mockTestConnect);

        // step 2 - when:
        testConnectElement.click();

        await flushPromises();

        // step 2 - then:
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
        // given:
        //   - because call in connect Callback
        const mockGetApiKey = { value: "api-key-for-test" };
        getApiKey.mockResolvedValue(mockGetApiKey);

        const element = createElement("c-setting-page", {
          is: Setting_page
        });
        document.body.appendChild(element);

        // when:
        await flushPromises();

        // step 1:
        // step 1 - then:
        const apiKeyElement = element.shadowRoot.querySelector(".api-key");
        expect(apiKeyElement.value).toBe("api-key-for-test");

        const testConnectElement =
          element.shadowRoot.querySelector(".test-connect");
        expect(testConnectElement.disabled).toBeFalsy();

        // step 2
        // step 2 - given:

        // Mock handler for toast event
        const toastHandler = jest.fn();
        element.addEventListener(ShowToastEventName, toastHandler);

        const mockTestConnect = { result: "FAILURE" };
        testConnect.mockResolvedValue(mockTestConnect);

        // step 2 - when:
        testConnectElement.click();

        await flushPromises();

        // step 2 - then:
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

      it("unexpected result", async () => {
        // given:
        //   - because call in connect Callback
        const mockGetApiKey = { value: "api-key-for-test" };
        getApiKey.mockResolvedValue(mockGetApiKey);

        const element = createElement("c-setting-page", {
          is: Setting_page
        });
        document.body.appendChild(element);

        // when:
        await flushPromises();

        // step 1:
        // step 1 - then:
        const apiKeyElement = element.shadowRoot.querySelector(".api-key");
        expect(apiKeyElement.value).toBe("api-key-for-test");

        const testConnectElement =
          element.shadowRoot.querySelector(".test-connect");
        expect(testConnectElement.disabled).toBeFalsy();

        // step 2
        // step 2 - given:

        // Mock handler for toast event
        const toastHandler = jest.fn();
        element.addEventListener(ShowToastEventName, toastHandler);

        const mockTestConnect = { result: "UNEXPECTED" };
        testConnect.mockResolvedValue(mockTestConnect);

        // step 2 - when:
        testConnectElement.click();

        await flushPromises();

        // step 2 - then:
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

      it("unexpected error occured", async () => {
        // given:
        //   - because call in connect Callback
        const mockGetApiKey = { value: "api-key-for-test" };
        getApiKey.mockResolvedValue(mockGetApiKey);

        const element = createElement("c-setting-page", {
          is: Setting_page
        });
        document.body.appendChild(element);

        // when:
        await flushPromises();

        // step 1:
        // step 1 - then:
        const apiKeyElement = element.shadowRoot.querySelector(".api-key");
        expect(apiKeyElement.value).toBe("api-key-for-test");

        const testConnectElement =
          element.shadowRoot.querySelector(".test-connect");
        expect(testConnectElement.disabled).toBeFalsy();

        // step 2
        // step 2 - given:

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

        // step 2 - when:
        testConnectElement.click();

        await flushPromises();

        // step 2 - then:
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

    describe("edit", () => {
      it("change to edit mode", async () => {
        // given:
        //   - because call in connect Callback
        const mockGetApiKey = { value: "api-key-for-test" };
        getApiKey.mockResolvedValue(mockGetApiKey);

        const element = createElement("c-setting-page", {
          is: Setting_page
        });
        document.body.appendChild(element);

        // when:
        await flushPromises();

        // then:
        const editElement = element.shadowRoot.querySelector(".edit");
        expect(editElement).not.toBeNull();
        expect(editElement.disabled).toBeFalsy();

        // Step 1
        // Step 1 - when:
        editElement.click();

        await flushPromises();

        // Step 1 - then
        const apiKeyElement = element.shadowRoot.querySelector(".api-key");
        expect(apiKeyElement.label).toBe("Enter api-key:");
        expect(apiKeyElement.placeholder).toBe("type your api-key...");
        expect(apiKeyElement.value).toBe("api-key-for-test");
        expect(apiKeyElement.disabled).toBeFalsy();
        expect(apiKeyElement.required).toBeTruthy();

        const testConnectElement =
          element.shadowRoot.querySelector(".test-connect");
        expect(testConnectElement.disabled).toBeFalsy();

        const editElementAfterClick = element.shadowRoot.querySelector(".edit");
        expect(editElementAfterClick).toBeNull();

        const editCancelElement =
          element.shadowRoot.querySelector(".edit-cancel");
        expect(editCancelElement.disabled).toBeFalsy();

        const saveConnectElement = element.shadowRoot.querySelector(".save");
        expect(saveConnectElement.disabled).toBeFalsy();
      });
    });
  });

  describe("edit mode", () => {
    describe("test connect", () => {
      it("success", async () => {
        // given:
        //   - because call in connect Callback
        const mockGetApiKey = { value: "api-key-for-test" };
        getApiKey.mockResolvedValue(mockGetApiKey);

        const element = createElement("c-setting-page", {
          is: Setting_page
        });
        document.body.appendChild(element);

        // when:
        await flushPromises();

        // step 1:
        // step 1 - then:
        const apiKeyElement = element.shadowRoot.querySelector(".api-key");
        expect(apiKeyElement.value).toBe("api-key-for-test");

        const testConnectElement =
          element.shadowRoot.querySelector(".test-connect");
        expect(testConnectElement.disabled).toBeFalsy();

        // step 2
        // step 2 - when:
        element.shadowRoot.querySelector(".edit").click();

        await flushPromises();

        // step 2 - then:
        expect(testConnectElement.disabled).toBeFalsy();

        // step 3
        // step 3 - given
        apiKeyElement.value = "api-key-for-test-changed";
        apiKeyElement.dispatchEvent(new CustomEvent("change"));

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
          apiKey: "api-key-for-test-changed"
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
        // given:
        //   - because call in connect Callback
        const mockGetApiKey = { value: "api-key-for-test" };
        getApiKey.mockResolvedValue(mockGetApiKey);

        const element = createElement("c-setting-page", {
          is: Setting_page
        });
        document.body.appendChild(element);

        // when:
        await flushPromises();

        // step 1:
        // step 1 - then:
        const apiKeyElement = element.shadowRoot.querySelector(".api-key");
        expect(apiKeyElement.value).toBe("api-key-for-test");

        const testConnectElement =
          element.shadowRoot.querySelector(".test-connect");
        expect(testConnectElement.disabled).toBeFalsy();

        // step 2
        // step 2 - when:
        element.shadowRoot.querySelector(".edit").click();

        await flushPromises();

        // step 2 - then:
        expect(testConnectElement.disabled).toBeFalsy();

        // step 3
        // step 3 - given
        apiKeyElement.value = "api-key-for-test-changed";
        apiKeyElement.dispatchEvent(new CustomEvent("change"));

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
          apiKey: "api-key-for-test-changed"
        });

        expect(toastHandler).toHaveBeenCalled();
        expect(toastHandler).toHaveBeenCalledTimes(1);
        expect(toastHandler.mock.calls[0][0].detail).toEqual({
          title: "Test connect",
          message: "failure",
          variant: "warning"
        });
      });

      it("unexpected result", async () => {
        // given:
        //   - because call in connect Callback
        const mockGetApiKey = { value: "api-key-for-test" };
        getApiKey.mockResolvedValue(mockGetApiKey);

        const element = createElement("c-setting-page", {
          is: Setting_page
        });
        document.body.appendChild(element);

        // when:
        await flushPromises();

        // step 1:
        // step 1 - then:
        const apiKeyElement = element.shadowRoot.querySelector(".api-key");
        expect(apiKeyElement.value).toBe("api-key-for-test");

        const testConnectElement =
          element.shadowRoot.querySelector(".test-connect");
        expect(testConnectElement.disabled).toBeFalsy();

        // step 2
        // step 2 - when:
        element.shadowRoot.querySelector(".edit").click();

        await flushPromises();

        // step 2 - then:
        expect(testConnectElement.disabled).toBeFalsy();

        // step 3
        // step 3 - given
        apiKeyElement.value = "api-key-for-test-changed";
        apiKeyElement.dispatchEvent(new CustomEvent("change"));

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
          apiKey: "api-key-for-test-changed"
        });

        expect(toastHandler).toHaveBeenCalled();
        expect(toastHandler).toHaveBeenCalledTimes(1);
        expect(toastHandler.mock.calls[0][0].detail).toEqual({
          title: "Test connect",
          message: "unexpected error occured",
          variant: "error"
        });
      });

      it("unexpected error occured", async () => {
        // given:
        //   - because call in connect Callback
        const mockGetApiKey = { value: "api-key-for-test" };
        getApiKey.mockResolvedValue(mockGetApiKey);

        const element = createElement("c-setting-page", {
          is: Setting_page
        });
        document.body.appendChild(element);

        // when:
        await flushPromises();

        // step 1:
        // step 1 - then:
        const apiKeyElement = element.shadowRoot.querySelector(".api-key");
        expect(apiKeyElement.value).toBe("api-key-for-test");

        const testConnectElement =
          element.shadowRoot.querySelector(".test-connect");
        expect(testConnectElement.disabled).toBeFalsy();

        // step 2
        // step 2 - when:
        element.shadowRoot.querySelector(".edit").click();

        await flushPromises();

        // step 2 - then:
        expect(testConnectElement.disabled).toBeFalsy();

        // step 3
        // step 3 - given
        apiKeyElement.value = "api-key-for-test-changed";
        apiKeyElement.dispatchEvent(new CustomEvent("change"));

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
          apiKey: "api-key-for-test-changed"
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
        // given:
        //   - because call in connect Callback
        const mockGetApiKey = { value: "api-key-for-test" };
        getApiKey.mockResolvedValue(mockGetApiKey);

        const element = createElement("c-setting-page", {
          is: Setting_page
        });
        document.body.appendChild(element);

        // when:
        await flushPromises();

        // step 1:
        // step 1 - then:
        const apiKeyElement = element.shadowRoot.querySelector(".api-key");
        expect(apiKeyElement.value).toBe("api-key-for-test");

        // step 2
        // step 2 - when:
        element.shadowRoot.querySelector(".edit").click();

        await flushPromises();

        // step 2 - then:
        const saveElement = element.shadowRoot.querySelector(".save");
        expect(saveElement.disabled).toBeFalsy();

        // step 3
        // step 3 - given
        apiKeyElement.value = "api-key-for-test-changed";
        apiKeyElement.dispatchEvent(new CustomEvent("change"));

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
          apiKey: "api-key-for-test-changed"
        });

        expect(toastHandler).toHaveBeenCalled();
        expect(toastHandler).toHaveBeenCalledTimes(1);
        expect(toastHandler.mock.calls[0][0].detail).toEqual({
          title: "Save",
          message: "success",
          variant: "success"
        });

        const apiKeyElementAfterSave =
          element.shadowRoot.querySelector(".api-key");
        expect(apiKeyElementAfterSave.label).toBe("Current api-key:");
        expect(apiKeyElementAfterSave.placeholder).toBe("");
        expect(apiKeyElementAfterSave.value).toBe("api-key-for-test-changed");
        expect(apiKeyElementAfterSave.disabled).toBeTruthy();
        expect(apiKeyElementAfterSave.required).toBeFalsy();

        const testConnectElement =
          element.shadowRoot.querySelector(".test-connect");
        expect(testConnectElement.disabled).toBeFalsy();

        const editElement = element.shadowRoot.querySelector(".edit");
        expect(editElement).not.toBeNull();
        expect(editElement.disabled).toBeFalsy();

        const editCancelElement =
          element.shadowRoot.querySelector(".edit-cancel");
        expect(editCancelElement).toBeNull();

        const saveElementAfterSave = element.shadowRoot.querySelector(".save");
        expect(saveElementAfterSave).toBeNull();
      });

      it("failure", async () => {
        // given:
        //   - because call in connect Callback
        const mockGetApiKey = { value: "api-key-for-test" };
        getApiKey.mockResolvedValue(mockGetApiKey);

        const element = createElement("c-setting-page", {
          is: Setting_page
        });
        document.body.appendChild(element);

        // when:
        await flushPromises();

        // step 1:
        // step 1 - then:
        const apiKeyElement = element.shadowRoot.querySelector(".api-key");
        expect(apiKeyElement.value).toBe("api-key-for-test");

        // step 2
        // step 2 - when:
        element.shadowRoot.querySelector(".edit").click();

        await flushPromises();

        // step 2 - then:
        const saveElement = element.shadowRoot.querySelector(".save");
        expect(saveElement.disabled).toBeFalsy();

        // step 3
        // step 3 - given
        apiKeyElement.value = "api-key-for-test-changed";
        apiKeyElement.dispatchEvent(new CustomEvent("change"));

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
          apiKey: "api-key-for-test-changed"
        });

        expect(toastHandler).toHaveBeenCalled();
        expect(toastHandler).toHaveBeenCalledTimes(1);
        expect(toastHandler.mock.calls[0][0].detail).toEqual({
          title: "Save",
          message: "failure",
          variant: "error"
        });
      });

      it("unexpected result", async () => {
        // given:
        //   - because call in connect Callback
        const mockGetApiKey = { value: "api-key-for-test" };
        getApiKey.mockResolvedValue(mockGetApiKey);

        const element = createElement("c-setting-page", {
          is: Setting_page
        });
        document.body.appendChild(element);

        // when:
        await flushPromises();

        // step 1:
        // step 1 - then:
        const apiKeyElement = element.shadowRoot.querySelector(".api-key");
        expect(apiKeyElement.value).toBe("api-key-for-test");

        // step 2
        // step 2 - when:
        element.shadowRoot.querySelector(".edit").click();

        await flushPromises();

        // step 2 - then:
        const saveElement = element.shadowRoot.querySelector(".save");
        expect(saveElement.disabled).toBeFalsy();

        // step 3
        // step 3 - given
        apiKeyElement.value = "api-key-for-test-changed";
        apiKeyElement.dispatchEvent(new CustomEvent("change"));

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
          apiKey: "api-key-for-test-changed"
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
        // given:
        //   - because call in connect Callback
        const mockGetApiKey = { value: "api-key-for-test" };
        getApiKey.mockResolvedValue(mockGetApiKey);

        const element = createElement("c-setting-page", {
          is: Setting_page
        });
        document.body.appendChild(element);

        // when:
        await flushPromises();

        // step 1:
        // step 1 - then:
        const apiKeyElement = element.shadowRoot.querySelector(".api-key");
        expect(apiKeyElement.value).toBe("api-key-for-test");

        // step 2
        // step 2 - when:
        element.shadowRoot.querySelector(".edit").click();

        await flushPromises();

        // step 2 - then:
        const saveElement = element.shadowRoot.querySelector(".save");
        expect(saveElement.disabled).toBeFalsy();

        // step 3
        // step 3 - given
        apiKeyElement.value = "api-key-for-test-changed";
        apiKeyElement.dispatchEvent(new CustomEvent("change"));

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
          apiKey: "api-key-for-test-changed"
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

    describe("edit cancel", () => {
      it("api-key value back to original", async () => {
        // given:
        //   - because call in connect Callback
        const mockGetApiKey = { value: "api-key-for-test" };
        getApiKey.mockResolvedValue(mockGetApiKey);

        const element = createElement("c-setting-page", {
          is: Setting_page
        });
        document.body.appendChild(element);

        // when:
        await flushPromises();

        // step 1:
        // step 1 - then:
        const apiKeyElement = element.shadowRoot.querySelector(".api-key");
        expect(apiKeyElement.value).toBe("api-key-for-test");

        // step 2
        // step 2 - when:
        element.shadowRoot.querySelector(".edit").click();

        await flushPromises();

        // step 2 - then:
        const editCancelElement =
          element.shadowRoot.querySelector(".edit-cancel");
        expect(editCancelElement.disabled).toBeFalsy();

        // step 3
        // step 3 - given:
        apiKeyElement.value = "api-key-for-test-changed";
        apiKeyElement.dispatchEvent(new CustomEvent("change"));

        await flushPromises();

        // step 4
        // step 4 - when:
        editCancelElement.click();

        await flushPromises();

        // step 4 - then:
        expect(apiKeyElement.value).toBe("api-key-for-test");
      });
    });
  });
});
