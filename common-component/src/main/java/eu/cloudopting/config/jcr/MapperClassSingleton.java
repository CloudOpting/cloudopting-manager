package eu.cloudopting.config.jcr;

import eu.cloudopting.exception.CommonException;

import java.util.ArrayList;
import java.util.List;

/**
 * singleton for classes used in mapper
 */
public enum MapperClassSingleton {
    INSTANCE;

    private List<Class> classes = new ArrayList<>();

    public void registerClass(Class clazz) {
        if (!classes.contains(clazz)) {
            classes.add(clazz);
        }
    }

    public List<Class> getClasses() {
        if(classes.size()==0){
            throw new CommonException("You need to register some classes to be managed by JacrabbitOCM");
        }
        return classes;
    }
}
