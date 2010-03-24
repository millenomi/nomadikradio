#!/bin/bash

HERE="`dirname "$0"`"
cd "$HERE"

javah -classpath bin -d jni it.polimi.elet.se.nomadikradio.Radio_Native
