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

// Opaque types
typedef void FMRadio;
typedef void FMRadioRDSEventSource;

typedef enum {
	kFMRadioNoError = 0, // Everything's fine!
	kFMRadioErrorPOSIX = 1, // Underlying POSIX call failed. See errno.
	kFMRadioFrequencyOutOfRange = 2, // Specified frequency is outside of the radio's range.
	kFMRadioIncorrectArgument = 3, // Arguments are not the way the function wants them to be.
	
	kFMRadioRDSUnavailable = 4, // RDS services are unavailable.
} FMRadioResult;
#define FMRadioOK(x) ((x) == kFMRadioNoError)

typedef enum {
	kFMRadioRDSNoEvent = 0, // The RDS event queue has found no events before timing out. You can retry later.
	kFMRadioRDSEventText = 1, // The RDS event queue has found that RDS station identity text has changed. The data parameter is a copy of the new text, owned by you, that must be deallocated using free() when done.
} FMRadioRDSEventType;


// ----------------- REGULAR RADIO STUFF

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


// ----------------- RDS EVENTS

// Allocates one event queue object for the given radio. PLEASE NOTE: Do not allocate one if you don't want to actually receive events, as this requires I/O that can be avoided otherwise.
// When you create an event queue, it becomes bound to the thread it was created on and can only be queried or destroyed on that thread. A radio queue can be created on any thread, and multiple queues can be created concurrently for the same radio instance.
// If kFMRadioNoError is returned, the value pointed to by 'queue' is upon return a handle for the newly created queue.
extern FMRadioResult FMRadioRDSEventSourceOpen(FMRadio* r, FMRadioRDSEventSource** queue);

// Waits for a RDS event to arrive.
// If kFMRadioNoError is returned, then the 'type' parameter will have been set to the type of the returned event. If the event type is kFMRadioRDSNoEvent, then the function timed out before an event arrived.
// The eventData parameter is used to obtain information upon the event. It will be set to a value that you typically will have to free() yourself, depending on the event type returned.
// ---
// EVENT TYPE              EVENT DATA VARIABLE CONTENT [type]
// ------------------------------------------------------------------------------------
// kFMRadioRDSNoEvent ---> (eventData unused)
// kFMRadioRDSEventText -> A NULL-terminated string containing the received text (ISO Latin 1-encoded). [uint8_t*, free() after use.]
// ---
// There is no guaranteed contract for event dropping or coalescing. All event reporting is best-effort.
// If an error is returned, you should destroy the event queue and create another later.
// You can only call this function on the same thread where you created the given event queue.
extern FMRadioResult FMRadioRDSEventSourceWaitForEventWithTimeout(FMRadioRDSEventSource* queue, int timeoutSeconds, FMRadioRDSEventType* type, void** eventData);


// Destroys an event queue, deallocating any resource it uses. As event queues require I/O, you should destroy the queue as soon as you don't need events anymore, and recreate them when you do.
// You can only call this function on the same thread where you created the given event queue.
extern void FMRadioRDSEventSourceClose(FMRadioRDSEventSource* queue);


// -------------------------------------------
#endif // #ifndef FMRadio_h_
