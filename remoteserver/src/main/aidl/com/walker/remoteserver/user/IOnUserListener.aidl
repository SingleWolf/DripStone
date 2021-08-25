// IOnUserListener.aidl
package com.walker.remoteserver.user;

interface IOnUserListener {
    void onError(int errCode,String errMsg);
}