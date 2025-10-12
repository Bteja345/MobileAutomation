# Mobile & Web Automation Framework

A comprehensive automation framework supporting both mobile (Android/iOS) and web applications using Java, Maven, Appium, Selenium, and TestNG.

## 🚀 Features

- **Cross-Platform Support**: Android, iOS, Chrome, Firefox
- **Parallel Execution**: Multi-threaded test execution
- **Smart Waits**: Implicit and explicit wait utilities
- **Screenshot Capture**: Automatic screenshots on test failures
- **Screen Recording**: Mobile test recording capabilities
- **Appium Server Management**: Programmatic Appium server control
- **Page Object Model**: Clean and maintainable test architecture
- **Comprehensive Logging**: Detailed logging with Log4j2
- **TestNG Integration**: Powerful test execution and reporting

## 📋 Prerequisites

### Required Software

1. **Java Development Kit (JDK) 11 or higher**
   ```bash
   java -version
   javac -version
   ```

2. **Maven 3.6 or higher**
   ```bash
   mvn -version
   ```

3. **Node.js and npm** (for Appium)
   ```bash
   node --version
   npm --version
   ```

4. **Appium Server**
   ```bash
   npm install -g appium
   npm install -g appium-doctor
   ```

### For Android Testing

5. **Android Studio** with Android SDK
6. **Android Virtual Device (AVD)** or physical device
7. **Environment Variables**:
   - `ANDROID_HOME`: Path to Android SDK
   - `JAVA_HOME`: Path to JDK

### For iOS Testing (macOS only)

8. **Xcode** with iOS Simulator
9. **iOS WebDriverAgent**
10. **Additional iOS dependencies**:
    ```bash
    brew install carthage
    npm install -g ios-deploy
    ```

### For Web Testing

11. **Chrome Browser** (latest version)
12. **Firefox Browser** (latest version)

## 🛠️ Setup Instructions

### 1. Clone and Setup Project

```bash
git clone <repository-url>
cd mobile-automation-framework
mvn clean install
```

### 2. Verify Appium Installation

```bash
appium-doctor --android  # For Android
appium-doctor --ios      # For iOS (macOS only)
```

### 3. Configure Test Environment

Edit `src/main/resources/config.properties` to match your environment:

```properties
# Update device names, versions, and app paths
android.device.name=Your_Android_Device
ios.device.name=Your_iOS_Device
```

### 4. Download Test Applications

- Place Android APK files in `src/test/resources/apps/android/`
- Place iOS APP files in `src/test/resources/apps/ios/`
- Update app paths in TestNG XML files

## 🏃‍♂️ Running Tests

### Web Tests Only

```bash
# Chrome browser tests
mvn test -DsuiteXmlFile=src/test/resources/testng-web.xml -Dbrowser=chrome

# Firefox browser tests
mvn test -DsuiteXmlFile=src/test/resources/testng-web.xml -Dbrowser=firefox
```

### Mobile Tests Only

```bash
# Android tests
mvn test -DsuiteXmlFile=src/test/resources/testng-mobile.xml -Dplatform=android

# iOS tests (macOS only)
mvn test -DsuiteXmlFile=src/test/resources/testng-mobile.xml -Dplatform=ios
```

### All Tests

