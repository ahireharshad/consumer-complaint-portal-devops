# Submission Evidence Checklist

Capture evidence from the real environment; do not fabricate logs or screenshots.

## Git and planning
- [ ] Repository page, README, folder structure and commit history
- [ ] Backlog/Kanban board with task states
- [ ] Feature branch and pull request
- [ ] Review comments and merge evidence
- [ ] Conflict creation/resolution log
- [ ] Release tag after green CI

## Application and testing
- [ ] Local home page
- [ ] Valid complaint submission and reference
- [ ] Invalid email and short-description rejection
- [ ] Tracking before/after reviewer decision
- [ ] Reject journey and approval journey
- [ ] Unknown reference
- [ ] Selenium run with all tests passing
- [ ] Failure screenshot mechanism demonstrated
- [ ] Surefire XML/HTML report

## CI/CD and operations
- [ ] GitHub Actions green run
- [ ] Jenkins successful build and archived JAR
- [ ] Deliberately failing test blocks pipeline, followed by fixed green run
- [ ] Docker build/image listing/running container/logs/stop/start/remove
- [ ] Registry image/tag if publishing is configured
- [ ] Ansible first and second run outputs (idempotency)
- [ ] HTTP health check
- [ ] Rollback/recovery demonstration

## Suggested commands
```bash
java -version
mvn -version
mvn -Dheadless=true -DbaseUrl=http://localhost:8080 test
git log --oneline --decorate -10
git tag --list
docker images
docker ps -a
docker logs complaint-portal
ansible-playbook -i ansible/inventory.ini ansible/deploy.yml
```
Redact tokens, passwords, personal consumer data and private infrastructure details.
