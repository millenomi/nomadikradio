
#include "it_polimi_elet_se_nomadikradio_Radio_Native.h"
#include "FMRadio.h"

#include <sys/types.h>

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
		return rself = (intptr_t) r;
	else
		return rself = 0;
	
	return (jlong) rself;
}

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_close
(JNIEnv* env, jobject self, jlong handle) {
	intptr_t r = handle;
	FMRadioClose((FMRadio*)r);
}

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    setNativeTurnedOn
 * Signature: (Z)I
 */
JNIEXPORT jint JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_setNativeTurnedOn
(JNIEnv* env, jobject self, jlong handle, jboolean on) {
	intptr_t r = handle;
	return (jint) FMRadioSetTurnedOn((FMRadio*)r, on == JNI_TRUE);
}

/*
 * Class:     it_polimi_elet_se_nomadikradio_Radio_Native
 * Method:    setNativeVolume
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_it_polimi_elet_se_nomadikradio_Radio_1Native_setNativeVolume
(JNIEnv* env, jobject self, jlong handle, jint volume) {
	intptr_t r = handle;
	if (volume > kFMVolumeMaximum)
		volume = kFMVolumeMaximum;
	else if (volume < kFMVolumeMute)
		volume = kFMVolumeMute;
	
	return (jint) FMRadioSetVolume((FMRadio*)r, volume);
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
	
	intptr_t r = handle;
	return (jint) FMRadioSetFrequency((FMRadio*)r, khz);
}
