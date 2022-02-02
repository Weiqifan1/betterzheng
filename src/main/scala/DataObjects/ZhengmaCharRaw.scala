package DataObjects

case class ZhengmaCharRaw(char:String,
                          existsInTrad:Boolean,
                          existsInSimp:Boolean,
                          tzaiFreq:Int,
                          jundaFreq:Int,
                          zhengmaAllCodes:List[CodeCollection],
                          zhengmaLongestCodes:List[String])
enum OriginSet:
  case Gongmu, Openvingen, Windows7
  
case class CodeCollection(codes:List[String],originalSet: OriginSet)
