
#ifndef _Terrain_h
#define _Terrain_h


#include <iostream>
#include <fstream>
#include <math.h>
#include <stdlib.h>

#include "esUtil.h"
#include "Drawable.h"

using namespace std;

#define MAP_X	32
#define MAP_Z	32
#define MAP_SCALE	4.0f
#define MAP_HEIGHT_SCALE	3.0f



class Terrain : public Drawable
{

	unsigned char* imageData;	   // the map image data
	unsigned char* landTexture;	   // land texture data
	unsigned int land;			   // the land texture object

	////// Terrain Data
	float terrain[MAP_X][MAP_Z][3];		// heightfield terrain data (0-255); 256x256
	float terrainColors[MAP_X][MAP_Z][4];		// terrain colors


public:
	Terrain();

	virtual void init(float width, float height);
	virtual void drawFrame(ESMatrix *);
	virtual void cleanup();

	void readHeightImage();

};

#endif
