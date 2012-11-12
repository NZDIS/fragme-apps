LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := FragTheBallRenderer
LOCAL_SRC_FILES := FragTheBallRenderer.cpp esUtil.c esShapes.c esTransform.c Drawable.cpp Origin.cpp Sphere.cpp 

LOCAL_LDLIBS := -lm -lGLESv2 -ldl -llog -landroid

include $(BUILD_SHARED_LIBRARY)


