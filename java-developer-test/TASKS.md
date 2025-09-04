# TASKS.md

## Bugs

### 1. BUG_Optional: Error when assigning a user
**Steps:**
1. Open an issue.
2. Click **"Click on me!"** → select a user → click **"Assign to User"**.

**Actual:** error message:  
`Error! Errors: {assignee=User '{userID}' does not exist.} Error Messages: []`

**Expected:** selected user should be set as the issue assignee.

---

### 2. BUG_Required: Close button unreliable
**Steps:**
1. Open an issue.
2. Click **"Click on me!"**.
3. Click **"Close"**.

**Actual:** dialog sometimes stays open.  
**Expected:** dialog should always close when clicking Close.

---

## Required Tasks

### 1. Update Summary
Add functionality to update the **Issue Summary** directly from the dialog.

**Requirements:**
- Input length: 5–100 chars.
- Success → show message, reload page.
- Failure → show error message.

**Implementation hints:**
- Frontend: input + "Update" button already exist, hook up to backend.
- Backend: add REST endpoint → accepts `issueKey` + `summary`, updates via Jira API.

---

### 2. Make buttons primary
Change styling: **"Assign to User"** and **"Update"** buttons should use primary style.

---

### 3. Move buttons to the right
Align those same buttons to the **right side** of their sections (per Atlassian UI standards).

see [Window1.jpg](./Window1.jpg).

---

### 4. Code Review
Go through the plugin code and note:
- issues,
- bad practices,
- potential bugs,
- suggestions for improvement.

You can add inline comments like `// TODO_NOTE: this method is too long` or make a list in a separate file.

---

## Optional Task

### 5. Add a second test panel
Add a new panel called **"Second Test Panel"** with a button **"Click on me too!"**.

Dialog content:
- **Read-only fields:** current Reporter, current Description.
- **Editable:**
   - Dropdown to select new Reporter + "Update Reporter" button.
   - Text input for new Description + "Update Description" button.
- Buttons → primary style, right-aligned.

see [Window2.jpg](./Window2.jpg).

---

That’s it. Focus on the required tasks first. Optional = bonus points.