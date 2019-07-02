package Spring.Oracle.POC.repository
import Spring.Oracle.POC.model.DestModel
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
trait DestRepository extends CrudRepository[DestModel,Long]{


  @Query("from dqm_sample")
  def querydata():Iterable[DestModel]

}
