#!/bin/sh

main() {
  local dir_self=$(dirname $0)

  local admin_user_org="$1"
  if [ -z "${admin_user_org}" ]; then
    echo "function main: 1st argument required. please specify admin_user_org." 1>&2
    exit 1
  fi

  standard_user_org="$2"
  if [ -z "${standard_user_org}" ]; then
    echo "function main: 2nd argument required. please specify standard_user_org." 1>&2
    exit 1
  fi

  ${dir_self}/generate-env-org.sh "${admin_user_org}"
  ${dir_self}/generate-env-user.sh "${admin_user_org}" test.admin-user
  ${dir_self}/generate-env-user.sh "${standard_user_org}" test.standard-user

}

main "$@"
