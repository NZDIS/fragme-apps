/**
 * Drawable.cpp
 *
 * Abstract base class for all drawables.
 *
 * Created on: Aug 27, 2012
 *      Author: mariusz
 */

#include <stdlib.h>
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

#include "Drawable.h"



Drawable::Drawable() {
	esInitContext(&esContext);
	esContext.userData = &userData;
}

Drawable::~Drawable() {
}


///
// Initialize the shader and program object
//
void Drawable::init(float width, float height)
{
   esContext.width = width;
   esContext.height = height;
}

ESContext* Drawable::getESContext()
{
	return &esContext;
}

void Drawable::cleanup() {
	UserData *userData = esContext.userData;

	if (userData->vertices != NULL) {
		free(userData->vertices);
	}

	if (userData->indices != NULL) {
		free(userData->indices);
	}

	// Delete program object
	glDeleteProgram(userData->programObject);
}
