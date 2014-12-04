$(document).ready(function() {
	$('#forgotPasswordLink').click(function(event) {
		ResetPassword.display();
		event.preventDefault();
	});
	// Added for Forgot UserName Feature
	$('#forgotUserLink').click(function(event){
		EmailUserName.displayDialog();
		event.preventDefault();
	});

	$('#createNewAccountLink').click(function(event) {
		Login.showRegistration();
		event.preventDefault();
	});

	$('#loginUserName').keypress(function(event) {
		if (event.keyCode == '13') {
			this.blur();
			Login.login();
			return false;
		}
	});

	$('#loginPassword').keypress(function(event) {
		if (event.keyCode == '13') {
			this.blur();
			Login.login();
			return false;
		}
	});

	$('#loginLink').click(function() {
		Login.login();
		return false;
	});

	$('#loginForm').submit(function() {
		//Make certain that the form doesn't submit normally
		return false;
	});

	// Set the focus to the userName field
	$('#loginUserName').focus();
});

var Login = {
	login : function(force) {
		var $form = $('#loginForm');
		if (force) {
			$('#loginForce').val(true);
		} else {
			$('#loginForce').val(false);
		}
		$.ajax({
			type: 'POST',
			cache: false,
			async: true,
			dataType: 'json',
			url: $form.attr('action'),
			data: $form.serialize(),
			context: Login,

			success: function (data) {
				this.loginResult(data);
			},

			error: function (xmlHttpRequest, textStatus, errorThrown) {
				Util.handleAjaxError(xmlHttpRequest, textStatus, errorThrown);
			}
		});
	},

	loginResult : function(data) {
		if (('INVALID' === data.resultCode) || ('UNAUTHORIZED' === data.resultCode) || ('UNVERIFIED' === data.resultCode)) {
			//Determine the text based on the error code
			var text = '';
			if ('INVALID' === data.resultCode) {
				text = 'The user name and password entered do not match any existing users in the system.';
			} else if ('UNAUTHORIZED' === data.resultCode) {
				text = 'Sorry, user ' + $('#loginUserName').val() + ' is not authorized for this application.';
			} else if ('UNVERIFIED' === data.resultCode) {
				text = 'You are currently blocked from login until you have validated your email address.';
			}

			//Display the error dialog
			Dialog.error({
				dialogControls : [
					{
						type : 'button',
						text : 'OK',
						onClick : function(event) {
							var $dialog = Dialog.getDialog($(event.target));
							$dialog.dialog('close');
							$dialog.dialog('destroy');

							$('#loginUserName').focus();
							return false;
						}
					}
				],
				content: text
			});
		} else if ('CONCURRENT' === data.resultCode) {
			var text = '<p>You are attempting to log into ConnectED from more than one location.</p>'
					+ '<p>Please click <strong>Proceed</strong> to log in at this location and have the system automatically log you out of the other location.</p>'
					+ '<p>If you want to remain logged in at the other location, click <strong>Cancel</strong> to abandon this login attempt.</p>';
			Dialog.warning({
				dialogControls : [
					{
						type : 'button',
						text : 'Cancel',
						onClick : function(event) {
							var $dialog = Dialog.getDialog($(event.target));
							$dialog.dialog('close');
							$dialog.dialog('destroy');

							$('#loginUserName').focus();
							return false;
						}
					},

					{
						type : 'button',
						text : 'Proceed',
						onClick : function(event) {
							var $dialog = Dialog.getDialog($(event.target));
							$dialog.dialog('close');
							$dialog.dialog('destroy');

							Login.login(true);
							return false;
						}
					}
				],
				content: text
			});
		} else if ('SUCCESS' === data.resultCode) {
			window.location = data.redirect;
		} else if ('INITIAL_PASSWORD_CHANGE' === data.resultCode) {
			InitialPasswordChange.display();
		} else {
			Dialog.systemError();
		}
	},

	showRegistration : function() {
		var that = this;
		Dialog.create({
			title : 'Create a New Account',
			container: $('#registrationDialog'),
			width : 680,
			dialogControls : [
				{
					type : 'button',
					text : 'Cancel',
					onClick : function(event) {
						$('#redCode1').val('');
						$('#redCode2').val('');
						$('#redCode3').val('');

						var $dialog = Dialog.getDialog($(event.target));
						$dialog.dialog('close');
						$dialog.dialog('destroy');
						return false;
					}
				}
			]
		});
	},


	doStudentRegistration : function() {
		var redemptionCode = $.trim($('#redCode1').val()) + $.trim($('#redCode2').val()) + $.trim($('#redCode3').val());
		$.ajax({
			type: 'POST',
			cache: false,
			async: false,
			dataType: 'json',
			url: 'validateRedemptionCode.do',
			data: {redemptionCode:redemptionCode},

			success: function (data) {
				if (!data.hasErrors) {
					//use sanitized redemption code
					window.location.href = "studentRegistration.do?redemptionCode=" + encodeURIComponent(data.redemptionCode);
				} else {
					var errorHtml = '<ul>';
					if (data.globalErrors) {
						$.each(data.globalErrors, function(index, errorMessage){
							errorHtml += ('<li>' + errorMessage + '</li>');
						});
					}

					if (data.fieldErrors) {
						$.each (data.fieldErrors, function (fieldName, errorMessages) {
							$.each(errorMessages, function(index, errorMessage) {
								errorHtml += ('<li>' + errorMessage + '</li>');
							});
						});
					}

					errorHtml += '</ul>';

					Dialog.error({
						content: errorHtml
					});
				}
			}
		});
	},

	doTeacherRegistration : function() {
		window.location.href = "/connected/teacherRegistration.do";
	}

};


