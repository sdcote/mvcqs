/*
 * Bind the Global Listeners common to all pages
 */
$(document).ready(function() {
	//Check for the presence of an error or warning dialog on the page
	if (0 != $('#pageErrors').length) {
		Dialog.error({
			content: $('#pageErrors').html()
		});
	};

	if (0 != $('#pageWarnings').length) {
		Dialog.warning({
			content: $('#pageWarnings').html()
		});
	};

	if (0 != $('#pageInfos').length) {
		Dialog.info({
			content: $('#pageInfos').html()
		});
	};
});

/**
 * Utility methods for creating and working with jQuery Dialogs
 */
DialogImpl = function() {
};
DialogImpl.prototype = {
	DEFAULT_DIALOG_OPTIONS : {
		modal : true,
		resizable : true,
		closeOnEscape : false,
		width: 350,
		height: 'auto',
		draggable: true,
		dialogClass:'formDialog',
		dialogControlOrder: 'reversed',
		onConfirm: function(){},
		onCancel: function(){}
	},
	DEFAULT_MESSAGE_DIALOG_OPTIONS : {
		closeOnEscape : false
	},

	/**
	 * Creates a new jQuery Dialog with special handling to populate our own
	 * custom buttons and dialog controls rather than the default buttons.  In addition
	 * to the standard options, this method supports the following options:
	 * 		- container: A jQuery object corresponding to the DOM element to create the dialog
	 * 			from.  If absent, a empty div is used instead.
	 * 		- content: The optional html content that will be appended to the container.
	 * 		- dialogControls: Specifies how the dialog control bar is populated.  This is
	 * 			intended for use as a replacement to the standard Buttons option.  See the
	 * 			createDialogControls method for more details.
	 * 	    - dialogControlOrder: Specifies the ordering of the controls within the dialogControls
	 * 	    	array. This is either 'reversed' or 'normal'.  Defaults to reversed.
	 * 		- initDialogControls: A optional callback function that is called after the dialog
	 * 			controls have been created but before the open event listener is called.
	 */
	create : function(options) {
		var that = this;
		var combinedOptions = $.extend({}, this.DEFAULT_DIALOG_OPTIONS, options);

		if (combinedOptions.hideTitlebarClose) {
			if (combinedOptions.dialogClass) {
				combinedOptions.dialogClass = combinedOptions.dialogClass + " dialog-titlebar-no-close";
			} else {
				combinedOptions.dialogClass = "dialog-titlebar-no-close";
			}
		}

		var $container = null;
		if (combinedOptions.container) {
			$container = combinedOptions.container;
		} else {
			$container = $('<div/>');
		}
		if (combinedOptions.content) {
			$container.html(combinedOptions.content);
		}
		this.createDialogControls(combinedOptions, $container);

		return $container.dialog(combinedOptions);
	},

	/**
	 * Creates the dialog controls within the existing jQuery buttonPane based on the value
	 * of the options.dialogControls value.  This value is expected to be one of two things:
	 * 		1. A jQuery selector string that will be executed within the context of the dialog
	 * 			container to lookup the dialog controls element.
	 * 		2. An array of control definitions objects.  Each object must include the
	 * 			following fields:
	 * 				- type: Either 'button' or 'link'.  Defaults to button.
	 * 				- text: The text of the control
	 * 				- onClick: The click handler for the control.
	 * 				- cssClass: The css class to apply to the controls
	 */
	createDialogControls : function(options, $container) {
		var that = this;
		var $dialogControls = null;
		if (options.dialogControls) {
			if (options.dialogControls instanceof Array) {
				$dialogControls = $('<div class="dialogControls" />');
				var $buttonBar = $('<div class="dialogButtons horizontal-button-group" />').appendTo($dialogControls);

				function createControl(control) {
					if (control != undefined) {
						var $control = that.createDialogControl(control);
						$buttonBar.append($control);
					}
				}

				if ('normal' === options.dialogControlOrder) {
					$.each(options.dialogControls, function(index, control) {
						createControl(control);
					});
				} else {
					for (var i=options.dialogControls.length - 1; i >=0; i--) {
						var control = options.dialogControls[i];
						createControl(control);
					}

				}
			} else {
				$dialogControls = $(options.dialogControls, $container);
			}
		}
		if ($dialogControls) {
			options.buttons = { 'REMOVEME' : function() {} };

			var currentOpen = options.open;
			options.open = function(event, ui) {
				var $buttonPane = that.getDialogButtonPane($(event.target));
				$buttonPane.empty();
				$buttonPane.append($dialogControls);

				if (typeof options.initDialogControls ==='function') {
					options.initDialogControls($buttonPane);
				}

				if (typeof currentOpen === 'function') {
					currentOpen(event, ui);
				}
			};
		}
	},

	createDialogControl : function(control) {
		var $control = $('<a href="#" class="button"><span><span class="btext">' + control.text + '</span></span></a>');
		if (control.id) {
			$control.attr('id', control.id);
		}
		if (control.cssClass) {
			$control.addClass(control.cssClass);
		}
		$control.click(control.onClick);
		return $control;
	},

	/**
	 * Convenience method for creating simple message dialogs.  This uses slightly different default
	 * options than the Dialog.create method and will create an OK button if no other controls are
	 * specified.
	 */
	message : function(options) {
		var that = this;
		var combinedOptions = $.extend({}, this.DEFAULT_MESSAGE_DIALOG_OPTIONS, options);
		if (!combinedOptions.dialogControls) {
			//Default to the OK Button
			combinedOptions.dialogControls = [{
				type : 'button',
		    	text : 'OK',
		    	onClick : function(event) {
		    		that.closeDialog(event, options);
		    		return false;
		    	}
			}];
		}

		this.create(combinedOptions);
	},

	error : function(options) {
		options = (typeof options !== 'string' ? options : { content: options });
		var combinedOptions = $.extend({}, { title : 'Error', dialogClass : 'errorDialog' }, options);
		this.message(combinedOptions);
	},

	warning : function(options) {
		options = (typeof options !== 'string' ? options : { content: options });
		var combinedOptions = $.extend({}, { title : 'Warning', dialogClass : 'warningDialog' }, options);
		this.message(combinedOptions);
	},

	info : function(options) {
		options = (typeof options !== 'string' ? options : { content: options });
		var combinedOptions = $.extend({}, { title : 'Info', dialogClass : 'infoDialog' }, options);
		this.message(combinedOptions);
	},

	confirm : function(options) {
		var that = this;
		var combinedOptions = $.extend({}, this.DEFAULT_MESSAGE_DIALOG_OPTIONS, options);

		if (!combinedOptions.dialogControls) {
			//Default to OK/Cancel Buttons
			combinedOptions.dialogControls = [
			    {
					type: 'link',
					text: combinedOptions.cancelLabel ? combinedOptions.cancelLabel : 'Cancel',
			    	onClick:function(event){
			    		that.closeDialog(event, options);
			    		if (typeof options.onCancel === 'function') {
			    			options.onCancel();
			    		}
			    	}
			    },
				{
					type: 'button',
					text: combinedOptions.confirmLabel ? combinedOptions.confirmLabel : 'OK',
					onClick:function(event){
						that.closeDialog(event, options);
						if (typeof options.onConfirm === 'function') {
							options.onConfirm();
						}
					}
				}];
		}

		this.create(combinedOptions);
	},

	closeDialog : function(event, options) {
		var $dialog = this.getDialog($(event.target));
		$dialog.dialog('close');
		if (options.container) {
			//Destroy the dialog without removing it
			$dialog.dialog('destroy');
		} else {
			//Completely remove the dialog html from the page.
			$dialog.remove();
		}
		event.preventDefault();
	},

	getDialog : function($element) {
		var $dialog = $element.closest('.ui-dialog').find('.ui-dialog-content');
		return $dialog.length ? $dialog : null;
	},

	getDialogWidget : function($element) {
		var $dialogWidget = $element.closest('.ui-dialog');
		return $dialogWidget.length ? $dialogWidget : null;
	},

	getDialogButtonPane : function($element) {
		var $buttonPane = $element.closest('.ui-dialog').find('.ui-dialog-buttonpane');
		return $buttonPane.length ? $buttonPane : null;
	},

	getDialogTitle : function($element) {
		var $title = $element.closest('.ui-dialog').find('.ui-dialog-title');
		return $title.length ? $title : null;
	},

	/*
	 * Displays a generic error for use in cases where an unexpected system error.
	 */
	systemError : function() {
		this.error({
			content : 'The system has encountered an unexpected error and is unable to proceed. Please contact Technical Support.'
		});
	},

	/*
	 * Displays a generic error for use in cases where an authorization error occrs.
	 */
	authorizationError : function() {
		//Should not occur in normal usage, so just display a system error message
		this.systemError();
	},

	/*
	 * Displays a generic error for use in cases where AJAX encounters issues communicating with the server.
	 */
	connectionError : function() {
		this.error({
			content : 'The system was unable to communicate with the server. Please try again. If the error continues, contact Technical Support.'
		});
	},

	/*
	 * Displays an error dialog for displaying a list of validation error messages.  The contents of the
	 * dialog will consist of an introductory message and then a list of errors.
	 *
	 * @param message The intro message
	 * @param message An object containing at least one of the following fields:
	 * 		- 'fieldErrors' : This is a Map of field names to messages.
	 * 		- 'globablErrors' : This is a List of messages.
	 */
	validationErrors : function(message, errors) {
		var check = [];
		var $errorList = $('<ul>');

		function addErrorToList(value) {
			if (!check[value]) {
				check[value] = true;
				$errorList.append('<li>' + value + '</li>');
			}
		}

		if (errors.fieldErrors) {
			$.each(errors.fieldErrors, function(key, values) {
				for (var i = 0; i < values.length; i++) {
					addErrorToList(values[i]);
				}
			});
		}
		if (errors.globalErrors) {
			for (var i = 0; i < errors.globalErrors.length; i++) {
				addErrorToList(errors.globalErrors[i]);
			}
		}

		var $errors = $('<div><p>' + message + '</p></div>')
			.addClass('pageMessages')
			.append($errorList);
		this.error({
			content: $errors
		});
	}
};
var Dialog = new DialogImpl();

