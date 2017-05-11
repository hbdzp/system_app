
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_PACKAGE_NAME := PanasonicHome
LOCAL_MODULE_TAGS := optional
LOCAL_PROGUARD_FLAG_FILES := proguard.flags
LOCAL_OVERRIDES_PACKAGES := Home Launcher2 LatinIME
LOCAL_CERTIFICATE := platform

LOCAL_RESOURCE_DIR += frameworks/support/v7/appcompat/res
LOCAL_RESOURCE_DIR += frameworks/support/v7/recyclerview/res

LOCAL_RESOURCE_DIR += $(LOCAL_PATH)/res
LOCAL_SRC_FILES := \
    $(call all-java-files-under, java) \
    $(call all-renderscript-files-under, java)

LOCAL_STATIC_JAVA_LIBRARIES += \
android-common \
android-support-v4 \
android-support-v8-renderscript \
android-support-v7-recyclerview \
volley \
codec \
fastJSON \
picasso

LOCAL_AAPT_FLAGS := --auto-add-overlay
LOCAL_AAPT_FLAGS += --extra-packages android.support.v7.appcompat
LOCAL_AAPT_FLAGS += --extra-packages android.support.v7.recyclerview

LOCAL_PROGUARD_ENABLED := disabled
LOCAL_JAVA_LIBRARIES := \
com.mstar.android \
org.apache.http.legacy

include $(BUILD_PACKAGE)

include $(CLEAR_VARS)

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES += codec:libs/commons-codec-1.9.jar \
fastJSON:libs/fastjson-1.2.23.jar

include $(BUILD_MULTI_PREBUILT)
include $(call all-makefiles-under,$(LOCAL_PATH))

