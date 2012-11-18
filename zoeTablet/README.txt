12 November 2012

ZOE TABLET DOCUMENTATION

The following documentation outlines the distribution for the robotic user interface zoeTablet that utilizes a Data Distribution Service (DDS) for communication. Any questions please contact the author:

Simon Engler
Sensor Networks Laboratory
Department of Electrical and Computer Engineering
University of Calgary
email: stengler@ucalgary.ca
       simon.engler@gmail.com


INSTALLATION:

Development of Zoe tablet requires the installation of the Android SDK and also the Coredx DDS library from TwinOaks Computing ltd.

INSTALLATION OF ANDROID SDK:
http://developer.android.com/sdk/index.html

SET UP ANDROID DEVELOPMENT ON ECLIPSE:
http://developer.android.com/sdk/installing/installing-adt.html

DOWNLOAD COREDX FILES:
http://www.twinoakscomputing.com/coredx
 
ON UBUNTU ADD THE FOLLOWING TO THE '.bashrc' file:
=============================================================================================
# HOME is the users home directoy
export COREDX_TOP=$HOME/coredx-3.4.0_p9-Evaluation
export COREDX_HOST=Linux_2.6_x86_64_gcc43
export COREDX_TARGET=Linux_2.6_x86_64_gcc43

# The above exports may be changed to the ones below depending on your computer
#export COREDX_TOP=$HOME/coredx-3.4.0_p21-Evaluation
#export COREDX_HOST=Android_2.2_armv5_gcc44
#export COREDX_TARGET=Android_2.2_armv5_gcc44

#Library path of the cordx android distribution
export LD_LIBRARY_PATH=$HOME/coredx-3.4.0_p21-Evaluation/target/Android_2.2_armv5_gcc44/lib

#Home location of the Android SDK
export ANDROID_SDK_HOME=$HOME/android-sdk-linux
export ANDROID_NDK_HOME=$HOME/android-ndk-r8
export TWINOAKS_LICENSE_FILE=$HOME/coredx-3.4.0_p21-Evaluation/CoreDXeval.lic

#Location of ADB android device 
export ADB=/home/stengler/android-sdks/platform-tools/adb

#Location of the Android SDK and NDK Tools
export ANDROID_SDK=$HOME/android-sdk-linux
export ANDROID_NDK=$HOME/android-ndk-r8
export ANDROID_NDK_TOOLCHAIN_ROOT=/home/stengler/android-9-toolchain

shopt -s expand_aliases
export PATH=$PATH:$ANDROID_SDK/tools:$ANDROID_NDK
android.toolchain.cmake
android_dependencies/android-cmake/toolchain/android.toolchain.cmake
-DCMAKE_INSTALL_NAME_TOOL=/usr/bin/install_name_tool '
export ADB=$HOME/android-sdk-linux/platform-tools/adb
alias adb='$ADB'
export ANDROID_STANDALONE_TOOLCHAIN=$HOME/android-ndk-r6/toolchains/arm-linux-androideabi-4.4.3
export ANDROID_NDK=$HOME/android-ndk-r6
=================================================================================

CREATE DDS MESSAGE

Create the Data Definition Language (DDL) file for the data type(s) you will
use for communications. The CoreDX DDS DDL syntax is very similar to the
OMG IDL syntax for describing data types. Here is the “hello world”
example provided with the distribution:

Compile the DDL to generate the type specific code using the CoreDX DDS
DDL compiler. This requires your TWINOAKS_LICENSE_FILE environment
variable be set as described above. Assuming the name of the DDL file is
dataDDS.ddl:

struct dataDDS {
        float XVel_DDS;
	float YVel_DDS;
	float CompassDir_DDS;
	float GPS_LN_DDS;
	float GPS_LT_DDS;
        string Log_DDS;
        sequence<octet> data_image_DDS;
};

Create code to publish data of this data type. Our sample Hello World
publisher is located in COREDX_TOP/workspace/Tablet/zoeTablet/ddspackage/dataDDS.c

% COREDX_TOP/host/COREDX_HOST/bin/coredx_ddl –f dataDDS.ddl             

The compilation will generate the following files (names are based on the
DDL filename):

dataDDSDataReader.java  
dataDDS.java             
dataDDSDataWriter.java  
dataDDSSeq.java
dataDDSTypeSupport.java

SETUP TO COMPILE IN ECLIPSE

Download file from github into the eclipse workspace and unpack:

https://github.com/SimonEngler/Tablet

The gihub contains folders:
DDS_Publisher - A program to send messages to the Zoe Tablet from a android device
DDS_Subscriber - Experimental Code 
PhotoDDS - A program to send images and data to the Zoe Tablet from an android device
zoeTablet - The zoe Tablet DDS user interface

In Eclipse:
- File -> New Project: Will open up selection wizard
- Select: Android Project From Existing Code
- Select the Eclipse workspace directory containing 'ZoeTablet'
- This will likely open a project 'com.example.zoetablet.BasicFragmentActivity' 
- Copy a current Coredx DDS License file 'coredx_dds.lic' to the projects 'assets'
-The project directory 'libs/armeabi' should contain:

libdds_java.so
android-support-v4.jar
coredx_dds.jar

If not these files can be found in the corexdx distribution file and copied to this location. 
- Right click on 'com.example.zoetablet.BasicFragmentActivity' and select Properties:

- Select 'Android' and check Android 4.1
- Select 'Java Build Path' Add the locations of files:

	android-support-v4.jar
	android.jar

Project should compile and run.

SET ANDROID PERMISSIONS

Open the project AndroidManifest.xml and ensure the following permissions are set:

INTERNET
CHANGE_WIFI_MULTICAST_STATE
SET_ORIENTATION
READ_LOGS
SET_ORIENTATION
CHANGE_WIFI_MULTICAST_STATE
SET_ORIENTATION
READ_LOGS
MOUNT_UNMOUNT_FILESYSTEMS
READ_EXTERNAL_STORAGE
ACCESS_NETWORK_STATE

ZOE TABLET SOURCE FILE STRUCTURE

Contained in the src folder there are two folders:

ddspackage: This folder contains the DDS java code compiled in the previous section.

zoetablet: 

The zoe tablet displays six seperate windows. Each window is a fragment activity. (See Android documentation on fragments) The program uses the following java files that are structured as follows:

BasicFragmentActivity: This class is the main controller for the zoeTablet code. All fragments and some data structures are initialized in this class. The program does the following:

-Initializes Wifi
-Opens and checks coredx license file
-Initializes variables to be displayed in Telemetry Window (NavigationFragment)
-Load default background image
-Creates DDS subscriber
-Loads all fragment windows:
 	BasicFragment (Image Display Window - Top Center)
	LoggingFragment (Display DDS Message logs - Top Left)
	ConnectionFragment (Connect to Wifi - Lower Left)
	BasicFragment2 (Compass Display - Top Right)
	DualJoyStickActivity (Virtual Control Sticks - Lower Center)
	DatalogFragment (Display Telemetry DDS Data - Lower Right)
-Initializes Compass
-Accesses Sensors to get tablet orientation
-Update compass display with sensor data














