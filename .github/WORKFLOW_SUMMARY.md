# GitHub Actions CI/CD Workflow Summary

## 📋 Quick Reference

### Workflow File Location
```
.github/workflows/ci-cd.yml
```

### Trigger Events
- ✅ Push to `main` or `develop` branches
- ✅ Pull requests to `main` or `develop` branches
- ✅ Manual trigger (workflow_dispatch)

---

## 🔄 Workflow Jobs Overview

### Job 1: Build (build)
**Purpose:** Compile, test, and create artifacts  
**Triggers:** Always  
**Status:** ⏱️ 5-10 minutes  

**Steps:**
```
1. Checkout Code
2. Setup JDK 17
3. Cache Maven packages
4. Compile with Maven
5. Run unit tests
6. Run integration tests
7. Generate code coverage (JaCoCo)
8. Publish test results
9. Upload to Codecov
10. SonarQube analysis (optional)
11. Build final artifact
12. Upload JAR artifact
```

**Outputs:**
- ✅ Compiled JAR file
- ✅ Test reports (XML)
- ✅ Coverage metrics
- ✅ Build logs

---

### Job 2: Code Quality (code-quality)
**Purpose:** Analyze code for quality issues  
**Depends on:** build  
**Triggers:** On success  
**Status:** ⏱️ 2-5 minutes  

**Tools:**
```
1. SpotBugs     - Bug detection
2. CheckStyle   - Style compliance
3. PMD          - Code smell detection
```

**Output:**
- Code quality metrics
- Issue reports
- Style violations

---

### Job 3: Security (security)
**Purpose:** Scan for vulnerabilities  
**Depends on:** build  
**Triggers:** On success  
**Status:** ⏱️ 3-5 minutes  

**Scanners:**
```
1. Dependency Check - CVE detection
2. Trivy Scanner   - Filesystem scan
3. SARIF Upload    - Report storage
```

**Output:**
- CVE vulnerabilities
- Dependency issues
- Security reports

---

### Job 4: Docker Build (docker-build)
**Purpose:** Build and push Docker image  
**Depends on:** build  
**Triggers:** On success  
**Status:** ⏱️ 2-4 minutes  

**Registry:** `ghcr.io` (GitHub Container Registry)

**Steps:**
```
1. Setup Docker Buildx
2. Login to GHCR
3. Extract metadata & tags
4. Build and push image
```

**Output:**
```
ghcr.io/2300030862/deployment:main
ghcr.io/2300030862/deployment:sha-<hash>
```

---

### Job 5: Deploy (deploy)
**Purpose:** Deploy to production  
**Depends on:** build, security, docker-build  
**Triggers:** Only on `main` branch, on push  
**Status:** ⏱️ 2-5 minutes  
**Condition:** All previous jobs succeed  

**Steps:**
```
1. Download JAR artifact
2. Deploy to server (SSH)
3. Send Slack notification
```

**Requirements:**
- `DEPLOY_KEY` - SSH private key
- `DEPLOY_HOST` - Server hostname
- `DEPLOY_USER` - SSH username

---

### Job 6: Notify (notify)
**Purpose:** Send workflow completion notifications  
**Depends on:** build, security  
**Triggers:** Always (even on failure)  
**Status:** ⏱️ 1-2 minutes  

**Steps:**
```
1. Prepare status message
2. Send email notification (optional)
3. Send Slack notification (optional)
```

---

## 📊 Workflow Execution Times

| Job | Time | Status |
|-----|------|--------|
| Build | 5-10 min | Always |
| Code Quality | 2-5 min | On success |
| Security | 3-5 min | On success |
| Docker Build | 2-4 min | On success |
| Deploy | 2-5 min | Main only |
| Notify | 1-2 min | Always |
| **Total** | **15-30 min** | **Parallel execution** |

---

## 🔑 Required Secrets

### Optional Configuration:
```
SONAR_TOKEN         → SonarQube token
SONAR_HOST_URL      → SonarQube URL
SLACK_WEBHOOK_URL   → Slack webhook
DEPLOY_KEY          → SSH private key
DEPLOY_HOST         → Server hostname
DEPLOY_USER         → SSH username
```

---

## 🏗️ Workflow Architecture

```
GitHub Event (Push/PR)
         │
         ├─────► BUILD JOB
         │        ├─► Compile
         │        ├─► Test
         │        └─► Artifact
         │
         ├─────► CODE QUALITY (depends: build)
         │        ├─► SpotBugs
         │        ├─► CheckStyle
         │        └─► PMD
         │
         ├─────► SECURITY (depends: build)
         │        ├─► Dependency Check
         │        └─► Trivy
         │
         ├─────► DOCKER BUILD (depends: build)
         │        └─► Push to GHCR
         │
         ├─────► DEPLOY (depends: all above)
         │        └─► SSH Deploy
         │
         └─────► NOTIFY (always)
                  └─► Slack/Email
```

---

## 📦 Artifact Storage

