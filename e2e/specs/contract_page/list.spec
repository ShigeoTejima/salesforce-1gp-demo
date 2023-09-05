# Contract page - list of contract
tags: assignPermissionSetOfContract

* demo-api - setup mappings by "fixtures/cdc/demo-api/contract" .

## List of contract is empty by unauthorized
tags: wrongApiKey

* try login.
* contract page - open page.
* contract page - list of contract is empty.
* salesforce ui - toast displayed. variant: "error", title: "Error loading Contract", message: "failed to retrieve the contract due to an authorization error"

## List of contract is empty by api-key does not set
tags: removeApiKey

* try login.
* contract page - open page.
* contract page - list of contract is empty.
* salesforce ui - toast displayed. variant: "error", title: "Error loading Contract", message: "failed to retrieve the contract due to apikey is not set"

## List of contract is empty by unexpected error
tags: unexpectedApiKey

* try login.
* contract page - open page.
* contract page - list of contract is empty.
* salesforce ui - toast displayed. variant: "error", title: "Error loading Contract", message: "failed to retrieve contract due to unexpected error"

## List of contract has record
tags: correctApiKey

* try login.
* contract page - open page.
* contract page - list of contract has record for <table:fixtures/contract_page/has_record/expected/records.csv> .
