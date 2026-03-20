# GitHub Actions Workflow - Visual Guide & Examples

## 📊 Workflow Visualization

### Complete Workflow Path

```
┌─────────────────────────────────────────────────────────────────────┐
│                         GITHUB EVENT                                │
│              (Push to main/develop or Pull Request)                 │
└────────────────────────┬────────────────────────────────────────────┘
                         │
                    START HERE
                         │
        ┌────────────────┴────────────────┐
        │                                 │
    ┌───▼────┐                       ┌───▼────┐
    │ BUILD  │◄──────┐              │PULL REQ│
    │ JOB    │       │              │ CHECK  │
    └───┬────┘       │              └────────┘
        │            │
        ├─► Checkout Code
        ├─► Setup JDK 17
        ├─► Cache Maven
        ├─► Compile Source
        ├─► Run Unit Tests
        ├─► Run Integration Tests
        ├─► Generate Coverage
        ├─► Publish Test Results
        ├─► Upload to Codecov
        ├─► SonarQube Scan
        ├─► Build JAR
        └─► Upload Artifact
             │
   ┌─────────┼─────────┬────────────────┐
   │         │         │                │
   │    ┌────▼─┐  ┌───▼───┐  ┌────▼───┐│
   │    │CODE  │  │SECURITY│  │DOCKER  ││
   │    │QUALITY│  │ SCAN   │  │BUILD   ││
   │    └─┬──┬─┘  └──┬──┬──┘  └──┬─┬───┘│
   │      │  │       │  │        │ │
   │  SpotBugs  Dep-Check  Build &
   │  CheckStyle Trivy    Push
   │  PMD      SARIF
   │
   └──────────┬──────────┘
              │
        ┌─────▼─────┐
        │  DEPLOY   │  (main branch only)
        │  JOB      │
        └─────┬─────┘
              │
    ┌─────────┼─────────┐
    │         │         │
  Download  Deploy   Notify
  Artifact   SSH     Slack


   ┌─────────────┐
   │  NOTIFY     │
   │  JOB        │  (always runs)
   └─────────────┘
```

---

## 🎯 Job Dependency Graph

```
BUILD
 ├─► CODE QUALITY
 ├─► SECURITY
 ├─► DOCKER BUILD
 │    └─► DEPLOY (main only)
 └─► NOTIFY
```

---

## 📍 Step-by-Step Execution

### BUILD JOB (5-10 minutes)

```
┌─ Checkout Code
│  └─ Pulls latest code from repository
│
├─ Setup JDK 17
│  └─ Installs Java 17 Temurin
│
├─ Cache Maven Packages
│  └─ Downloads/caches dependencies
│
├─ Build with Maven
│  └─ $ mvn clean install -DskipTests
│     ✅ Compilation successful
│     📦 JAR file ready
│
├─ Run Unit Tests
│  └─ $ mvn test
│     ✅ 25 tests passed
│     ❌ 0 tests failed
│
├─ Run Integration Tests
│  └─ $ mvn verify -DskipUnitTests
│     ✅ 5 integration tests passed
│
├─ Generate JaCoCo Coverage
│  └─ target/site/jacoco/index.html
│     Coverage: 85%
│
├─ Publish Test Results
│  └─ Creates GitHub PR comment
│     Test Results:
│     • 30 passed
│     • 0 failed
│     • 0 skipped
│
├─ Upload Coverage to Codecov
│  └─ Sends report to codecov.io
│
├─ SonarQube Analysis
│  └─ Static code analysis
│     Quality Gate: PASSED
│
├─ Build Final Artifact
│  └─ $ mvn package -DskipTests
│     deployment-service-0.0.1-SNAPSHOT.jar (42MB)
│
└─ Upload Artifact
   └─ GitHub Actions Storage (30 days)
      deployment-service-jar
```

### CODE QUALITY JOB (2-5 minutes)

```
┌─ SpotBugs Analysis
│  └─ $ mvn spotbugs:check
│     ✅ No critical bugs found
│
├─ CheckStyle Analysis
│  └─ $ mvn checkstyle:check
│     ⚠️  3 style violations
│     → Fixed in next commit
│
└─ PMD Analysis
   └─ $ mvn pmd:check
      ✅ No code smells detected
```

### SECURITY JOB (3-5 minutes)

```
┌─ Dependency Check
│  └─ Scans for known CVEs
│     ✅ 0 HIGH vulnerabilities
│     ⚠️  2 LOW vulnerabilities
│     → Update dependencies
│
├─ Trivy Vulnerability Scan
│  └─ Filesystem scan
│     ✅ Clean scan
│
└─ Upload SARIF Report
   └─ GitHub Security tab updated
```

