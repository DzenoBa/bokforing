
package se.chalmers.bokforing.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.chalmers.bokforing.model.AccessKey;
import se.chalmers.bokforing.model.AccessKeyType;
import se.chalmers.bokforing.model.user.UserAccount;

/**
 *
 * @author Dženan
 */
@Repository
public interface AccessKeyRepository extends JpaRepository<AccessKey,Long> {
    
    List<AccessKey> findByUserAccount(UserAccount userAccount);
    
    AccessKey findByUserAccountAndKtype(UserAccount userAccount, AccessKeyType type);
    
    void removeByUserAccount(UserAccount userAccount);
}
