cmake_minimum_required(VERSION 3.22.1)

project("fclient")

add_library(fclient SHARED
        # List C/C++ source files with relative paths to this CMakeLists.txt.
        native-lib.cpp)

target_link_libraries(fclient
        # List libraries link to the target library
        android
        spdlog
        mbedcrypto
        ${log-lib}
        log)

add_library(spdlog STATIC IMPORTED)
set_target_properties(spdlog PROPERTIES IMPORTED_LOCATION
        C:/Users/alexs/AndroidStudioProjects/RPO_labs/libs/spdlog/build/${ANDROID_ABI}/libspdlog.a)
target_include_directories(fclient PUBLIC
        C:/Users/alexs/AndroidStudioProjects/RPO_labs/libs/spdlog/spdlog/include)


add_library(mbedcrypto SHARED IMPORTED)

set_target_properties(mbedcrypto PROPERTIES IMPORTED_LOCATION
        ${CMAKE_CURRENT_SOURCE_DIR}/../nativeLibs/${ANDROID_ABI}/library/libmbedcrypto.so)

target_include_directories(fclient PUBLIC
        /Users/alexs/AndroidStudioProjects/RPO_labs/libs/spdlog/spdlog/include
        /Users/alexs/AndroidStudioProjects/RPO_labs/libs/mbedtls/mbedtls/include
)