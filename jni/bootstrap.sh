#!/bin/sh

if [ "$1" == "" ]; then
	echo "Usage: bootstrap.sh <path to Android NDK>" >&2
	exit 1
fi

ANDROID_NDK="$1"
ANDROID_NDK="`cd "$ANDROID_NDK"; pwd`"
HERE="`dirname "$0"`"

if [ ! -e "$ANDROID_NDK"/apps/NomadikRadio ]; then
	ln -s "$HERE/.." "$ANDROID_NDK"/apps/NomadikRadio
fi
echo ANDROID_NDK = "$ANDROID_NDK" > "$HERE"/libNomadikRadio/Local.xcconfig
