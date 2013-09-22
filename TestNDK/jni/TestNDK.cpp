#include <jni.h>
#include "com_et_testndk_NativeClass.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_et_testndk_NativeClass
 * Method:    getResult
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_et_testndk_NativeClass_getResult
  (JNIEnv *env, jobject jthiz)
{
	return env->NewStringUTF( "Hello from JNI !");
}
#ifdef __cplusplus
}
#endif

