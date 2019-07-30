# Intraposition Android Sdk
Intraposition's Android Sdk is a wrapper around the main API calls to Intraposition's REST API. It  Simplifies the calls by managing token handling and parsing the results. In addition the sdk define interfaces  for interacting with Intraposition I-Frame.
## Prerequisites
**Minimum android SDK :**   23,

**Required permissions:**  
android.permission.INTERNET, android.permission.ACCESS_NETWORK_STATE
## installation
***step 1.*** Add it in your root build.gradle at the end of repositories:
```java
		allprojects {
			repositories {
				...
				maven { url 'https://jitpack.io' }
			}
		}
```
***step 2.*** Add the dependency:
```java
		dependencies {
			implementation 'com.github.YaronAA:IntraPosition-android-sdk:Tag'
		}

