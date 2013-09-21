LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := TestNDK
LOCAL_SRC_FILES := TestNDK.cpp

include $(BUILD_SHARED_LIBRARY)
