/*
 * Util.cpp
 *
 * OpenGL ES utilities.
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <sys/time.h>

#include <GLES2/gl2.h>
//#include <GLES2/gl2ext.h>
#include <jni.h>
#include <android/sensor.h>
#include <android/looper.h>
#include <android/log.h>

#include "esUtil.h"


///
// Initialize ES utility context.  This must be called before
// calling any other functions.
void ESUTIL_API esInitContext ( ESContext *esContext )
{
   if ( esContext != NULL )
   {
      memset( esContext, 0, sizeof( ESContext) );
   }
}



GLuint esLoadShader(GLenum shader_type, const char* src)
{
  GLint succeeded;
  GLuint sid;

  sid = glCreateShader(shader_type);
  glShaderSource(sid, 1, (const GLchar **) &src, NULL);
  glCompileShader(sid);
  glGetShaderiv(sid, GL_COMPILE_STATUS, &succeeded);

  GLint log_length;
  glGetShaderiv(sid, GL_INFO_LOG_LENGTH, &log_length);

  if (log_length > 1) {
    char *log =  malloc(sizeof(char) * log_length);
    GLint chars_written;
    glGetShaderInfoLog(sid, log_length, &chars_written, log);
    checkGLError(" *********    Error esLoadShader: Loading and compiling a shader.");
    free(log);
  }
  checkGLError(" *********** Error esLoadShader: Something went wrong with the shader.");
  return sid;
}


GLuint esCreateProgram(const char* pVertexSource, const char* pFragmentSource) {
	GLuint vertexShader;
	GLuint fragmentShader;
	GLuint programObject;
	GLint linked;

	// Load the vertex/fragment shaders
	vertexShader = esLoadShader(GL_VERTEX_SHADER, pVertexSource);
	if (vertexShader == 0)
		return 0;
	fragmentShader = esLoadShader(GL_FRAGMENT_SHADER, pFragmentSource);
	if (fragmentShader == 0) {
		glDeleteShader(vertexShader);
		return 0;
	}

	// Create the program object
	programObject = glCreateProgram();

	if (programObject == 0)
		return 0;

	glAttachShader(programObject, vertexShader);
	glAttachShader(programObject, fragmentShader);

	// Link the program
	glLinkProgram(programObject);

	// Check the link status
	glGetProgramiv(programObject, GL_LINK_STATUS, &linked);

	if (!linked) {
		GLint infoLen = 0;

		glGetProgramiv(programObject, GL_INFO_LOG_LENGTH, &infoLen);

		if (infoLen > 1) {
			char* infoLog = malloc(sizeof(char) * infoLen);

			glGetProgramInfoLog(programObject, infoLen, NULL, infoLog);
			checkGLError(infoLog);

			free(infoLog);
		}

		glDeleteProgram(programObject);
		return 0;
	}

	// Free up no longer needed shader resources
	glDeleteShader(vertexShader);
	glDeleteShader(fragmentShader);

	return programObject;
}


void printGLString(const char *name, GLenum s)
{
    const char *v = (const char *) glGetString(s);
    LOGI(" **********    GL %s = %s\n", name, v);
}


void checkGLError(const char* op)
{
	GLint error;
    for (error = glGetError(); error; error = glGetError())
    {
        LOGI(" **********    after %s() glError (0x%x)\n", op, error);
    }
}
/*
// normalize a passed in vector
void esNormalize(float v[3])
{
	GLfloat d = sqrt(float(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]));
	if (d == 0.0f)
	{
		// ERROR
		return;
	}
	v[0] /= d;
	v[1] /= d;
	v[2] /= d;
}

// normalized cross product of two vectors
void esNorm_crossprod(float v1[3], float v2[3], float out[3])
{
	out[0] = v1[1]*v2[2] - v1[2]*v2[1];
	out[1] = v1[2]*v2[0] - v1[0]*v2[2];
	out[2] = v1[0]*v2[1] - v1[1]*v2[0];
	esNormalize(out);
}*/

///
// esLoadTGA()
//
//    Loads a 24-bit TGA image from a file. This is probably the simplest TGA loader ever.
//    Does not support loading of compressed TGAs nor TGAa with alpha channel. But for the
//    sake of the examples, this is sufficient.
//

char* ESUTIL_API esLoadTGA ( char *fileName, int *width, int *height )
{
    char *buffer = NULL;
    FILE *f;
    unsigned char tgaheader[12];
    unsigned char attributes[6];
    unsigned int imagesize;

    f = fopen(fileName, "rb");
    if(f == NULL) return NULL;

    if(fread(&tgaheader, sizeof(tgaheader), 1, f) == 0)
    {
        fclose(f);
        return NULL;
    }

    if(fread(attributes, sizeof(attributes), 1, f) == 0)
    {
        fclose(f);
        return 0;
    }

    *width = attributes[1] * 256 + attributes[0];
    *height = attributes[3] * 256 + attributes[2];
    imagesize = attributes[4] / 8 * *width * *height;
    buffer = malloc(imagesize);
    if (buffer == NULL)
    {
        fclose(f);
        return 0;
    }

    if(fread(buffer, 1, imagesize, f) != imagesize)
    {
        free(buffer);
        return NULL;
    }
    fclose(f);
    return buffer;
}


