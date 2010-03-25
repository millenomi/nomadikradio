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

typedef enum {
	kFMRadioNoError = 0, // Everything's fine!
	kFMRadioErrorPOSIX = 1, // Underlying POSIX call failed. See errno.
	kFMRadioFrequencyOutOfRange = 2, // Specified frequency is outside of the radio's range.
	kFMRadioIncorrectArgument = 3, // Arguments are not the way the function wants them to be.
} FMRadioResult;
#define FMRadioOK(x) ((x) == kFMRadioNoError)


// Allocates one radio controller object, opens it and returns it by reference.
// The pointed-to variable is only modified if the function returns with no errors.
extern FMRadioResult FMRadioOpen(FMRadio** radio);
// Closes the radio controller. Pointer may be dangling afterwards.
extern void FMRadioClose(FMRadio* f);

// Turns the radio on and off. The radio needs to be on before any of the other functions can be used to change volume and frequency.
// Frequency will be arbitrary after being turned on. Change it immediately afterwards.
// Volume is always set to an extremely low value (or zero) by this call. Set it to the desired level afterwards after you've tuned the radio on.
extern FMRadioResult FMRadioSetTurnedOn(FMRadio* r, bool on);

// Returns whether the radio is turned on.
extern FMRadioResult FMRadioGetTurnedOn(FMRadio* r, bool* on);

// The minimum and maximum volume.
enum {
	kFMVolumeMute = 0,
	kFMVolumeMaximum = UINT16_MAX,
};

// Sets the volume of the radio.
extern FMRadioResult FMRadioSetVolume(FMRadio* r, uint16_t volume);

// Gets the volume of the radio. The returned value is only meaningful if the radio is turned on.
extern FMRadioResult FMRadioGetVolume(FMRadio* r, uint16_t* volume);

// Sets the frequency (in KHz) the radio is tuned on.
extern FMRadioResult FMRadioSetFrequency(FMRadio* r, uint32_t khz);

// Gets the frequency the radio is tuned on. The returned value is only meaningful if the radio is turned on.
extern FMRadioResult FMRadioGetFrequency(FMRadio* r, uint32_t* khz);

// Gets the frequency range (maximum and minimum, inclusive, in KHz) the radio can be tuned on.
// No argument can be NULL.
extern FMRadioResult FMRadioGetFrequencyRange(FMRadio* r, uint32_t* min, uint32_t* max);

// -------------------------------------------
#endif // #ifndef FMRadio_h_
