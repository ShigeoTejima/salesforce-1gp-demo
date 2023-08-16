#!/bin/sh -e -o pipefail

function print_org_info() {
  local file_org_info="${DIR_TMP}/org_info.json"
  sf org display -o "${TARGET_ORG}" --json > "${file_org_info}"

  local instanceUrl=$(cat "${file_org_info}" | jq -r '.result.instanceUrl')
  echo "selenide.baseUrl=${instanceUrl}"
  echo "test.instanceUrl=${instanceUrl}"
}

function print_user_info() {
  local file_users_info="${DIR_TMP}/users_info.json"
  sf org list users -o "${TARGET_ORG}" --json > "${file_users_info}"

  local file_admin_user_info="${DIR_TMP}/admin_user_info.json"
  cat "${file_users_info}" | jq '.result[] | select(.profileName == "システム管理者")' > "${file_admin_user_info}"
  local userId=$(cat "${file_admin_user_info}" | jq -r '.userId')
  local username=$(cat "${file_admin_user_info}" | jq -r '.username')

  local file_admin_org_info="${DIR_TMP}/admin_org_info.json"
  sf org display -o "${username}" --json > "${file_admin_org_info}"
  local accessToken=$(cat "${file_admin_org_info}" | jq -r '.result.accessToken')
  local password=$(cat "${file_admin_org_info}" | jq -r '.result.password')

  echo "test.userId=${userId}"
  echo "test.username=${username}"
  echo "test.password=${password}"
  echo "test.accessToken=${accessToken}"

  print_user_name "${username}"
}

function print_user_name() {
  local username="$1"
  if [[ -z "${username}" ]]; then
    echo "function print_user_name: required username." 1>&2
    exit 1
  fi

  local file_org_info="${DIR_TMP}/org_info.json"
  sf org display -o "${TARGET_ORG}" --json > "${file_org_info}"

  local accessToken=$(cat "${file_org_info}" | jq -r '.result.accessToken')
  local apiVersion=$(cat "${file_org_info}" | jq -r '.result.apiVersion')
  local instanceUrl=$(cat "${file_org_info}" | jq -r '.result.instanceUrl')

  local file_user_info="${DIR_TMP}/user_info.json"
  curl "${instanceUrl}/services/data/v${apiVersion}/query?q=SELECT+Id%2C+Username%2C+Name%2C+FirstName%2C+LastName+FROM+User+WHERE+Username='${username}'" -H "Authorization: Bearer ${accessToken}" > "${file_user_info}"

  local fullname=$(cat "${file_user_info}" | jq -r '.records[] | select(.Username == "'${username}'").Name')

  echo "test.fullname=${fullname}"
}

function print_permissionSet_info() {
  local file_org_info="${DIR_TMP}/org_info.json"
  sf org display -o "${TARGET_ORG}" --json > "${file_org_info}"

  local apiVersion=$(cat "${file_org_info}" | jq -r '.result.apiVersion')
  local accessToken=$(cat "${file_org_info}" | jq -r '.result.accessToken')
  local instanceUrl=$(cat "${file_org_info}" | jq -r '.result.instanceUrl')

  local file_permissionSet_info="${DIR_TMP}/permissionSet_info.json"
  curl "${instanceUrl}/services/data/v${apiVersion}/query?q=SELECT+Id%2C+Name%2C+NamespacePrefix%2C+Label+FROM+PermissionSet+WHERE+Name='demo'" -H "Authorization: Bearer ${accessToken}" > "${file_permissionSet_info}"
  local permissionSet_demo_Id=$(cat "${file_permissionSet_info}" | jq -r '.records[] | select(.Name == "demo" and .NamespacePrefix == "demo_ahd").Id')

  echo "test.permissionSet.demo_ahd_demo.id=${permissionSet_demo_Id}"

}

function rm_dir_tmp() {
  [[ -d "${DIR_TMP}" ]] && rm -fr "${DIR_TMP}"
}

function main() {
  local dir_self=$(dirname $0)
  local dir_parent=$(cd ${dir_self}/.. && pwd)
  TARGET_ORG="$1"
  if [[ -z "${TARGET_ORG}" ]]; then
    echo "function main: 1st argument required. please specify TARGET_ORG." 1>&2
    exit 1
  fi

  DIR_TMP=$(mktemp -d ${dir_self}/tmp.XXXXXX) || exit 1
  trap rm_dir_tmp EXIT
  trap "trap - EXIT; rm_dir_tmp; exit -1" INT PIPE TERM

  print_org_info
  print_user_info
  print_permissionSet_info

}

main "$@"