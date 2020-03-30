# Running the Application via an Emulator

### Configuring Emulator Port-Forwarding on MacOSX:

1). Install `adb` with the following `brew` command:

```bash
$ brew cask install android-platform-tools

==> Downloading https://dl.google.com/android/repository/platform-tools_r29.0.6-
######################################################################## 100.0%
==> Verifying SHA-256 checksum for Cask 'android-platform-tools'.
==> Installing Cask android-platform-tools
==> Linking Binary 'adb' to '/usr/local/bin/adb'.
==> Linking Binary 'dmtracedump' to '/usr/local/bin/dmtracedump'.
==> Linking Binary 'etc1tool' to '/usr/local/bin/etc1tool'.
==> Linking Binary 'fastboot' to '/usr/local/bin/fastboot'.
==> Linking Binary 'hprof-conv' to '/usr/local/bin/hprof-conv'.
==> Linking Binary 'mke2fs' to '/usr/local/bin/mke2fs'.
🍺  android-platform-tools was successfully installed!
```

2). Verify that `adb` was installed correctly by opening Android Studio and launching a device emulator instance (the kind of device does not matter).

![emulator instance running](resources/emulatorRunning.png)

Leave the emulator device running. `adb`can detect any active emulator instances. Run the following command to list all of the instances that `adb` has detected.  The emulator instance, along with its port number, will be displayed.

```bash
$ adb devices
List of devices attached
emulator-5554	device
```

If multiple devices were running, the `adb devices` command would return a list of all running emulators. The example above shows a list of emulators when a single emulator instance exists, the following list will be displayed if two emulators are running simultarneously:


```bash
$ adb devices
List of devices attached
emulator-5554	device
emulator-5556	device
```
