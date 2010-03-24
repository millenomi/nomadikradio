/*
 *  FMTools.c
 *  libNomadikRadio
 *
 */

#include "FMRadio.h"
#include "videodev.h"

#include <stdio.h>
#include <fcntl.h>

#define kFMRadioDefaultBalance (32768)
#define kFMRadioDefaultVolume (8192)

// TODO: Let the user pick this one out (maybe FMRadioOpenFromDeviceFile(...))
static char* const kFMRadioDefaultDeviceFile = "/dev/radio0";

// ==========================
// = Internal, opaque state =
// ==========================

typedef struct {
	
	int FileDescriptor;
	
} FMRadioInternals;

// TONS better than a macro for our lovely autocompleting tools.
static inline FMRadioInternals* FMR(FMRadio* r) __attribute__((always_inline));
static inline FMRadioInternals* FMR(FMRadio* r) {
	return (FMRadioInternals*) r;
}


// We have a single radio on the Nomadik, so we expose a single instance
// of a FMRadio*. This is designed to be changeable.
static bool FMIsOpen = false;
static FMRadioInternals FMDefaultRadio;


// ============================
// = State-grabbing from V4L2 =
// ============================

static bool FMRadioGetAudioState(FMRadio* r, struct video_audio* vaRef) {
	return ioctl(FMR(r)->FileDescriptor, VIDIOCGAUDIO, vaRef) >= 0;
}

static bool FMRadioGetTuningState(FMRadio* r, struct video_tuner* vtRef) {
	return ioctl(FMR(r)->FileDescriptor, VIDIOCGTUNER, vtRef) >= 0;
}

// =======================
// = Opening and closing =
// =======================

FMRadioResult FMRadioOpen(FMRadio** radio) {
	if (!FMIsOpen) {
		int fd = open(kFMRadioDefaultDeviceFile, O_RDONLY);
		if (fd < 0)
			return kFMRadioErrorPOSIX;
		
		FMDefaultRadio.FileDescriptor = fd;
		FMIsOpen = true;
	}
	
	*radio = &FMDefaultRadio;
	return kFMRadioNoError;
}

void FMRadioClose(FMRadio* f) {
	close(FMR(f)->FileDescriptor);
	
	if (f != &FMDefaultRadio) {
		// in case of multiple radios, we'd free() this instance.
		// but we have just one, so TODO in the case we want to refactor for multiple ones.
	} else
		FMIsOpen = false;
}


// ====================
// = Tuning & Setting =
// ====================

FMRadioResult FMRadioSetTurnedOn(FMRadio* r, bool on) {
	struct video_audio va;
	if (!FMRadioGetAudioState(r, &va)) // we init from the current state.
		return kFMRadioErrorPOSIX;
	
	// we mimic the default-setting in fmtools.
	va.balance = kFMRadioDefaultBalance;
	va.audio = 0; // we assume radios only have one channel.
	
	if (on) {
		
		va.volume = kFMRadioDefaultVolume;
		va.flags = 0;
		va.mode = VIDEO_SOUND_STEREO;
		
	} else {
		
		va.volume = 0;
		va.flags = VIDEO_AUDIO_MUTE;
		
	}
	
	return (ioctl(FMR(r)->FileDescriptor, VIDIOCSAUDIO, &va) >= 0)? kFMRadioNoError : kFMRadioErrorPOSIX;
}

FMRadioResult FMRadioSetVolume(FMRadio* r, uint16_t volume) {
	struct video_audio va;
	if (!FMRadioGetAudioState(r, &va))
		return kFMRadioErrorPOSIX;
	
	va.volume = volume;
	return (ioctl(FMR(r)->FileDescriptor, VIDIOCSAUDIO, &va) >= 0)? kFMRadioNoError : kFMRadioErrorPOSIX;
}

FMRadioResult FMRadioSetFrequency(FMRadio* r, uint32_t khz) {
	struct video_tuner vt;
	if (!FMRadioGetTuningState(r, &vt))
		return kFMRadioErrorPOSIX;
	
	// C type rather than exact-sized as this is what VIDIOCSFREQ expects.
	unsigned long frequency = khz;
	if (vt.flags & VIDEO_TUNER_LOW == 0) // "LOW" unset means the radio wants a MHz value.
		frequency /= 1000;
	
	if (frequency < vt.rangelow || frequency > vt.rangehigh)
		return kFMRadioFrequencyOutOfRange;
	
	return (ioctl(FMR(r)->FileDescriptor, VIDIOCSFREQ, &frequency) >= 0)? kFMRadioNoError : kFMRadioErrorPOSIX;
}

FMRadioResult FMRadioGetFrequencyRange(FMRadio* r, uint32_t* min, uint32_t* max) {
	if (!min || !max)
		return kFMRadioIncorrectArgument;
	
	struct video_tuner vt;
	if (!FMRadioGetTuningState(r, &vt))
		return kFMRadioErrorPOSIX;
	
	*min = vt.rangelow;
	*max = vt.rangehigh;
	return kFMRadioNoError;
}
