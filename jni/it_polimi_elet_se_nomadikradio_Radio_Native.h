/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class it_polimi_elet_se_nomadikradio_Radio_Native */

#ifndef _Included_it_polimi_elet_se_nomadikradio_Radio_Native
#define _Included_it_polimi_elet_se_nomadikradio_Radio_Native
#ifdef __cplusplus
extern "C" {
#endif
#undef it_polimi_elet_se_nomadikradio_Radio_Native_Muted
#define it_polimi_elet_se_nomadikradio_Radio_Native_Muted 0L
#undef it_polimi_elet_se_nomadikradio_Radio_Native_MaximumVolume
#define it_polimi_elet_se_nomadikradio_Radio_Native_MaximumVolume 65535L
#undef it_polimi_elet_se_nomadikradio_Radio_Native_Debugging
#define it_polimi_elet_se_nomadikradio_Radio_Native_Debugging 1L
/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    open
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_open
  (JNIEnv *, jobject);

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    close
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_close
  (JNIEnv *, jobject, jlong);

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    getNativeTurnedOn
 * Signature: (J[Z)I
 */
JNIEXPORT jint JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_getNativeTurnedOn
  (JNIEnv *, jobject, jlong, jbooleanArray);

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    getNativeVolume
 * Signature: (J[I)I
 */
JNIEXPORT jint JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_getNativeVolume
  (JNIEnv *, jobject, jlong, jintArray);

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    getNativeFrequency
 * Signature: (J[J)I
 */
JNIEXPORT jint JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_getNativeFrequency
  (JNIEnv *, jobject, jlong, jlongArray);

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    setNativeTurnedOn
 * Signature: (JZ)I
 */
JNIEXPORT jint JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_setNativeTurnedOn
  (JNIEnv *, jobject, jlong, jboolean);

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    setNativeVolume
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_setNativeVolume
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    setNativeFrequency
 * Signature: (JJ)I
 */
JNIEXPORT jint JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_setNativeFrequency
  (JNIEnv *, jobject, jlong, jlong);

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    getNativeFrequencyRange
 * Signature: (J[J)I
 */
JNIEXPORT jint JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_getNativeFrequencyRange
  (JNIEnv *, jobject, jlong, jlongArray);

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    createNativeEventSource
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_createNativeEventSource
  (JNIEnv *, jobject, jlong);

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    waitForNativeTextEvent
 * Signature: (JILjava/lang/StringBuffer;)I
 */
JNIEXPORT jint JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_waitForNativeTextEvent
  (JNIEnv *, jobject, jlong, jint, jobject);

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    closeNativeEventSource
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_closeNativeEventSource
  (JNIEnv *, jobject, jlong);

#ifdef __cplusplus
}
#endif
#endif
