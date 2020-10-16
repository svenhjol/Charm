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
DATAROOT="../src/main/resources/data/${THISMOD}"
ASSETS="../src/main/resources/assets/${THISMOD}"
RECIPES="./woodcutter_recipes"

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

copy_replace "${RECIPES}/button_from_planks.json" "${DATAROOT}/recipes/woodcutters/${TYPE}_button_from_${TYPE}_planks.json"
copy_replace "${RECIPES}/door_from_planks.json" "${DATAROOT}/recipes/woodcutters/${TYPE}_door_from_${TYPE}_planks.json"
copy_replace "${RECIPES}/fence_from_planks.json" "${DATAROOT}/recipes/woodcutters/${TYPE}_fence_from_${TYPE}_planks.json"
copy_replace "${RECIPES}/fence_gate_from_planks.json" "${DATAROOT}/recipes/woodcutters/${TYPE}_fence_gate_from_${TYPE}_planks.json"
copy_replace "${RECIPES}/sign_from_planks.json" "${DATAROOT}/recipes/woodcutters/${TYPE}_sign_from_${TYPE}_planks.json"
copy_replace "${RECIPES}/slab_from_planks.json" "${DATAROOT}/recipes/woodcutters/${TYPE}_slab_from_${TYPE}_planks.json"
copy_replace "${RECIPES}/stairs_from_planks.json" "${DATAROOT}/recipes/woodcutters/${TYPE}_stairs_from_${TYPE}_planks.json"

# there are no crimson or warped boats
if [[ "${TYPE}" != "crimson" ]] && [[ "${TYPE}" != "warped" ]]; then
  copy_replace "${RECIPES}/boat_from_planks.json" "${DATAROOT}/recipes/woodcutters/${TYPE}_boat_from_${TYPE}_planks.json"
fi

# variant ladders
copy_replace "${RECIPES}/ladder_from_planks.json" "${DATAROOT}/recipes/variant_ladders/${TYPE}_ladder_from_${TYPE}_planks.json"

# copy the default ladder recipe; this is overridden by ladders module
copy_replace "${RECIPES}/vanilla_ladder_from_planks.json" "${DATAROOT}/recipes/woodcutters/vanilla_ladder_from_planks.json"

# copy the default woodcutter recipe in case it gets overwritten
copy_replace "${RECIPES}/woodcutter.json" "${DATAROOT}/recipes/woodcutters/woodcutter.json"