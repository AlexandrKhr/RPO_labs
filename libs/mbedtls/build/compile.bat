set ABI=x86

set ANDROID_NDK=C:\Users\alexs\AppData\Local\Android\Sdk\ndk\28.0.13004108
set TOOL_CHAIN=%ANDROID_NDK%\build\cmake\android.toolchain.cmake
set CMAKE=C:\Users\alexs\AppData\Local\Android\Sdk\cmake\3.31.5\bin\cmake.exe

if not exist %ABI% mkdir %ABI%
cd %ABI%

%CMAKE% -GNinja ../../mbedtls -DCMAKE_SYSTEM_NAME=Android -DCMAKE_SYSTEM_VERSION=21 -DANDROID_ABI=%ABI% -DCMAKE_TOOLCHAIN_FILE=%TOOL_CHAIN% -DUSE_SHARED_MBEDTLS_LIBRARY=On -DENABLE_TESTING=Off

%CMAKE% --build .
