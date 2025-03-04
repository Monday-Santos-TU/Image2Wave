#! /usr/bin/bash

mkdir imgs
java ImageWave $1
convert -delay 5 -loop 0 imgs/*.jpg wave.gif
mogrify -layers 'optimize' -fuzz 7% wave.gif
rm -rf imgs
