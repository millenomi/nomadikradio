/*
 *  fm.h
 *  libNomadikRadio
 *
 *  Created by ∞ on 18/03/10.
 *  Copyright 2010 __MyCompanyName__. All rights reserved.
 *
 */

#include <stdbool.h>

typedef void FMRadio;

extern FMRadio* FMRadioOpen(const char* deviceFile);
extern void FMRadioClose(FMRadio* f); // deallocs it too

extern void FMSetTurnedOn(FMRadio* r, bool on);

extern void FMSetFrequency(FMRadio* r, int hz);

enum {
	kFMVolumeMute = 0,
	kFMVolumeMaximum = 65536,
};
extern int FMGetVolume(FMRadio* r);
extern void FMSetVolume(FMRadio* r, int volume);

