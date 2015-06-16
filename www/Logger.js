module.exports = {
  initialize: function(filename,scb,fcb) {
  	console.log("now Initializing")
    cordova.exec(scb,
    fcb,
     "Logger",
     "initialize",
     [filename]);
  },

  write : function (data,scb,fcb){
  	console.log("now writing");
  	cordova.exec(scb,
  	fcb,
  	"Logger",
  	"write",
  	[data]
  	);
  },

  close : function (scb,fcb){
  	console.log("now closing");
  	cordova.exec(scb, fcb,
  		"Logger",
  		"close",
  		[]);
  }
}