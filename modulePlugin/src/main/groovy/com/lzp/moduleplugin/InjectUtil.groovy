package com.lzp.moduleplugin

import javassist.ClassPool

import java.util.jar.JarFile

class InjectUtil {
    static ClassPool pool = ClassPool.getDefault()

    void injectJar(File file) {
        def jarFile = new JarFile(jarFile)
        def entries = jarFile.entries()
        while (entries.hasMoreElements()) {
            def element = entries.nextElement()
            def is = jarFile.getInputStream(element)
            def ctClass = pool.makeClass(is)
            if(containMTaskAnno(ctClass.getAnnotations())){
            }
        }
    }

    private boolean containMTaskAnno(Object[] annos) {
        if (annos == null || annos.length == 0) return false
        for (java.lang.Object obj : annos) {
            if (obj.getClass().getSimpleName().equals("MTask")) {
                return true
            }
        }
        return false
    }
}
