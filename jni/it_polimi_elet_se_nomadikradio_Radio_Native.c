
#include "it_polimi_elet_se_nomadikradio_Radio_Native.h"
#include "FMRadio.h"

#include <sys/types.h>

static inline FMRadio* FMRadioFromHandle(jlong handle) {
	FMRadio* r = (FMRadio*) ((intptr_t)handle);
	return r;
}

static inline FMRadioRDSEventSource* FMRadioRDSEventSourceFromHandle(jlong handle) {
	FMRadioRDSEventSource* r = (FMRadioRDSEventSource*) ((intptr_t)handle);
	return r;
}

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    open
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_open
(JNIEnv* env, jobject self) {
	FMRadio* r;
	intptr_t rself;
	if (FMRadioOK(FMRadioOpen(&r)))
		rself = (intptr_t) r;
	else
		rself = 0;
	
	return (jlong) rself;
}

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_close
(JNIEnv* env, jobject self, jlong handle) {
	FMRadioClose(FMRadioFromHandle(handle));
}

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    setNativeTurnedOn
 * Signature: (Z)I
 */
JNIEXPORT jint JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_setNativeTurnedOn
(JNIEnv* env, jobject self, jlong handle, jboolean on) {
	return (jint) FMRadioSetTurnedOn(FMRadioFromHandle(handle), on == JNI_TRUE);
}

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    setNativeVolume
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_setNativeVolume
(JNIEnv* env, jobject self, jlong handle, jint volume) {
	if (volume > kFMVolumeMaximum)
		volume = kFMVolumeMaximum;
	else if (volume < kFMVolumeMute)
		volume = kFMVolumeMute;
	
	return (jint) FMRadioSetVolume(FMRadioFromHandle(handle), volume);
}

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    setNativeFrequency
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_setNativeFrequency
(JNIEnv* env, jobject self, jlong handle, jlong khz) {
	if (khz < 0)
		return (jint) kFMRadioFrequencyOutOfRange;
	
	return (jint) FMRadioSetFrequency(FMRadioFromHandle(handle), khz);
}

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    getNativeFrequencyRange
 * Signature: (J[I)I
 */
JNIEXPORT jint JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_getNativeFrequencyRange
(JNIEnv* env, jobject self, jlong handle, jlongArray minMax) {
	FMRadio* r = FMRadioFromHandle(handle);
	
	if ((*env)->GetArrayLength(env, minMax) != 2)
		return kFMRadioIncorrectArgument;
	
	uint32_t min, max;
	FMRadioResult err = FMRadioGetFrequencyRange(r, &min, &max);
	if (err != kFMRadioNoError)
		return err;
	
	jlong* minMaxC = (*env)->GetLongArrayElements(env, minMax, NULL);
	minMaxC[0] = min;
	minMaxC[1] = max;
	(*env)->ReleaseLongArrayElements(env, minMax, minMaxC, 0 /* 0 means 'copy back changes' */);

	return kFMRadioNoError;
}

// -- Getters --

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    getNativeTurnedOn
 * Signature: (J[Z)I
 */
JNIEXPORT jint JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_getNativeTurnedOn
(JNIEnv* env, jobject self, jlong handle, jbooleanArray onResult) {
	FMRadio* r = FMRadioFromHandle(handle);
	
	if ((*env)->GetArrayLength(env, onResult) != 1)
		return kFMRadioIncorrectArgument;
	
	bool on;
	FMRadioResult err = FMRadioGetTurnedOn(r, &on);
	if (err != kFMRadioNoError)
		return err;
	
	jboolean* onResultC = (*env)->GetBooleanArrayElements(env, onResult, NULL);
	onResultC[0] = (on? JNI_TRUE : JNI_FALSE);
	(*env)->ReleaseBooleanArrayElements(env, onResult, onResultC, 0 /* 0 means 'copy back changes' */);
	
	return kFMRadioNoError;
}

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    getNativeVolume
 * Signature: (J[I)I
 */
JNIEXPORT jint JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_getNativeVolume
(JNIEnv* env, jobject self, jlong handle, jintArray volumeResult) {
	FMRadio* r = FMRadioFromHandle(handle);
	
	if ((*env)->GetArrayLength(env, volumeResult) != 1)
		return kFMRadioIncorrectArgument;
	
	uint16_t volume;
	FMRadioResult err = FMRadioGetVolume(r, &volume);
	if (err != kFMRadioNoError)
		return err;
	
	jint* volumeC = (*env)->GetIntArrayElements(env, volumeResult, NULL);
	volumeC[0] = (jint) volume;
	(*env)->ReleaseIntArrayElements(env, volumeResult, volumeC, 0 /* 0 means 'copy back changes' */);
	
	return kFMRadioNoError;
}

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    getNativeFrequency
 * Signature: (J[J)I
 */
JNIEXPORT jint JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_getNativeFrequency
(JNIEnv* env, jobject self, jlong handle, jlongArray freqResult) {
	FMRadio* r = FMRadioFromHandle(handle);
	
	if ((*env)->GetArrayLength(env, freqResult) != 1)
		return kFMRadioIncorrectArgument;
	
	uint32_t freq;
	FMRadioResult err = FMRadioGetFrequency(r, &freq);
	if (err != kFMRadioNoError)
		return err;
	
	jlong* freqC = (*env)->GetLongArrayElements(env, freqResult, NULL);
	freqC[0] = (jlong) freq;
	(*env)->ReleaseLongArrayElements(env, freqResult, freqC, 0 /* 0 means 'copy back changes' */);
	
	return kFMRadioNoError;	
}

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    createNativeEventSource
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_createNativeEventSource
(JNIEnv* env, jobject self, jlong radioHandle) {
	
	FMRadio* r = FMRadioFromHandle(radioHandle);
	
	FMRadioRDSEventSource* source;
	FMRadioResult result = FMRadioRDSEventSourceOpen(r, &source);
	if (FMRadioOK(result))
		return (jlong)((intptr_t)source);
	else
		return 0;
	
}

enum {
	kFMRadioRDS_JNI_NoEvent = 0,
	kFMRadioRDS_JNI_ReceivedEvent = 1,
	kFMRadioRDS_JNI_Error = 2,
};

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    waitForNativeTextEvent
 * Signature: (JILjava/lang/StringBuffer;)I
 */
JNIEXPORT jint JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_waitForNativeTextEvent
(JNIEnv* env, jobject self, jlong rdsHandle, jint timeout, jobject stringBuffer) {
	
	FMRadioRDSEventSource* source = FMRadioRDSEventSourceFromHandle(rdsHandle);
	
	FMRadioRDSEventType type; void* eventData;
	FMRadioResult result = FMRadioRDSEventSourceWaitForEventWithTimeout(source, timeout, &type, &eventData);
	
	if (!FMRadioOK(result))
		return kFMRadioRDS_JNI_Error;
	
	if (type != kFMRadioRDSEventText)
		return kFMRadioRDS_JNI_NoEvent;
	else {
		
		jstring theTextAsJavaString = (*env)->NewStringUTF(env, (const char*)eventData);
		if (!theTextAsJavaString)
			return kFMRadioRDS_JNI_Error;
		
		jclass stringBufferClass = (*env)->FindClass(env, "java/lang/StringBuffer");
		jmethodID mid = (*env)->GetMethodID(env, stringBufferClass, "setLength", "(I)V");
		if (!mid)
			return kFMRadioRDS_JNI_Error;
		
		(*env)->CallVoidMethod(env, stringBuffer, mid, (jint) 0);
		
		mid = (*env)->GetMethodID(env, stringBufferClass, "append", "(Ljava/lang/String;)Ljava/lang/StringBuffer;");
		if (!mid)
			return kFMRadioRDS_JNI_Error;
		
		(void) (*env)->CallObjectMethod(env, stringBuffer, mid, (jstring) theTextAsJavaString);
		return kFMRadioRDS_JNI_ReceivedEvent;
		
	}
}

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    closeNativeEventSource
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_closeNativeEventSource
(JNIEnv* env, jobject self, jlong rdsHandle) {
	FMRadioRDSEventSourceClose(FMRadioRDSEventSourceFromHandle(rdsHandle));
}
