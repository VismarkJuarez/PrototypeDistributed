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

If multiple devices were running, the `adb devices` command would return a list of all running emulators. The example above shows a list of emulators when a single emulator instance exists, the following list will be displayed if two emulators are running simultaneously:

![Multiple Emulators](resources/multipleEmulators.png)

```bash
$ adb devices
List of devices attached
emulator-5554	device
emulator-5556	device
```

3). Choose one of the emulators to behave as the server. In this walkthrough, the emulator named `emulator-5554` will be configured to run as the server.  The configuration steps are as follows:

a. Navigate to `Settings -> Network & internet -> Wi-Fi -> AndroidWiFi`.  Then, click on the edit button on the top right-hand side of the screen:

![Edit button](resources/editButton.png)

This will open a pop-up. Click on `Advanced Options`:

![Advanced Options](resources/advancedOptions.png)

You are will now be able to modify the device's network settings. First, you'll notice the `IP Settings` are `DHCP` by default. Click on the `IP Settings` drop-down and select `static`.  You will now see a form that looks like below:

![IP Configuration](resources/IPConfiguration.png)

Populate the `IP address` section with the IP `10.0.2.18` as follows:

![IP Configured](resources/ipAdded.png)

Click on `SAVE`.
