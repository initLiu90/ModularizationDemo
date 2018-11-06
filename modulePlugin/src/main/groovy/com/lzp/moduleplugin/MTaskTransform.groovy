package com.lzp.moduleplugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.lzp.annotation.MTask
import javassist.ClassPool

import java.lang.reflect.Field
import java.util.jar.JarFile

class MTaskTransform extends Transform {
    ClassPool pool = ClassPool.getDefault()

    @Override
    String getName() {
        return 'MTaskTransform'
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        def result = [:]
        for (TransformInput input : transformInvocation.getInputs()) {
            input.jarInputs.each { jarInput ->
                File src = jarInput.file
                def cp = pool.insertClassPath(src.absolutePath)
                scanJarFile(src, result)
                pool.removeClassPath(cp)
            }
            input.directoryInputs.each { directoryInput ->
                scanDirectoryInput(directoryInput.file)
            }
        }
    }

    private void scanDirectoryInput(File file) {
        if (file.isDirectory()) {
            file.listFiles().each {
                scanDirectoryInput(it)
            }
        } else {
            println(file.absolutePath)
        }
    }

    private void scanJarFile(def file, def result) {
        def jarFile = new JarFile(file)
        def entries = jarFile.entries()
        while (entries.hasMoreElements()) {
            def entry = entries.nextElement()
            def className = entry.name.substring(entry.name.lastIndexOf("/") + 1, entry.name.length())
            if (className.matches("MT_[\\w]*.class")) {
                def fullClassName = entry.name.substring(0, entry.name.lastIndexOf(".")).replace('/', '.')
                result[fullClassName] = []

                def ctClass = pool.get(fullClassName)
                MTask mtaskAnno = ctClass.getAnnotation(MTask.class)
                if (mtaskAnno != null) {
                    mtaskAnno.dependsOn().each {
                        result[fullClassName].add(it)
                    }
                }
            }
        }
    }
}
