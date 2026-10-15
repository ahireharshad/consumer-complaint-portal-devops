# Submission and demonstration evidence checklist

Capture real screenshots/logs from your own system and add them to your report. Do not present planned evidence as completed.

- [ ] Repository page, folder structure, README and commit history
- [ ] Issue template and backlog/task board
- [ ] Feature branch, pull request, review and merge
- [ ] Conflict demonstration and resolution (create two edits on separate branches; do not manufacture a false log)
- [ ] Local application home page and successful complaint submission/reference
- [ ] Validation failure for invalid email/short description
- [ ] Status tracking before and after reviewer decision
- [ ] Maven Selenium test command and `target/surefire-reports`
- [ ] Jenkins build stages, published JUnit report, archived JAR
- [ ] Docker build, `docker images`, running container, logs, stop/start/remove
- [ ] Registry tag/push if Docker Hub credentials are configured
- [ ] Ansible first run and second run showing idempotency
- [ ] HTTP health check and rollback/recovery evidence
- [ ] Release tag and final repository URL

## Suggested terminal evidence commands
```bash
java -version
mvn -version
mvn -Dheadless=true -DbaseUrl=http://localhost:8080 test
git log --oneline --decorate -10
docker images
docker ps -a
docker logs complaint-portal
ansible-playbook -i ansible/inventory.ini ansible/deploy.yml
```

Redact email addresses, access tokens, passwords, private hostnames and any real consumer data before including screenshots in a report.
