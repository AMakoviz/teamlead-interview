/**
 * Renders and initializes a dialog for managing user assignment and summary for a Jira issue.
 *
 * This function dynamically creates the HTML structure for the dialog, appends it to the document body,
 * and then initializes the AUI dialog.
 *
 * @function

 * @see {@link initUsersDropdown} to see how the user dropdown is populated.
 * @see {@link getCurrentAssignee} for fetching the current issue assignee.
 * @see {@link getCurrentSummary} for fetching the current issue summary.
 */

function renderDialog() {
	if (jQuery("#tl-user-dialog").length) {
		AJS.dialog2("#tl-user-dialog").show();
		return;
	}
	const dialogHtml = `
		<section id="tl-user-dialog" class="aui-dialog2 aui-dialog2-medium" role="dialog" aria-hidden="true">
			<header class="aui-dialog2-header">
				<button class="aui-close-button aui-dialog2-header-close"
			aria-label="${AJS.I18n.getText('tl.button.close')}"
			data-dialog2-close></button>
				<h2 class="aui-dialog2-header-main">${AJS.I18n.getText("tl.dialog.header")}</h2>
			</header>
			<div class="aui-dialog2-content">
				<div class="tl-user-area">
					<div>
						<label for="tl-current-assignee" class="tl-label">${AJS.I18n.getText('tl.test.currentAssignee')}:</label>
						<input id="tl-current-assignee" type="text" class="tl-input" disabled="disabled" style="" value="">
					</div>
					<div class="tl-border">
						<label for="tl-current-summary" class="tl-label">${AJS.I18n.getText('tl.test.currentSummary')}</label>
						<input id="tl-current-summary" type="text" class="tl-input" disabled="disabled" style="" value="">
					</div>

					<div class="tl-border">
						<span class="tl-label">${AJS.I18n.getText('tl.test.updateAssignee')}</span>
						<select id="tl-user-select" class="aui-select2" style="width: 100%;">
							<option value="" disabled selected hidden>${AJS.I18n.getText('tl.test.selectUserTitle')}</option>
						</select>

						<div class="tl-button-group">
							<button id="tl-set-assignee-button"
									class="aui-button aui-button-primary"
									onClick="assignUser()">Assign to User</button>
						</div>
					</div>
				</div>

				<div class="tl-summary-area">
					<div>
						<span class="tl-label">${AJS.I18n.getText('tl.test.updateSummary')}</span>
						<input id="tl-summary" name="tl-summary" style="width:550px;" type="text" class="text tl-input"
							   value="" placeholder="${AJS.I18n.getText('tl.test.summaryTitle')}"/>
					</div>
					<div class="tl-button-group">
						<button id="tl-set-summary-button"
								class="aui-button aui-button-primary">Update</button>
					</div>
				</div>
			</div>
			<footer class="aui-dialog2-footer">
				<div class="aui-dialog2-footer-actions">
					<button
						id="tl-close-button"
						class="aui-button aui-button-link"
						type="button"
						data-dialog2-close>
						${AJS.I18n.getText("tl.button.close")}
					</button>
				</div>
			</footer>

		</section>
	`;


	jQuery("body").append(dialogHtml);

	setTimeout(() => AJS.dialog2("#tl-user-dialog").show(), 0);


	jQuery(document)
		.off("click.tlClose", "#tl-user-dialog [data-dialog2-close]")
		.on("click.tlClose", "#tl-user-dialog [data-dialog2-close]", function (e) {
			e.preventDefault();
			AJS.dialog2("#tl-user-dialog").hide();
		});
	jQuery(document)
		.off("click.tl", "#tl-set-summary-button")
		.on("click.tl", "#tl-set-summary-button", function (e) {
			e.preventDefault();
			updateSummary();
		});


	jQuery("#tl-user-select").select2();

	initUsersDropdown();

	getCurrentAssignee();
	getCurrentSummary();
	}

/**
 * Fetches the current assignee for the Jira issue and updates a designated input field.
 *
 * @function
 */
function getCurrentAssignee() {
	const issueKey = JIRA.Issue.getIssueKey();

	jQuery.ajax({
		url: AJS.contextPath() + `/rest/test-rest/1.0/test-plugin/issues/${issueKey}`,
		method: "get",
		contentType: "application/json",
		success: function(response) {
			jQuery("#tl-current-assignee").val(response.assigneeName);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			const errorMessage = AJS.I18n.getText("tl.response.error") + '\n' + jqXHR.responseText;
			JIRA.Messages.showErrorMsg(errorMessage, {closeable: true, timeout: 5});
		}
	});
}

/**
 * Fetches the current summary for the Jira issue and updates a designated input field.
 *
 * @function
 */
