/*
 * Drawable.h
 *
 *  Created on: Aug 27, 2012
 *      Author: mariusz
 */

#ifndef DRAWABLE_H_
#define DRAWABLE_H_

#include "esUtil.h"

class Drawable {

protected:
	ESContext esContext;
	UserData userData;

public:
	Drawable();
	virtual ~Drawable();
	virtual ESContext* getESContext();

	virtual void init(float width, float height);

	virtual void cleanup();
	virtual void drawFrame(ESMatrix*) = 0;
};

#endif /* DRAWABLE_H_ */
