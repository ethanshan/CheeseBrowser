adb push ./screen_off.sh /system/bin
adb shell chmod 777 /system/bin/screen_off.sh

adb push ./screen_on.sh /system/bin
adb shell chmod 777 /system/bin/screen_on.sh

adb push ./system_reboot.sh /system/bin
adb shell chmod 777 /system/bin/system_reboot.sh
