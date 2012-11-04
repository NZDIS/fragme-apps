
#include <iostream>
#include <GLES2/gl2.h>

#include "Terrain.h"
#include "esUtil.h"


// InitializeTerrain()
// desc: initializes the heightfield terrain data
Terrain::Terrain()
{
	readHeightImage();
	// loop through all of the heightfield points, calculating
	// the coordinates for each point
	for (int z = 0; z < MAP_Z; z++)
	{
		for (int x = 0; x < MAP_X; x++)
		{
			terrain[x][z][0] = (float(x) / MAP_X) * MAP_SCALE;
			terrain[x][z][1] = ((float) imageData[(z * MAP_Z + x)] / 256) * MAP_HEIGHT_SCALE;
			terrain[x][z][2] = -(float(z) / MAP_Z) * MAP_SCALE;

			terrainColors[x][z][0] = 0.5f;
			terrainColors[x][z][1] = 0.5f;
			terrainColors[x][z][2] = 0.0f;
			terrainColors[x][z][3] = 1.0f;
		}
	}
}

void Terrain::cleanup()
{
	free(imageData);
	//free(landTexture);
}

// Initialize
void Terrain::init(float width, float height)
{
	/*
	glShadeModel(GL_SMOOTH);	   // use smooth shading
	glEnable(GL_DEPTH_TEST);	   // hidden surface removal
	glEnable(GL_CULL_FACE);		   // do not calculate inside of poly's
	glFrontFace(GL_CCW);    	   // counter clock-wise polygons are out
	glEnable(GL_TEXTURE_2D);	   // enable 2D texturing
	*/
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

   // Load the shaders and get a linked program object
   userData.programObject = esCreateProgram(vShaderStr, fShaderStr);
   // Get the attribute locations
   userData.positionLoc = glGetAttribLocation(userData.programObject, "av4_position");
   userData.colorLoc = glGetAttribLocation(userData.programObject, "av4_color");
   // Get the uniform locations
   userData.mvpLoc = glGetUniformLocation(userData.programObject, "um4_mvp");
}



void Terrain::readHeightImage() {
	imageData = new unsigned char[MAP_X * MAP_Z * sizeof(char)];
	for (int z = 0; z < MAP_Z; z++)	{
		for (int x = 0; x < MAP_X; x++)	{
			imageData[z * MAP_Z + x] = 0;
		}
	}

	imageData[(MAP_Z / 2)*MAP_Z + (MAP_X / 2)] = 255;

	// prepare indices
	int numStripsRequired = MAP_Z - 1;
	int numDegensRequired = 2 * (numStripsRequired - 1);
	int verticesPerStrip = 2 * MAP_X;

	userData.numIndices = (verticesPerStrip * numStripsRequired) + numDegensRequired;
	userData.indices = new unsigned int[userData.numIndices];

	int offset = 0;

	for (int y = 0; y < MAP_Z - 1; y++) {
		if (y > 0) {
	        // Degenerate begin: repeat first vertex
	        userData.indices[offset++] = (int) (y * MAP_Z);
	    }

	    for (int x = 0; x < MAP_X; x++) {
	        // One part of the strip
	        userData.indices[offset++] = (int) ((y * MAP_Z) + x);
	        userData.indices[offset++] = (int) (((y + 1) * MAP_Z) + x);
	    }

	    if (y < MAP_Z - 2) {
	        // Degenerate end: repeat last vertex
	        userData.indices[offset++] = (int) (((y + 1) * MAP_Z) + (MAP_X - 1));
	    }
	}
}


void Terrain::drawFrame(ESMatrix *perspective) {
	ESMatrix modelview;

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
			3 * sizeof(GLfloat), terrain);
	glEnableVertexAttribArray(userData.positionLoc);
	// load colors
	glVertexAttribPointer(userData.colorLoc, 4, GL_FLOAT, GL_FALSE,
			4 * sizeof(GLfloat), terrainColors);
	glEnableVertexAttribArray(userData.colorLoc);

	// Load the MVP matrix
	glUniformMatrix4fv(userData.mvpLoc, 1, GL_FALSE,
						(GLfloat*) &userData.mvpMatrix.m[0][0]);

	glDrawElements(GL_TRIANGLE_STRIP, userData.numIndices, GL_UNSIGNED_INT, userData.indices);
}


