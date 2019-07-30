# Interposition Android Sdk
Interposition's Android Sdk is a wrapper around the main API calls to Interposition's REST API. It  Simplifies the calls by managing token handling and parsing the results. In addition the sdk define interfaces  for interacting with Interposition I-Frame.
## Prerequisites
**Minimum android SDK :**   23,
**Required permissions:**  
android.permission.INTERNET, android.permission.ACCESS_NETWORK_STATE
## installation
***step 1.*** Add it in your root build.gradle at the end of repositories:
```json
		allprojects {
			repositories {
				...
				maven { url 'https://jitpack.io' }
			}
		}
```
***step 2.*** Add the dependency:
```json
		dependencies {
			implementation 'com.github.YaronAA:IntraPosition-android-sdk:Tag'
		}
```
##  Getting Started
 In order to use the sdk you must first initilize it with a context and base url.
 ```javascript
   BuzCartSdk.getInstance().init(getApplicationContext(),baseUrl) 
 ```
 baseUrl is the url of intrapositions server ( e.g. .....)