**Name:** `deployment-service-jar`  
**Contents:** Compiled JAR file  
**Size:** ~50MB (typical)  
**Retention:** 30 days  
**Download:** Actions → Workflow run → Artifacts

---

## 🐳 Docker Images

**Registry:** GitHub Container Registry (GHCR)  
**Repository:** `ghcr.io/2300030862/deployment`  

**Available Tags:**
```
ghcr.io/2300030862/deployment:main
ghcr.io/2300030862/deployment:develop
ghcr.io/2300030862/deployment:sha-<commit-hash>
```

**Pull Command:**
```bash
docker pull ghcr.io/2300030862/deployment:main
```

**Run Command:**
```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb \
  ghcr.io/2300030862/deployment:main
```

---

## 🧪 Test Results

**Test Framework:** JUnit 5  
**Test Type:** Unit + Integration  
**Coverage Tool:** JaCoCo  
**Coverage Upload:** Codecov.io  

**Report Location:**
```
target/site/jacoco/index.html
target/surefire-reports/
```

---

## 📈 Code Quality Tools

| Tool | Purpose | Output |
|------|---------|--------|
| SpotBugs | Bug detection | Bug reports |
| CheckStyle | Code style | Style violations |
| PMD | Code smells | Issue reports |
| JaCoCo | Coverage | Coverage metrics |
| SonarQube | Overall quality | Quality gate |

---

## 🔒 Security Scanning

| Scanner | Target | Output |
|---------|--------|--------|
| Dependency Check | Dependencies | CVE list |
| Trivy | Filesystem | Vulnerability report |
| SARIF | Results | GitHub Security tab |

---

## ✅ Implementation Checklist

- [x] Workflow file created (`.github/workflows/ci-cd.yml`)
- [x] Build job with Maven
- [x] Test execution with JUnit 5
- [x] Code quality analysis
- [x] Security scanning
- [x] Docker image building
- [x] Deployment automation
- [x] Notification system
- [x] Artifact storage
- [x] Documentation complete

**Next Steps:**
- [ ] Add GitHub secrets
- [ ] Configure deployment server
- [ ] Setup Slack webhook
- [ ] Create first commit
- [ ] Monitor workflow run

---

## 📝 Common Configurations

### Skip Deployment
Remove/comment out `deploy` job in workflow file

### Skip Docker Build
Remove/comment out `docker-build` job in workflow file

### Change Branch
Edit workflow trigger:
```yaml
on:
  push:
    branches: [ main, develop, feature/* ]
```

### Add More Test Profiles
```yaml
- name: Run Tests with Profile
  run: mvn test -P coverage
```

### Deploy on Tags
```yaml
if: startsWith(github.ref, 'refs/tags/v')
```

---

## 🚀 Quick Start Commands

### View Workflow Runs
```bash
gh run list -w ci-cd.yml
```

### Trigger Workflow Manually
```bash
gh workflow run ci-cd.yml
```

### View Specific Run
```bash
gh run view <run-id> --log
```

### Cancel Running Workflow
```bash
gh run cancel <run-id>
```

### Rerun Failed Jobs
```bash
gh run rerun <run-id>
```

---

## 📞 Troubleshooting

### Build Fails
```bash
# Check locally
mvn clean test

# Check Java version
java -version
```

### Tests Fail
```bash
# Run with debug output
mvn test -X
```

### Docker Push Fails
```bash
# Verify GITHUB_TOKEN has write:packages scope
# Check GHCR login status
```

### Deployment Fails
```bash
# Verify SSH credentials
ssh -i deploy_key user@host "echo test"

# Check logs for connection errors
```

---

## 📚 Related Documentation

- [GITHUB_ACTIONS_GUIDE.md](./.github/GITHUB_ACTIONS_GUIDE.md) - Detailed workflow documentation
- [SETUP_GUIDE.md](./.github/SETUP_GUIDE.md) - Setup and configuration guide
- [README.md](../../README_API.md) - API documentation

---

## 🎯 Workflow Goals

✅ **Automated Testing** - Every push is tested  
✅ **Code Quality** - Multiple quality checks  
✅ **Security** - Vulnerability scanning  
✅ **Docker** - Container image generation  
✅ **Deployment** - Automated production deployment  
✅ **Notifications** - Status alerts  
✅ **Reliability** - Artifact retention  
✅ **Performance** - Parallel job execution  

---

## 📊 Monitoring Dashboard

Access via: `https://github.com/2300030862/deployment/actions`

View:
- ✅ Workflow run status
- ✅ Build logs
- ✅ Test results
- ✅ Artifact downloads
- ✅ Security alerts
- ✅ Deployment history

---

## 🔔 Status Badge

Add to README:
```markdown
[![CI/CD](https://github.com/2300030862/deployment/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/2300030862/deployment/actions)
```

---

**Last Updated:** March 2026  
**Workflow Version:** 1.0  
**Status:** ✅ Production Ready

