package com.lzp.moduleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

import java.util.regex.Pattern;

class Modularization implements Plugin<Project> {
    @Override
    void apply(Project project) {
        addDependecys(project)
//        generateLifecycleCode(project)
    }

    private void addDependecys(Project project) {
        boolean debug = true
        project.gradle.startParameter.taskNames.each { String task ->
            if (task.toLowerCase().contains('assemble') ||
                    task.toLowerCase().contains('install')) {
                debug = false
            }
        }

        println('=========debug=' + debug)
        if (!debug) {
            //add project dependencies
            def moduleProjects = project.properties.get('modules')
            moduleProjects = moduleProjects ? moduleProjects : ''
            moduleProjects.split(',').each { String module ->
                if (module.contains(':')) {//仓库下载方式
//                    println('=========1111module=' + module)
                    project.dependencies.add('implementation', module)
                } else if (module.endsWith('.aar')) {//本地aar方式
//                    println('=========2222module=' + module)
                    def depend = [:]
                    depend.name = module.substring(0, module.indexOf('.'))
                    depend.ext = 'aar'
                    project.dependencies.add('implementation', depend)
                } else {//module工程依赖方式
                    //implementation project(':module1')
//                    println('=========3333module=' + module)
                    project.dependencies.add('implementation', project.rootProject.project(module))
                }
            }
        }
    }


//    private void generateLifecycleCode(Project project) {
//        project.afterEvaluate {
//            String application = project.properties.get('main.application')
//            String applicationClzName = application.substring(application.lastIndexOf('.') + 1, application.length())
//            String applicationPackage = application.substring(0, application.lastIndexOf('.'))
//            println("+++++++applicationClzName=" + applicationClzName)
//            println("+++++++applicationPackage=" + applicationPackage)
//
//
//            String lifecycles = project.properties.modulelifecycles
//            lifecycles = lifecycles ? lifecycles : ''
//
//            StringBuilder sbcode = new StringBuilder()
//            sbcode.append("super.onCreate();\r\n")
//            lifecycles.split(',').each { life ->
//                sbcode.append("        ((LifecycleManager) getAppRuntime().getManager(AppRuntime.LIFECYCLE)).registerModule(\"${life}\");")
//                sbcode.append("\r\n")
//            }
//            sbcode.delete(sbcode.length() - 2, sbcode.length())
//
//            StringBuilder sbimport = new StringBuilder();
//            project.android.sourceSets.main.getJava().getSrcDirs().each { File dir ->
//                def path = applicationPackage.replace('.', File.separator)
//                File destFile = new File(dir, path + File.separator + applicationClzName + '.java')
//                if (destFile.exists()) {
//                    String fileContent = destFile.getText('utf-8')
//                    //add import
//                    if (!fileContent.find(Pattern.compile("import(\\s)+com\\.lzp\\.core\\.AppRuntime;"))) {
//                        sbimport.append("import com.lzp.core.AppRuntime;\r\n")
//                    }
//                    if (!fileContent.find(Pattern.compile("import(\\s)+com\\.lzp\\.core\\.manager\\.LifecycleManager;"))) {
//                        sbimport.append("import com.lzp.core.manager.LifecycleManager;\r\n")
//                    }
//                    if (sbimport.length() > 0) {
//                        sbimport.insert(0, "package ${applicationPackage};\r\n")
//                    }
//                    if (sbimport.length() > 0) {
//                        def tmp = applicationPackage.replace('.', '\\.')
//                        fileContent = fileContent.replaceFirst(Pattern.compile("package(\\s)+" + tmp + ";"), sbimport.toString())
//                    }
//
//                    //add code
//                    //删除之前添加的
////                    fileContent = fileContent.replaceAll(Pattern.compile("(\r\n)*com\\.lzp\\.core\\.LifecycleManager\\.getInstance\\(\\)\\.registerModule\\((.)+;(\r\n)*"), "")
//                    //((LifecycleManager)getAppRuntime().getManager(AppRuntime.LIFECYCLE)).registerModule("com.lzp.module1.Module1Lifecycle");
//                    fileContent = fileContent.replaceAll(Pattern.compile("(\r\n)*\\(\\(LifecycleManager\\)(\\s)*getAppRuntime\\(\\)\\.getManager\\(AppRuntime\\.LIFECYCLE\\)\\)\\.registerModule\\((.)+;(\r\n)*"), "")
//                    //添加新的
//                    fileContent = fileContent.replaceFirst(Pattern.compile("super\\.onCreate\\(\\);"), sbcode.toString())
//
//                    destFile.setText(fileContent, 'utf-8')
//                }
//            }
//        }
//    }
}
