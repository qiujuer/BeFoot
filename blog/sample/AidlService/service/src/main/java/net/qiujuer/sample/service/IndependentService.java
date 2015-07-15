package net.qiujuer.sample.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import net.qiujuer.sample.service.bean.User;

public class IndependentService extends Service {
    private ServiceBinder mBinder;

    public IndependentService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (mBinder == null)
            mBinder = new ServiceBinder();
        return mBinder;
    }

    class ServiceBinder extends IServiceAidlInterface.Stub {
        private User user;

        public ServiceBinder() {
            user = new User(21, "XiaoMing");
        }

        @Override
        public void addAge() throws RemoteException {
            user.setAge(user.getAge() + 1);
        }

        @Override
        public void setName(String name) throws RemoteException {
            user.setName(name);
        }

        @Override
        public User getUser() throws RemoteException {
            return user;
        }
    }

}
