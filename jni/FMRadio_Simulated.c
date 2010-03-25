/*
 *  FMRadio_Simulated.c
 *  libNomadikRadio
 *
 *  Created by ∞ on 19/03/10.
 *  Copyright 2010 __MyCompanyName__. All rights reserved.
 *
 */


#include "FMRadio.h"

#include <android/log.h>
#include <stdlib.h>

#define FMLogString(x) __android_log_write(ANDROID_LOG_DEBUG, "FMRadio_Simulated", x)
#define FMLog(x, ...) __android_log_print(ANDROID_LOG_DEBUG, "FMRadio_Simulated", x , ## __VA_ARGS__)

FMRadioResult FMRadioOpen(FMRadio** radio) {
	void* uniquePointer = malloc(sizeof(uint8_t));
	FMLog("Opened radio with unique pointer %p", uniquePointer);
	
	*radio = uniquePointer;
	return kFMRadioNoError;
}

FMRadioResult FMRadioSetTurnedOn(FMRadio* r, bool on) {
	FMLog("Set radio with unique pointer %p on state to %d", r, (int) on);
	return kFMRadioNoError;
}

void FMRadioClose(FMRadio* r) {
	FMLog("Closed radio with unique pointer %p", r);
	free(r); // alloc'd in FMRadioOpen
}

FMRadioResult FMRadioSetVolume(FMRadio* r, uint16_t volume) {
	FMLog("Set radio volume to %d", (int) volume);
	return kFMRadioNoError;
}

FMRadioResult FMRadioSetFrequency(FMRadio* r, uint32_t khz) {
	FMLog("Set radio frequency to %ld KHz", (long) khz);
	return kFMRadioNoError;
}

FMRadioResult FMRadioGetFrequencyRange(FMRadio* r, uint32_t* min, uint32_t* max) {
	if (!min || !max)
		return kFMRadioIncorrectArgument;

	*min = 80000;
	*max = 110000;
	FMLog("Will return frequency range KHz %d-%d", (int) *min, (int) *max);
	return kFMRadioNoError;
}