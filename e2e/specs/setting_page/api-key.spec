# Setting page - api-key
tags: assignPermissionSetOfSetting

NOTE: un-testable cases
(1) test connect is unexpected error that caused by unexpected error of salesforce platform
(2) save api-key is failure that caused by unexpected error of salesforce platform


## setting page - show mode by initial when api-key does set already
tags: correctApiKy

* try login.
* setting page - open page.
* setting page - field 'api-key' is "correct-api-key".
* setting page - field 'api-key' is disable.
* setting page - button 'Test connect' is enable.
* setting page - button 'Edit' is enable.
* setting page - button 'Cancel' is not visible.
* setting page - button 'Save' is not visible.

## setting page - change to edit mode, change api-key then cancel
tags: correctApiKy

* try login.
* setting page - open page.
show
* setting page - field 'api-key' is "correct-api-key".
* setting page - field 'api-key' is disable.
* setting page - button 'Test connect' is enable.
* setting page - button 'Edit' is enable.
* setting page - button 'Cancel' is not visible.
* setting page - button 'Save' is not visible.
edit
* setting page - button 'Edit' click.
* setting page - field 'api-key' is "correct-api-key".
* setting page - field 'api-key' is enable.
* setting page - button 'Test connect' is enable.
* setting page - button 'Edit' is not visible.
* setting page - button 'Cancel' is enable.
* setting page - button 'Save' is enable.
change api-key to empty
* setting page - field 'api-key' set "".
* setting page - button 'Test connect' is disable.
* setting page - button 'Cancel' is enable.
* setting page - button 'Save' is disable.
change api-key
* setting page - field 'api-key' set "changed-api-key".
* setting page - button 'Test connect' is enable.
* setting page - button 'Save' is enable.
cancel
* setting page - button 'Cancel' click.
show back
* setting page - field 'api-key' is "correct-api-key".
* setting page - field 'api-key' is disable.
* setting page - button 'Test connect' is enable.
* setting page - button 'Edit' is enable.
* setting page - button 'Cancel' is not visible.
* setting page - button 'Save' is not visible.

## setting page - test connect is success in show mode
tags: correctApiKy

* demo-api - setup mappings by "fixtures/cdc/demo-api/contract" .
* try login.
* setting page - open page.
* setting page - field 'api-key' is "correct-api-key".
* setting page - field 'api-key' is disable.
* setting page - button 'Test connect' is enable.
* setting page - button 'Test connect' click.
* setting page - toast displayed. variant: "success", title: "Test connect", message: "success"

## setting page - test connect is failure in show mode
tags: wrongApiKy

* demo-api - setup mappings by "fixtures/cdc/demo-api/contract" .
* try login.
* setting page - open page.
* setting page - field 'api-key' is "wrong-api-key".
* setting page - field 'api-key' is disable.
* setting page - button 'Test connect' is enable.
* setting page - button 'Test connect' click.
* setting page - toast displayed. variant: "warning", title: "Test connect", message: "failure"

## setting page - edit mode by initial when api-key does not set
tags: removeApiKy

* try login.
* setting page - open page.
* setting page - field 'api-key' is empty.
* setting page - field 'api-key' is enable.
* setting page - button 'Test connect' is disable.
* setting page - button 'Edit' is not visible.
* setting page - button 'Cancel' is not visible.
* setting page - button 'Save' is disable.

## setting page - test connect is success in edit mode
tags: removeApiKy

* demo-api - setup mappings by "fixtures/cdc/demo-api/contract" .
* try login.
* setting page - open page.
* setting page - field 'api-key' is empty.
* setting page - field 'api-key' is enable.
* setting page - button 'Test connect' is disable.
* setting page - field 'api-key' set "correct-api-key".
* setting page - button 'Test connect' is enable.
* setting page - button 'Test connect' click.
* setting page - toast displayed. variant: "success", title: "Test connect", message: "success"

## setting page - test connect is failure in edit mode
tags: removeApiKy

* demo-api - setup mappings by "fixtures/cdc/demo-api/contract" .
* try login.
* setting page - open page.
* setting page - field 'api-key' is empty.
* setting page - field 'api-key' is enable.
* setting page - button 'Test connect' is disable.
* setting page - field 'api-key' set "wrong-api-key".
* setting page - button 'Test connect' is enable.
* setting page - button 'Test connect' click.
* setting page - toast displayed. variant: "warning", title: "Test connect", message: "failure"

## setting page - save api-key is success in edit mode by initial
tags: removeApiKy

* try login.
* setting page - open page.
* setting page - field 'api-key' is empty.
* setting page - field 'api-key' is enable.
* setting page - button 'Save' is disable.
* setting page - field 'api-key' set "correct-api-key".
* setting page - button 'Save' is enable.
* setting page - button 'Save' click.
* setting page - toast displayed. variant: "success", title: "Save", message: "success"
* salesforce data - custom setting 'DemoApiSetting' api-key is "correct-api-key".

## setting page - save api-key is success in edit mode
tags: correctApiKy

* try login.
* setting page - open page.
* setting page - field 'api-key' is "correct-api-key".
* setting page - button 'Edit' is enable.
* setting page - button 'Edit' click.
* setting page - field 'api-key' is "correct-api-key".
* setting page - field 'api-key' is enable.
* setting page - button 'Save' is enable.
* setting page - field 'api-key' set "changed-api-key".
* setting page - button 'Save' is enable.
* setting page - button 'Save' click.
* setting page - toast displayed. variant: "success", title: "Save", message: "success"
* salesforce data - custom setting 'DemoApiSetting' api-key is "changed-api-key".
