@Grab('org.codehaus.groovy.modules.http-builder:http-builder:0.7')
import groovy.transform.Field
import groovyx.net.http.RESTClient
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import uk.co.cacoethes.util.NameType

import static groovyx.net.http.ContentType.JSON

AUTHOR = 'The softcake authors.'

/**
 *  Prints the String with a --- surround ---
 *
 * @param value the String
 * @return
 */
def strongEcho(String value) {
    def length = 100

    def n = (length - value.length()) / 2

    repeated = new String(new char[n]).replace("\0", "-")
    println()
    println(repeated + " " + value + " " + repeated)
    println()
}

/**
 *
 * @param configFile
 * @return the Config Object
 */
static getConfigObject(File configFile) {
    if (configFile.exists()) {
        return new ConfigSlurper().parse(configFile.toURI().toURL())
    } else {
        throw new FileNotFoundException("Cant find config.groovy")
    }
}

/**
 * Convert a String value to a Groovy truthy or false (blank) value.
 *
 * @param val
 * @return the Groovy truthy or false (blank) value
 */
static toBoolean(String val) {

    val = val.toLowerCase()
    if (val.startsWith("n") || val.equals("false")) {
        return ''
    } else {
        return val
    }
}

/**
 * Ask the user a question expecting a yes/no/true/false response.
 *
 * @param message (Question)
 * @param defaultValue
 * @param propertyName
 * @return the Groovy truthy or false (blank) value
 */
def askBoolean(String message, String defaultValue, String propertyName) {

    String val = ask(message, defaultValue, propertyName)
    val = toBoolean(val)
    parentParams[propertyName] = val
    return val
}

/**
 * Ask the user a question expecting a yes/no/true/false response.
 *
 * @param message (Question)
 * @param defaultValue
 * @return the Groovy truthy or false (blank) value
 */
def askBoolean(String message, String defaultValue) {

    String val = ask(message, defaultValue)
    return toBoolean(val)
}

/**
 * Clear the package name, removes unusable signs
 *
 * @param packageName
 * @return e.g. org.softcake.package
 */
def getPackageName(String packageName) {

    String name = packageName.replaceAll("[\\W]+|[_]+", ".")
    name = ask("Change value for 'packageName' [${name}.....]: ", "${name}", "name").toLowerCase()
    name = name.replaceAll("[\\W]+|[_]+", ".")
    //there is an bug in regex "aaa???__bbb"
    name = name.replaceAll("[\\W]+|[_]+", ".")
    name = name.substring(0, 1).matches("^[\\W]+|[_]+") ? name.substring(1) : name
    return name.charAt(name.length() - 1).toString().matches("^[\\W]+|[_]+") ?
            name.substring(0, name.length() - 1) : name
}

/**
 * Repeats the given command, if the result is not 0
 *
 * @param repeats Number of repeats
 * @param cmd
 */
def repeatTravis(int repeats, String cmd) {

    def result = 1
    while (result == 1 && repeats > 0) {
        result = executeOnShell(cmd, projectDir)
        repeats--
    }
}

/**
 * Creates the secure environment variables for .travis.yml file.
 *
 */
def createSecSystemEnvForTravis() {

    config.systemProp.softcake.each { k, v ->
        String env = transformText(k, from: NameType.PROPERTY, to: NameType.HYPHENATED)
                .toUpperCase()
                .replaceAll("-", "_")

        def envWithValue = env + "=" + v
        executeOnShell("travis encrypt ${envWithValue} --add env.global", projectDir)
    }

}

/**
 * Prepare all module names for template processing.
 *
 * @param modules from input processing
 * @param isPublished
 * @return a String for the template variable
 */
def getModules(List modules, boolean isPublished) {

    StringBuilder builder = new StringBuilder()
    for (String item : modules) {
        String moduleName = isPublished ? "${props.projectName}.${item}" : "${item}"
        builder.append("include " + "\"${moduleName}\"\n")
        createProjectModule(item, isPublished)
    }
    builder.toString()
}
/**
 * Prepare all module names for template processing.
 *
 * @param modules from input processing
 * @param isPublished
 * @return a String for the template variable
 */
def getPublishedModules(List modules) {

    StringBuilder builder = new StringBuilder()


    for (i = 0; i < modules.size(); i++) {
        String item = modules.get(i)
        String moduleName = "${props.projectName}.${item}"
        builder.append("\"${moduleName}\"")

        if (i < modules.size() - 1) {
            builder.append(",\n")
        }
        createReadmeForPublishedModules(new File(projectDir, "${moduleName}"), item)
    }
    builder.toString()

}

