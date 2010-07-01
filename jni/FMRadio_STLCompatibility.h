/*
 *  string.h
 *  libNomadikRadio
 *
 *  Created by ∞ on 15/05/10.
 *  Copyright 2010 __MyCompanyName__. All rights reserved.
 *
 */

#ifndef FMRadio_STLCompatibility
#define FMRadio_STLCompatibility 1

#if kFMRadioAllowSTLCompatibility

#include <sys/types.h>
#include <math.h>

#define kILStringMaximumPossibleCharacterIndex (2 ^ sizeof(size_t) - 1)
#define kILStringToEnd (kILStringMaximumPossibleCharacterIndex)

class string {
public:
	~string();
	explicit string();
	string (const string& str);
	string ( const string& str, size_t pos, size_t n = kILStringToEnd );
	string ( const char * s, size_t n );
	string ( const char* cString );
	string ( size_t n, char c );
	
	size_t size() const;
	size_t length() const;
	const char* data() const;
	
	size_t max_size ( ) const;
	
 	void resize ( size_t n, char c = 0 );
	size_t capacity() const;
	
	void reserve ( size_t res_arg=0 );

	void clear();
	
	bool empty() const;
	
	const char& operator[] ( size_t pos ) const;
	char& operator[] ( size_t pos );
	
	const char& at ( size_t pos ) const;
	char& at ( size_t pos );

	string& operator+= ( const string& str );
	string& operator+= ( const char* s );
	string& operator+= ( char c );
	
	string& append ( const string& str );
	string& append ( const string& str, size_t pos, size_t n );
	string& append ( const char* s, size_t n );
	string& append ( const char* s );
	string& append ( size_t n, char c );
	
	void push_back(char c);
	
	string& assign ( const string& str );
	string& assign ( const string& str, size_t pos, size_t n );
	string& assign ( const char* s, size_t n );
	string& assign ( const char* s );
	string& assign ( size_t n, char c );

	string& insert ( size_t pos1, const string& str );
	string& insert ( size_t pos1, const string& str, size_t pos2, size_t n );
	string& insert ( size_t pos1, const char* s, size_t n);
	string& insert ( size_t pos1, const char* s );
	string& insert ( size_t pos1, size_t n, char c );
	
	string& erase ( size_t pos = 0, size_t n = kILStringToEnd );
	
	string& replace ( size_t pos1, size_t n1,   const string& str );	
	string& replace ( size_t pos1, size_t n1, const string& str, size_t pos2, size_t n2 );
	string& replace ( size_t pos1, size_t n1,   const char* s, size_t n2 );
	string& replace ( size_t pos1, size_t n1,   const char* s );	
	string& replace ( size_t pos1, size_t n1,   size_t n2, char c );

	size_t copy ( char* s, size_t n, size_t pos = 0) const;
	
	void swap ( string& str );
	
	const char* c_str ( );
	
private:
	void splice(size_t index, size_t len, const char* replacement, size_t replLen);
	void initializeByCopying( const char* chars, size_t pos, size_t n );
	char* _buffer;
	unsigned long long _size;
	char* _cString;
};

#endif // #if kFMRadioAllowSTLCompatibility

#endif // #ifndef FMRadio_STLCompatibility