```bash
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

### IDE Execution

1. **IntelliJ IDEA**:
   - Right-click on `testng.xml` → Run
   - Or run individual test classes

2. **Android Studio**:
   - Import as Maven project
   - Run tests from Test Explorer

## 📁 Project Structure

```
mobile-automation-framework/
├── src/
│   ├── main/
│   │   ├── java/com/automation/
│   │   │   ├── base/                  # Base classes
│   │   │   │   ├── AppiumServerManager.java
│   │   │   │   └── BaseTest.java
│   │   │   ├── pages/                 # Page Object Models
│   │   │   │   ├── mobile/
│   │   │   │   │   └── FacebookMobilePage.java
│   │   │   │   └── web/
│   │   │   │       └── FacebookWebPage.java
│   │   │   ├── tests/                 # Test classes
│   │   │   │   ├── mobile/
│   │   │   │   │   └── FacebookMobileTest.java
│   │   │   │   └── web/
│   │   │   │       └── FacebookWebTest.java
│   │   │   └── utils/                 # Utility classes
│   │   │       ├── ScreenshotUtils.java
│   │   │       ├── ScreenRecordingUtils.java
│   │   │       ├── WaitUtils.java
│   │   │       └── TestListener.java
│   │   └── resources/
│   │       ├── config.properties
│   │       └── log4j2.xml
│   └── test/
│       └── resources/
│           ├── testng.xml
│           ├── testng-web.xml
│           └── testng-mobile.xml
├── screenshots/                       # Auto-generated screenshots
├── recordings/                        # Auto-generated recordings
├── logs/                             # Auto-generated logs
├── pom.xml
└── README.md
```

## 🔧 Configuration

### Appium Server Settings

The framework automatically manages Appium server lifecycle:

```java
// Start server programmatically
AppiumServerManager.startServer();

// Stop server
AppiumServerManager.stopServer();
```

### Device Configuration

Update TestNG XML parameters:

```xml
<parameter name="platform" value="android"/>
<parameter name="deviceName" value="Your_Device_Name"/>
<parameter name="platformVersion" value="11.0"/>
<parameter name="appPath" value="path/to/your/app.apk"/>
```

### Browser Configuration

```xml
<parameter name="browserName" value="chrome"/>
<!-- or -->
<parameter name="browserName" value="firefox"/>
```

## 📊 Test Reporting

### Built-in Reports

- **TestNG Reports**: `target/surefire-reports/`
- **Screenshots**: `screenshots/` directory
- **Screen Recordings**: `recordings/` directory
- **Logs**: `logs/` directory

### Custom Reporting

Extend `TestListener.java` to integrate with:
- ExtentReports
- Allure
- Custom reporting solutions

## 🚨 Troubleshooting

### Common Issues

1. **Appium Server Connection Failed**
   ```bash
   # Check if port is available
   lsof -i :4723

   # Kill existing processes
   pkill -f appium
   ```

2. **Android Device Not Detected**
   ```bash
   adb devices
   adb kill-server
   adb start-server
   ```

3. **iOS Simulator Issues**
   ```bash
   xcrun simctl list devices
   xcrun simctl shutdown all
   xcrun simctl boot "Device Name"
   ```

4. **WebDriver Issues**
   - Framework uses WebDriverManager for automatic driver management
   - Check browser versions match ChromeDriver/GeckoDriver versions

### Debug Mode

Enable debug logging in `log4j2.xml`:

```xml
<Logger name="com.automation" level="DEBUG">
```

## 🧪 Writing New Tests

### 1. Create Page Object

```java
public class YourAppPage {
    private AppiumDriver driver;

    public YourAppPage() {
        this.driver = BaseTest.getMobileDriver();
        // Initialize page elements
    }

    public void performAction() {
        // Implement page actions
    }
}
```

### 2. Create Test Class

```java
public class YourAppTest extends BaseTest {

    @Test
    public void testYourApp() {
        YourAppPage page = new YourAppPage();
        page.performAction();
        // Add assertions
    }
}
```

### 3. Update TestNG XML

Add your test class to appropriate TestNG XML file.

## 🤝 Contributing

1. Fork the repository
2. Create feature branch
3. Make changes with tests
4. Submit pull request

## 📄 License

This project is licensed under the MIT License.

## 📞 Support

For issues and questions:
- Create GitHub issue
- Check troubleshooting section
- Review logs in `logs/` directory

## 📚 Additional Resources

- [Appium Documentation](http://appium.io/docs/)
- [Selenium Documentation](https://selenium-python.readthedocs.io/)
- [TestNG Documentation](https://testng.org/doc/)
- [Maven Documentation](https://maven.apache.org/guides/)
