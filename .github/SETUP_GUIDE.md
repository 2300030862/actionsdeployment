# GitHub Actions Setup Guide

## Step 1: Generate GitHub Personal Access Token

1. Go to **GitHub Settings** → **Developer settings** → **Personal access tokens** → **Tokens (classic)**
2. Click **Generate new token (classic)**
3. Select scopes:
   - `repo` (full control of private repositories)
   - `write:packages` (for Docker image push)
   - `read:packages` (for Docker image pull)
4. Click **Generate token** and copy it (save securely)

---

## Step 2: Add GitHub Secrets to Repository

1. Go to your repository: `https://github.com/2300030862/deployment`
2. Navigate to **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret**

### Add These Secrets:

#### (Optional) SonarQube Integration
```
Name: SONAR_TOKEN
Value: <your-sonarqube-token>
```

```
Name: SONAR_HOST_URL
Value: https://sonarqube.example.com
```

#### (Optional) Slack Integration
```
Name: SLACK_WEBHOOK_URL
Value: https://hooks.slack.com/services/YOUR/WEBHOOK/URL
```

#### (Optional) Deployment Credentials
```
Name: DEPLOY_KEY
Value: <your-ssh-private-key-content>
```

```
Name: DEPLOY_HOST
Value: your-production-server.com
```

```
Name: DEPLOY_USER
Value: deploy-user
```

#### (Optional) Docker Hub Credentials
```
Name: DOCKER_USERNAME
Value: your-docker-hub-username
```

```
Name: DOCKER_PASSWORD
Value: your-docker-hub-password
```

---

## Step 3: Configure SSH Key for Deployment (Optional)

### Generate SSH Key Pair:
```bash
ssh-keygen -t rsa -b 4096 -f deploy_key -N ""
```

This creates:
- `deploy_key` (private key)
- `deploy_key.pub` (public key)

### Add Public Key to Server:
```bash
# On your production server
cat deploy_key.pub >> ~/.ssh/authorized_keys
chmod 600 ~/.ssh/authorized_keys
```

### Add Private Key to GitHub Secrets:
1. Read the private key:
```bash
cat deploy_key
```

2. Copy the entire content (including BEGIN/END lines)
3. Add to GitHub as `DEPLOY_KEY` secret

---

## Step 4: Setup Slack Integration (Optional)

### Create Slack Webhook:
1. Go to **Slack Workspace Settings** → **App Management**
2. Create new app or select existing
3. Enable **Incoming Webhooks**
4. Click **Add New Webhook to Workspace**
5. Select channel and click **Allow**
6. Copy webhook URL

### Add to GitHub:
1. Go to repository **Settings** → **Secrets**
2. Create `SLACK_WEBHOOK_URL` secret
3. Paste the webhook URL

---

## Step 5: Setup SonarQube Integration (Optional)

### Generate SonarQube Token:
1. Go to **SonarQube** → **My Account** → **Security**
2. Generate new token
3. Copy the token

### Add to GitHub:
```
Name: SONAR_TOKEN
Value: <your-sonarqube-token>

Name: SONAR_HOST_URL
Value: https://sonarqube.example.com
```

---

## Step 6: Verify Workflow File

Ensure `.github/workflows/ci-cd.yml` exists:
```
✅ File present: .github/workflows/ci-cd.yml
```

---

## Step 7: Test the Workflow

### Push Code to GitHub:
```bash
git add .
git commit -m "Setup GitHub Actions CI/CD"
git push origin main
```

### Monitor Workflow:
1. Go to repository **Actions** tab
2. Click on **Java CI/CD Pipeline**
3. Watch the workflow run
4. Check logs for any issues

---

## Workflow Status Indicators

| Status | Meaning |
|--------|---------|
| ✅ Green | All jobs passed |
| ❌ Red | At least one job failed |
| ⏳ Yellow | Workflow in progress |
| ⊘ Gray | Workflow skipped/cancelled |

---

## Viewing Logs

### Access Build Logs:
1. Go to **Actions** tab
2. Click on workflow run
3. Click on specific job
4. Expand step to view logs

### Common Log Messages:

