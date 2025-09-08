#!/usr/bin/env bash
set -eux
SDK_ROOT="$HOME/android-sdk"
mkdir -p "$SDK_ROOT/cmdline-tools"
curl -sLo sdk.zip https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip
unzip -q sdk.zip -d "$SDK_ROOT/cmdline-tools"
mv "$SDK_ROOT/cmdline-tools/cmdline-tools" "$SDK_ROOT/cmdline-tools/latest"
yes | "$SDK_ROOT/cmdline-tools/latest/bin/sdkmanager" --sdk_root="$SDK_ROOT" \
  "platform-tools" "platforms;android-34" "build-tools;34.0.0" "patcher;v4"
echo "sdk.dir=$SDK_ROOT" > local.properties
