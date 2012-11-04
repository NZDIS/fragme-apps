/*
 * Origin.cpp
 *
 * Origin draws a 0.5 long axis along X Y Z dimensions,
 * with RGB colors accordingly.
 *
 *
 * Created on: Aug 19, 2012
 *      Author: mariusz
 */

#include <stdlib.h>
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

#include "Drawable.h"
#include "Origin.h"



Origin::Origin() : Drawable() {
}

Origin::~Origin() {
}


float changingColour = 0.5f;

///
// Initialize the shader and program object
//
void Origin::init(float width, float height)
{
	Drawable::init(width, height);
	char vShaderStr[] =
      "attribute vec4 av4_position;                \n"
	  "attribute vec4 av4_color;                   \n"
	  "uniform mat4 um4_mvp; 	                   \n"
      "varying vec4 color;                         \n"
      "void main()                                 \n"
      "{                                           \n"
      "   gl_Position = um4_mvp * av4_position;    \n"
	  "   color = av4_color;                       \n"
      "}                                           \n";

	char fShaderStr[] =
      "precision mediump float;                    \n"
	  "varying vec4 color;                         \n"
      "void main()                                 \n"
      "{                                           \n"
      "  gl_FragColor = color;                     \n"
      "}                                           \n";

   // show what we have under the bonet
   printGLString("Version", GL_VERSION);
   printGLString("Vendor", GL_VENDOR);
   printGLString("Renderer", GL_RENDERER);
   printGLString("Extensions", GL_EXTENSIONS);

   // Load the shaders and get a linked program object
   userData.programObject = esCreateProgram(vShaderStr, fShaderStr);

   // Get the attribute locations
   userData.positionLoc = glGetAttribLocation(userData.programObject, "av4_position");
   userData.colorLoc = glGetAttribLocation(userData.programObject, "av4_color");

   // Get the uniform locations
   userData.mvpLoc = glGetUniformLocation(userData.programObject, "um4_mvp");
}

void Origin::drawFrame(ESMatrix *perspective) {
	ESMatrix modelview;
	changingColour += 0.01f;
	if (changingColour > 0.9f) changingColour = 0.25f;
	GLfloat vVertices[] = {
			0.0f, 0.0f, 0.0f,
			0.5f, 0.0f, 0.0f,
			0.0f, 0.0f,	0.0f,
			0.0f, 0.5f, 0.0f,
			0.0f, 0.0f, 0.0f,
			0.0f, 0.0f, 0.5f
	};

	GLfloat vColors[] = {
			changingColour, 0.0f, 0.0f, 1.0f,
			changingColour, 0.0f, 0.0f, 1.0f,
			0.0f, changingColour, 0.0f, 1.0f,
			0.0f, changingColour, 0.0f, 1.0f,
			changingColour, changingColour, 0.0f, 1.0f,
			changingColour, changingColour, 0.0f, 1.0f
	};

	// Generate a model view matrix to rotate/translate the cube
	esMatrixLoadIdentity(&modelview);
	userData.angle = 0.0f;
	// Compute the final MVP by multiplying the
	// modevleiw and perspective matrices together

	esMatrixMultiply(&userData.mvpMatrix, &modelview, perspective);

	// use the program object
	glUseProgram(userData.programObject);
	// Load the vertex position
	glVertexAttribPointer(userData.positionLoc, 3, GL_FLOAT, GL_FALSE,
			3 * sizeof(GLfloat), vVertices);
	glEnableVertexAttribArray(userData.positionLoc);
	// load colors
	glVertexAttribPointer(userData.colorLoc, 4, GL_FLOAT, GL_FALSE,
			4 * sizeof(GLfloat), vColors);
	glEnableVertexAttribArray(userData.colorLoc);

	// Load the MVP matrix
	glUniformMatrix4fv(userData.mvpLoc, 1, GL_FALSE,
						(GLfloat*) &userData.mvpMatrix.m[0][0]);

	glDrawArrays(GL_LINES, 0, 6);
}