/**
 * Create the module directory's included build.gradle and package-info.java.
 *
 * @param shortModuleName
 * @param isPublished
 */
def createProjectModule(String shortModuleName, boolean isPublished) {

    def moduleName = isPublished ? "${props.projectName}.${shortModuleName}" : shortModuleName
    def packagePath = "${props.packagePath}/${shortModuleName}"
    def mainDir = new File(projectDir, "${moduleName}/src/main/java/")
    def packageDir = new File(projectDir, "${moduleName}/src/main/java/" + packagePath)
    packageDir.mkdirs()
    def testDir = new File(projectDir, "${moduleName}/src/test/java/" + packagePath)
    testDir.mkdirs()
    createModuleInfo(mainDir, "${props.packageName}.${shortModuleName}")
    createPackageInfo(packageDir, "${props.packageName}.${shortModuleName}")
    createBuildGradle(new File(projectDir, moduleName), shortModuleName)
    createJavaClass(packageDir, "${props.packageName}.${shortModuleName}", shortModuleName)
}
/**
 * Create the module directory's included build.gradle and package-info.java.
 *
 * @param shortModuleName
 * @param isPublished
 */
def createDocModule() {
    String moduleName = "documentation"
    def packagePath = "${props.packagePath}/${moduleName}"
    def mainDir = new File(projectDir, "${moduleName}/src/main/java/")
    def packageDir = new File(projectDir, "${moduleName}/src/main/java/" + packagePath)
    packageDir.mkdirs()
    def testDir = new File(projectDir, "${moduleName}/src/test/java/" + packagePath)
    testDir.mkdirs()
    createModuleInfoForDoc(mainDir, "${props.packageName}.${moduleName}")
    createPackageInfo(packageDir, "${props.packageName}.${moduleName}")
    createJavaClassForDoc(packageDir, "${props.packageName}.${moduleName}", moduleName)
}
/**
 * Creates an package-info.java.
 *
 * @param dir
 * @param packageName
 */
def createPackageInfo(File dir, String packageName) {

    writeToFile(dir, "package-info.java",
            "\n" +
                    "/**\n" +
                    " *\n" +
                    " * @author " + AUTHOR + "\n" +
                    " */\n" +
                    "package ${packageName};" +
                    "\n")
}

/**
 * Creates an module-info.java.
 *
 * @param dir
 * @param moduleName
 */
def createModuleInfoForDoc(File dir, String moduleName) {

    writeToFile(dir, "module-info.java",
            "\n" +
                    "module ${moduleName} {\n" +
                    "\n" +
                    "exports ${moduleName};\n" +
                    "requires com.google.common;\n" +
                    "}" +
                    "\n")
}
/**
 * Creates an module-info.java.
 *
 * @param dir
 * @param moduleName
 */
def createModuleInfo(File dir, String moduleName) {

    writeToFile(dir, "module-info.java",
            "\n" +
                    "module ${moduleName} {\n" +
                    "\n" +
                    "}" +
                    "\n")
}
/**
 * Creates a Java class.
 *
 * @param dir
 * @param packageName
 * @param className
 */
def createJavaClass(File dir, String packageName, String className) {

    writeToFile(dir, "${className.capitalize()}.java",
            "\n" +
                    "package ${packageName};\n" +
                    "\n" +
                    "/**\n" +
                    " * The ${className.capitalize()} class.\n" +
                    " *\n" +
                    " * @author " + AUTHOR + "\n" +
                    " */\n" +
                    "public class ${className.capitalize()} {\n" +
                    "\n" +
                    "}" +
                    "\n")
}
/**
 * Creates a Java class.
 *
 * @param dir
 * @param packageName
 * @param className
 */
def createJavaClassForDoc(File dir, String packageName, String className) {

    writeToFile(dir, "${className.capitalize()}.java",
            "package ${packageName};\n" +
                    "\n" +
                    "// tag::exampleDemo[]\n" +
                    "/**\n" +
                    " * ${className.capitalize()} class.\n" +
                    " *\n" +
                    " * @author " + AUTHOR + "\n" +
                    " */\n" +
                    "public final class ${className.capitalize()} {\n" +
                    "\n" +
                    "    private ${className.capitalize()}() {\n" +
                    "\n" +
                    "        throw new IllegalStateException(\"No instances!\");\n" +
                    "    }\n" +
                    "\n" +
                    "    public static void checkNotNull(final Object obj) {\n" +
                    "\n" +
                    "        if (obj == null) {\n" +
                    "            throw new IllegalArgumentException(\"Parameter must not be null!\");\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "}\n" +
                    "// end::exampleDemo[]\n")
}

