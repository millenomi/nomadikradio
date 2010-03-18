/*
 *  FMTools.c
 *  libNomadikRadio
 *
 */


#ifndef FMRadio_h_
#define FMRadio_h_
// -------------------------------------------


#include <stdbool.h>
#include <stdint.h>


// Opaque type
typedef void FMRadio;

// Allocates one radio controller object, opens it and returns it.
extern FMRadio* FMRadioOpen();
// Closes the radio controller. Pointer may be dangling afterwards.
extern void FMRadioClose(FMRadio* f);


/* All the following functions return an error in errno if they fail (return false). */

// Turns the radio on and off. The radio needs to be on before any of the other functions can be used to change volume and frequency.
// Frequency and volume will be arbitrary after being turned on. Change them immediately afterwards.
extern bool FMRadioSetTurnedOn(FMRadio* r, bool on);

// The minimum and maximum volume.
enum {
	kFMVolumeMute = 0,
	kFMVolumeMaximum = UINT16_MAX,
};

// Sets the volume of the radio.
extern bool FMRadioSetVolume(FMRadio* r, uint16_t volume);

// Sets the frequency (in KHz) the radio is tuned on.
extern bool FMRadioSetFrequency(FMRadio* r, uint32_t khz);

// -------------------------------------------
#endif // #ifndef FMRadio_h_
