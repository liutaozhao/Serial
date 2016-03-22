LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
#LOCAL_MODULE_TAGS := eng
# This is the target being built.
#
LOCAL_MODULE:= libserial_runtime
#
#  
#
#  # All of the source files that we will compile.
LOCAL_SRC_FILES:= \
cn_com_farsight_SerialService_SerialService.cpp
# All of the shared libraries we link against.
#
LOCAL_SHARED_LIBRARIES := \
        libandroid_runtime \
        libnativehelper \
        libcutils \
        libutils \
        libhardware

# No static libraries.
LOCAL_STATIC_LIBRARIES :=
# Also need the JNI headers.
#
LOCAL_C_INCLUDES += \
        $(JNI_H_INCLUDE)
        # No specia compiler flags.
LOCAL_CFLAGS +=

# Don't prelink this library.  For more efficient code, you may want
#
# # to add this library to the prelink map and set this to true.
#
LOCAL_PRELINK_MODULE := false
LOCAL_MODULE_TAGS := eng
include $(BUILD_SHARED_LIBRARY) 
