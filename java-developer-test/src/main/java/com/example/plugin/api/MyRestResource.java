package com.example.plugin.api;

import com.atlassian.jira.bc.JiraServiceContextImpl;
import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.bc.user.search.UserSearchService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.event.type.EventDispatchOption;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.jira.util.I18nHelper;
import com.example.plugin.dto.SelectOptionDTO;
import com.example.plugin.dto.UpdateRequestDTO;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST resource for a Jira plugin, providing endpoints for user management and issue updates.
 *
 * @author V. H
 * @version 1.0
 * @since 1.0.0
 */
@Path("/test-plugin")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class MyRestResource {

    private static final Logger log = Logger.getLogger(MyRestResource.class);

    private final IssueManager issueManager;
    private final UserManager userManager;
    private final UserSearchService userSearchService;
    private final IssueService issueService;
    private final I18nHelper i18nHelper;
    private final JiraAuthenticationContext authContext;

    /*
        https://aui.atlassian.com/aui/latest/docs/getting-started.html
        https://developer.atlassian.com/server/framework/atlassian-sdk/tutorials-and-guides/
     */

    @Inject
    public MyRestResource(IssueManager issueManager,
                          UserManager userManager,
                          I18nHelper i18nHelper, JiraAuthenticationContext authContext) {
        this.issueManager = issueManager;
        this.userManager = userManager;
        this.i18nHelper = i18nHelper;
        this.userSearchService = ComponentAccessor.getComponent(UserSearchService.class);
        this.issueService = ComponentAccessor.getIssueService();
        this.authContext = authContext;
    }

    /**
     * REST endpoint to retrieve a list of all Jira users.
     *
     * <p>This method finds all users accessible to the current logged-in user,
     * converts them into a DTO format suitable for a select dropdown.</p>
     *
     * @return A {@link Response} containing a list of users.
     */
    @GET
    @Path("/users")
    public Response getUsers() {

        ApplicationUser loggedInUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        List<ApplicationUser> users = userSearchService.findUsersAllowEmptyQuery(new JiraServiceContextImpl(loggedInUser), "");

        SelectOptionDTO selectOptionDTO = new SelectOptionDTO();
        List<SelectOptionDTO> options = new ArrayList<>();
        for (ApplicationUser user : users) {
            options.add(new SelectOptionDTO(user.getName(), user.getDisplayName()));
        }

        selectOptionDTO.setOptions(options);
        return Response.ok(selectOptionDTO).build();
    }

    /**
     * REST endpoint to get summary and assignee details for a specific issue.
     *
     * @param issueKey The key of the issue to retrieve.
     * @return A {@link Response} containing issue details.
     */
    @GET
    @Path("/issues/{issueKey}")
    public Response getIssueDetails(@PathParam("issueKey") String issueKey) {

        MutableIssue issue = issueManager.getIssueByCurrentKey(issueKey);

        ApplicationUser assignee = issue.getAssignee();
        Map<String, String> response = new HashMap<>();
        response.put("assigneeName", assignee.getName());
        response.put("assigneeKey", assignee.getKey());
        response.put("summary", issue.getSummary());
        response.put("description", issue.getDescription());

        return Response.ok(response).build();
    }

    /**
     * REST endpoint to assign a user to a Jira issue.
     *
     * <p>A comment is added to the issue to log the assignee change.</p>
     *
     * @param request The {@link UpdateRequestDTO} containing the user key and issue key.
     * @return A {@link Response} with a 200 OK status on success.
     * Returns a 400 BAD REQUEST status with an error message if the user is not found,
     * or if the issue update validation fails.
     */
    @POST
    @Path("/assign")
    public Response assignUserToIssue(UpdateRequestDTO request) {
        ApplicationUser assignee = userManager.getUserByName(request.getUserKey());

        if (assignee == null) {
            log.error("#TEST user not found!!!");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("User not found").build();
        }

        MutableIssue issue = issueManager.getIssueByCurrentKey(request.getIssueKey());
        ApplicationUser currentUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();

        IssueInputParameters input = issueService.newIssueInputParameters();
        input.setAssigneeId(assignee.getName());
        input.setComment(i18nHelper.getText("tl.issue.updated.assignee"));

        IssueService.UpdateValidationResult validationResult = issueService.validateUpdate(currentUser, issue.getId(), input);
        if (!validationResult.isValid()) {
            log.error("#TEST UpdateValidationResult: !validationResult.isValid():" + validationResult.getErrorCollection().toString());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(validationResult.getErrorCollection().toString()).build();
        }

        IssueService.IssueResult updateResult = issueService.update(currentUser, validationResult, EventDispatchOption.DO_NOT_DISPATCH, false);
        if (!updateResult.isValid()) {
            log.error("#TEST updateResult: !validationResult.isValid():" + updateResult.getErrorCollection().toString());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(updateResult.getErrorCollection().toString()).build();
        }

        return Response.ok().build();
    }
    @POST
    @Path("/updateSummary")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateSummary(final UpdateSummaryRequest request) {
        if (request == null) {
            log.error("#TEST updateSummary request is NULL");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Request body is null").build();
        }
        log.info("#TEST updateSummary request: issueKey=" + request.getIssueKey()
                + ", summary=" + request.getSummary());

        ApplicationUser user = authContext.getLoggedInUser();
        IssueService.IssueResult issueResult = issueService.getIssue(user, request.getIssueKey());

        if (!issueResult.isValid()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid issue key: " + request.getIssueKey())
                    .build();
        }



        IssueInputParameters params = issueService.newIssueInputParameters();
        params.setSummary(request.getSummary());
        params.setRetainExistingValuesWhenParameterNotProvided(true);

        IssueService.UpdateValidationResult validation =
                issueService.validateUpdate(user, issueResult.getIssue().getId(), params);

        if (!validation.isValid()) {
            log.error("#TEST validation errors: " + validation.getErrorCollection());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(validation.getErrorCollection().toString())
                    .build();
        }
        IssueService.IssueResult updateResult =
                issueService.update(user, validation, EventDispatchOption.ISSUE_UPDATED, false);
        if (!updateResult.isValid()) {
            log.error("#TEST update errors: " + updateResult.getErrorCollection());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(updateResult.getErrorCollection().toString())
                    .build();
        }
        Map<String, Object> result = new HashMap<>();
        result.put("status", "ok");
        result.put("newSummary", request.getSummary());
        return Response.ok(result).build();
    }
    public static class UpdateSummaryRequest {
        private String issueKey;
        private String summary;
        public UpdateSummaryRequest() {}

        public String getIssueKey() { return issueKey; }
        public void setIssueKey(String issueKey) { this.issueKey = issueKey; }
        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }
    }

}
