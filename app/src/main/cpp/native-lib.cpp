#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_com_om_tat_sat_brightbirth_Birth_data_getEncryptedKey(JNIEnv *env, jobject object) {
    std::string encrypted_key = "This is encrypted key";
    return env->NewStringUTF(encrypted_key.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_om_1tat_1sat_brightbirth_Birth_1data_getEncryptedKey(JNIEnv *env, jobject thiz) {
    std::string encrypted_key = "AIzaSyAYT0bN8a2zxCkrVdOV0NRGxY18fPzWqzw";
    return env->NewStringUTF(encrypted_key.c_str());
}