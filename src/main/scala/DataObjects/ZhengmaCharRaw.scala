package DataObjects

case class ZhengmaCharRaw(char:String,
                          existsInTrad:Boolean,
                          existsInSimp:Boolean,
                          tzaiFreq:Int,
                          jundaFreq:Int,
                          zhengmaAllCodesWithTypes:List[CodeCollection],
                          zhengmaAllCodes:List[String],
                          zhengmaLongestCodes:List[String])

enum OriginSet:
  case Gongmu, Openvingen, Windows7
  
case class CodeCollection(codes:List[String],originalSet: OriginSet)
