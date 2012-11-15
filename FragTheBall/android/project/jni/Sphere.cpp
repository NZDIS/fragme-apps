/*
 * Sphere.cpp
 *
 * Sphere draws a sphere in the scene.
 *
 *
 * Created on: Aug 19, 2012
 *      Author: mariusz
 */

#include <stdlib.h>
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

#include "Sphere.h"
#include "esUtil.h"


Sphere::Sphere() : Drawable() {
	positionX = 0.5f;
	positionY = 0.5f;
	positionZ = 0.0f;
	myself = false;
}

Sphere::~Sphere() {
}


///
// Initialize the shader and program object
//
void Sphere::init(float width, float height)
{
   Drawable::init(width, height);
   /*char vShaderStr[] =
      "attribute vec4 av4_position;                \n"
	   "uniform mat4 um4_mvp;                       \n"
      "void main()                                 \n"
      "{                                           \n"
      "   gl_Position = um4_mvp * av4_position;    \n"
      "}                                           \n";
   */

   char vShaderStr[] =
    "uniform mat4 um4_MVPMatrix;      \n"     // A constant representing the combined model/view/projection matrix.
    "uniform mat4 um4_MVMatrix;       \n"     // A constant representing the combined model/view matrix.
    "uniform vec3 uv3_LightPos;       \n"     // The position of the light in eye space.
	"uniform vec4 uv4_Color;          \n"
 
    "attribute vec4 av4_Position;     \n"     // Per-vertex position information we will pass in.
    "attribute vec3 av3_Normal;       \n"     // Per-vertex normal information we will pass in.
 
    "varying vec4 v4_Color;          \n"     // This will be passed into the fragment shader.
 
    "void main()                    \n"     // The entry point for our vertex shader.
    "{                              \n"
// Transform the vertex into eye space.
    "   vec3 modelViewVertex = vec3(um4_MVMatrix * av4_Position);          \n"
// Transform the normal's orientation into eye space.
    "   vec3 modelViewNormal = vec3(um4_MVMatrix * vec4(av3_Normal, 0.0)); \n"
// Will be used for attenuation.
    "   float distance = length(uv3_LightPos - modelViewVertex);           \n"
// Get a lighting direction vector from the light to the vertex.
    "   vec3 lightVector = normalize(uv3_LightPos - modelViewVertex);      \n"
// Calculate the dot product of the light vector and vertex normal. 
//  If the normal and light vector are
// pointing in the same direction then it will get max illumination.
    "   float diffuse = max(dot(modelViewNormal, lightVector), 0.1);       \n"
// Attenuate the light based on distance.
    "   diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));  \n"
// Multiply the color by the illumination level. It will be interpolated across the triangle.
	  " v4_Color = uv4_Color * diffuse; \n"
// gl_Position is a special variable used to store the final position.
// Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
    "   gl_Position = um4_MVPMatrix * av4_Position;                        \n"
    "}                                                                     \n";



   char fShaderStr[] =
      "precision mediump float;          \n"
	  "varying vec4 v4_Color;            \n"
      "void main()                       \n"
      "{                                 \n"
      "  gl_FragColor = v4_Color;        \n"
      "}                                 \n";


   esContext.width = width;
   esContext.height = height;

   // Load the shaders and get a linked program object
   userData.programObject = esCreateProgram(vShaderStr, fShaderStr);
   userData.positionLoc = glGetAttribLocation(userData.programObject, "av4_Position");
   userData.normalsLoc = glGetAttribLocation(userData.programObject, "av3_Normal");

   userData.colorLoc = glGetUniformLocation(userData.programObject, "uv4_Color");
   userData.mvpLoc = glGetUniformLocation(userData.programObject, "um4_MVPMatrix");
   userData.mvLoc = glGetUniformLocation(userData.programObject, "um4_MVMatrix");
   userData.lightPosLoc = glGetUniformLocation(userData.programObject, "uv3_LightPos");

   userData.numIndices = esGenSphere(30, 0.2f, &userData.vertices,
                                        &userData.normals, NULL, &userData.indices);
}

void Sphere::drawFrame(ESMatrix* perspective, float x, float y, float z, bool m) {
	// Stretch the data to the scale of the gui
	positionX = x * 4.0f - 2.0f;
	positionY = -(y * 4.0f - 2.0f);
	positionZ = -2.0f;
	myself = m;
	drawFrame(perspective);
}

void Sphere::drawFrame(ESMatrix* perspective) {
   GLfloat vMyColor[] = { 0.0f, 0.0f, 5.0f, 1.0f };
   GLfloat vOtherColor[] = { 5.0f, 0.0f, 0.0f, 1.0f };
   GLfloat vLightPos[] = { 0.0f, 0.0f, 2.3f };

	ESMatrix modelview;
   // Use culling to remove back faces.
   // glEnable(GL_CULL_FACE);
   // Enable depth testing
   glEnable(GL_DEPTH_TEST);

	esMatrixLoadIdentity(&modelview);
	esTranslate(&modelview, positionX, positionY, positionZ);
	//LOGI("%f %d %d", positionX, positionY, positionZ);

	// Compute the final MVP by multiplying the
	// modelview and perspective matrices together
	esMatrixMultiply(&userData.mvpMatrix, &modelview, perspective);
	// use the program object
	glUseProgram(userData.programObject);
	// Load the vertex position
	glVertexAttribPointer(userData.positionLoc, 3, GL_FLOAT, GL_FALSE,
			3 * sizeof(GLfloat), userData.vertices);
	glEnableVertexAttribArray(userData.positionLoc);
   // Load the normals
   glVertexAttribPointer(userData.normalsLoc, 3, GL_FLOAT, GL_FALSE,
         3 * sizeof(GLfloat), userData.normals);
   glEnableVertexAttribArray(userData.normalsLoc);

   // load color
   if (myself) {
	   glUniform4fv(userData.colorLoc, 1, (GLfloat *) vMyColor);
   } else {
	   glUniform4fv(userData.colorLoc, 1, (GLfloat *) vOtherColor);
   }
   glUniform3fv(userData.lightPosLoc, 1, (GLfloat*) vLightPos);
   // Load the MVP matrix
   glUniformMatrix4fv(userData.mvpLoc, 1, GL_FALSE,
			(GLfloat*) &userData.mvpMatrix.m[0][0]);
   // Load the MV matrix
   glUniformMatrix4fv(userData.mvLoc, 1, GL_FALSE,
         (GLfloat*) &modelview.m[0][0]);

   //LOGI("drawFrame drawing, number of indices: %d", userData.numIndices);
   // Draw a sphere
   glDrawElements(GL_TRIANGLE_STRIP, userData.numIndices, GL_UNSIGNED_INT, userData.indices);
}