**Build Success:**
```
✅ Build and Tests Completed Successfully!
```

**Test Failure:**
```
❌ 2 tests failed, 23 passed
FAILURE: Tests failed, see error details above
```

**Docker Push Success:**
```
Pushed: ghcr.io/2300030862/deployment:main
```

---

## Adding Status Badge to README

Add this to your `README.md`:

```markdown
## CI/CD Status

[![Java CI/CD Pipeline](https://github.com/2300030862/deployment/actions/workflows/ci-cd.yml/badge.svg?branch=main)](https://github.com/2300030862/deployment/actions/workflows/ci-cd.yml)

[View Workflow Runs](https://github.com/2300030862/deployment/actions)
```

---

## Troubleshooting Common Issues

### Issue: "Error: Resource not accessible by integration"
**Solution:** Ensure `GITHUB_TOKEN` has proper permissions:
1. Go to repo **Settings** → **Actions** → **General**
2. Under "Workflow permissions", select **Read and write permissions**
3. Check **Allow GitHub Actions to create and approve pull requests**

### Issue: "Secret not found"
**Solution:** Verify secret name matches exactly:
```yaml
${{ secrets.SONAR_TOKEN }}  # Correct
${{ secrets.sonar_token }}  # Wrong (case-sensitive)
```

### Issue: "Docker image push fails"
**Solution:** 
1. Verify GitHub token has `write:packages` scope
2. Check workflow file uses correct registry: `ghcr.io`
3. Ensure user is logged in to GHCR

### Issue: "Deployment fails - Connection refused"
**Solution:**
1. Verify `DEPLOY_HOST` and `DEPLOY_USER` are correct
2. Test SSH connection locally:
   ```bash
   ssh -i deploy_key deploy-user@your-server.com
   ```
3. Check firewall allows SSH (port 22)
4. Verify public key added to server's `~/.ssh/authorized_keys`

### Issue: "Maven build fails - out of memory"
**Solution:** Increase heap size in workflow:
```yaml
- name: Build with Maven
  env:
    MAVEN_OPTS: "-Xmx1024m"
  run: mvn clean install
```

---

## Best Practices

✅ **Never commit secrets** to repository  
✅ **Use GitHub Secrets** for sensitive data  
✅ **Rotate SSH keys** periodically  
✅ **Monitor build logs** for failures  
✅ **Test locally** before pushing  
✅ **Keep dependencies updated**  
✅ **Review security scans** regularly  
✅ **Backup artifacts** important builds  

---

## Next Steps

1. ✅ Create GitHub Personal Access Token
2. ✅ Add repository secrets
3. ✅ Configure SSH key (if deploying)
4. ✅ Setup Slack webhook (optional)
5. ✅ Push code to GitHub
6. ✅ Monitor first workflow run
7. ✅ Add status badge to README
8. ✅ Configure branch protection rules

---

## Additional Configuration Options

### Branch Protection Rules

1. Go to **Settings** → **Branches**
2. Click **Add rule**
3. Branch name pattern: `main`
4. Enable:
   - Require a pull request before merging
   - Require status checks to pass before merging
   - Select `build` job as required check
   - Dismiss stale reviews

### Automatic Deployment Rules

```yaml
# Deploy only on version tags
if: startsWith(github.ref, 'refs/tags/v')
```

### Notification Settings

1. Go to **Settings** → **Notifications**
2. Choose when to receive notifications:
   - On failure
   - On completion
   - Always

---

## Support and Resources

- **GitHub Docs:** https://docs.github.com/actions
- **Action Marketplace:** https://github.com/marketplace?type=actions
- **Troubleshooting:** https://docs.github.com/en/actions/troubleshooting

---

## Workflow Commands Cheat Sheet

```bash
# Run workflow manually
gh workflow run ci-cd.yml

# View workflow runs
gh run list -w ci-cd.yml

# View specific run
gh run view <run-id>

# View logs
gh run view <run-id> --log

# Cancel workflow
gh run cancel <run-id>

# Rerun failed jobs
gh run rerun <run-id>
```

Install GitHub CLI: https://cli.github.com/

---

Last Updated: March 2024

