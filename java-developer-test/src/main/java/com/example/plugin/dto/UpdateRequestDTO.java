package com.example.plugin.dto;

import java.util.Objects;

/**
 * Data Transfer Object (DTO) for updating a Jira issue.
 * <p>
 * This class is used to carry data from a client-side request to a server-side
 * </p>
 *
 * @author V. H
 * @since 1.0.0
 */
public class UpdateRequestDTO {
    public String userKey;
    public String issueKey;
    public String summary;

    public UpdateRequestDTO() {
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpdateRequestDTO that = (UpdateRequestDTO) o;

        if (!Objects.equals(userKey, that.userKey)) return false;
        if (!Objects.equals(issueKey, that.issueKey)) return false;
        return Objects.equals(summary, that.summary);
    }

    @Override
    public int hashCode() {
        int result = userKey != null ? userKey.hashCode() : 0;
        result = 31 * result + (issueKey != null ? issueKey.hashCode() : 0);
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        return result;
    }
}
