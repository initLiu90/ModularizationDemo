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

        def lifecycles = []
        fullCNames.each { cname ->
            CtClass cc = pool.get(cname)
            if (isModuleLifecycle(cc)) {//如果是实现了IModuleLifecycle接口的类
                lifecycles.add(cc)
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
                            injectEnter(lifecycles, enter, directoryInput.file.absolutePath)
                        }
                    }
                }

                FileUtils.copyDirectory(src, dst)
            }
        }
    }

    private boolean isModuleLifecycle(CtClass ctClass) {
        for (def cc : ctClass.getInterfaces()) {
            if (cc.name.equals("com.lzp.core.module.IModuleLifecycle")) {
                return true
            }
        }
        return false
    }

    private void injectEnter(List<CtClass> lifecycles, String enter, String path) {
        def ccEnter = pool.get(enter)
        if (ccEnter.isFrozen()) {
            ccEnter.defrost()
        }

        def code = new StringBuilder();
        lifecycles.each {
            code.append("((com.lzp.core.manager.LifecycleManager) getAppRuntime().getManager(com.lzp.core.AppRuntime.LIFECYCLE)).registerModule(\"" + it.name + "\");")
        }

        code.append("com.lzp.core.manager.MTaskManager manager = (com.lzp.core.manager.MTaskManager) getApplication().getAppRuntime().getManager(com.lzp.core.AppRuntime.MTASK);")
        code.append("manager.configTasks();")
        code.append("manager.flatTasks();")
        code.append("com.lzp.core.mtask.MTaskList taskList = manager.getTaskList();")
        code.append("taskList.exec();")

        CtMethod cmOnCreate = ccEnter.getDeclaredMethod("onCreate")
        println('code=' + code.toString())
        cmOnCreate.insertAfter(code.toString())
        ccEnter.writeFile(path)
        ccEnter.detach()
    }
}
