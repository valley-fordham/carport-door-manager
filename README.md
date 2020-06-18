# Carport Door Manager for Android

Simple carport door controller, designed to interface with a home web server which can open/close/stop and return the status of your carport door.

### Features:
- Big shiny button for opening/closing/stopping your carport door
- Door status displayed on screen
- Automatic background service checks the status of your door and displays a toast notification if open, will display an error if unavailable
- Environment configurable - point to your home server and provide URL parameters which suit your environment
- App-side polling configurable - modify the interval of door status checks for both the main screen and background service

## Install
1. Setup a home web server that can listen for your web-hooks and operate the door/return its status
2. Download and install the latest [APK](https://github.com/valley-fordham/carportdoormanager/releases)

## Usage
1. Open the 'Door Manager' app, and click the 'Settings' spanner
2. Set the Host details and the URL parameters to suit your web server. (optional: modification of the default polling intervals is optional but not required)
3. Press the big button to open/close/stop your carport door (this may depend on how you've interfaced your web server with your carport door)
4. A message will report success or failure of the button trigger

## License
[![Creative Commons Licence](https://i.creativecommons.org/l/by-nc/4.0/88x31.png)](http://creativecommons.org/licenses/by-nc/4.0/)