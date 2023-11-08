package org.jenkinsci.plugins.github.release.ListReleaseStepTests

node {
    def releases = listGitHubReleases(
            credentialId: 'a1234',
            githubServer: '%s',
            includeDrafts: false,
            repository: 'jcustenborder/xjc-kafka-connect-plugin'
    )
    if (!releases) {
        throw new Exception("Releases should not be null.")
    }
    if(releases.size() != 3) {
        throw new Exception("3 Releases should be present.")
    }
}