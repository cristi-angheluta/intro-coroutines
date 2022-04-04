package tasks

import contributors.*

suspend fun loadContributorsProgress(
    service: GitHubService,
    req: RequestData,
    updateResults: suspend (List<User>, completed: Boolean) -> Unit
) {
    val repos = service
        .getOrgRepos(req.org)
        .also { logRepos(req, it) }
        .bodyList()

    var allUsers = emptyList<User>()
    repos.forEachIndexed { index, repo ->
        val contribsByRepo = service.getRepoContributors(req.org, repo.name)
            .also { logUsers(repo, it) }
            .bodyList()
        allUsers = (allUsers + contribsByRepo).aggregate()
        updateResults(allUsers, index == repos.lastIndex)
    }
}
