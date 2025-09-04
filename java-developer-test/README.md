# README.md

## Test Task for Atlassian Java Developer

Welcome! This test task is meant to give you a small look into Atlassian plugin development.  
Don’t worry - we don’t expect you to finish everything perfectly. The idea is:
- see how you approach problems,
- check your coding style,
- and give you a chance to decide if Jira plugin development feels interesting to you (or if you’re the priest from the meme who says “ну нахер” 🙃).

---

## Setup

1. **Run Jira locally**  
   Use the provided Docker image ([Docker image here](https://stash.teamlead.ru/projects/JIRAPLUG/repos/teamlead-interview/browse)).  
   After startup, Jira will be available on `http://localhost:8080`.

2. **Build the test plugin**  
   Clone the repository: [link to repo].  
   Build and package it with Maven.

3. **Install plugin into Jira**  
   Go to your Jira:  
   `{yourJiraInstanceUrl}/plugins/servlet/upm`  
   Upload the built `.jar` file.

4. **Check it works**
    - Create a test project.
    - Create a test issue.
    - Open the issue → you should see a **Test Panel** with a **"Click on me!"** button.
    - Click the button → dialog opens.

---

## Tasks

- The full list of tasks (bugs, features, optional extras) is in [TASKS.md](./TASKS.md).
- Some are **required**, others are **optional** (bonus).

Your final step: do a mini **code review** of the plugin. Comment in the code (e.g., `// TODO_NOTE: ...`) or write a short list of observations about bad practices, potential bugs, or improvements.

---

## Helpful Links

- Atlassian Plugin SDK: [https://developer.atlassian.com/server/framework/atlassian-sdk/](https://developer.atlassian.com/server/framework/atlassian-sdk/)
- Writing a Jira plugin: [https://developer.atlassian.com/server/framework/atlassian-sdk/create-a-helloworld-plugin-project/](https://developer.atlassian.com/server/framework/atlassian-sdk/create-a-helloworld-plugin-project/)
- Atlassian UI Guidelines: [https://aui.atlassian.com/aui/latest/docs/avatars.html](https://aui.atlassian.com/aui/latest/docs/avatars.html)

---

## What we expect

- Updated plugin code (with fixes/features implemented).
- Notes from your **code review** (in code comments or a separate file).

If you make it to the interview, we will ask you to comment on what you did and explain your decisions, pls keep that in mind.

That’s it. Have fun, and good luck!