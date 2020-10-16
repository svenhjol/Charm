#!/usr/bin/env bash

if [ -z `which sed` ]; then
  echo "Missing sed"
  exit 1
fi

if [ -z "$1" ]; then
  echo "Missing type"
  exit 1
fi

TYPE=$1
THISMOD="charm"
DATA="../src/main/resources/data/${THISMOD}"
ASSETS="../src/main/resources/assets/${THISMOD}"
SOURCE="./simple_block"

copy_replace() {
  SRC=$1
  DEST=$2
  IT=$3

  cp "${SRC}" "${DEST}"
  sed -i "s/TYPE/${TYPE}/g" "${DEST}"
  sed -i "s/NAMESPACE/${NAMESPACE}/g" "${DEST}"
  sed -i "s/THISMOD/${THISMOD}/g" "${DEST}"

  if [ -n "${IT}" ]; then
    sed -i "s/?/${IT}/g" "${DEST}"
  fi
}

# blockstates
copy_replace "${SOURCE}/blockstates/block.json" "${ASSETS}/blockstates/${TYPE}.json"

# models/block
copy_replace "${SOURCE}/models/block/block.json" "${ASSETS}/models/block/${TYPE}.json"

# models/item
copy_replace "${SOURCE}/models/item/block.json" "${ASSETS}/models/item/${TYPE}.json"

# loot_tables/blocks
copy_replace "${SOURCE}/loot_tables/blocks/block.json" "${DATA}/loot_tables/blocks/${TYPE}.json"