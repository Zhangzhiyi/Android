#include <jni.h>
#include "com_et_testndk_NativeClass.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

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
/*
 * Class:     com_et_testndk_NativeClass
 * Method:    getLine
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_et_testndk_NativeClass_getLine
  (JNIEnv *env, jobject obj, jstring value)
{
	  const char *value_ = env->GetStringUTFChars(value, NULL);
	 printf("get a variable form Java£º%s", value_);
	  env->ReleaseStringUTFChars(value, value_);
	  return env->NewStringUTF("pass a value to C++");
}

