# GitHub Actions CI/CD Workflow Documentation

## Overview

This GitHub Actions workflow automates the continuous integration and deployment (CI/CD) process for the Employee Management API Spring Boot application.

## Workflow Triggers

The workflow is triggered by:

```yaml
- Push to main or develop branches
- Pull requests to main or develop branches
- Manual trigger (workflow_dispatch)
```

## Workflow Jobs

### 1. **Build Job** (build)

**Runs on:** Ubuntu Latest  
**Purpose:** Compile, test, and build the Maven project

#### Steps:
1. **Checkout Code** - Clones the repository with full history
2. **Set up JDK 17** - Configures Java 17 using Temurin distribution
3. **Display Java Version** - Verification step
4. **Cache Maven Packages** - Speeds up subsequent builds
5. **Build with Maven** - Compiles without running tests
6. **Run Unit Tests** - Executes all unit tests
7. **Run Integration Tests** - Runs integration test suites
8. **Generate JaCoCo Coverage Report** - Creates code coverage metrics
9. **Publish Test Results** - Displays test results in PR/commit
10. **Upload Coverage to Codecov** - Sends coverage data to Codecov
11. **SonarQube Analysis** - Optional static code analysis
12. **Build Artifact** - Creates final JAR file
13. **Upload Artifact** - Stores JAR for 30 days

#### Output:
- Maven JAR artifact
- Test reports in XML format
- Code coverage metrics
- Build logs

---

### 2. **Code Quality Job** (code-quality)

**Runs on:** Ubuntu Latest  
**Depends on:** build job  
**Purpose:** Analyze code quality using multiple tools

#### Steps:
1. **SpotBugs Analysis** - Detects potential bugs in Java code
2. **CheckStyle Analysis** - Validates code style/conventions
3. **PMD Analysis** - Identifies problematic patterns and code smells

#### Tools Used:
- **SpotBugs**: Bug detection in compiled Java bytecode
- **CheckStyle**: Style and convention checking
- **PMD**: Pattern Matching Detection for code issues

---

### 3. **Security Job** (security)

**Runs on:** Ubuntu Latest  
**Depends on:** build job  
**Purpose:** Scan for security vulnerabilities

#### Steps:
1. **Dependency Check** - Scans dependencies for known CVEs
2. **Trivy Scanner** - Filesystem vulnerability scanning
3. **Upload Reports** - Stores security scan results

#### Scanners:
- **OWASP Dependency-Check**: Identifies vulnerable libraries
- **Trivy**: Container and filesystem vulnerability scanner

---

### 4. **Docker Build Job** (docker-build)

**Runs on:** Ubuntu Latest  
**Depends on:** build job  
**Purpose:** Build and push Docker image to GitHub Container Registry

#### Steps:
1. **Set up Docker Buildx** - Multi-platform Docker builder
2. **Login to GitHub Container Registry** - Authenticates with GHCR
3. **Extract Metadata** - Generates Docker tags
4. **Build and Push Docker Image** - Creates and pushes container image

#### Docker Tags Generated:
```
ghcr.io/username/deployment:branch-name
ghcr.io/username/deployment:sha-<commit-hash>
ghcr.io/username/deployment:v1.0.0 (if tagged)
```

#### Note:
- Images pushed to GitHub Container Registry (GHCR)
- Automatic caching for faster builds
- Multi-platform support ready

---

### 5. **Deploy Job** (deploy)

**Runs on:** Ubuntu Latest  
**Depends on:** build, security, docker-build  
**Condition:** Only on main branch, on push events, when all jobs succeed  
**Purpose:** Deploy application to production

#### Steps:
1. **Download Artifact** - Gets the built JAR file
2. **Deploy to Production** - Executes deployment commands
3. **Slack Notification** - Sends deployment status to Slack

#### Deployment Configuration Required:
```
DEPLOY_KEY: SSH private key for server access
DEPLOY_HOST: Production server hostname/IP
DEPLOY_USER: SSH username for server
```

#### Example Deployment Command:
```bash
ssh -i $DEPLOY_KEY $DEPLOY_USER@$DEPLOY_HOST "cd /app && ./deploy.sh"
```

---

### 6. **Notify Job** (notify)

**Runs on:** Ubuntu Latest  
**Depends on:** build, security  
**Condition:** Always runs (even on failure)  
**Purpose:** Send notifications about workflow status

#### Steps:
1. **Prepare Notification Message** - Creates status message
2. **Send Email Notification** - Email notification (optional)
3. **Slack Webhook** - Sends Slack message (optional)

---

## Required GitHub Secrets

Add these secrets to your GitHub repository settings (`Settings > Secrets and variables > Actions`):

### Optional Secrets:
```
SONAR_TOKEN           # SonarQube authentication token
SONAR_HOST_URL        # SonarQube server URL
SLACK_WEBHOOK_URL     # Slack webhook for notifications
DEPLOY_KEY            # SSH private key for deployment
DEPLOY_HOST           # Production server hostname
DEPLOY_USER           # SSH user for deployment
```

### GitHub Token (Automatic):
```
GITHUB_TOKEN          # Auto-provided by GitHub Actions
```

---

## Environment Variables

```yaml
REGISTRY: ghcr.io                          # GitHub Container Registry
IMAGE_NAME: ${{ github.repository }}       # Repository name
```

---

## Workflow Flow Diagram

