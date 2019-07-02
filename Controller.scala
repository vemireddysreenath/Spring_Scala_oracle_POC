package Spring.Oracle.POC.controller

import java.io.FileWriter
import java.lang
import java.sql.Date
import java.util.List

import Spring.Oracle.POC.model.{DestModel, SourceModel}
import Spring.Oracle.POC.repository.{DestRepository, SourceRepository}
import com.opencsv.CSVWriter
import com.opencsv.bean.{StatefulBeanToCsv, StatefulBeanToCsvBuilder}
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation._

@Bean
@RestController
@RequestMapping(Array("/api"))
class Controller(@Autowired sourceRepository: SourceRepository, @Autowired destRepository: DestRepository) {

    @GetMapping(Array("/getsource"))
    def getWholeTab : List[SourceModel] = {println("Full table printed");sourceRepository.findAll()}

    @GetMapping(Array("/put"))
    def uploadData:String = {
      val sourceTab = sourceRepository.findAll().iterator()
      while(sourceTab.hasNext){
        val fields = sourceTab.next()
        val dq_project_id : Long= fields.dq_project_id.toLong
        val entity_name : String = fields.entity_name.toString
        val error_date_of_run: Date = fields.error_date_of_run
        val destModel = new DestModel
        destModel.setDq_project_id(dq_project_id)
        destModel.setEntity_name(entity_name)
        destModel.setError_date_of_run(error_date_of_run)
        destRepository.save(destModel)
      }
      return "Data transfer is completed"
    }

    @GetMapping(Array("/getdest"))
    def getDestTab : lang.Iterable[DestModel] = {
      print(destRepository.querydata)
      destRepository.findAll
    }
    @PostMapping(Array("/getdest"))
    def updateField(@Valid @RequestBody destModel: DestModel)= {
      destRepository.save(destModel)

    }

    //@Query("from dqm_sample")
    //def findall=

 // @Query("select * from DestModel")
  //def findall(destmod : List[DestModel])=

    @PutMapping(Array("/getdest"))
    def addField(@Valid @RequestBody destModel: DestModel)= {
      destRepository.save(destModel)
    }

    @DeleteMapping(Array("/del/{dq_project_id}"))
    def delRecord(@PathVariable("dq_project_id") dq_project_id :Int): String ={
      destRepository.deleteById(dq_project_id)
      return s"$dq_project_id record is deleted from destination table"
    }

  /*
  @PutMapping(Array("/posts/{postId}"))
  def updatePost(@PathVariable postId: Long, @Valid @RequestBody postRequest: Nothing): Nothing =
          postRepository.findById(postId).map((post) => {
    def foo(post) = {
      post.setTitle(postRequest.getTitle)
      post.setDescription(postRequest.getDescription)
      post.setContent(postRequest.getContent)
      postRepository.save(post)
    }
    foo(post)
  })*/

  //@PostMapping(Array("/notes"))
  //def createNote(@Valid @RequestBody note: Note): Note = noteRepository.save(note)

    @GetMapping(Array("/{getDq_project_id}"))
    def getById(@PathVariable("getDq_project_id") getDq_project_id: Long) =sourceRepository.findById(getDq_project_id)

    /*@GetMapping(Array("/downFile"))
    def downloadFile  = {
            var data = tabRepository.findAll
            var pw = new PrintWriter("/sample11.txt")
            pw.print(data)
            pw.close()
    }*/

   @GetMapping(Array("/downloadsource"))
    def downloadFile1(response : HttpServletResponse):Unit  = {
            val filename = new FileWriter("users.csv")
            response.setContentType("text/csv")
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            val writer : StatefulBeanToCsv[SourceModel]= {
                        new StatefulBeanToCsvBuilder[SourceModel](response.getWriter)
                        .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator('\t').build()
            }
            writer.write(getWholeTab)
   }
}