/**
 * Splits the value from the first field into all the fields.
 * If the first character in a field will be a '-' it is left out.
 * This is to allow users to paste entire Master/Redemption Codes into the first field.
 */
function checkFieldLength(fields, fieldLength) {
	var inputString = $.trim($(fields[0]).val());
	var i=0;
	while (i < fields.length && inputString.length > 0) {
		if (inputString.length > fieldLength) {
			$(fields[i]).val(inputString.substring(0,fieldLength));
			if(inputString.charAt(fieldLength)=='-'){
				inputString = inputString.substring(fieldLength+1);
			} else {
				inputString = inputString.substring(fieldLength);
			}
			i++;
		} else {
			$(fields[i]).val(inputString);
			inputString = '';
		}
	}
	if (i == fields.length) {
		$(fields[i-1]).focus();
	} else if($(fields[i]).val().length == fieldLength && (i<fields.length-1)){
		$(fields[i+1]).focus();
	} else {
		$(fields[i]).focus();
	}
}

var ResetPassword = {
	$dialog : null,

	display : function() {
		var userName = $.trim($('#loginUserName').val());

		if (userName.length === 0) {
			Dialog.error({
				content: 'Your username must be provided before attempting to change your password.'
			});
		} else {
			$.ajax({
				type: 'POST',
				dataType: 'html',
				async: false,
				url: 'resetPasswordDisplay.do',
				data: 'userName=' + userName,
				success: function (data) {
					ResetPassword.openDialog(data);
				},

				error: function (xmlHttpRequest, textStatus, errorThrown) {
					Util.handleAjaxError(xmlHttpRequest, textStatus, errorThrown);
				}
			});
		}
	},

	openDialog : function(data) {
		$('body').append(data);
		$dialog = Dialog.create({
			title : 'Reset Password',
			container: $('#resetPasswordDialog'),
			width : 680,
			dialogControls : [
				{
					type : 'button',
					text : 'Cancel',
					onClick : function(event) {
						ResetPassword.close();
						return false;
					}
				},

				{
					type : 'button',
					text : 'Reset Password',
					onClick : function(event) {
						// actual functionality here
						ResetPassword.resetPassword();
						return false;
					}
				}
			]
		});
	},

	resetPassword : function() {
		var $form = $('#resetPasswordForm');
		$.ajax({
			type: 'POST',
			async: false,
			dataType: 'json',
			url: $form.attr('action'),
			data: $form.serialize(),

			success: function (data) {
				ResetPassword.resetPasswordResult(data);
			},

			error: function (xmlHttpRequest, textStatus, errorThrown) {
				Util.handleAjaxError(xmlHttpRequest, textStatus, errorThrown);
			}
		});
	},

	resetPasswordResult : function(data) {
		if (data.hasErrors) {
			Dialog.validationErrors('Your password was not reset for the following reasons:', data);
		} else {
			Dialog.info({
				title: "Password Change Successful",
				content: "Your password has been changed."
			});

			ResetPassword.close();
		}
	},

	close : function() {
		if ($dialog) {
			$dialog.dialog('destroy');
			$('#resetPasswordDialog').remove();
			$dialog = null;
		}
	}
};


