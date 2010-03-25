# Copyright (C) 2009 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
LOCAL_PATH := $(call my-dir)

# Real radio driver.

include $(CLEAR_VARS)

LOCAL_MODULE    := FMRadio_V4L2
LOCAL_SRC_FILES := FMRadio.c it_polimi_elet_se_nomadikradio_Radio_Native.c
LOCAL_CFLAGS    := -Wall -Wno-parentheses

include $(BUILD_SHARED_LIBRARY)

# Simulated radio driver

include $(CLEAR_VARS)

LOCAL_MODULE    := FMRadio_Simulated
LOCAL_SRC_FILES := FMRadio_Simulated.c it_polimi_elet_se_nomadikradio_Radio_Native.c
LOCAL_LDLIBS    := -llog
LOCAL_CFLAGS    := -Wall -Wno-parentheses

include $(BUILD_SHARED_LIBRARY)
