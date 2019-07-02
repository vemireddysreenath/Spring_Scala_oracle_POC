package Spring.Oracle.POC.repository

import Spring.Oracle.POC.model.SourceModel
import org.springframework.data.jpa.repository.JpaRepository

trait SourceRepository extends JpaRepository[SourceModel,Long]{

}
