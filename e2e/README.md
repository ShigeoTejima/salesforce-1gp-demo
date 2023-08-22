# Salesforce E2E demo

## Required

- gauge
  - java plugin

- Allow users to log in from E2E
  - Manual pre-login
  - Set login IP address range to allow login

## Setup configuration

### .env

See .env-example for what to define.

#### for default, `.env-default`

Default definitions should be defined in `.env-default`.

#### for local default, `.env-local-default`

To run in a local environment,
If necessary, create `.env-local-default`.
If you want to override `.env-default` configuration values,
for example, by changing chromeOptions.

#### for local, `.env-local`

To run in a local environment,
create `.env-local` and define your settings there.

see `.env-example`.

##### How to generate `.env-local`

- `${ADMIN_USER_ORG}` is org to test for admin user.
  - `sf org list`
- `${STANDARD_USER_ORG}` is org to test for standard user.
  - one of the following
    - `sf org list users -o ${ADMIN_USER_ORG}`
    - `sf org list auth`
```shell
./script/generate-env.sh ${ADMIN_USER_ORG} ${STANDARD_USER_ORG} > .env-local
```

**NOTE:**

After some time, `test.admin-user.accessToken` will become invalid and the test will fail.
In that case, you can run `sf org display -o ${ADMIN_USER_ORG}` and replace it with the contents of `Access Token`.
