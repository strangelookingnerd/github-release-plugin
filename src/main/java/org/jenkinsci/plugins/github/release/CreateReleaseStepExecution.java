package org.jenkinsci.plugins.github.release;

import hudson.FilePath;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.github.GitHubUtils;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.SynchronousStepExecution;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHReleaseBuilder;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import static org.jenkinsci.plugins.github.ParameterUtils.checkArgument;

public class CreateReleaseStepExecution extends SynchronousStepExecution<Release> {
  private final CreateReleaseStep step;

  protected CreateReleaseStepExecution(CreateReleaseStep step, StepContext context) {
    super(context);
    this.step = step;
  }

  @Override
  protected Release run() throws Exception {
    checkArgument("tag", this.step.tag);

    if (null == this.step.commitish) {
      throw new IllegalArgumentException(
          "Could not determine commitish from build. 'commitish' parameter must be set."
      );
    }

    TaskListener listener = getContext().get(TaskListener.class);
    FilePath filePath = getContext().get(FilePath.class);
    GitHub gitHub = GitHubUtils.loginToGithub(this.step, listener);
    GHRepository repository = GitHubUtils.getRepository(gitHub, this.step);

    String body;
    if (null != this.step.bodyText) {
      body = this.step.bodyText;
    } else if (null != this.step.bodyFile) {
      FilePath bodyFile = filePath.child(this.step.bodyFile);
      listener.getLogger().printf("Reading from %s.\n", bodyFile);
      body = bodyFile.readToString();
    } else {
      body = null;
    }

    GHReleaseBuilder ghReleaseBuilder = repository.createRelease(this.step.tag)
        .commitish(this.step.commitish);

    if(null != this.step.name) {
      ghReleaseBuilder = ghReleaseBuilder.name(this.step.name);
    }

    if (null != body) {
      ghReleaseBuilder = ghReleaseBuilder.body(body);
    }
    if (null != this.step.categoryName) {
      ghReleaseBuilder = ghReleaseBuilder.body(this.step.categoryName);
    }
    if (null != this.step.draft) {
      ghReleaseBuilder = ghReleaseBuilder.draft(this.step.draft);
    }
    if (null != this.step.prerelease) {
      ghReleaseBuilder = ghReleaseBuilder.prerelease(this.step.prerelease);
    }

    GHRelease release = ghReleaseBuilder.create();

    return Release.from(release);
  }
}
