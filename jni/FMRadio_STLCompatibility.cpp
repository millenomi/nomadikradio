/*
 *  string.cpp
 *  libNomadikRadio
 *
 *  Created by ∞ on 15/05/10.
 *  Copyright 2010 __MyCompanyName__. All rights reserved.
 *
 */

#include "FMRadio_STLCompatibility.h"

#if kFMRadioAllowSTLCompatibility

#include <string.h>
#include <stdio.h>
#include <stdlib.h>

string::string() {
	_buffer = NULL;
	_size = 0;
	_cString = NULL;
}

string::string (const string& str) {
	this->initializeByCopying(str.data(), 0, str.size());
	_cString = NULL;
}

string::string ( const string& str, size_t pos, size_t n ) {
	this->initializeByCopying(str.data(), pos, n < str.size()? n : str.size());
	_cString = NULL;
}

string::string ( const char * s, size_t n ) {
	this->initializeByCopying(s, 0, n);
	_cString = NULL;
}

string::string ( const char* cString ) {
	this->initializeByCopying(cString, 0, strlen(cString));
	_cString = NULL;
}

string::~string() {
	if (_buffer)
		free(_buffer);
	
	if (_cString)
		free(_cString);
}

string::string ( size_t n, char c ) {
	_size = n;
	if (_size == 0)
		_buffer = NULL;
	else {
		size_t len = sizeof(char) * _size;
		_buffer = (char*) malloc(len);
		memset(_buffer, (int) c, n);
	}
}

void string::initializeByCopying( const char* chars, size_t pos, size_t n ) {
	_size = n;
	if (_size > 0) {
		size_t len = sizeof(char) * _size;
		_buffer = (char*) malloc(len);
		memcpy(_buffer, (chars + pos), len);
	} else
		_buffer = NULL;
}


size_t string::size() const { return _size; }
size_t string::length() const { return _size; }
const char* string::data() const { return _buffer; }

size_t string::max_size ( ) const { return kILStringMaximumPossibleCharacterIndex; }

void string::resize ( size_t n, char c ) {
	_buffer = (char*) realloc(_buffer, n);
	if (n >= _size)
		memset(_buffer + _size, c, n - _size);
	_size = n;
}

size_t string::capacity ( ) const { return _size; }

void string::reserve ( size_t res_arg ) {}

void string::clear() {
	if (_buffer)
		free(_buffer);
	_size = 0;
}

bool string::empty ( ) const { return _size == 0; }

const char& string::operator[] ( size_t pos ) const {
	return ((const char*)_buffer)[pos];
}

char& string::operator[] ( size_t pos ) {
	return _buffer[pos];
}

const char& string::at ( size_t pos ) const {
	return ((const char*)_buffer)[pos];
}

char& string::at ( size_t pos ) {
	return _buffer[pos];
}

void string::splice(size_t index, size_t len, const char* replacement, size_t replLen) {
	
	if (index + len > _size) {
		fprintf(stderr, "Error: string index or len for splicing out of bounds.");
		abort();
	}
	
	if (_buffer == NULL) {
		if (replLen > 0) {
			_buffer = (char*) malloc(sizeof(char) * replLen);
			memcpy(_buffer, replacement, replLen);
			_size = replLen;
		}
		return;
	}
	
	size_t newTotalSize = _size - len + replLen;
	char* buf = (char*) malloc(sizeof(char) * newTotalSize);
	
	if (index > 0)
		memcpy(buf, _buffer, index);
	if (replLen > 0)
		memcpy(buf + index, replacement, replLen);

	if (newTotalSize > index + replLen)
		memcpy(buf + index + replLen, _buffer + index + len, _size - (index + len));
	
	free(_buffer);
	_buffer = buf;
	_size = newTotalSize;
}

string& string::operator+= ( const string& str ) {
	return this->append(str);
}

string& string::operator+= ( const char* s ) {
	return this->append(s);
}

string& string::operator+= ( char c ) {
	this->push_back(c);
	return *this;
}

