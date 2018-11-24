package com.lzp.moduleplugin

import com.android.SdkConstants
import com.android.build.api.transform.TransformInvocation
import javassist.ClassPool
import org.apache.commons.io.FileUtils

import java.util.jar.JarFile
import java.util.regex.Matcher

class TransformUtil {
    static getClasses(TransformInvocation transformInvocation, ClassPool pool) {
        def fullCNames = []
        transformInvocation.inputs.each { input ->
            input.jarInputs.each { jarInput ->
                pool.insertClassPath(jarInput.file.absolutePath)

                def jarFile = new JarFile(jarInput.file)
                def enumeration = jarFile.entries()
                while (enumeration.hasMoreElements()) {
                    def jarEntry = enumeration.nextElement()
                    def className = jarEntry.name
                    if (className.endsWith(SdkConstants.DOT_CLASS)) {
                        className = className.substring(0, className.length() - SdkConstants.DOT_CLASS.length()).replace('/', '.')
                        if (fullCNames.contains(className)) {
                            throw RuntimeException("You have duplicate classes with the same name : ${className} please remove duplicate classes")
                        }
//                        println('className1=' + className)
                        fullCNames.add(className)
                    }
                }
            }

            input.directoryInputs.each { directoryInput ->
                def dirPath = directoryInput.file.absolutePath
//                println('dirPath=' + dirPath)
                pool.insertClassPath(directoryInput.file.absolutePath)
                FileUtils.listFiles(directoryInput.file, null, true).each { file ->
                    if (file.absolutePath.endsWith(SdkConstants.DOT_CLASS)) {
//                        println('filePath=' + file.absolutePath)
                        def className = file.absolutePath.substring(dirPath.length() + 1, file.absolutePath.length() - SdkConstants.DOT_CLASS.length()).replaceAll(Matcher.quoteReplacement(File.separator), '.')
                        if (fullCNames.contains(className)) {
                            throw RuntimeException("You have duplicate classes with the same name : ${className} please remove duplicate classes")
                        }
//                        println('className2=' + className)
                        fullCNames.add(className)
                    }
                }
            }
        }
        return fullCNames
    }
}
