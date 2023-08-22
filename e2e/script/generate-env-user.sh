#!/bin/sh

is_print() {
  local property="$1"
  if [ -z "${property}" ]; then
    echo "function is_print: required property." 1>&2
    exit 1
  fi

  grep ^${property}$ ${DIR_SELF}/${VAR_PREFIX}_property.list > /dev/null
}

print_user_info() {
  local file_user_info="${DIR_TMP}/user_info.json"
  sf org display user -o "${TARGET_ORG}" --json > "${file_user_info}"

  local userId=$(cat "${file_user_info}" | jq -r '.result.id')
  local username=$(cat "${file_user_info}" | jq -r '.result.username')
  local accessToken=$(cat "${file_user_info}" | jq -r '.result.accessToken')

  is_print "userId"      && echo "${VAR_PREFIX}.userId=${userId}"
  is_print "username"    && echo "${VAR_PREFIX}.username=${username}"
  is_print "accessToken" && echo "${VAR_PREFIX}.accessToken=${accessToken}"

  print_user_name_and_password "${username}"

}

print_user_name_and_password() {
  local username="$1"
  if [ -z "${username}" ]; then
    echo "function print_user_name: required username." 1>&2
    exit 1
  fi

  is_print "fullname" || is_print "password" || return

  local file_org_info="${DIR_TMP}/org_info.json"
  sf org display -o "${TARGET_ORG}" --json > "${file_org_info}"

  local accessToken=$(cat "${file_org_info}" | jq -r '.result.accessToken')
  local apiVersion=$(cat "${file_org_info}" | jq -r '.result.apiVersion')
  local instanceUrl=$(cat "${file_org_info}" | jq -r '.result.instanceUrl')
  local password=$(cat "${file_org_info}" | jq -r '.result.password')

  local file_user_info="${DIR_TMP}/user_info.json"
  sf data query --query "SELECT Id, Username, Name, FirstName, LastName FROM User WHERE Username='${username}'" -o "${TARGET_ORG}" --json > "${file_user_info}"
  local fullname=$(cat "${file_user_info}" | jq -r '.result.records[] | select(.Username == "'${username}'").Name')

  is_print "fullname" && echo "${VAR_PREFIX}.fullname=${fullname}"
  is_print "password" && echo "${VAR_PREFIX}.password=${password}"
}

rm_dir_tmp() {
  [ -d "${DIR_TMP}" ] && rm -fr "${DIR_TMP}"
}

main() {
  DIR_SELF=$(dirname $0)

  TARGET_ORG="$1"
  if [ -z "${TARGET_ORG}" ]; then
    echo "function main: 1st argument required. please specify TARGET_ORG." 1>&2
    exit 1
  fi

  VAR_PREFIX="$2"
  if [ -z "${VAR_PREFIX}" ]; then
    echo "function main: 2nd argument required. please specify VAR_PREFIX. example: VAR_PREFIX.username" 1>&2
    exit 1
  fi

  DIR_TMP=$(mktemp -d ${DIR_SELF}/tmp.XXXXXX) || exit 1
  trap rm_dir_tmp EXIT
  trap "trap - EXIT; rm_dir_tmp; exit -1" INT PIPE TERM

  print_user_info

}

main "$@"