/**
 * Creates a build.gradle file in the given directory.
 *
 * @param dir
 * @param shortModuleName
 * @return
 */
def createBuildGradle(File dir, String shortModuleName) {

    String content = "plugins {\n" +
            "    id 'java-library'\n" +
            "}\n" +
            "\n" +
            "javaModule.name = \"${props.packageName}.${shortModuleName}\"  \n" +
            "description = \" The ${shortModuleName.capitalize()} module of the ${props.projectName.capitalize()} project.\"\n" +
            "\n" +
            "dependencies {\n" +
            "    //api project(':${props.projectName.toLowerCase()}.___')\n" +
            "    //implemetation project(':${props.projectName.toLowerCase()}.___')\n" +
            "}" +
            "\n"
    writeToFile(dir, "build.gradle", content)
}

/**
 * Creates a Java class.
 *
 * @param dir
 * @param packageName
 * @param className
 */
def createReadmeForPublishedModules(File dir, String simpleModuleName) {
    String moduleName = "${props.projectName}.${simpleModuleName}"
    writeToFile(dir, "README.md",
            "# The ${props.projectName} Project  - ${simpleModuleName.capitalize()} module. \n" +

                    "[![Bintray](https://img.shields.io/bintray/v/softcake/${props.projectName}/${moduleName}.svg)](https://bintray.com/softcake/${props.projectName}/${moduleName})\n" +
                    "[![Maven Central](https://img.shields.io/maven-central/v/${props.packageName}/${moduleName}.svg)](https://maven-badges.herokuapp.com/maven-central/${props.packageName}/${moduleName})\n" +
                    "[![GitHub version](https://img.shields.io/github/tag/${props.githubUser}/${props.projectName}.svg)](https://github.com/${props.githubUser}/${props.projectName})\n" +
                    "[![Travis](https://img.shields.io/travis/${props.githubUser}/${props.projectName}.svg)](https://travis-ci.org/${props.githubUser}/${props.projectName})\n" +
                    "[![Codecov](https://img.shields.io/codecov/c/github/${props.githubUser}/${props.projectName}.svg)](https://codecov.io/gh/${props.githubUser}/${props.projectName})\n" +
                    "[![Quality Gate](https://sonar.aldeso.com/api/badges/gate?key=${props.packageName}:master)](https://sonar.aldeso.com/dashboard/index/${props.packageName}:master)\n" +
                    "[![Quality Gate](https://sonar.aldeso.com/api/badges/measure?key=${props.packageName}:master&metric=bugs&blinking=true )](https://sonar.aldeso.com/dashboard/index/${props.packageName}:master)\n" +
                    "[![SonarQube Tech Debt](https://img.shields.io/sonar/https/sonar.aldeso.com/${props.packageName}:master/tech_debt.svg)](https://sonar.aldeso.com/dashboard/index/${props.packageName}:master)\n" +
                    "[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)\n" +
                    "\n" +
                    "#### [Documentation](https://softcake.github.io/${props.projectName}/ \"${props.projectName} Documentation\")\n")
}

/**
 * Write a String to a file with the given name in the given directory.
 *
 * @param dir
 * @param fileName
 * @param content
 */
def writeToFile(File dir, String fileName, String content) {

    FileUtils.write(new File(dir, fileName), content, 'ISO-8859-1')
}

/**
 * Removes the file extension for given file.
 *
 * @param path as String
 * @param fileName as String
 */
void removeFileExtension(String path, String fileName) {

    String filePath = path + "\\" + fileName
    File file = new File(filePath)
    if (file.exists()) {
        String extension = FilenameUtils.removeExtension(filePath)
        file.renameTo(new File(extension))
    }
}

/**
 * Executes bash commands in the right way!
 *
 * @param command
 * @param workingDir
 * @return the exit value of the process represented by this
 * {@code Process} object.  By convention, the value
 * {@code 0} indicates normal termination.
 */
def executeOnShell(String command, File workingDir) {

    println command
    def process = new ProcessBuilder(addShellPrefix(command))
            .directory(workingDir)
            .redirectErrorStream(true)
            .start()
    process.inputStream.eachLine { println it }
    process.waitFor()
    return process.exitValue()
}

/**
 * Add the required shell prefixes for bash execution.
 *
 * @param command
 * @return an command array
 */
def addShellPrefix(String command) {

    commandArray = new String[3]
    commandArray[0] = "sh"
    commandArray[1] = "-c"
    commandArray[2] = command
    return commandArray
}

/**
 * Create a git repository in project directory.
 *
 */
