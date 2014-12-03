var Logger = {

	//Default logging level is DEBUG which displays messages for any loggingLevel with a value less than
	//or equal to the loggingLevel. Setting loggingLevel to 0, or no logging, returns immediately.

	loggingLevel:5,

	level:function (str) {
		switch(str.toUpperCase()) {
			case "DEBUG":
				this.loggingLevel = 5;
			break;
			case "INFO":
				this.loggingLevel = 4;
			break;
			case "WARN":
				this.loggingLevel = 3;
			break;
			case "ERROR":
				this.loggingLevel = 2;
			break;
			case "SEVERE":
				this.loggingLevel = 1;
			break;
			case "NONE":
				this.loggingLevel = 0;
			break;
		}
	},
	
	getLevel:function () {
		return this.loggingLevel;
	},
	
	debug:function (p0, p1, p2) {
		if (this.loggingLevel == 0) {
			return;
		} else if (window.console) {
			if (this.loggingLevel == 5) {
				this.formatMessage("DEBUG", p0, p1, p2);
			}
		}
	},
	
	info:function (p0, p1, p2) {
		if (this.loggingLevel == 0) {
			return;
		} else if (window.console) {
			if (this.loggingLevel == 4 || (this.loggingLevel != 4 && this.loggingLevel > 4)) {
				this.formatMessage("INFO", p0, p1, p2);
			}
		}
	},
	
	warn:function (p0, p1, p2) {
		if (this.loggingLevel == 0) {
			return;
		} else if (window.console) {
			if (this.loggingLevel == 3 || (this.loggingLevel != 3 && this.loggingLevel > 3)) {
				this.formatMessage("WARN", p0, p1, p2);
			}
		}
	},
	
	error:function (p0, p1, p2) {
		if (this.loggingLevel == 0) {
			return;
		} else if (window.console) {
			if (this.loggingLevel == 2 || (this.loggingLevel != 2 && this.loggingLevel > 2)) {
				this.formatMessage("ERROR", p0, p1, p2);
			}
		} 
	},
	
	severe:function (p0, p1, p2) {
		if (this.loggingLevel == 0) {
			return;
		} else if (window.console) {
			this.formatMessage("SEVERE", p0, p1, p2);
		}
	},
	
	formatMessage:function (type, p0, p1, p2) {
	
		var str = type;
		var obj = null;

		if (p0) {
			if (typeof p0 != 'object') {
				str += ":" + p0.toString();
			} else {
				obj = p0;
			}
		}
		
		if (p1) {
			if (typeof p1 != 'object') {
				str += ":" + p1.toString();
			} else {
				obj = p1;
			}
		}
		
		if (p2) {
			if (typeof p2 != 'object') {
				str += ":" + p2.toString();
			} else {
				obj = p2;
			}
		}

		if (obj) {
			console.log(str, obj);
		} else {
			console.log(str);
		}
	}
}