#include <jni.h>

extern "C" {
    JNIEXPORT jobjectArray JNICALL
    Java_com_ggditpanel_FloatingService_getFeatureList(JNIEnv* env, jobject)
    {
        const char* features[] ={
                "1_ButtonOnOff_EXAMPLE"
        };

        int total = sizeof(features) / sizeof(features[0]);

        jobjectArray arr = env->NewObjectArray(total, env->FindClass("java/lang/String"), env->NewStringUTF(""));

        for (int i = 0; i < total; i++)
        {
            env->SetObjectArrayElement(arr, i, env->NewStringUTF(features[i]));
        }

        return arr;
    }

    JNIEXPORT void JNICALL
    Java_com_ggditpanel_FloatingService_Preferences_1Changes(JNIEnv *env, jclass clazz, jint featNum, jstring featName, jint value, jboolean boolean, jstring str)
    {
        switch (featNum)
        {
            case 0:
                if (boolean) {

                } else {

                }
            break;
        }
    }

    JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved)
    {
        return JNI_VERSION_1_6;
    }
}