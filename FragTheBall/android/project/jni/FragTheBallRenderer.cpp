
#include <jni.h>
#include <GLES2/gl2.h>
#include "FragTheBallRenderer.h"
#include "esUtil.h"
#include "Origin.h"
#include "Sphere.h"


Origin origin;
Sphere sphere;

ESMatrix perspective;
float aspect;

JNIEXPORT void JNICALL Java_org_nzdis_fragtheball_GLESView_myCleanup
(JNIEnv *env, jclass c)
{
	origin.cleanup();
	sphere.cleanup();
}

JNIEXPORT void JNICALL Java_org_nzdis_fragtheball_GLESView_myDrawFrame
  (JNIEnv *env, jclass c, jfloat x, jfloat y, jfloat z)
{
	glClear(GL_COLOR_BUFFER_BIT);
	glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
	sphere.drawFrame(&perspective, x, y, z);
	origin.drawFrame(&perspective);
}


JNIEXPORT void JNICALL Java_org_nzdis_fragtheball_GLESView_mySurfaceChanged
(JNIEnv *env, jclass c, jint width, jint height)
{
	glViewport(0, 0, width, height);
	aspect = (float)width / (float)height;
	// Generate a perspective matrix with a 60 degree FOV
	esMatrixLoadIdentity(&perspective);
    //LOGI("%f %d %d", aspect, width, height);
	esPerspective(&perspective, 60.0f, aspect, 1.0f, 30.0f);
	esTranslate(&perspective, 0.0f, 0.0f, -5.0f);
	//esRotate(&perspective, 45.0f, 1.0f, 0.0f, 0.0f);
	//esRotate(&perspective, -5.0f, 0.0f, 1.0f, 0.0f);

	origin.init(width, height);
	sphere.init(width, height);
}


JNIEXPORT void JNICALL Java_org_nzdis_fragtheball_GLESView_mySurfaceCreated
(JNIEnv *e, jclass c)
{
}