### DOCKER BUILD JOB (2-4 minutes)

```
┌─ Setup Docker Buildx
│  └─ Multi-platform builder ready
│
├─ Login to GHCR
│  └─ Authenticated with GITHUB_TOKEN
│
├─ Extract Metadata
│  └─ Generate tags:
│     • ghcr.io/2300030862/deployment:main
│     • ghcr.io/2300030862/deployment:sha-a1b2c3d4
│
└─ Build & Push Docker Image
   └─ $ docker build -t ...
      Docker image pushed successfully
      Size: 285MB
```

### DEPLOY JOB (2-5 minutes) [main branch only]

```
┌─ Download Artifact
│  └─ Retrieves JAR from storage
│
├─ Deploy to Server
│  └─ $ ssh -i $DEPLOY_KEY user@host
│     ✅ Connected to server
│     ✅ Copied JAR
│     ✅ Restarted service
│
└─ Send Slack Notification
   └─ #deployments channel
      "✅ Deployment successful"
```

### NOTIFY JOB (1-2 minutes) [always]

```
┌─ Prepare Status Message
│  └─ ✅ All jobs successful
│
├─ Send Email Notification
│  └─ Sent to: developer@example.com
│
└─ Send Slack Message
   └─ #ci-cd channel updated
```

---

## 📋 Example Outputs

### ✅ Successful Workflow Run

```
Java CI/CD Pipeline - Employee Management API

Status: ✅ All checks passed

BUILD JOB (5 min 32 sec)
├─ Checkout Code ✅
├─ Setup JDK 17 ✅
├─ Cache Maven Packages ✅
├─ Build with Maven ✅
├─ Run Unit Tests ✅
│  └─ 25 passed, 0 failed
├─ Run Integration Tests ✅
│  └─ 5 passed
├─ Generate Coverage ✅
│  └─ 85% coverage
├─ Publish Test Results ✅
├─ Upload to Codecov ✅
├─ SonarQube Analysis ✅
│  └─ Quality Gate: PASSED
├─ Build Artifact ✅
└─ Upload Artifact ✅

CODE QUALITY JOB (3 min 12 sec)
├─ SpotBugs ✅
├─ CheckStyle ⚠️ (3 issues)
└─ PMD ✅

SECURITY JOB (4 min 18 sec)
├─ Dependency Check ✅
│  └─ 0 HIGH, 2 LOW vulnerabilities
├─ Trivy Scanner ✅
└─ Upload SARIF ✅

DOCKER BUILD JOB (3 min 45 sec)
├─ Setup Buildx ✅
├─ Login to GHCR ✅
├─ Extract Metadata ✅
└─ Build & Push ✅
   └─ ghcr.io/2300030862/deployment:main

DEPLOY JOB (2 min 30 sec) [main only]
├─ Download Artifact ✅
├─ Deploy to Server ✅
└─ Slack Notification ✅

NOTIFY JOB (1 min 05 sec)
├─ Status Message ✅
├─ Email Notification ✅
└─ Slack Message ✅

═══════════════════════════════════════════════════════════════
Total Time: 23 minutes 42 seconds
Status: ✅ PASSED
Artifacts: deployment-service-jar (42MB)
Docker: ghcr.io/2300030862/deployment:main
═══════════════════════════════════════════════════════════════
```

### ❌ Failed Workflow Run

```
Java CI/CD Pipeline - Employee Management API

Status: ❌ Build Failed

BUILD JOB (6 min 15 sec)
├─ Checkout Code ✅
├─ Setup JDK 17 ✅
├─ Cache Maven Packages ✅
├─ Build with Maven ✅
├─ Run Unit Tests ❌ FAILED
│  └─ 23 passed, 2 failed
│
│  Failed Tests:
│  • EmployeeServiceTest::testCreateEmployeeWithDuplicateEmail
│    NullPointerException at line 108
│
│  • EmployeeControllerTest::testGetEmployeeById
│    AssertionError: expected 200 but was 404
│
└─ Build halted

CODE QUALITY JOB (skipped)
SECURITY JOB (skipped)
DOCKER BUILD JOB (skipped)
DEPLOY JOB (skipped)

NOTIFY JOB (1 min 02 sec) [always runs]
├─ Status: ❌ FAILED
├─ Email Sent ✅
└─ Slack Message ✅

═══════════════════════════════════════════════════════════════
Total Time: 7 minutes 17 seconds
Status: ❌ FAILED
Artifacts: Not generated
═══════════════════════════════════════════════════════════════

Fix required in:
→ EmployeeServiceTest.java:108
→ EmployeeControllerTest.java:62

View logs: https://github.com/.../actions/runs/12345
```

---

## 📈 Performance Metrics

### Build Performance

