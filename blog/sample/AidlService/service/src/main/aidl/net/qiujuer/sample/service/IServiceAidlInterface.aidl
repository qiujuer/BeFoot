// IServiceAidlInterface.aidl
package net.qiujuer.sample.service;
import net.qiujuer.sample.service.bean.User;


// Declare any non-default types here with import statements

interface IServiceAidlInterface {
    void addAge();
    void setName(String name);
    User getUser();
}
