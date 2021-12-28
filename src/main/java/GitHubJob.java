import org.kohsuke.github.GHMyself;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class GitHubJob {
    private final GitHub gitHub;
    private final Gui gui = new Gui();
    private final Set<Long> allPrIds = new HashSet<>();

    public GitHubJob() {
        try {
            gitHub = new GitHubBuilder()
                    .withAppInstallationToken(System.getenv("GITHUB_TOKEN"))
                    .build();
            init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void init() throws IOException {
        GHMyself myself = gitHub.getMyself();
        String login = myself.getLogin();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    boolean notifyForNewPrs = !allPrIds.isEmpty();
                    HashSet<GHPullRequest> newPrs = new HashSet<>();
                    myself.getAllRepositories()
                            .values()
                            .stream()
                            .map(repository -> {
                                List<GHPullRequest> prs = null;
                                try {
                                    prs = repository.queryPullRequests()
                                            .list()
                                            .toList();
                                    Set<Long> prIds = prs.stream()
                                            .map(GHPullRequest::getId)
                                            .collect(Collectors.toSet());
                                    prIds.removeAll(allPrIds);
                                    allPrIds.addAll(prIds);
                                    prs.forEach(pr->{
                                        if(prIds.contains(pr.getId())){
                                            newPrs.add(pr);

                                                }
                                            });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return new RepositoryDescription(
                                        repository.getFullName(),
                                        repository,
                                        prs
                                );
                            })
                            .collect(Collectors.toList());
                    if(notifyForNewPrs){
                    newPrs.forEach(pr->{gui.showNotification(
                            "New PR in "+ pr.getRepository().getFullName(), pr.getTitle());});}
                } catch (IOException e) {
                    throw new RuntimeException();
                }
            }
        }, 1000, 1000);


    }
}