DialogLink = function(text, onClick) {
	this.type = 'link';
	this.text = text;
	this.onClick = onClick;
}
DialogButton = function(text, onClick) {
	this.type = 'button';
	this.text = text;
	this.onClick = onClick;
}

/**
 * Creates a new Form Dialog popup that retrieves the form popup html from the provided url
 * and submits the returned form to the defined action on the form using ajax. This will
 * currently support only 1 form per popup.The class that is defined on the popup's div tag
 * is used as the class for the dialog. Also displays validation errors on the form if there
 * are any. In addition to the standard options, this method supports the following options:
 * 		- popupUrl: The URL to retrieve the popup content from
 *      - title: 	The title of the popup
 *      - submitBtnName:The text to display for the submit button
 *      - onSubmit:		The function to call after the response has been returned from clicking
 *      			the submit button on the popup. Takes the result from the ajax reponse
 *      			as an argument
 *      - onCancel: The function to call if the form is canceled
 *      - additionalDialogControls : Any additional buttons to prepend in front of the Submit
 *      							and cancel buttons
 */

FormDialogImpl = function() {
};

FormDialogImpl.prototype = {
	DEFAULT_FORMDIALOG_OPTIONS : {
		title: 'Input Form',
		onSubmit: function(){return true;},
		onCancel: function(){},
		additionalDialogControls: null,
		width: 300,
		height: 'auto',
		resizable: false,
		closeOnEscape : false,
		submitOnEnter : true,
		formFieldLookup : {}
	},

	create : function(options) {
		var that = this;
		var combinedOptions = $.extend({}, this.DEFAULT_FORMDIALOG_OPTIONS, options);
		$.ajax({
			type: 'GET',
			async: false,
			url: options.popupUrl,
			data: options.popupUrlData,
			dataType: 'html',
			success: function(popupHtml) {
				that._popupHtml = popupHtml;
				that._dialog = that.createFormDialog(combinedOptions);
	        	that._popupId = $(popupHtml).attr("id");

			}
		});
		return this;
	},

	createFormDialog : function (options){
		var that = this;
		var combinedOptions = $.extend({}, options, {
			 content: that._popupHtml,
	         dialogClass: $(that._popupHtml).attr('class'),
	         dialogControls : that.buildFormDialogControls(options)
		});
		var dialog = Dialog.create(combinedOptions);
		if (options.onCancel){
			if ( $('.ui-dialog-titlebar-close').length > 0 ) {
				$('.ui-dialog-titlebar-close', $(dialog).parent()).click(function(event) {
					 options.onCancel(event);
					 that.closeDialog(event);
				});
			}
		}
		//Prevent enter key submission on the form
		$(dialog).keypress(function(event) {
			if(event.keyCode == 13) {
				if (combinedOptions.submitOnEnter){
					$(".submit", $(event.target).parents(".ui-dialog").first()).trigger("click");
					return false;
				}
		    }
	    });

		return dialog;
	},

	buildFormDialogControls: function (options){
		var that = this;
		var dialogControls = [
			{
				type : 'link',
			    text : 'Cancel',
			    cssClass : 'cancel',
			    onClick : function(event) {
			    	options.onCancel(event);
			    	that.closeDialog(event);
			    	return false;
			    }
			},
            {
                type : 'button',
                cssClass : 'submit',
                text : options.submitBtnName,
                onClick : function(event){
               	 	that.submitButton(event, options);
               	 	return false;
                }
            }
         ];
		if (options.additionalDialogControls){
			dialogControls = options.additionalDialogControls.concat(dialogControls);
		}
		return dialogControls;
	},

	submitButton: function(event, options){
		var that = this;
		success = true;
		$form = $('form', $(that._dialog));
		if(!options.noSubmit){
			$.ajax({
				type: 'POST',
				async: false,
				url: $form.attr('action'),
				data: $form.serialize(),
				beforeSend: function(){
					if (options.beforeSubmit != undefined){
						options.beforeSubmit();
					}
				},
				success: function(result) {
					if (result.hasErrors) {
						if (options.errorIdLookup) {
							Util.populateErrors(result, options.errorIdLookup);
						} else {
		        			var globalErrMsg="";
		        			if (result.globalErrors){
		        				$.each(result.globalErrors,function(index,err){
			        				globalErrMsg+="<div class='error'>" + err + "</div>";
			        			});
		        			}
		        			$('span#globalErrors', that._dialog).html(globalErrMsg);
		        			$('label > .error', that._dialog).html("");
		        			$.each (result.fieldErrors, function (fieldName, errors) {
		        				var fieldId = options.formFieldLookup[fieldName];
		        				if (!fieldId) {
		        					fieldId = fieldName;
		        				}
		                		$('label[for=' + fieldId + '] > .error', that._dialog).html(errors[0]);
		                	});
						}
	        		} else {
	        			that.closeDialog(event);
	        			options.onSubmit(result);
	        		}
					if (options.completeSubmit != undefined){
						options.completeSubmit();
					}
				}
			});
		}
		return success;
	},

	isDataChanged: function(){
		var that = this;
		var changedData = false;
		$('input[id]', that._resultForm).each(function(){
			origValue = $( 'input#' + $(this).attr('id'), $(that._popupHtml)).val();
			if (/^\s*$/.test($(this).val()) || $(this).val() != origValue){
				changedData = true;
			}
		});
		return changedData;
	},

	closeDialog: function(event) {
		var that = this;
        var $dialog = Dialog.getDialog($(event.target));
        $dialog.dialog('close');
        $('#' + that._popupId).remove();
        $dialog.dialog('destroy');
        event.preventDefault();
    },

    hideDialog: function() {
		var that = this;
		var $dialog = Dialog.getDialog(that._dialog);
		$dialog.parent().hide();
    },

    showDialog: function(event) {
		var that = this;
		var $dialog = Dialog.getDialog(that._dialog);
		$dialog.parent().show();
    }
};
var FormDialog = new FormDialogImpl();

