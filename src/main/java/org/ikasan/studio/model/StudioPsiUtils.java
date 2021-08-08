package org.ikasan.studio.model;

import com.intellij.lang.Language;
import com.intellij.lang.jvm.JvmMethod;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.search.*;
import com.intellij.psi.xml.XmlFile;
import com.intellij.testFramework.PsiTestUtil;
import com.intellij.util.IncorrectOperationException;
import org.apache.log4j.Logger;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.ikasan.studio.Context;
import org.ikasan.studio.StudioUtils;
import org.ikasan.studio.model.ikasan.IkasanModule;
import org.ikasan.studio.model.ikasan.IkasanPomModel;
import org.ikasan.studio.model.psi.PIPSIIkasanModel;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

public class StudioPsiUtils {
    private static final Logger LOG = Logger.getLogger(StudioPsiUtils.class);

    // Enforce utility nature upon class
    private StudioPsiUtils() {}

    public static String getSimpleFileData(PsiFile file) {
        StringBuilder message = new StringBuilder();
        if(file != null)
        {
            message.append("File name was [" + file.getName() +"]\n");
            message.append("File was [" + file.toString() +"]\n");
            Language lang = file.getLanguage();
            message.append("Language was [" + lang.getDisplayName().toLowerCase() +"]");
        }
        return message.toString();
    }

    /**
     * Remove the start and end quotes from a String to prevent double quoting.
     * @param value to be examined
     * @return the string with the start and end quites removed if there were any present
     */
    public static String stripStartAndEndQuotes(String value) {
        if (value != null && !value.isEmpty()) {
            if (value.startsWith("\"")) {
                value = value.substring(1);
            }
            if (value.endsWith("\"")) {
                value = value.substring(0,value.length()-1);
            }
        }
        return value;
    }

    public static String findClassFile(Project project, String className) {
        StringBuilder message = new StringBuilder();
        PsiFile[] files = FilenameIndex.getFilesByName(project, className, GlobalSearchScope.projectScope(project));
        for (PsiFile myFile : files) {
            message.append("looking for file " + className + ", found [" + myFile.getName() +"]");
        }
        return message.toString();
    }

    /**
     * Attempt to load in all files ending in properties
     * @todo we could scope this to the JAVA root to avoid catching test properties.
     * @param project to load the files file
     * @return a java Properties instance
     */
    public static Properties getApplicationProperties(Project project) {
        Properties properties = new Properties();
        Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(StdFileTypes.PROPERTIES, GlobalSearchScope.projectScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
            try {
                properties.load(new StringReader(psiFile.getText()));
            } catch (IOException e) {
                LOG.error("Problems loading in application properties " + e);
            }
        }
        return properties;
    }

    public static IkasanPomModel loadPom(Project project) {
        IkasanPomModel pom = Context.getPom(project.getName());
        if (pom == null) {
            Model model = null;
            PsiFile pomPsiFile = getPom(project);
            if (pomPsiFile != null) {
                try (Reader reader = new StringReader(pomPsiFile.getText())) {
                    MavenXpp3Reader xpp3Reader = new MavenXpp3Reader();
                    model = xpp3Reader.read(reader);
                    pom = new IkasanPomModel(model, pomPsiFile);
                    Context.setPom(project.getName(), pom);
                } catch (IOException | XmlPullParserException ex) {
                    LOG.error("Unable to load project pom", ex);
                }
            }
        }
        return pom;
    }

    public static void addDependancies(String projectKey, List<Dependency> dependencies) {
        Project project = Context.getProject(projectKey);
        IkasanPomModel pom = loadPom(project);
        boolean pomUpdated = false;

        if (pom!= null && pom.getModel() != null && dependencies != null && !dependencies.isEmpty()) {
            Set<Dependency>  existingDependencies = new HashSet<>(pom.getModel().getDependencies());
            for (Dependency dependency: dependencies) {
                if (!existingDependencies.contains(dependency)) {
                    pom.getModel().addDependency(dependency);
                    existingDependencies.add(dependency);
                    pomUpdated = true;
                }
            }
        }

        if (pomUpdated) {
            savePom(project, pom);
//            ProjectManager.getInstance().reloadProject(project);
        }
    }

