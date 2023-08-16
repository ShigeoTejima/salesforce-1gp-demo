# Salesforce E2E demo

## Required

- gauge
  - java plugin

## .env

See .env-example for what to define.

### for default, `.env-default`

Default definitions should be defined in `.env-default`.

### for local, `.env-local`

To run in a local environment,
create `.env-local` and define your settings there.

#### How to generate `.env-local`

- `${TARGET_ORG}` is org to test.
```shell
./script/generate-env.sh ${TARGET_ORG} > .env-local
```