/**
 * This is a set if miscellaneous utility functions.
 */
UtilImpl = function(contextPath, resourcePath) {
	this.init(contextPath, resourcePath);
};
UtilImpl.prototype = {
	init : function(contextPath, resourcePath) {
		this._contextPath = contextPath;
		this._resourcePath = resourcePath;

		this.months = {};
		this.months["JAN"] = "01";
		this.months["FEB"] = "02";
		this.months["MAR"] = "03";
		this.months["APR"] = "04";
		this.months["MAY"] = "05";
		this.months["JUN"] = "06";
		this.months["JUL"] = "07";
		this.months["AUG"] = "08";
		this.months["SEP"] = "09";
		this.months["OCT"] = "10";
		this.months["NOV"] = "11";
		this.months["DEC"] = "12";
	},

	getContextPath : function() {
		if (!this._contextPath) {
			throw "Context Path has not been set.";
		}

		return this._contextPath;
	},

	/**
	 * Returns the resource path under which the static resources are stored.  Use of this method is
	 * STRONGLY discouraged.  Whenever possible, paths to images should be defined in the page
	 * or within the CSS.
	 *
	 * Sample Usage: myImage.src = Util.getResourcePath() + '/images/someImage.gif'
	 */
	getResourcePath : function() {
		if (!this._resourcePath) {
			throw "Resource Path has not been set.";
		}

		return this._resourcePath;
	},

	/**
	 * Sets up a textarea such that the default text of the textbox clears if selected.
	 * And if unselected, the textbox returns back to its default text if the textbox is empty.
	 *
	 * @param	$field			The jQuery field element to apply the effect to.
	 * @param   defaultText		The default text to display for the textbox.
	 * @param 	cleanWhiteSpace	Boolean value/flag indicating whether or not to clean white space.
	 */
	setDefaultTextArea : function($field, defaultText, cleanWhiteSpace) {
		if ($field.length == 0) {
			return;
		}

		//If the field is currently empty, set the text of the field to the default text
		var initialValue = cleanWhiteSpace ? $.trim($field.val()) : $field.val();
		// Check if the search field does NOT have focus.  Jquery 1.5 does not support ':focus'
		if (!($field.get(0) === document.activeElement && ($field.get(0).type || $field.get(0).href ))) {
			if ((initialValue === '') || $field.hasClass('defaultText')) {
				$field.val(defaultText);
				$field.addClass('defaultText');
			}
		} else {
			if ($field.hasClass('defaultText')) {
				$field.val('');
				$field.addClass('defaultText');
			}
		}

		//Bind events to the focus and blur events to toggle the text as
		//needed
		$field.focus(function() {
	        if($(this).hasClass('defaultText')) {
	            $(this).val('');
	        	$(this).removeClass('defaultText');
	        }
	    });
		$field.blur(function() {
			var currentValue = cleanWhiteSpace ? $.trim($field.val()) : $field.val();
	        if(currentValue === '') {
	        	$(this).addClass('defaultText');
	            $(this).val(defaultText);
	        }
	    });
	},

	/**
	 * Provides default error handling for errors that come back from an Ajax required.  Depending on
	 * the type of error, this will either display the appropriate error message or in the case of a
	 * 401 code, redirect the user back to the login page.
	 *
	 * This method should be called with the local error handler for all Ajax calls in addition to any
	 * local cleanup that needs to be done in response to the error.  Eg:
	 * 		$.ajax({
	 * 			...
	 * 			error : function(xmlHttpRequest, textStatus, errorThrown) {
	 * 				Util.handleAjaxError(xmlHttpRequest, textStatus, errorThrown);
	 * 			}
	 * 		})
	 *
	 * @param xmlHttpRequest The XMLHttpRequest object
	 * @param textStatus	 A string describing the type of error that occurred
	 * @param errorThrown	 An optional exception object, if one occurred
	 */
	handleAjaxError : function(xmlHttpRequest, textStatus, errorThrown) {
		if (xmlHttpRequest.status === 401) {
			//The user's authentication is no longer valid, so we need to redirect
			//them to the login page.  Assuming we got this code from the AuthenticationFilter,
			//the redirect path should be specified in the JSON payload from the response.
			var data = null;
			try {
				data = jQuery.parseJSON(xmlHttpRequest.responseText);
			} catch (err) {
				//Ignore, we'll just handle this as a generic error
			}

			if (data.redirect) {
				window.location.replace(data.redirect);
				return;
			}
		} else if (xmlHttpRequest.status === 403) {
			Dialog.authorizationError();
		} else {
			//Popup the appropriate error dialog
			if (("timeout" === textStatus) || (xmlHttpRequest.status === 503)) {
				Dialog.connectionError();
			} else {
				var responseText = xmlHttpRequest.responseText ? $.trim(xmlHttpRequest.responseText) : '';
                if (0 != responseText.indexOf('<div class="mgh-dialog')) {
                    Dialog.systemError();
                } else {
                	Dialog.error({
            			content: responseText,
            			width : 600
            		});
                }
			}
		}
	},

	/**
	 * Clears the errors from a form.
	 *
	 * @param errorIdLookup A map from field names to dom ids for the error messages locations
	 */
	clearErrors : function(errorIdLookup) {
		//First clear the existing errors
		$.each(errorIdLookup, function(fieldName, errorMessageId) {
			$('#' + errorMessageId).html('').removeClass('error');
		});
	},

	/**
	 * Renders the errors in a form.
	 *
	 * @param errors The errors returned from a ajax form submission
	 * @param errorIdLookup A map from field names to dom ids for the error messages locations
	 */
	populateErrors : function(errors, errorIdLookup, tag) {
		this.clearErrors(errorIdLookup);

		var startTag = (tag ? '<' + tag + '>' : '<span>');
		var endTag = (tag ? '</' + tag + '>' : '</span>');

		//Populate the global errors first
		var globalErrorHtml = '';
		if (errors.globalErrors) {
			$.each(errors.globalErrors, function(index, errorMessage){
				globalErrorHtml += startTag  + errorMessage + endTag;
			});
		}

		if (errors.fieldErrors) {
			$.each (errors.fieldErrors, function (fieldName, errorMessages) {
				var errorHtml = '';
				$.each(errorMessages, function(index, errorMessage) {
					errorHtml += startTag + errorMessage + endTag;
				});

				var fieldId = errorIdLookup[fieldName];
				if ((fieldId) && $('#' + fieldId).length) {
					$('#' + fieldId).html(errorHtml).addClass('error');
				} else {
					//Append the error message to the global errors
					globalErrorHtml += errorHtml;
				}
	    	});
		}

		if (globalErrorHtml) {
			//Populate the global error html
			var $globalErrors = $('#' + errorIdLookup['GLOBAL_ERRORS']);
			$globalErrors.html(globalErrorHtml).addClass('error');
		}
	},

	/**
	 * Renders an individual field error message following the same conventions as the
	 * populateErrors method.  Useful for when validation is performed using javascript
	 * rather than submission.
	 *
	 * @param fieldName 	The name of the form field
	 * @param errorMessage	The text of the error message
	 * @param errorIdLookup A map from field names to dom ids for the error messages locations
	 */
	populateFieldError : function(fieldName, errorMessage, errorIdLookup) {
		var fieldErrorId = errorIdLookup[fieldName];
		if (fieldErrorId) {
			var $fieldError = $('#' + fieldErrorId);
			if ($fieldError.length) {
				$fieldError.html('<span>' + errorMessage + '</span>').addClass('error');
			}
		}
	},

	/**
	 * Adds parser to tablesorter plugin
	 * @param options 	parameters to initialize parser
	 * 			-id		set a unique id
	 * 			-format format your data for normalization
	 * 			-type 	set type, either numeric or text
	 */
	addTableSortParser : function (options){
		$.tablesorter.addParser({
		     id: options.id,
		     is: function(s) {
		    	 return false;
		     },
		     format: options.format,
		     type: options.type
		 });
	},

	/**
	 * Initializes a table to be sortable using the jQuery tablesorter plugin.  Sorting
	 * on the column's are initialized using the classes on the column header based on
	 * the following rules:
	 * 		- A column is only initialized as sortable if the 'sortable' class is present on
	 * 			the column header
	 * 		- The presence of an 'ascSort' or 'descSort' class on a sortable TH indicates
	 * 			the initial sorting for the column.

	 * @param $table The table jQuery object to initialize.
	 * @param params Additional parameters to control the sorting
	 * 		- sortList : specifies the default sorting.  This overrides the
	 * 			ascSort and descSort classes.
	 */
	initSortableTable : function($table, params) {
		if ($table.hasClass('sortable') && $('td', $table).length > 1) {
			params = params ? params : {}; //Default the params if empty

			//Build the header settings based on the sortable columns
			var headers = [];
			var sortList = [];
			$('th', $table).each(function(index) {
				var $th = $(this);
				if ($th.hasClass('sortable')) {
					if ($th.hasClass('ascSort')) {
						sortList.push([index, 0]);
					} else if ($th.hasClass('descSort')) {
						sortList.push([index, 1]);
					}
					if ($th.attr("app:sorter")){
						headers[index] = { sorter: $th.attr("app:sorter")};
					}
				} else {
					headers[index] = { sorter: false };
				}
			});
			$table.tablesorter({
				headers: headers,
				sortList: params.sortList ? params.sortList : sortList,
				textExtraction: params.textExtraction ? params.textExtraction : 'simple',
				widgets: params.noZebra ? [] : ['zebra'],
				onRenderHeader: function () {
					if (!$('> span.sort', this).length) {
						this.append(' <span class="sort">&nbsp;</span>');
					}
				}
            });

			Util.initTablePagination($table, {});

			if (params.hasOwnProperty("onSort")){
				$table.bind("sortEnd", function(){
					params.onSort(this);
				});
			}
		}
	},

	initTablePagination : function($tables, params) {
		//pageinate if paginateTable class is added to tablesorter table
		//injects pager controls before and after table
		$tables.each(function() {
			if ($(this).hasClass("paginateTable")) {
				$(this).before('<div class="pager pagerTop pagerHidden"><form><button class="first pagerButton"></button><button class="prev pagerButton"></button><span class="pagedisplay"></span><button class="next pagerButton"></button><button class="last pagerButton"></button><select class="pagesize pagesizeHidden"><option selected="selected" value="9999">ALL</option></select></form></div>');
				$(this).after('<div class="pager pagerBottom pagerHidden"><form><button class="first pagerButton"></button><button class="prev pagerButton"></button><span class="pagedisplay"></span><button class="next pagerButton"></button><button class="last pagerButton"></button><select class="pagesize pagesizeHidden"><option selected="selected" value="9999">ALL</option></select></form></div>');
				$(this).tablesorterPager({container: $(".pager")});
			}
		});
	},

	/**
	 * Returns the sorted column info for a sortable table.
	 */
	getSortedColumns : function($table) {
		var sortList = [];
		$('th', $table).each(function(index) {
			var $th = $(this);
			if ($th.hasClass('headerSortUp')) {
				sortList.push([index, 1]);
			} else if ($th.hasClass('headerSortDown')) {
				sortList.push([index, 0]);
			}
		});

		return sortList;
	},

	openWindow : function(windowUrl, width, height, showChrome, resizable, overrideWindowName) {
		var windowName;

		//If a window name was passed in use that
		//If not use the secure portion of the assetUrl
		var windowName = overrideWindowName;
		if (!windowName) {
			//Construct a unique window name from the window Url
			windowName = windowUrl.replace(new RegExp(/[\@]/g),"");
		}
		//Strip all non-alphanumerid characters
		windowName = windowName.replace(/[^a-zA-Z0-9]+/g,'');

		//Build the options list
		var options = "'directories=0";

		if (showChrome) {
			options += ",menubar=1,location=1,scrollbars=1,status=1";
		} else {
			options += ",menubar=0,location=0,scrollbars=1,status=0";
		}

		if (resizable) {
			options += ",resizable=1";
		} else {
			options += ",resizable=0";
		}

		if (width && height) {
			options += ",width=" + width + ",height=" + height;
		}

		window.open(windowUrl, windowName, options);
	},

	/**
	 * Configures a Default Text Overlay for a password field.  This works by having a second 'clear'
	 * version of the password field immediately before the actually password field.  This field should
	 * be a text input field whose styling is identical to the standard password field.  The default text
	 * and defaulted style will be applied by this method.  It is recommended the the the style for the
	 * clear field be initially set to 'display: none'.  This ensures that it is not visible until this
	 * method is called.
	 *
	 * @param	$passwordField			The jQuery password field element to apply the effect to.
	 * @param	$passwordFieldClear		The jQuery clear password field element.
	 * @param	defaultText				The default text to display for the textbox.
	 * @return	void
	 */
	setDefaultPasswordArea : function($passwordField, $passwordFieldClear, defaultText) {
		//Set the style and text for the clear version of the field.
		$passwordFieldClear.val(defaultText);
		$passwordFieldClear.addClass('defaultText');

		//Show the clear version of the field and hide the password version
		$passwordFieldClear.show();
		$passwordField.hide();

		//Bind to the focus and blur events to toggle the visibility of the
		//clear or password version of the field as needed.
		$passwordFieldClear.focus(function() {
			$passwordFieldClear.hide();
			$passwordField.show();
		    $passwordField.focus();
		});
		$passwordField.blur(function() {
		    if($passwordField.val() == '') {
		    	$passwordFieldClear.show();
		    	$passwordField.hide();
		    }
		});
	},

	scrollToElement : function($element, top, complete) {
		var scrollTop;
		if (!$element.length) {
			scrollTop = 0;
		} else {
			var browserHeight = document.documentElement.clientHeight;
			var elementHeight = $element.outerHeight();
			scrollTop = $element.offset().top -(top ? 0 : ((browserHeight-elementHeight)/2));
		}

		$('html, body').animate({
	         scrollTop: (scrollTop < 0 ? 0 : scrollTop)
	    }, 800, undefined, complete);
}
};

