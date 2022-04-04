package tasks

import contributors.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend fun loadContributorsConcurrent(service: GitHubService, req: RequestData): List<User> = coroutineScope {
    val repos = service
        .getOrgRepos(req.org)
        .also { logRepos(req, it) }
        .bodyList()

    val contribDeferred = repos.map { repo ->
        val contributorsByRepo = async {
            log("starting loading for ${repo.name}")

            service.getRepoContributors(req.org, repo.name)
                .also { logUsers(repo, it) }.bodyList()
        }
        contributorsByRepo
    }

    val contribs = contribDeferred.awaitAll().flatten()

    contribs.aggregate()
}
