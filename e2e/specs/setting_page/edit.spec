# Setting page - edit mode
tags: assignPermissionSetOfSetting

## setting page - edit mode by initial when api-key does not set
tags: removeApiKy

* try login.
* setting page - open page.
* setting page - filed 'api-key' is empty.
* setting page - filed 'api-key' is enable.
* setting page - button 'Test connect' is disable.
* setting page - button 'Save' is disable.

## setting page - test connect is success
tags: removeApiKy

* demo-api - setup mappings by "fixtures/cdc/demo-api/contract" .
* try login.
* setting page - open page.
* setting page - filed 'api-key' is empty.
* setting page - filed 'api-key' is enable.
* setting page - button 'Test connect' is disable.
* setting page - button 'Save' is disable.
* setting page - field 'api-key' set "correct-api-key".
* setting page - button 'Test connect' is enable.
* setting page - button 'Test connect' click.
* setting page - toast displayed. variant: "success", title: "Test connect", message: "success"

## setting page - test connect is failure
tags: removeApiKy

* demo-api - setup mappings by "fixtures/cdc/demo-api/contract" .
* try login.
* setting page - open page.
* setting page - filed 'api-key' is empty.
* setting page - filed 'api-key' is enable.
* setting page - button 'Test connect' is disable.
* setting page - button 'Save' is disable.
* setting page - field 'api-key' set "wrong-api-key".
* setting page - button 'Test connect' is enable.
* setting page - button 'Test connect' click.
* setting page - toast displayed. variant: "warning", title: "Test connect", message: "failure"