string& string::append ( const string& str ) {
	return this->append(str, 0, str.size());
}

string& string::append ( const string& str, size_t pos, size_t n ) {
	this->splice(this->size() - 1, 0, str.data() + pos, n > str.size()? str.size() : n);
	return *this;	
}

string& string::append ( const char* s, size_t n ) {
	this->splice(this->size() - 1, 0, s, n);	
	return *this;
}

string& string::append ( const char* s ) {
	return this->append(s, strlen(s));
}

string& string::append ( size_t n, char c ) {
	char* x = (char*) alloca(n);
	memset(x, (int) c, n);
	return this->append(x, n);
}

void string::push_back(char c) {
	this->splice(this->size() - 1, 0, &c, sizeof(char));
}

string& string::assign ( const char* s, size_t n ) {
	free(_buffer);
	_buffer = (char*) malloc(sizeof(char) * n);
	memcpy(_buffer, s, n);
	_size = n;
	
	return *this;
}

string& string::assign ( const string& str ) {
	return this->assign(str.data(), str.size());
}

string& string::assign ( const string& str, size_t pos, size_t n ) {
	return this->assign(str.data() + pos, n > str.size()? str.size() : n);	
}

string& string::assign ( const char* s ) {
	return this->assign(s, strlen(s));
}

string& string::assign ( size_t n, char c ) {
	char* x = (char*) alloca(n);
	memset(x, (int) c, n);
	return this->assign(x, n);
}

string& string::insert ( size_t pos1, const string& str ) {
	this->splice(pos1, 0, str.data(), str.size());
	return *this;
}

string& string::insert ( size_t pos1, const string& str, size_t pos2, size_t n ) {
	this->splice(pos1, 0, str.data() + pos2, n > str.size()? str.size() : n);
	return *this;
}

string& string::insert ( size_t pos1, const char* s, size_t n) {
	this->splice(pos1, 0, s, n);
	return *this;
}

string& string::insert ( size_t pos1, const char* s ) {
	this->splice(pos1, 0, s, strlen(s));
	return *this;	
}

string& string::insert ( size_t pos1, size_t n, char c ) {
	char* x = (char*) alloca(n);
	memset(x, (int) c, n);
	this->splice(pos1, 0, x, n);
	return *this;	
}

string& string::erase ( size_t pos, size_t n ) {
	this->splice(pos, n, NULL, 0);
	return *this;
}

string& string::replace ( size_t pos1, size_t n1,   const string& str ) {
	this->splice(pos1, n1, str.data(), str.size());
	return *this;
}

string& string::replace ( size_t pos1, size_t n1, const string& str, size_t pos2, size_t n2 ) {
	this->splice(pos1, n1, str.data() + pos2, n2);
	return *this;
}

string& string::replace ( size_t pos1, size_t n1,   const char* s, size_t n2 ) {
	this->splice(pos1, n1, s, n2);
	return *this;
}

string& string::replace ( size_t pos1, size_t n1,   const char* s ) {
	this->splice(pos1, n1, s, strlen(s));
	return *this;	
}

string& string::replace ( size_t pos1, size_t n1,   size_t n2, char c ) {
	char* x = (char*) alloca(n2);
	memset(x, (int) c, n2);
	this->splice(pos1, n1, x, n2);	
	return *this;
}

size_t string::copy ( char* s, size_t n, size_t pos ) const {
	size_t len = pos + n > _size? _size - pos : n;
	memcpy(s, _buffer + pos, len);
	return len;
}

void string::swap ( string& str ) {
	char* buffer = _buffer; size_t size = _size;
	_buffer = str._buffer; _size = str._size;
	str._buffer = buffer; str._size = size;
}

const char* string::c_str ( ) {
	if (_cString)
		free(_cString);
	
	_cString = (char*) malloc(_size + 1);
	memcpy(_buffer, _cString, _size);
	
	const char nul = 0;
	_cString[_size] = nul;
	
	return _cString;
}

#endif // #if kFMRadioAllowSTLCompatibility
