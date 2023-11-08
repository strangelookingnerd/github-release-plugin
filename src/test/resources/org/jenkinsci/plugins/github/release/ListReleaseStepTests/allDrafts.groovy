package org.jenkinsci.plugins.github.release.ListReleaseStepTests

node {
    def foo = listGitHubReleases(
            credentialId: 'a1234',
            githubServer: '%s',
            includeDrafts: true,
            repository: 'jcustenborder/xjc-kafka-connect-plugin'
    )
    if (!foo) {
        throw new Exception("Releases should not be null.")
    }
    if(foo.size() != 10) {
        throw new Exception("10 Releases should be present.")
    }
}