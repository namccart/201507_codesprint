#!/bin/bash

NDK=/home/namccart/android/android-ndk-r10e
SYSROOT=/home/namccart/android/android-toolchain/sysroot
TOOLCHAIN=/home/namccart/android/android-toolchain
function build_one
{
../configure \
--prefix=$PREFIX \
--enable-shared \
--disable-static \
--disable-doc \
--disable-ffmpeg \
--disable-ffplay \
--disable-ffprobe \
--disable-ffserver \
--disable-avdevice \
--disable-doc \
--disable-symver \
--cross-prefix=$TOOLCHAIN/bin/arm-linux-androideabi- \
--target-os=linux \
--arch=arm \
--enable-cross-compile \
--sysroot=$SYSROOT \
--extra-cflags="-Os -fpic $ADDI_CFLAGS" \
--extra-ldflags="$ADDI_LDFLAGS" \
$ADDITIONAL_CONFIGURE_FLAG
make clean
make -j 4
}
CPU=arm
PREFIX=/home/namccart/android/android-toolchain/user
ADDI_CFLAGS="-marm"
build_one