```
┌─────────────────────────────────┐
│   Event Trigger                 │
│   (Push/PR/Manual)              │
└──────────────┬──────────────────┘
               │
         ┌─────▼─────┐
         │   BUILD   │ (compile, test, coverage)
         └─────┬─────┘
               │
        ┌──────┴──────┐
        │             │
   ┌────▼────┐  ┌────▼───────┐
   │ CODE    │  │ SECURITY   │
   │ QUALITY │  │ SCAN       │
   └────┬────┘  └────┬───────┘
        │            │
        └──────┬─────┘
               │
        ┌──────▼──────┐
        │   DOCKER    │ (build & push image)
        └──────┬──────┘
               │
        ┌──────▼──────────────────┐
        │   DEPLOY (main only)    │
        │   (production)          │
        └──────┬──────────────────┘
               │
        ┌──────▼──────┐
        │   NOTIFY    │
        └─────────────┘
```

---

## Example GitHub Actions Output

### Successful Build:
```
✅ Build and Tests Completed Successfully!
- Compiled Java code
- Ran 25 unit tests (all passed)
- Generated JaCoCo coverage report
- Built Docker image
- Pushed to GHCR
```

### Failed Build:
```
❌ Build Failed! Check logs for details.
- Test failures: 2 failed, 23 passed
- Code quality issues detected
- Build halted
```

---

## Cache Strategy

The workflow uses GitHub Actions cache to speed up builds:

```yaml
Key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}
```

**Cache invalidates when:**
- `pom.xml` file changes
- OS changes (Ubuntu version update)

**Benefits:**
- Faster builds (10-30% speedup)
- Reduced external API calls
- Cheaper GitHub Actions minutes

---

## Test Coverage

The workflow generates test coverage using JaCoCo:

**Coverage Report Location:**
```
target/site/jacoco/index.html
```

**Uploaded to:**
- GitHub Actions Artifacts
- Codecov.io (if configured)

**Coverage Metrics:**
- Line coverage
- Branch coverage
- Cyclomatic complexity

---

## Security Scanning

### Dependency Check
```bash
mvn org.owasp:dependency-check-maven:aggregate
```

**Checks for:**
- Known CVEs in dependencies
- Security vulnerabilities
- License compliance

### Trivy Scan
```bash
trivy fs .
```

**Scans:**
- Filesystem for vulnerabilities
- Configuration files
- Infrastructure as code issues

---

## Docker Image Registry

Images are pushed to **GitHub Container Registry (GHCR)**:

```
ghcr.io/2300030862/deployment
```

### Pull Command:
```bash
docker pull ghcr.io/2300030862/deployment:main
```

### Run Docker Container:
```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb \
  ghcr.io/2300030862/deployment:main
```

---

## Manual Workflow Trigger

To manually trigger the workflow:

1. Go to **Actions** tab in GitHub repository
2. Select **Java CI/CD Pipeline - Employee Management API**
3. Click **Run workflow**
4. Select branch (main or develop)
5. Click **Run workflow**

---

## Artifact Management

Artifacts are automatically stored:

**Artifact:** `deployment-service-jar`
- **Contents:** Built JAR file
- **Retention:** 30 days
- **Size:** ~50MB (typical)
- **Download:** From Actions > Workflow run > Artifacts

---

## Troubleshooting

### Build Fails on Compilation
```bash
# Check Java version
java -version

# Clear Maven cache
mvn clean

# Run offline check
mvn validate
```

### Tests Fail
```bash
# Run tests locally
mvn test

# Verbose output
mvn test -X
```

### Docker Build Fails
```bash
# Check Dockerfile
cat Dockerfile

# Build locally
docker build -t test .
```

### Deployment Fails
```bash
# Verify secrets are set
# Check DEPLOY_KEY, DEPLOY_HOST, DEPLOY_USER

# Test SSH connection manually
ssh -i <key> user@host "echo 'Connected'"
```

---

## Monitoring and Alerts

### View Workflow Runs:
1. Go to **Actions** tab
2. Select workflow
3. View run details, logs, and artifacts

### GitHub Status Badge:
```markdown
![Java CI/CD Pipeline](https://github.com/2300030862/deployment/actions/workflows/ci-cd.yml/badge.svg?branch=main)
```

Add to README.md for status visibility.

---

## Best Practices Implemented

✅ **Caching** - Maven dependencies cached for faster builds  
✅ **Parallelization** - Multiple jobs run in parallel  
✅ **Error Handling** - `continue-on-error` for non-critical steps  
✅ **Artifacts** - Test reports and coverage uploaded  
✅ **Security** - Multiple security scanners included  
✅ **Notifications** - Integration-ready for Slack/Email  
✅ **Staging** - Separate dev and production workflows  
✅ **Rollback** - Easy rollback via artifact retention  

---

## Cost Optimization

### GitHub Actions Minutes:
- Build: ~2-3 minutes
- Tests: ~1-2 minutes
- Security: ~3-5 minutes
- Total: ~6-10 minutes per run

**Free tier includes:** 2,000 minutes/month for public repositories

---

## Integration Points

### Slack Integration
Set webhook URL to receive build notifications in Slack channel.

### Codecov Integration
Track code coverage trends over time.

### Docker Hub Integration
Optionally push to Docker Hub instead of GHCR.

### Email Notifications
GitHub automatic notifications via email.

---

## Next Steps

1. **Add Secrets:** Go to repo settings and add required secrets
2. **Configure Deployment:** Update deployment script in `deploy` job
3. **Test Locally:** Run `mvn clean test` before pushing
4. **Create Release:** Tag commits for version releases
5. **Monitor:** Check Actions tab for build status

---

## Additional Resources

- [GitHub Actions Documentation](https://docs.github.com/actions)
- [Maven GitHub Actions](https://github.com/actions/setup-java)
- [Docker Build Action](https://github.com/docker/build-push-action)
- [SonarQube GitHub Action](https://github.com/sonarsource/sonarqube-scan-action)

