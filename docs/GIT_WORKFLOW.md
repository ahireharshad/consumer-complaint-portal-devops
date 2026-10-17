# Git & GitHub Collaboration Workflow Guide

**Project:** Consumer Complaint Portal — Selenium Testing and DevOps  
**Student:** Harshad Ahire · **Roll No.:** 23102C0056  
**Repository:** [https://github.com/ahireharshad/consumer-complaint-portal-devops](https://github.com/ahireharshad/consumer-complaint-portal-devops)

---

## 1. Branch Strategy (GitFlow-Adapted)

The project follows a standard structured branching model:

```
  main ──────●─────────────────────────────●─────────────● (v1.0.0 Tag)
             │                             ▲
  develop ───●──────────●──────────●───────┘
                        │          ▲
  feature/xxx ──────────●──────────┘
```

1. **`main` Branch:**
   - Production-stable code only.
   - Every merge into `main` must pass all CI checks (build, package, headless Selenium tests).
   - Only tagged releases (`v1.0.0`) are deployed to target environments from `main`.

2. **`develop` Branch:**
   - Active integration branch for ongoing sprint development.
   - Feature branches branch off from `develop` and merge back into `develop` via pull requests.

3. **`feature/<short-name>` Branches:**
   - Isolated work for a specific feature or backlog task (e.g., `feature/selenium-suite`, `feature/reviewer-guard`).
   - Short-lived; merged and deleted after PR approval.

4. **`bugfix/<short-name>` Branches:**
   - Used to address test failures or defects identified during testing.

---

## 2. Pull Request & Code Review Procedure

1. **Create Branch:**
   ```bash
   git checkout -b feature/t09-selenium-enhancements develop
   ```
2. **Commit Work:**
   Make atomic, meaningful commits using clear commit message formats:
   ```bash
   git commit -m "feat(test): add 6-journey Selenium E2E suite with failure screenshot extension"
   ```
3. **Push to Remote:**
   ```bash
   git push -u origin feature/t09-selenium-enhancements
   ```
4. **Open Pull Request:**
   - Fill out the `.github/pull_request_template.md`.
   - Link the relevant issue (e.g., `Closes #12`).
   - Confirm test execution and verify green CI build.
5. **Peer Review:**
   - Review code diffs for adherence to project standards, lack of committed secrets, and explicit wait usage in tests.
6. **Merge:**
   - Use `Squash and merge` or standard merge commit with fast-forward prevention.

---

## 3. Merge Conflict Creation and Resolution Procedure (Academic Demonstration)

To demonstrate merge conflict resolution for evaluator assessment:

### A. Simulating a Conflict
1. Branch `feature/branch-a` modifies line 18 of `Complaint.java`:
   ```java
   this.description = description.trim();
   ```
   Commit and push.
2. Branch `feature/branch-b` modifies the same line in `Complaint.java`:
   ```java
   this.description = description != null ? description.strip() : "";
   ```
   Commit and push.
3. Merge `feature/branch-a` into `develop`.
4. Attempt to merge `feature/branch-b` into `develop`:
   ```bash
   git checkout develop
   git merge feature/branch-b
   # CONFLICT (content): Merge conflict in src/main/java/in/ac/consumerportal/Complaint.java
   ```

### B. Resolving the Conflict
1. Inspect conflict status:
   ```bash
   git status
   ```
2. Open `src/main/java/in/ac/consumerportal/Complaint.java` and locate conflict markers:
   ```java
   <<<<<<< HEAD
   this.description = description.trim();
   =======
   this.description = description != null ? description.strip() : "";
   >>>>>>> feature/branch-b
   ```
3. Edit the file to select the best unified implementation, removing all conflict markers.
4. Stage the resolved file:
   ```bash
   git add src/main/java/in/ac/consumerportal/Complaint.java
   ```
5. Commit the resolution:
   ```bash
   git commit -m "fix(merge): resolve conflict in Complaint.java description sanitization"
   ```
6. Run the build to ensure tests still pass:
   ```bash
   mvn test
   ```

---

## 4. Semantic Release Tagging

Once all Phase 1 and Phase 2 criteria are verified on `main`:

```bash
git checkout main
git pull origin main
git tag -a v1.0.0 -m "Release v1.0.0: Consumer Complaint Portal with verified Selenium CI suite"
git push origin v1.0.0
```

---

## 5. Security & Secret Protection Rules
- Never commit credentials, `.env` files, or private SSH keys.
- Never commit `target/` binaries, `.log` files, or `app.pid`.
- All environment variables are injected at runtime (`PORT`, `DB_URL`, `DB_USER`).
