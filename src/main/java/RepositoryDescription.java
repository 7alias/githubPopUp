import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;

import java.util.List;

public class RepositoryDescription {
private String name;
private GHRepository repository;
private List<GHPullRequest> prs;


    public RepositoryDescription(String name, GHRepository repository, java.util.List<GHPullRequest> prs) {
        this.name = name;
        this.repository = repository;
        this.prs = prs;
    }

    public String getName() {
        return name;
    }

    public GHRepository getRepository() {
        return repository;
    }

    public java.util.List<GHPullRequest> getPrs() {
        return prs;
    }
}