var InitialPasswordChange = {
	$dialog : null,

	display : function() {
		$.ajax({
			type: 'POST',
			dataType: 'html',
			async: false,
			url: 'initialPasswordChangeDisplay.do',
			success: function (data) {
				InitialPasswordChange.openDialog(data);
			}
		});
	},

	openDialog : function(data) {
		$('body').append(data);
		$dialog = Dialog.create({
			title : 'Set Password',
			container: $('#initialPasswordChangeDialog'),
			width : 600,
			dialogControls : [
				{
					type : 'button',
					text : 'Cancel',
					onClick : function(event) {
						window.location = "/connected/logout.do";
						return false;
					}
				},

				{
					type : 'button',
					text : 'Set Password',
					onClick : function(event) {
						InitialPasswordChange.validate();
						return false;
					}
				}
			]
		});
	},

	validate : function() {
		var $form = $('#initialPasswordChangeForm');
		$.ajax({
			type: 'POST',
			async: false,
			dataType: 'json',
			url: $('#initialPasswordChangeValidationAction').val(),
			data: $form.serialize(),
			success: function (data) {
				if (data.hasErrors) {
					Dialog.validationErrors('Your password was not set for the following reasons:', data);
				} else {
					$('#initialPasswordChangeDialog').dialog('close');
					InitialPasswordChange.showEula();
				}
			},
			error: function (xmlHttpRequest, textStatus, errorThrown) {
				Util.handleAjaxError(xmlHttpRequest, textStatus, errorThrown);
			}
		});
	},

	showEula : function() {
		$.ajax({
			type: 'GET',
			dataType: 'html',
			async: false,
			url: 'eula.mchDialog.do',
			success: function (data) {
				InitialPasswordChange.openEulaDialog(data);
			}
		});
	},

	openEulaDialog : function(data) {
		$('body').append(data);
		$dialog = Dialog.create({
			title : 'ConnectED Terms of Service',
			container: $('#eulaDialog'),
			width : 640,
			height: 520,
			dialogClass: 'formDialog eulaDialog',
			dialogControls : [
				{
					type : 'button',
					text : 'No, I Decline',
					onClick : function(event) {
						window.location = "/connected/logout.do";
						return false;
					}
				},

				{
					type : 'button',
					text : 'Yes, I Accept',
					onClick : function(event) {
						InitialPasswordChange.setPassword();
						return false;
					}
				}
			]
		});
	},


	setPassword : function() {
		var $form = $('#initialPasswordChangeForm');
		$.ajax({
			type: 'POST',
			async: false,
			dataType: 'json',
			url: $form.attr('action'),
			data: $form.serialize(),

			success: function (data) {
				InitialPasswordChange.setPasswordResult(data);
			},

			error: function (xmlHttpRequest, textStatus, errorThrown) {
				Util.handleAjaxError(xmlHttpRequest, textStatus, errorThrown);
			}
		});
	},

	setPasswordResult : function(data) {
		if (data.hasErrors) {
			Dialog.validationErrors('Your password was not set for the following reasons:', data);
		} else {
			Dialog.info({
				title: "Password Change Successful",
				content: "Your password has been changed.",
				dialogControls : [
					{
						type : 'button',
						text : 'OK',
						onClick : function(event) {
							window.location = "/connected/home.do"
							return false;
						}
					}
				]
			});
		}
	},

	close : function() {
		if ($dialog) {
			$dialog.dialog('destroy');
			$('#initialPasswordChangeDialog').remove();
			$dialog = null;
		}
	}
};
// Forgot Username feature
var EmailUserName = {
	$dialog : null,
	displayDialog : function(){
		$.ajax({
			type: 'GET',
			dataType: 'html',
			async: false,
			url: 'emailUserNameDisplay.do',
			data: 'emailUserDialog',
			success: function (data) {
				EmailUserName.openDialog(data);
			},

			error: function (xmlHttpRequest, textStatus, errorThrown) {
				Util.handleAjaxError(xmlHttpRequest, textStatus, errorThrown);
			}
		});
	},

	openDialog : function(data){
		$('body').append(data);
		$dialog = Dialog.create({
			title : 'Forgot Username',
			container: $('#emailUserNameDialog'),
			width : 600,
			dialogControls : [
				{
					type : 'button',
					text : 'Cancel',
					onClick : function(event) {
						EmailUserName.close();
						return false;
					}
				},

				{
					type : 'button',
					text : 'Submit',
					onClick : function(event) {
						// actual functionality here
						EmailUserName.submitEmail(event);
						return false;
					}
				}
			]
		});
		$('#emailUserNameDialog input').bind('keydown', function(event) {
			// It seems that forms with a SINGLE visible input cause a regular form
			// submission on 'enter' keydown, this causes a page reload or download
			// to prevent this we'll prevent the default action, but allow the event to propagate
			if(event.keyCode == 13) {
				event.preventDefault();
				EmailUserName.submitEmail(event);
			}
		});
	},

	submitEmail : function (event){
		var $form = $('#emailUserForm');
		$.ajax({
			type: 'POST',
			async: false,
			dataType: 'json',
			url: $form.attr('action'),
			data: $form.serialize(),

			success: function (data) {
				EmailUserName.submitEmailResults(data);
			},

			error: function (xmlHttpRequest, textStatus, errorThrown) {
				Util.handleAjaxError(xmlHttpRequest, textStatus, errorThrown);
			}
		});
	},

	submitEmailResults : function(data) {
		if (data.hasErrors) {
			Dialog.validationErrors('',data);
		} else {
			Dialog.info({
				title: "Username Emailed",
				content: "Your username will be sent to the email address you have provided. If you do not receive an email please contact Digital Technical Support at 800-437-3715."
			});

			EmailUserName.close();
		}
	},

	close : function() {
		if ($dialog) {
			$('#emailUserNameDialog input').unbind('keydown');
			$dialog.dialog('destroy');
			$('#emailUserNameDialog').remove();
			$dialog = null;
		}
	}

};