def createGitRepository() {

    executeOnShell("git init", projectDir)
    executeOnShell("git add .", projectDir)
    executeOnShell("git commit -m 'First commit'", projectDir)
    //TODO
    executeOnShell("git update-index --chmod=+x gradlew", projectDir)
    executeOnShell("git update-index --chmod=+x buildscripts/buildViaTravis.sh", projectDir)
    executeOnShell("git commit -m 'permission access for travis'", projectDir)

}

/**
 * Create a Github Pages branch.
 *
 */
def createGhPagesBranch() {

    executeOnShell("git checkout --orphan gh-pages", projectDir)
    executeOnShell("git rm -rf .", projectDir)
    executeOnShell("echo \"${props.projectName}\" > index.html", projectDir)
    executeOnShell("git add index.html", projectDir)
    executeOnShell("git commit -a -m 'First gh-pages commit'", projectDir)
    executeOnShell("git push origin gh-pages", projectDir)

}

/**
 * Create a Github repository.
 *
 * @param isOrganization
 */
def createGithubRepository(boolean isOrganization) {

    println("Create Github repository \"${props.projectName}\".")
    String path = isOrganization ? "orgs/softcake/repos" : "user/repos"

    try {
        // expect an exception from a 404 response:

        def response = postToGithub(path, [name: props.projectName])

        if (response.status == 201) {
            println("Github repository \"${props.projectName}\" successfully created.")
            executeOnShell("git remote add origin https://github.com/${props.githubUser}/" +
                    "${props.projectName}.git", projectDir)
            executeOnShell("git push -u origin master", projectDir)
        } else {
            println("Error!... creating Github repository \"${props.projectName}\".")
        }
    }

    catch (ex) {
        println("Error!... creating Github repository \"${props.projectName}\".")
        println(ex)
    }

}

/**
 * Change Github default branch.
 *
 * @param defaultBranch the new branch
 * @return
 */
def changeDefaultBranch(String defaultBranch) {

    println("Set Github default branch \"${defaultBranch}\".")

    String path = "repos/${props.githubUser}/${props.projectName}"


    try {
        def response = postToGithub(path, [name          : props.projectName,
                                           default_branch: defaultBranch])

        if (response.status == 200) {
            println("Github default branch is now: \"${defaultBranch}\".")
        } else {
            println("Error!... set default sranch \"${defaultBranch}\".")
        }

    }

    catch (ex) {
        println("Error!... set default Branch \"${defaultBranch}\".")
        println(ex)
    }

}

/**
 * POST to Github.
 *
 * @param path
 * @param data to post
 * @return the response Object
 */
def postToGithub(String path, Object data) {

    def rest = new RESTClient("https://api.github.com/")

    def base64UserPass = "${config.systemProp.softcake.ghUser}:${config.systemProp.softcake.ghPass}"
            .getBytes('iso-8859-1')
            .encodeBase64()
    rest.setHeaders(['User-Agent' : 'ReneNeubert',
                     Authorization: "Basic " + base64UserPass])
    try {

        def response = rest.post(path: path,
                contentType: JSON,
                body: data
        )

        return response

    }

    catch (ex) {
        println("Error!... on Github Post: " + ex)
    }

}
/**
 * Create a Bintray repository.
 *
 * @param reponame
 */
def createBintrayRepository(String reponame) {

    try {
        String user = config.systemProp.softcake.bintrayUser
        String pass = config.systemProp.softcake.bintrayKey

        def rest = new RESTClient("https://api.bintray.com/repos/")
        rest.auth.basic(user, pass)

        def response = rest.post(path: "softcake/${reponame}",
                contentType: JSON,
                body: [name             : reponame,
                       type             : "maven",
                       private          : false,
                       desc             : props.description,
                       gpg_sign_metadata: true,
                       gpg_sign_files   : true,
                       gpg_use_owner_key: false]
        )
        if (response.status == 201) {

            println("Bintray repository \"${props.projectName}\" successfully created.")

        } else {

            println("Error!... creating Bintray repository \"${props.projectName}\".")
        }
    }

    catch (ex) {

        println("Error!... creating Bintray repository \"${props.projectName}\".")
        println(ex)
    }
}

@Field ConfigObject config = getConfigObject(new File(System.getProperty("user.home") +
        "/.lazybones/config.groovy"))
@Field Map<String, String> props = [:]

strongEcho("Processing input")


strongEcho("Project Properties")
props.group = "org.softcake"
props.projectName = projectDir.getName().toLowerCase()
props.description = ask("Define value for 'description' [The ${props.projectName.capitalize()} Project]: ",
        "The ${props.projectName.capitalize()} Project.", "description")
