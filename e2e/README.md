# Salesforce E2E demo

## Required

- gauge
  - java plugin

## .env

See .env-example for what to define.

### for default, `.env-default`

Default definitions should be defined in `.env-default`.

### for local default, `.env-local-default`

To run in a local environment,
If necessary, create `.env-local-default`.
If you want to override `.env-default` configuration values,
for example, by changing chromeOptions.

### for local, `.env-local`

To run in a local environment,
create `.env-local` and define your settings there.

#### How to generate `.env-local`

- `${ADMIN_USER_ORG}` is org to test for admin user.
  - `sf org list`
- `${STANDARD_USER_ORG}` is org to test for standard user.
  - one of the following
    - `sf org list users -o ${ADMIN_USER_ORG}`
    - `sf org list auth`
```shell
./script/generate-env-org.sh ${ADMIN_USER_ORG} > .env-local
```
```shell
./script/generate-env-user.sh ${ADMIN_USER_ORG} test.admin-user >> .env-local
```
```shell
./script/generate-env-user.sh ${STANDARD_USER_ORG} test.standard-user >> .env-local
```
