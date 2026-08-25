#!/bin/bash

cd "$(dirname "$0")"
source build.conf
cd ..

set -e # stop on error

./scripts/clean.sh

mkdir "$TEMP_DIR"
mkdir "$MACHINE_PROOF_DIR"

build_proof() {
	echo "building: $PROOFS_DIR/$1"
	java -cp "$BIN_DIR" main/Main ExprEngine -in "$PROOFS_DIR"/$1 -out "$MACHINE_PROOF_DIR"/$2 -axi "$AXIOMS_FILE" -macdir "$MACHINE_PROOF_DIR"
}

find src -type f -name "*.java" -print0 | xargs -0 javac -d "$BIN_DIR"

build_proof "proof_1.txt" "proof_1.txt"
build_proof "proof_2.txt" "proof_2.txt"
build_proof "proof_3.txt" "proof_3.txt"
build_proof "proof_4.txt" "proof_4.txt"
build_proof "proof_5.txt" "proof_5.txt"