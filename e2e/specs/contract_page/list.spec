# contract page - list of contract
tags: assignPermissionSetOfContract

* salesforce-demo-api - setup mappings by "fixtures/cdc/salesforce-demo-api/contract" .

## List of contract is empty by unauthorized
tags: wrongApiKy

* try login.
* contract page - open page.
* contract page - list of contract is empty.

## List of contract has record
tags: correctApiKy

* try login.
* contract page - open page.
* contract page - list of contract has record for <table:fixtures/contract_page/has_record/expected/records.csv> .