/**
 * The ExternalClickHandler is a convenience object for working with popups they need to be
 * dismissed if the user clicks outside of the popup. It works by binding a global mousedown
 * listener which will execute a callback method supplied by the caller when a click is
 * determined to be outside the popup object.
 *
 * Instances of this object cannot be reused.
 *
 * @param params The configuration object for the ExternalClickHandler.  This object should
 * 		provide the following methods:
 * 			- isExternalClick($target): Called on the mousedown event.  The $target parameter
 * 				is the dom object that received the event.  This method should return true if
 * 				handleExternalClick should be called, false otherwise.
 * 			- handleExternalClick: Callback method that should be used to dismiss the popup
 * 				and perform any cleanup.  This method should also be responsible for calling
 * 				destroy on the click handler.
 */
function ExternalClickHandler(params) {
	this.init(params);
}
ExternalClickHandler.prototype = {
	init : function(params) {
		this._listener = function(event) {
			var $target = $(event.target);
			if (params.isExternalClick($target)) {
				params.handleExternalClick();
			}
		};

		//Bind an event listener to the global mousedown event
		$(document).bind('mousedown', this._listener);
	},

	/**
	 * Unbinds the mousedown listener, effectively destroying this handler.
	 */
	destroy : function() {
		$(document).unbind('mousedown', this._listener);
	}
};

StringBuffer = function () {
	this._buffer = [];
};

StringBuffer.prototype = {

	append : function append(string) {
		this._buffer.push(string);
		return this;
	},

	toString : function toString() {
		return this._buffer.join("");
	}

};
