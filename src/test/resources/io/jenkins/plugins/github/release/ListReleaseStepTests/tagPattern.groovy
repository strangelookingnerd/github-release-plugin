package io.jenkins.plugins.github.release.ListReleaseStepTests

node {
    def foo = listGitHubReleases(
            credentialId: 'a1234',
            githubServer: '%s',
            includeDrafts: false,
            repository: 'jcustenborder/xjc-kafka-connect-plugin',
            tagNamePattern: '^0\\.2\\.\\d+$'
    )
    if (!foo) {
        throw new Exception("Releases should not be null.")
    }
    if(foo.size() != 1) {
        throw new Exception("1 Release should be present.")
    }
}