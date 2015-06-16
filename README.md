# Logger for Android Only
A Cordova Plugin for Sensor Logging using java buffered writing.

Logger.initialize( filename, successCallBack, errorCallBack)
------------------------------------------------------------

Logger.write(data, successCallBack , errorCallBack)
---------------------------------------------------

Logger.close(data, successCallBack, errorCallBack)
--------------------------------------------------


Should be closed at the very end execution otherwise any residual buffers will be discarded.

- Romba