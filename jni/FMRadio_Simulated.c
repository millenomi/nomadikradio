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

typedef struct {
	bool On;
	uint16_t Volume;
	uint32_t Frequency;
} FMSimulatedRadioInternals;
static inline FMSimulatedRadioInternals* FMR(FMRadio* r) __attribute__((always_inline));
static inline FMSimulatedRadioInternals* FMR(FMRadio* r) {
	return (FMSimulatedRadioInternals*) r;
}


FMRadioResult FMRadioOpen(FMRadio** radio) {
	void* uniquePointer = calloc(sizeof(FMSimulatedRadioInternals), 1);
	FMLog("Opened radio with unique pointer %p", uniquePointer);
	
	*radio = uniquePointer;
	return kFMRadioNoError;
}

void FMRadioClose(FMRadio* r) {
	FMLog("Closed radio with unique pointer %p", r);
	free(r); // alloc'd in FMRadioOpen
}

FMRadioResult FMRadioSetTurnedOn(FMRadio* r, bool on) {
	if (FMR(r)->On != on) {
		FMR(r)->On = on;
		if (on) {
			FMR(r)->Volume = 1;
			FMR(r)->Frequency = 92345;
		}
	}
	
	FMLog("Set radio with unique pointer %p on state to %d", r, (int) on);
	return kFMRadioNoError;
}

FMRadioResult FMRadioGetTurnedOn(FMRadio* r, bool* on) {
	*on = FMR(r)->On;
	return kFMRadioNoError;
}

FMRadioResult FMRadioGetVolume(FMRadio* r, uint16_t* volume) {
	*volume = FMR(r)->Volume;
	return kFMRadioNoError;
}

FMRadioResult FMRadioSetVolume(FMRadio* r, uint16_t volume) {
	FMLog("Set radio volume to %d", (int) volume);
	FMR(r)->Volume = volume;
	return kFMRadioNoError;
}

FMRadioResult FMRadioGetFrequency(FMRadio* r, uint32_t* khz) {
	*khz = FMR(r)->Frequency;
	return kFMRadioNoError;
}

FMRadioResult FMRadioSetFrequency(FMRadio* r, uint32_t khz) {
	if (khz < 80000 || khz > 110000)
		return kFMRadioFrequencyOutOfRange;
	
	FMLog("Set radio frequency to %ld KHz", (long) khz);
	FMR(r)->Frequency = khz;
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