function getCurrentSummary() {
	const issueKey = JIRA.Issue.getIssueKey();

	jQuery.ajax({
		url: AJS.contextPath() + "/rest/test-rest/1.0/test-plugin/issues/" + issueKey,
		method: "get",
		contentType: "application/json",
		success: function(response) {
			jQuery("#tl-current-summary").val(response.summary);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			const errorMessage = AJS.I18n.getText("tl.response.error") + '\n' + jqXHR.responseText;
			JIRA.Messages.showErrorMsg(errorMessage, {closeable: true, timeout: 5});
		}
	});
}

/**
 * Populates a select dropdown with a list of users fetched from a REST endpoint.
 *
 * @function
 */
function initUsersDropdown() {
	jQuery.ajax({
		method: "get",
		dataType: "json",
		url: AJS.contextPath() + "/rest/test-rest/1.0/test-plugin/users",
		success: function (result, textStatus, jqXHR) {
			console.log("result:" + result);

			const $select = jQuery("#tl-user-select");
			if (result && result.options) {
				result.options.forEach(function (option) {
					const displayName = option.name || option.id;
					const userKey = option.id;
					$select.append(new Option(displayName, userKey));
				});
			}

		}, error: function (jqXHR, textStatus, errorThrown) {
			console.log("_____ERROR, jqXHR: " + jqXHR, + " errorThrown: " + errorThrown);
		}
	});
}

function updateSummary() {

	console.log("⚡ updateSummary called");
	var summary = jQuery("#tl-summary").val().trim();
	var issueKey = JIRA.Issue.getIssueKey();

	console.log("⚡ issueKey =", issueKey);

	if (summary.length < 5 || summary.length > 100) {
		JIRA.Messages.showErrorMsg(
			AJS.I18n.getText("tl.validation.summaryLength") || "Summary must be between 5 and 100 characters",
			{ closeable: true, timeout: 5 }
		);
		return;
	}
	console.log("⚡ sending ajax", {
		issueKey: issueKey,
		summary: summary
	});

	jQuery.ajax({
		url: AJS.contextPath() + "/rest/test-rest/1.0/test-plugin/updateSummary",
		method: "post",
		contentType: "application/json",
		data: JSON.stringify({
			issueKey: issueKey,
			summary: summary
		}),
		success: function(response) {
			console.log("Update summary SUCCESS:", response);
			JIRA.Messages.showSuccessMsg(
				AJS.I18n.getText("tl.response.success") || "Summary updated successfully",
				{ closeable: true, timeout: 5 }
			);
			location.reload();
		},
		error: function(jqXHR) {
			console.error("Update summary ERROR:", jqXHR.responseText);
			const errorMessage =
				AJS.I18n.getText("tl.response.error") + "\n" + jqXHR.responseText;
			JIRA.Messages.showErrorMsg(errorMessage, { closeable: true, timeout: 5 });
		}
	});
}

/**
 * Assigns a selected user to the current Jira issue.
 *
 * @function
 */
function assignUser() {
	var userKey = jQuery("#tl-user-select").val();
	var issueKey = JIRA.Issue.getIssueKey();

	if (!userKey) {
		JIRA.Messages.showErrorMsg(AJS.I18n.getText("tl.validation.selectUser"), {closeable: true, timeout: 5});
		return;
	}

	jQuery.ajax({
		url: AJS.contextPath() + "/rest/test-rest/1.0/test-plugin/assign",
		method: "post",
		contentType: "application/json",
		data: JSON.stringify({
			userKey: userKey,
			issueKey: issueKey
		}),
		success: function(response) {
			JIRA.Messages.showSuccessMsg(AJS.I18n.getText("tl.response.success"), {closeable: true, timeout: 5});
			AJS.dialog2("#tl-user-dialog").hide();
			location.reload();
		},
		error: function(jqXHR, textStatus, errorThrown) {
			const errorMessage = AJS.I18n.getText("tl.response.error") + "\n" + jqXHR.responseText;
			JIRA.Messages.showErrorMsg(errorMessage, {closeable: true, timeout: 5});
		}
	});
}

/**
 * Attaches a click event handler to the close button of a dialog when the AUI framework is initialized.
 *
 * @function
 */
// AJS.toInit(function () {
// handle close button
// AJS.$("#tl-close-button").click(function (e) {
// 	e.preventDefault();
// 	AJS.dialog2("#tl-user-dialog").hide();
// });
// jQuery(document)
// 	.off("click.tl", "#tl-close-button")
// 	.on("click.tl", "#tl-close-button", function (e) {
// 	e.preventDefault();
// 	console.log("Close button clicked");
// 	AJS.dialog2("#tl-user-dialog").hide();
// 		jQuery("#tl-user-dialog").remove();
// });
// 	jQuery("body").off("click", "#tl-close-button"); // сначала убираем старые
// 	jQuery("body").on("click", "#tl-close-button", function (e) {
// 		e.preventDefault();
// 		console.log("Close button clicked");
//
// 		// гарантированно убираем диалог
// 		AJS.dialog2("#tl-user-dialog").hide();
// 		jQuery("#tl-user-dialog").remove();
// 		jQuery(".aui-blanket").remove();
// 	});
// });