    public static void savePom(Project project, IkasanPomModel pom) {
        MavenXpp3Writer writer = new MavenXpp3Writer();
        StringWriter sw = new StringWriter();
        try {
            writer.write(sw, pom.getModel());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Always need to reget the PsiFile since a reload might have disposed previous handle.
        pom.setPomPsiFile(getPom(project));

        PsiDirectory containtingDirectory = pom.getPomPsiFile().getContainingDirectory();
        String fileName = pom.getPomPsiFile().getName();
        if (pom.getPomPsiFile() != null) {
            pom.getPomPsiFile().delete();
            pom.setPomPsiFile(null);
        }

        XmlFile newPomFile = (XmlFile)PsiFileFactory.getInstance(project).createFileFromText(fileName, StdFileTypes.XML, sw.toString());
        // When you add the file to the directory, you need the resulting psiFile not the one you sent in.
        newPomFile = (XmlFile)containtingDirectory.add(newPomFile);
        CodeStyleManager.getInstance(project).reformat(newPomFile);
        // Technically this is a testing Util
        PsiTestUtil.checkFileStructure(newPomFile);
        pom.setPomPsiFile(newPomFile);
    }

    public static PsiFile createFile1(final String filename, final String text, Project project) {
        //  ************************* PsiJavaParserFacadeImpl ********************************
        PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
        PsiMethodCallExpression equalsCall = (PsiMethodCallExpression) factory.createExpressionFromText("a.equals(b)", null);
        @NotNull Module[] module = ModuleManager.getInstance(project).getModules();
        PsiDirectory baseDir = PsiDirectoryFactory.getInstance(project).createDirectory(project.getBaseDir());
        PsiFile newFile = PsiFileFactory.getInstance(project).createFileFromText(filename, StdFileTypes.JAVA, text);
        return newFile;
    }


    public static PsiFile getPom(Project project) {
        PsiFile[] pomFiles = PsiShortNamesCache.getInstance(project).getFilesByName("pom.xml");
        if (pomFiles.length > 0) {
            return pomFiles[0];
        } else {
            return null;
        }
    }

    public static String findFile(Project project, String filename) {
        StringBuilder message = new StringBuilder();

        PsiFile[] files2 = PsiShortNamesCache.getInstance(project).getFilesByName(filename);
        long t1 = System.currentTimeMillis();
        message.append("looking for file " + filename + " method 1 found [");
        for (PsiFile myFile : files2) {
            message.append("" + myFile.getName());
        }
        long t2 = System.currentTimeMillis();

        message.append("] method 2 found [");
        PsiFile[] files = FilenameIndex.getFilesByName(project, filename, GlobalSearchScope.projectScope(project));
        for (PsiFile myFile : files) {
            message.append("" + myFile.getName() +"] ");
        }
        long t3 = System.currentTimeMillis();

        message.append("] method 1 = " + (t2-t1) + " ms method 2 = " + (t3-t2));
        return message.toString();
    }


    public static void refreshCodeFromModelAndCauseRedraw(String projectKey) {
        PIPSIIkasanModel pipsiIkasanModel = Context.getPipsiIkasanModel(projectKey);
        pipsiIkasanModel.generateSourceFromModel();
        StudioPsiUtils.generateModelFromSourceCode(projectKey, false);
        Context.getDesignerCanvas(projectKey).setInitialiseAllDimensions(true);
        Context.getDesignerCanvas(projectKey).repaint();
    }

    public static String dumpProject(Project project) {
        StringBuilder dump = new StringBuilder();
        dump.append("** All class names **");
        String[] classNames = PsiShortNamesCache.getInstance(project).getAllClassNames();
        dump.append(classNames);
        dump.append("** All field names **");
        String[] fieldNames = PsiShortNamesCache.getInstance(project).getAllFieldNames();
        dump.append(fieldNames);
        dump.append("** All file names **");
        String[] fileNames = PsiShortNamesCache.getInstance(project).getAllFileNames();
        dump.append(fileNames);
        dump.append("** All method names **");
        String[] methodNames = PsiShortNamesCache.getInstance(project).getAllMethodNames();
        dump.append(methodNames);
        return dump.toString();
    }

    /**
     * Primarily, this class is looking for the method that returns 'Module' but could be used to get anything
     *
     * I suspect this is NOT very efficient.
     *
     * @param project
     * @param methodReturnType
     * @return the method whose return type matches methodReturnType
     */
    public  static PsiMethod findFirstMethodByReturnType(Project project, String methodReturnType) {
        PsiMethod returnPsiMethod = null;
        PsiShortNamesCache cache = PsiShortNamesCache.getInstance(project);
        // In this release,  processAllClassNames does not scope all the classnames, in future release it might so keep code.
        
//        cache.processAllClassNames(new Processor<String>() {
//            @Override
//            public boolean process(String className) {
//                System.out.println("try className " + className);
//                if (!returnPsiMethodFound) {
//                    System.out.println("try " + className);
//                    PsiClass[] psiClasses = cache.getClassesByName(className, ProjectScope.getProjectScope(project));
//                    if (psiClasses != null) {
//                        for (PsiClass psiClass : psiClasses) {
//                            System.out.println(" L1 " + psiClass.getName());
//                            PsiMethod[] psiMethods = psiClass.getAllMethods();
//                            if (psiMethods != null) {
//                                for (PsiMethod psiMethod : psiMethods) {
//                                    PsiType returnType = psiMethod.getReturnType();
//
//                                    if (returnType == null) {
//                                        System.out.println(" L2 " + psiMethod.getName() + " ret is null");
//                                    } else {
//                                        System.out.println(" L2 " + psiMethod.getName() + " ret is " + returnType.getCanonicalText(true) + " eq " + returnType.equalsToText(methodReturnType));
//                                    }
//                                    if (returnType != null && returnType.equalsToText(methodReturnType)) {
//                                        returnPsiMethod = psiMethod;
//                                        break;
//                                    }
//                                }
//                            }
//                            if (returnPsiMethod != null) {
//                                break;
//                            }
//                        }
//                    }
//                }
//                return returnPsiMethodFound;
//            }
//        }, ProjectScope.getProjectScope(project), IdFilter.getProjectIdFilter(project, false));
//        return returnPsiMethod;

// or this should work but does not

//        cache.processAllMethodNames(new Processor<String>() {
//            @Override
//            public boolean process(String methodNames) {
//    System.out.println("try methodNames " + methodNames);
//                    if (!returnPsiMethodFound) {
//                        PsiMethod[] psiMethods = cache.getMethodsByName( methodNames, ProjectScope.getProjectScope(project));
//                        if (psiMethods != null) {
//                            for (PsiMethod psiMethod : psiMethods) {
//                                PsiType returnType = psiMethod.getReturnType();
//    System.out.println(" L2 " + psiMethod.getName() + " ret is " + returnType.getCanonicalText(true) + " eq " + returnType.equalsToText(methodReturnType));
//                                if (returnType != null && returnType.equalsToText(methodReturnType)) {
//                                    returnPsiMethod = psiMethod;
//                                    returnPsiMethodFound = true;
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                return returnPsiMethodFound;
//            }
//        }, ProjectScope.getProjectScope(project), IdFilter.getProjectIdFilter(project, false));
//        return returnPsiMethod;
//            });


        // Very brute force, go through 52K classes, if its not in project scope ignore, otherwise inspect
        //@todo maybe we can force this to be governed by convention i.e. agree must be ModuleConfig.java
        //@todo use getSourceRootContaining(JAVA)
        for (String className : cache.getAllClassNames()) {
            for (PsiClass psiClass : cache.getClassesByName(className, ProjectScope.getProjectScope(project))) {
                returnPsiMethod = findMethodFromClassByReturnType(psiClass, methodReturnType);
                if (returnPsiMethod != null) {
                    break;
                }
            }
            if (returnPsiMethod != null) {
                break;
            }
        }
        return returnPsiMethod;
    }

    /**
     * For the given class, look for the first method with the given return type.
     * @param psiClass to look for
     * @param methodReturnType (canonical and fully qualified) string of the return type searched for.
     * @return
     */
    public static PsiMethod findMethodFromClassByReturnType(PsiClass psiClass, String methodReturnType) {
        PsiMethod methodFound = null ;
        if (psiClass != null) {
            for (PsiMethod psiMethod : psiClass.getAllMethods()) {
                PsiType returnType = psiMethod.getReturnType();
                //@todo determine if "<?>" needs to be here or elsewhere, maybe pass in
                if (returnType != null && (returnType.equalsToText(methodReturnType) || returnType.equalsToText(methodReturnType+"<?>"))) {
                    methodFound = psiMethod;
                    break;
                }
            }
        }
        return methodFound;
    }

    /***
     * Find the class in the project
     * @param project the class
     * @param className the non-qualified or fully qualified classname
     * @return
     */
    public static PsiClass[] findClass(Project project, String className) {
        // Note, getClassesByName will only work with non-qualified classname
        @NotNull PsiClass[] files = null;
        if (className.contains(".")) {
            String baseClassName = StudioUtils.getLastToken( "\\.", className);
            files = PsiShortNamesCache.getInstance(project).getClassesByName(baseClassName, ProjectScope.getProjectScope(project));
            files = Arrays.stream(files).filter(x -> className.equals(x.getQualifiedName())).toArray(PsiClass[]::new);
        } else {
            files = PsiShortNamesCache.getInstance(project).getClassesByName(className, ProjectScope.getProjectScope(project));
        }

        if (files != null) {
            for (PsiClass myClass : files) {
                LOG.debug("looking for class " + className + ", found [" + myClass.getName() +"]");
            }
        }
        return files;
    }

     /***
     * Find the first class of the given classname in the project
     * @param project the class
     * @param className the non-qualified or fully qualified classname
     * @return
     */
    public static PsiClass findFirstClass(Project project, String className) {
        PsiClass[] classes = findClass(project, className);
        if (classes!= null && classes.length > 0) {
            if (classes.length > 1) {
                LOG.warn("Found more than one class of name " + className+ " but only expected 1");
            }
            return classes[0];
        } else {
            return null;
        }
    }

    /**
     * Given a PsiClass, get the first method that matches the method name
     * @param clazz to examine
     * @param methodName to look for
     * @return method name within given class or null if not found.
     */
    public static JvmMethod findFirstMethod (PsiClass clazz, String methodName) {
        JvmMethod[] methods = clazz.findMethodsByName(methodName);
        if (methods.length > 0) {
            if (methods.length > 1) {
                LOG.warn("Found more than one class of name " + methodName + " but only expected 1");
            }
            return methods[0];
        } else {
            return null;
        }
    }


    //@ todo make a plugin property to switch on / off assumeModuleConfigClass
    public static void generateModelFromSourceCode(String projectKey, boolean assumeModuleConfigClass) {
        IkasanModule ikasanModule = Context.getIkasanModule(projectKey);
        ikasanModule.reset();
        PIPSIIkasanModel pipsiIkasanModel = Context.getPipsiIkasanModel(projectKey);
        if (pipsiIkasanModel.getModuleConfigClazz() == null || !pipsiIkasanModel.getModuleConfigClazz().isValid() ) {
            updatePIPSIIkasanModelWithModuleConfigClazz(projectKey, assumeModuleConfigClass);
        }
        if (pipsiIkasanModel.getModuleConfigClazz() != null && pipsiIkasanModel.getModuleConfigClazz().isValid()) {
            pipsiIkasanModel.updateIkasanModule();
        }
        ikasanModule.resetRegenratePermissions();
    }

    public static String getTypeOfVariable(PsiVariable psiVariable) {
        return psiVariable.getType().getCanonicalText();
    }

    private static final PIPSIIkasanModel updatePIPSIIkasanModelWithModuleConfigClazz(String projectKey, boolean assumeModuleConfigClass) {
        PIPSIIkasanModel pipsiIkasanModel = Context.getPipsiIkasanModel(projectKey);
        PsiClass moduleConfigClazz = getModuleConfigClass(projectKey, assumeModuleConfigClass);
        if (moduleConfigClazz != null && moduleConfigClazz.getContainingFile() != null) {
            pipsiIkasanModel.setModuleConfigClazz(moduleConfigClazz);
        }
        return pipsiIkasanModel;
    }

    private static final PsiClass getModuleConfigClass(String projectKey, boolean assumeModuleConfigClass) {
        PsiClass moduleConfigClazz = null ;
        Project project = Context.getProject(projectKey);
        if (!assumeModuleConfigClass) {
            //@todo this seems quite expensive, see if better way
            PsiMethod getModuleMethod = findFirstMethodByReturnType(project, PIPSIIkasanModel.OLD_MODULE_BEAN_CLASS);
            if (getModuleMethod != null) {
                moduleConfigClazz = getModuleMethod.getContainingClass();
            }
        } else {
            moduleConfigClazz = StudioPsiUtils.findFirstClass(Context.getProject(projectKey), "ModuleConfig");
        }
        return moduleConfigClazz;
    }

    public static void getAllSourceRootsForProject(Project project) {
        String projectName = project.getName();
        VirtualFile[] vFiles = ProjectRootManager.getInstance(project).getContentSourceRoots();
        String sourceRootsList = Arrays.stream(vFiles).map(VirtualFile::getUrl).collect(Collectors.joining("\n"));
        LOG.info("Source roots for the " + projectName + " plugin:\n" + sourceRootsList +  "Project Properties");
    }

    public static String JAVA_CODE = "main/java";
    public static String JAVA_RESOURCES = "main/resources";
    public static String JAVA_TESTS = "test";
    /**
     * Get the source root that contains the supplied string, possible to get java source, resources or test
     * @param project to work on
     * @param searchString to look for
     * @return the rood of the module / source directory that contains the supplied string.
     */
    public static VirtualFile getSourceRootContaining(Project project, String searchString) {
        VirtualFile sourceCodeRoot = null;
        VirtualFile[] vFiles = ProjectRootManager.getInstance(project).getContentSourceRoots();
        for (VirtualFile vFile : vFiles) {
            if (vFile.toString().contains(searchString)) {
                sourceCodeRoot = vFile;
                break;
            }
        }
        return sourceCodeRoot;
    }
    public static PsiFile createFileInDirectory(final PsiDirectory baseDir, final String filename, final String text, Project project) {
        PsiFile newFile = PsiFileFactory.getInstance(project).createFileFromText(filename, StdFileTypes.JAVA, text);
        return newFile;
    }



    //From a file by offset: PsiFile.findElementAt(). Note: this returns the lowest level element ("leaf") at the specified offset, normally a lexer token. Most likely, you should use PsiTreeUtil.getParentOfType() to find the element you really need.

//    PsiFile psiFile = anActionEvent.getData(CommonDataKeys.PSI_FILE);
//    PsiElement element = psiFile.findElementAt(offset);
//    PsiMethod containingMethod = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
//    PsiClass containingClass = containingMethod.getContainingClass();
// binary expression holds a PSI expression of the form x==y  whch we need to change to s.equals(y)
    public static void bob(Project project) {
//        ReadonlyStatusHandler.getInstance(project).ensureFilesWritable();
        // PsiBinaryExpression binaryExpression = (PsiBinaryExpression) descriptor.getPsiElement();
//        IElementType opSign = binaryExpression.getOperationTokenType();
//        PsiExpression lExpr = binaryExpression.getLOperand();
//        PsiExpression rExpr = binaryExpression.getROperand();
        // 1 Create replacement fragment from test with 'a' and 'b' as placeholders
//        PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
//        PsiMethodCallExpression equalsCall = (PsiMethodCallExpression) factory.createExpressionFromText("a.equals(b)", null);
        // 2 replace a and b
//        equalsCall.getMethodExpression().getQualifierExpression().replace(lExpr);
//        equalsCall.getArgumentList().getExpressions()[0].replace(rExpr);
        // 3 replace larger element in original file
//        PsiExpression result = (PsiExpression)binaryExpression.replace(equalsCall);
    }


    /**
     * Create the supplied directory in the PSI file system if it does not already exists
     * @param parent directory to contain the new directory
     * @param newDirectoryName to be created
     * @return the directory created or a handle to the existing directory if it exists
     * @throws IncorrectOperationException
     */
    public static PsiDirectory createDirectory(PsiDirectory parent, String newDirectoryName)
            throws IncorrectOperationException {
        PsiDirectory newDirectory = null;

        if (newDirectoryName != null) {
            for (PsiDirectory subdirectoryOfParent : parent.getSubdirectories()) {
                if (subdirectoryOfParent.getName().equalsIgnoreCase(newDirectoryName)) {
                    newDirectory = subdirectoryOfParent;
                    break;
                }
            }
        }
        // doesn't exist, create it.
        if (null == newDirectory) {
            newDirectory = parent.createSubdirectory(newDirectoryName);
        }

        return newDirectory;
    }

    /**
     * Create the directories for the supplied package name, returning the handle to the leaf directory
     * @param sourceRootDir that contains the package
     * @param qualifiedPackage i.e. dotted notation
     * @return
     * @throws IncorrectOperationException
     */
    public static PsiDirectory createPackage(PsiDirectory sourceRootDir, String qualifiedPackage)
            throws IncorrectOperationException {
        PsiDirectory parentDir = sourceRootDir;
        if (sourceRootDir != null) {
            StringTokenizer token = new StringTokenizer(qualifiedPackage, ".");
            while (token.hasMoreTokens()) {
                String dirName = token.nextToken();
                parentDir = createDirectory(parentDir, dirName);
            }
        }
        return parentDir;
    }
}
