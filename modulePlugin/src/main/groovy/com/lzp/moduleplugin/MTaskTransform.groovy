package com.lzp.moduleplugin

import com.android.SdkConstants
import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

class MTaskTransform extends Transform {
    private ClassPool pool = ClassPool.getDefault()
    private Project project

    private MTaskTransform(Project project) {
        this.project = project
    }

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
        //把android.jar中类加载进来
        project.android.bootClasspath.each {
            pool.appendClassPath((String) it.absolutePath)
        }
        //get Application
        def enter = project.extensions.getByType(MExtension).enter
        def fullCNames = TransformUtil.getClasses(transformInvocation, pool)

        def mTasks = []
        fullCNames.each { cname ->
            CtClass cc = pool.get(cname)
            if (isMTask(cc)) {//如果是MTask
                mTasks.add(cc)
            }
        }

        transformInvocation.inputs.each { input ->
            input.jarInputs.each { jarInput ->
                def src = jarInput.file
                def dst = transformInvocation.outputProvider.getContentLocation(jarInput.name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtils.copyFile(src, dst)
            }
            input.directoryInputs.each { directoryInput ->
                def src = directoryInput.file
                def dst = transformInvocation.outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)

                def dir = src.absolutePath
                src.eachFileRecurse { file ->
                    def filePath = file.absolutePath
//                    println('filePath=' + filePath)
                    if (filePath.endsWith(SdkConstants.DOT_CLASS)) {
                        filePath = filePath.substring(dir.length() + 1, filePath.length() - SdkConstants.DOT_CLASS.length())
                        def fullClzName = filePath.replace('\\', '.').replace('/', '.')
                        if (fullClzName.equals(enter)) {
//                            println('fullClzName=' + fullClzName)
                            injectEnter(mTasks, enter, directoryInput.file.absolutePath)
                        }
                    }
                }

                FileUtils.copyDirectory(src, dst)
            }
        }
    }

    private boolean isMTask(CtClass ctClass) {
        if (ctClass.getSuperclass().name.equals("com.lzp.core.mtask.AbsMTask")) {
            return true
        }
        return false
    }

    private void injectEnter(List<CtClass> tasks, String enter, String path) {
        def ccEnter = pool.get(enter)
        if (ccEnter.isFrozen()) {
            ccEnter.defrost()
        }

        def sb = new StringBuilder("com.lzp.core.manager.MTaskManager manager = (com.lzp.core.manager.MTaskManager) getApplication().getAppRuntime().getManager(com.lzp.core.AppRuntime.MTASK);")
        tasks.each { ccTask ->
            sb.append("com.lzp.core.mtask.MTask ${ccTask.simpleName} = new ${ccTask.name}();")
        }
        sb.append("manager.collectTasks(new com.lzp.core.mtask.MTask[]{")
        tasks.each { ccTask ->
            sb.append(ccTask.simpleName).append(",")
        }
        sb.delete(sb.length() - 1, sb.length())
        sb.append("});")

        sb.append("manager.configTasks();")
        sb.append("manager.flatTasks();")
        sb.append("manager.exec();")

        CtMethod cmOnCreate = ccEnter.getDeclaredMethod("onCreate")
        println('code=' + sb.toString())
        cmOnCreate.insertAfter(sb.toString())
        ccEnter.writeFile(path)
        ccEnter.detach()
    }
}
