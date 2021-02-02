package com.github.davidhilton68.ikasanstudio2.services

import com.github.davidhilton68.ikasanstudio2.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
