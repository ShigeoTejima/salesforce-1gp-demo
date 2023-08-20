#!/bin/sh

print_org_info() {
  local file_org_info="${DIR_TMP}/org_info.json"
  sf org display -o "${TARGET_ORG}" --json > "${file_org_info}"

  local instanceUrl=$(cat "${file_org_info}" | jq -r '.result.instanceUrl')
  local apiVersion=$(cat "${file_org_info}" | jq -r '.result.apiVersion')
  echo "selenide.baseUrl=${instanceUrl}"
  echo "test.instanceUrl=${instanceUrl}"
  echo "test.apiVersion=${apiVersion}"
}

print_user_info() {
  local file_users_info="${DIR_TMP}/users_info.json"
  sf org list users -o "${TARGET_ORG}" --json > "${file_users_info}"

  print_user_info_by_profile "test.admin-user" "システム管理者" "${file_users_info}"
  print_user_info_by_profile "test.standard-user" "標準ユーザ" "${file_users_info}"
}

print_user_info_by_profile() {
  local property_prefix="$1"
  if [ -z "${property_prefix}" ]; then
    echo "function print_user_info_by_profile: required property_prefix." 1>&2
    exit 1
  fi

  local profile="$2"
  if [ -z "${profile}" ]; then
    echo "function print_user_info_by_profile: required profile." 1>&2
    exit 1
  fi

  local file_users_info="$3"
  if [ -z "${file_users_info}" -o ! -r "${file_users_info}" ]; then
    echo "function print_user_info_by_profile: required file_users_info." 1>&2
    exit 1
  fi

  local file_user_info="${DIR_TMP}/user_info.json"
  cat "${file_users_info}" | jq '.result[] | select(.profileName == "'${profile}'")' > "${file_user_info}"
  local userId=$(cat "${file_user_info}" | jq -r '.userId')
  local username=$(cat "${file_user_info}" | jq -r '.username')

  local file_org_user_info="${DIR_TMP}/org_user_info.json"
  sf org display user -o "${username}" --json > "${file_org_user_info}"
  local accessToken=$(cat "${file_org_user_info}" | jq -r '.result.accessToken')
  local password=$(cat "${file_org_user_info}" | jq -r '.result.password')

  echo "${property_prefix}.userId=${userId}"
  echo "${property_prefix}.username=${username}"
  echo "${property_prefix}.password=${password}"
  echo "${property_prefix}.accessToken=${accessToken}"

  print_user_name "${property_prefix}" "${username}"

}

print_user_name() {
  local property_prefix="$1"
  if [ -z "${property_prefix}" ]; then
    echo "function print_user_name: required property_prefix." 1>&2
    exit 1
  fi

  local username="$2"
  if [ -z "${username}" ]; then
    echo "function print_user_name: required username." 1>&2
    exit 1
  fi

  local file_org_info="${DIR_TMP}/org_info.json"
  sf org display -o "${TARGET_ORG}" --json > "${file_org_info}"

  local accessToken=$(cat "${file_org_info}" | jq -r '.result.accessToken')
  local apiVersion=$(cat "${file_org_info}" | jq -r '.result.apiVersion')
  local instanceUrl=$(cat "${file_org_info}" | jq -r '.result.instanceUrl')

  local file_user_info="${DIR_TMP}/user_info.json"
  sf data query --query "SELECT Id, Username, Name, FirstName, LastName FROM User WHERE Username='${username}'" -o "${TARGET_ORG}" --json > "${file_user_info}"
  local fullname=$(cat "${file_user_info}" | jq -r '.result.records[] | select(.Username == "'${username}'").Name')

  echo "${property_prefix}.fullname=${fullname}"
}

rm_dir_tmp() {
  [ -d "${DIR_TMP}" ] && rm -fr "${DIR_TMP}"
}

main() {
  local dir_self=$(dirname $0)
  local dir_parent=$(cd ${dir_self}/.. && pwd)
  TARGET_ORG="$1"
  if [ -z "${TARGET_ORG}" ]; then
    echo "function main: 1st argument required. please specify TARGET_ORG." 1>&2
    exit 1
  fi

  DIR_TMP=$(mktemp -d ${dir_self}/tmp.XXXXXX) || exit 1
  trap rm_dir_tmp EXIT
  trap "trap - EXIT; rm_dir_tmp; exit -1" INT PIPE TERM

  print_org_info
  print_user_info

}

main "$@"