props.packageName = getPackageName("org.softcake.${props.projectName}")
props.packagePath = props.packageName.replaceAll("[\\W]+|[_]+", "/")



strongEcho("Version Control")
props.githubUser = ask("Define value for github user or organization [softcake]: ", "softcake",
        "githubUser")
props.isOrganization = !askBoolean("Is github user \"${props.githubUser}\" a organization? [yes]: ",
        "yes", "isOrganization").isEmpty()
props.useGithub = !askBoolean("Would you like to create github repository? [yes]: ", "yes").isEmpty()



strongEcho("Publishing Repositories")
props.useBintray = false
if (props.useGithub) {
    props.useBintray = !askBoolean("Would you like to create Bintray repository? [yes]: ", "yes").isEmpty()
}


strongEcho("TRAVIS CI")
props.useTravis = false
props.createEnv = false

if (props.useGithub) {
    props.useTravis = !askBoolean("Would you like enable Travis CI? [yes]: ", "yes").isEmpty()
}
if (props.useTravis) {
    props.createEnv = !askBoolean("Would you like create secure environment variables? [yes]: ", "yes").isEmpty()
}

strongEcho("Setup project modules")
projectModules = []
publishedModules = []

strongEcho("1. Create first project module")

String mod = ask("Define the prefix name of first project module " +
        "[${props.projectName}.<prefix>]: ", "").toLowerCase()

if (mod.equals("")) {
    throw new IllegalArgumentException("The prefix Can not be empty!")
}
projectModules.add("${mod}")
println "Project module ${props.projectName}.${mod} created."

strongEcho("2. Setup next project modules")

while (true) {
    def createModule = askBoolean("Would you like to create other project module " +
            "[${props.projectName}....]? [no]: ", "no")
    if (!createModule) break


    def module = ask("Define the prefix name of other project module " +
            "[${props.projectName}.<prefix>]: ", "").toLowerCase()
    if (module.equals("")) {
        throw new IllegalArgumentException("The prefix can not be empty!")
    }

    projectModules.add("${module}")
    println "Project module ${props.projectName}.${module} created."
}

strongEcho("2.1 Define project modules as published or not.")

for (String item : projectModules) {
    def createModule = askBoolean("Should project module <${props.projectName}.${item}> published? " +
            "[yes]: ", "yes")

    if (createModule) {
        publishedModules.add("${item}")
    }
}




strongEcho("3. Setup non-project modules")
nonProjectModules = []

while (true) {
    def createModule = askBoolean("Would you like to create a non-project module? [no]: ", "no")
    if (!createModule) break

    def module = ask("Define the name of non-project module [example]: ", "example").toLowerCase()
    nonProjectModules.add("${module}")
    println "Non project module ${module} created."
}

strongEcho("Processing...")

removeFileExtension(projectDir.getAbsolutePath(), ".gitattributes.txt")
removeFileExtension(projectDir.getAbsolutePath(), ".gitignore.txt")


createDocModule()
props.module = getModules(projectModules, true)
props.module += getModules(nonProjectModules, false)
props.pubmod = getPublishedModules(publishedModules)

processTemplates("build.gradle", props)
processTemplates("settings.gradle", props)
processTemplates("README.md", props)
processTemplates("documentation/content/index.adoc", props)
processTemplates("documentation/build.gradle", props)
createGitRepository()

if (props.useGithub) {
    createGithubRepository(props.isOrganization)
}

if (props.useTravis) {
    def login = executeOnShell("travis login --github-token \"${config.systemProp.softcake.ghToken}\"", projectDir)

    if (login == 0) {
        repeatTravis(10, "travis enable -r ${props.githubUser}/${props.projectName}")
        repeatTravis(10, "travis settings builds_only_with_travis_yml --enable")
    }
}

if (props.useBintray) {
    println "Create Bintray repository..."
    createBintrayRepository(props.projectName)
}

if (props.createEnv) {
    println "Create secure Environment for Travis CI..."
    createSecSystemEnvForTravis()
    executeOnShell("git commit -a -m 'sec env for travis'", projectDir)
    executeOnShell("git push -u origin master", projectDir)
}

executeOnShell("git checkout -b develop", projectDir)
executeOnShell("git push origin develop", templateDir)
executeOnShell("git flow init -fd", templateDir)
executeOnShell("git push origin --all", templateDir)
createGhPagesBranch()
executeOnShell("git push origin --all", templateDir)
executeOnShell("git checkout develop", projectDir)
executeOnShell("git show-ref", projectDir)
changeDefaultBranch("develop")
strongEcho("Ready!")
