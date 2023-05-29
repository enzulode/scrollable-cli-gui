## Simple scrollable console gui
#### ```by enzulode```

### Description
This library is a simple java approach implementing a scrollable & pageable command line interface

### Available operations
- Paged output
- Simple command line output

### Usage

#### With Gradle
First of all, you have to create file ```local.properties``` in your project root folder.
Then add following lines there
```properties
gpr.user=<your github username>
gpr.key=<your github token>
```

**Notice:** your token is your private, secret information. Do not upload the ```local.properties``` with token in it

Then add the repository in your ```build.gradle```
```groovy

Properties properties = new Properties()
properties.load(project.rootProject.file('local.properties').newDataInputStream())
def gitUsername = properties.getProperty('gpr.user')
def gitToken = properties.getProperty('gpr.key')

repositories {
    maven {
        url = uri("https://maven.pkg.github.com/enzulode/networking-library")
        credentials {
                username = gitUsername
                password = gitToken
            }
    }
}
```

And then specify the dependency
```groovy
dependencies {
    implementation 'com.enzulode:scrollable-gui:<required library version>'
}
```
