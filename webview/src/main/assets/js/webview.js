var webviewjs = {};
webviewjs.os = {};
webviewjs.os.isIOS = /iOS|iPhone|iPad|iPod/i.test(navigator.userAgent);
webviewjs.os.isAndroid = !webviewjs.os.isIOS;
webviewjs.callbacks = {}

webviewjs.callback = function(callbackname, response) {
   console.log("[callback] callbackname=");
   var callbackobject = webviewjs.callbacks[callbackname];
   if (callbackobject !== undefined){
       if(callbackobject.callback != undefined){
          console.log("[callback] response="+response);
            var ret = callbackobject.callback(response);
           if(ret === false){
               return
           }
           delete webviewjs.callbacks[callbackname];
       }
   }
}

webviewjs.dispatchEvent = function(response){
    console.log("[dispatchEvent] response="+JSON.stringify(response));
}

webviewjs.takeNativeAction = function(commandname, parameters){
    console.log("webviewjs takenativeaction")
    var request={};
    request.name=commandname;
    request.param = JSON.stringify(parameters);
    if(window.webviewjs.os.isAndroid){
        console.log("android take native action" + JSON.stringify(request));
        window.walkerJs.takeNativeAction(JSON.stringify(request));
    } else {
        window.webkit.messageHandlers.walkerJs.postMessage(JSON.stringify(request))
    }
}

webviewjs.takeNativeActionWithCallback = function(commandname, parameters, callback) {
    var callbackname = "nativetojs_callback_" +  (new Date()).getTime() + "_" + Math.floor(Math.random() * 10000);
    webviewjs.callbacks[callbackname] = {callback:callback};
    var request = {};
    request.name = commandname;
    request.param = JSON.stringify(parameters);
    request.callbackname = callbackname;
    if(window.webviewjs.os.isAndroid){
        window.walkerJs.takeNativeAction(JSON.stringify(request));
    } else {
        window.webkit.messageHandlers.walkerJs.postMessage(JSON.stringify(request))
    }
}

window.webviewjs = webviewjs;
