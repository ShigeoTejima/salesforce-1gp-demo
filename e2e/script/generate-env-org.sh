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

}

main "$@"
