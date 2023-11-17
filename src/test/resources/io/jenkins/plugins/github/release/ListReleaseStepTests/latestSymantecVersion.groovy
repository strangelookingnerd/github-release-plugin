package io.jenkins.plugins.github.release.ListReleaseStepTests

node {
    def releases = listGitHubReleases(
            credentialId: 'a1234',
            githubServer: '%s',
            repository: 'jcustenborder/xjc-kafka-connect-plugin',
            sortBy: 'SymantecVersion',
            sortAscending: false,
            tagNamePattern: "^0\\.2\\.\\d+"
    )
    if (!releases) {
        throw new Exception('Releases should not be null.')
    }
    if(releases.size() != 1) {
        throw new Exception('1 Releases should be present.')
    }
    def release = releases[0]
    def tagVersion = release.tagName

    def nextVersion = release.nextSymantecRevision()

    def expectedVersion = "0.2.14"

    if(nextVersion != expectedVersion) {
        throw new Exception("Expected version is ${expectedVersion} but ${nextVersion} was returned. Input version ${tagVersion}")
    }
}