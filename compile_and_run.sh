#!/bin/bash
# Script de compilation et lancement
# Usage: ./compile_and_run.sh [--gui]

SRC_DIR="src"
OUT_DIR="out"

echo "🔨 Compilation du projet Labyrinthe..."
mkdir -p "$OUT_DIR"

javac -d "$OUT_DIR" "$SRC_DIR"/*.java

if [ $? -eq 0 ]; then
    echo "✅ Compilation réussie !"
    echo ""
    if [ "$1" == "--gui" ]; then
        echo "🖥️  Lancement en mode GUI..."
        java -cp "$OUT_DIR" Main --gui
    else
        echo "💻 Lancement en mode Console..."
        java -cp "$OUT_DIR" Main
    fi
else
    echo "❌ Erreur de compilation."
    exit 1
fi
