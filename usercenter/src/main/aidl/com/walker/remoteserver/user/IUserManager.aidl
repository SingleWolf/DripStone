// IUserManager.aidl
package com.walker.remoteserver.user;

import com.walker.remoteserver.user.IOnUserListener;
import com.walker.remoteserver.bean.UserInfo;

interface IUserManager {
   void onRegister(in UserInfo userinfo);
   UserInfo onLogin(String loginName,String password);
   void onLogout();
   void setOnUserLtener(IOnUserListener listener);
   void removeUserLtener(IOnUserListener listener);
}