| Step | Time | Notes |
|------|------|-------|
| Checkout | 5s | Code clone |
| Setup JDK | 8s | Java 17 download |
| Maven Cache | 15s | Dependency resolution |
| Compilation | 45s | Java compile |
| Unit Tests | 60s | 25 tests |
| Integration Tests | 30s | 5 tests |
| Coverage | 20s | JaCoCo report |
| **Total Build** | **3-5 min** | **Sequential** |

### Total Workflow Time

```
Sequential: 20-30 minutes
Parallel:   15-25 minutes (due to job dependency)

Cost: ~0.5 GitHub Actions minutes per run
```

---

## 🎛️ Workflow Configuration

### Trigger Conditions

```yaml
# Build and test on every push and PR
on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]
  workflow_dispatch:  # Manual trigger
```

### Conditional Job Execution

```yaml
# Only deploy on main branch
deploy:
  if: github.ref == 'refs/heads/main' && github.event_name == 'push'

# Skip on PR
docker-build:
  if: github.event_name != 'pull_request'

# Always run
notify:
  if: always()
```

---

## 📊 Dashboard Views

### GitHub Actions Dashboard

```
Repository → Actions Tab

Workflows:
├─ Java CI/CD Pipeline (active)
│  ├─ All workflow runs
│  │  ├─ Run #125 ✅ (latest)
│  │  ├─ Run #124 ❌ (failed)
│  │  ├─ Run #123 ✅
│  │  └─ ...
│  │
│  ├─ Usage
│  │  ├─ This month: 1,250 minutes
│  │  ├─ Limit: 2,000 minutes
│  │  └─ Usage: 62.5%
│  │
│  └─ Settings
│     ├─ Schedule
│     ├─ Permissions
│     └─ Artifacts

Artifacts:
├─ deployment-service-jar
│  ├─ Size: 42MB
│  ├─ Created: 1 hour ago
│  ├─ Expires: in 29 days
│  └─ [Download]
```

---

## 🔍 Log Examples

### Successful Build Log

```
Run: mvn clean install -DskipTests

[INFO] Scanning for projects...
[INFO] 
[INFO] -----------------------< com.actions:deployment >-----------------------
[INFO] Building  0.0.1-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- clean:3.2.0:clean (default-clean) @ deployment ---
[INFO] Deleting /home/runner/work/deployment/deployment/target
[INFO]
[INFO] --- resources:3.3.1:resources (default-resources) @ deployment ---
[INFO] Copying 1 resource from src/main/resources to target/classes
[INFO]
[INFO] --- compiler:3.11.0:compile (default-compile) @ deployment ---
[INFO] Compiling 14 source files with javac
[INFO] 
[INFO] --- jar:3.3.0:jar (default-jar) @ deployment ---
[INFO] Building jar: /home/runner/work/deployment/deployment/target/deployment-0.0.1-SNAPSHOT.jar
[INFO]
[INFO] --- install:3.1.1:install (default-install) @ deployment ---
[INFO] Installing /home/runner/.../deployment-0.0.1-SNAPSHOT.jar to /home/runner/.m2/repo...
[INFO]
[INFO] BUILD SUCCESS
[INFO] Total time:  3.456 s
[INFO] Finished at: 2024-03-20T12:35:45Z
```

### Failed Test Log

```
 T E S T S
-------------------------------------------------------
Running com.actions.deployment.service.EmployeeServiceTest
Tests run: 10, Failures: 1, Errors: 0, Skipped: 0, Time elapsed: 1.234s
------ FAILURE: testCreateEmployeeWithDuplicateEmail -------
java.lang.NullPointerException
    at com.actions.deployment.service.EmployeeServiceTest.testCreateEmployee(EmployeeServiceTest.java:108)

Results :

Failed tests:
  EmployeeServiceTest.testCreateEmployeeWithDuplicateEmail

Tests run: 10, Failures: 1, Errors: 0, Skipped: 0

[ERROR] Build failure
```

---

## 🚀 Workflow Tips

### Fast-track Builds
- Use GitHub Actions cache
- Skip unnecessary steps
- Parallel job execution

### Reduce Build Time
```yaml
# Skip tests on documentation changes
if: "!contains(github.event.head_commit.message, '[skip-tests]')"
```

### Monitor Performance
- GitHub Actions usage dashboard
- Build time trends
- Cost tracking

---

## 📞 Support Commands

```bash
# List workflow runs
gh run list -w ci-cd.yml

# View run details
gh run view <run-id>

# View logs
gh run view <run-id> --log

# Cancel run
gh run cancel <run-id>

# Rerun failed jobs
gh run rerun <run-id>
```

---

**This completes the GitHub Actions workflow setup for your Employee Management API!**

