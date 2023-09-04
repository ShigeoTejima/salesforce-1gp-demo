# Setting page - show mode
tags: assignPermissionSetOfSetting

## setting page - show mode by initial when api-key does set already
tags: correctApiKy

* try login.
* setting page - open page.
* setting page - filed 'api-key' is "correct-api-key".
* setting page - filed 'api-key' is disable.
* setting page - button 'Test connect' is enable.
* setting page - button 'Edit' is enable.

## setting page - change to edit mode
tags: correctApiKy

* try login.
* setting page - open page.
* setting page - filed 'api-key' is "correct-api-key".
* setting page - filed 'api-key' is disable.
* setting page - button 'Test connect' is enable.
* setting page - button 'Edit' is enable.
* setting page - button 'Edit' click.
* setting page - filed 'api-key' is "correct-api-key".
* setting page - filed 'api-key' is enable.
* setting page - button 'Test connect' is enable.
* setting page - button 'Edit' is not visible.
* setting page - button 'Cancel' is enable.
* setting page - button 'Save' is enable.

## setting page - test connect is success
tags: correctApiKy

* demo-api - setup mappings by "fixtures/cdc/demo-api/contract" .
* try login.
* setting page - open page.
* setting page - filed 'api-key' is "correct-api-key".
* setting page - filed 'api-key' is disable.
* setting page - button 'Test connect' is enable.
* setting page - button 'Edit' is enable.
* setting page - button 'Test connect' click.
* setting page - toast displayed. variant: "success", title: "Test connect", message: "success"

## setting page - test connect is failure
tags: wrongApiKy

* demo-api - setup mappings by "fixtures/cdc/demo-api/contract" .
* try login.
* setting page - open page.
* setting page - filed 'api-key' is "wrong-api-key".
* setting page - filed 'api-key' is disable.
* setting page - button 'Test connect' is enable.
* setting page - button 'Edit' is enable.
* setting page - button 'Test connect' click.
* setting page - toast displayed. variant: "warning", title: "Test connect", message: "failure"
