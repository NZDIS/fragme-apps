This file will hopefully help you to get an Android NDK application building and installing using Eclipse.




Open a command console (windows start button -> in the text field type "cmd" and press enter). 
Navigate to your project folder. Enter "ndk-build". 
If you set up NDK correctly and set the path to it then the C/C++ code will now be compiled.

In the same console as above, type "ant debug install".
If you correctly set the path to ANT then the java and c/c++ code will be compiled together.
If a device is plugged in (or if you have a VM running?) and its drivers are installed and it has usb debugging enabled then the program will be installed.
