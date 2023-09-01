#!/bin/sh

ready_metadata() {
  local metadata_src_dir=${DIR_SELF}/demo-api/metadata
  local metadata_dir=${DIR_TMP}/metadata

  cp -r ${metadata_src_dir} ${metadata_dir}

  for target in $(echo "customMetadata/demo_aho__api_config.demo_aho__demo_api.md" "remoteSiteSettings/demo_aho__DemoApi.remoteSite"); do
    env demo_api_host=${DEMO_API_HOST} envsubst < ${metadata_src_dir}/${target} > ${metadata_dir}/${target}
  done

}

rm_dir_tmp() {
  [ -d "${DIR_TMP}" ] && rm -fr "${DIR_TMP}"
}

deploy_metadata() {
  local metadata_dir=${DIR_TMP}/metadata
  sf project deploy start --metadata-dir=${metadata_dir} -o ${TARGET_ORG}
}

main() {
  DIR_SELF=$(dirname $0)

  TARGET_ORG="$1"
  if [ -z "${TARGET_ORG}" ]; then
    echo "function main: 1st argument required. please specify TARGET_ORG." 1>&2
    exit 1
  fi

  DEMO_API_HOST="$2"
  if [ -z "${DEMO_API_HOST}" ]; then
    echo "function main: 2nd argument required. please specify DEMO_API_HOST." 1>&2
    exit 1
  fi

  DIR_TMP=$(mktemp -d ${DIR_SELF}/tmp.XXXXXX) || exit 1
  trap rm_dir_tmp EXIT
  trap "trap - EXIT; rm_dir_tmp; exit -1" INT PIPE TERM

  ready_metadata
  deploy_metadata
}

main "$@"