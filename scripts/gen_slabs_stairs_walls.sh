#!/usr/bin/env bash

if [ -z `which sed` ]; then
  echo "Missing sed"
  exit 1
fi

if [ -z "$1" ]; then
  echo "Missing short type (e.g. prismarine_brick)"
  exit 1
fi

if [ -z "$2" ]; then
  LONGTYPE=$1
else
  LONGTYPE=$2
fi

TYPE=$1
THISMOD="charm"
DATA="../src/main/resources/data/${THISMOD}"
ASSETS="../src/main/resources/assets/${THISMOD}"
SOURCE="./slabs_stairs_walls"

copy_replace() {
  SRC=$1
  DEST=$2
  IT=$3

  cp "${SRC}" "${DEST}"
  sed -i "s/LONGTYPE/${LONGTYPE}/g" "${DEST}"
  sed -i "s/TYPE/${TYPE}/g" "${DEST}"
  sed -i "s/NAMESPACE/${NAMESPACE}/g" "${DEST}"
  sed -i "s/THISMOD/${THISMOD}/g" "${DEST}"

  if [ -n "${IT}" ]; then
    sed -i "s/?/${IT}/g" "${DEST}"
  fi
}

add_tags() {
  # walls
  for f in "${DATA}/minecraft/tags/blocks/walls.json"
  do
    if [ -e "$f" ]; then
      remove_last_entry "${f}" "]" "}"

      {
        echo "    \"${THISMOD}:${TYPE}_wall\"";
        echo "  ]"
        echo "}"
      } >> $f
      strip_empty_lines $f
    fi
  done
}

add_lang_strings() {
  NAME="$(tr '[:lower:]' '[:upper:]' <<< ${TYPE:0:1})${TYPE:1}"
  LANGFILE="${ASSETS}/lang/en_us.json"
  remove_last_entry "${LANGFILE}" "}"

  # add new lang entries
  {
    echo "  \"block.${THISMOD}.${TYPE}_slab\": \"${NAME} Slab\",";
    echo "  \"block.${THISMOD}.${TYPE}_stairs\": \"${NAME} Stairs\","
    echo "  \"block.${THISMOD}.${TYPE}_wall\": \"${NAME} Wall\"";
    echo "}"
  } >> $LANGFILE
}

remove_last_entry() {
  sed -i ':a;N;$!ba;s/"\n/",\n/g' "${1}" # add a comma after the last entry
  sed -i "s/${2}//g" "${1}" # remove bracket
  if [ -n "$3" ]; then
    sed -i "s/${3}//g" "${1}" # remove bracket
  fi
}

strip_empty_lines() {
  sed -i '/^ *$/d' "${1}"
}

# blockstates
copy_replace "${SOURCE}/blockstates/slab.json" "${ASSETS}/blockstates/${TYPE}_slab.json"
copy_replace "${SOURCE}/blockstates/stairs.json" "${ASSETS}/blockstates/${TYPE}_stairs.json"
copy_replace "${SOURCE}/blockstates/wall.json" "${ASSETS}/blockstates/${TYPE}_wall.json"

# models/block
copy_replace "${SOURCE}/models/block/slab.json" "${ASSETS}/models/block/${TYPE}_slab.json"
copy_replace "${SOURCE}/models/block/slab_top.json" "${ASSETS}/models/block/${TYPE}_slab_top.json"
copy_replace "${SOURCE}/models/block/stairs.json" "${ASSETS}/models/block/${TYPE}_stairs.json"
copy_replace "${SOURCE}/models/block/stairs_inner.json" "${ASSETS}/models/block/${TYPE}_stairs_inner.json"
copy_replace "${SOURCE}/models/block/stairs_outer.json" "${ASSETS}/models/block/${TYPE}_stairs_outer.json"
copy_replace "${SOURCE}/models/block/wall_inventory.json" "${ASSETS}/models/block/${TYPE}_wall_inventory.json"
copy_replace "${SOURCE}/models/block/wall_post.json" "${ASSETS}/models/block/${TYPE}_wall_post.json"
copy_replace "${SOURCE}/models/block/wall_side.json" "${ASSETS}/models/block/${TYPE}_wall_side.json"
copy_replace "${SOURCE}/models/block/wall_side_tall.json" "${ASSETS}/models/block/${TYPE}_wall_side_tall.json"

# models/item
copy_replace "${SOURCE}/models/item/slab.json" "${ASSETS}/models/item/${TYPE}_slab.json"
copy_replace "${SOURCE}/models/item/stairs.json" "${ASSETS}/models/item/${TYPE}_stairs.json"
copy_replace "${SOURCE}/models/item/wall.json" "${ASSETS}/models/item/${TYPE}_wall.json"

# loot_tables/blocks
copy_replace "${SOURCE}/loot_tables/blocks/slab.json" "${DATA}/loot_tables/blocks/${TYPE}_slab.json"
copy_replace "${SOURCE}/loot_tables/blocks/stairs.json" "${DATA}/loot_tables/blocks/${TYPE}_stairs.json"
copy_replace "${SOURCE}/loot_tables/blocks/wall.json" "${DATA}/loot_tables/blocks/${TYPE}_wall.json"

# recipes
mkdir -p "${DATA}/recipes/${TYPE}"
copy_replace "${SOURCE}/recipes/slab.json" "${DATA}/recipes/${TYPE}/${TYPE}_slab.json"
copy_replace "${SOURCE}/recipes/stairs.json" "${DATA}/recipes/${TYPE}/${TYPE}_stairs.json"
copy_replace "${SOURCE}/recipes/wall.json" "${DATA}/recipes/${TYPE}/${TYPE}_wall.json"
copy_replace "${SOURCE}/recipes/slab_stonecutter.json" "${DATA}/recipes/${TYPE}/${TYPE}_slab_stonecutter.json"
copy_replace "${SOURCE}/recipes/stairs_stonecutter.json" "${DATA}/recipes/${TYPE}/${TYPE}_stairs_stonecutter.json"
copy_replace "${SOURCE}/recipes/wall_stonecutter.json" "${DATA}/recipes/${TYPE}/${TYPE}_wall_stonecutter.json"

add_tags
add_lang_strings