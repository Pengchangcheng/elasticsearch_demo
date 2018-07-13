# elasticsearch_demo
研究研究es， 主要是说一下我遇到的问题，初学者共勉   
## 1.版本问题   
我用到了3个工具，es（2.4.0），ik（1.10.0），spring-data-elasticsearch(2.1.7.RELEASE)，我原本以为高版本会兼容低版本，然鹅并不是，
es版本更新的太快了，现在都6.3.1了，我采用的是以上版本，主要是兼容前辈的代码。
## 2.配置
es配置，es集群cluster就是多个集群，在config/elasticsearch.yml下配置cluster的名字和node的名称，不配置会默认分配，Chrome有elasticsearch-head的插件，蛮好用的。多个集群就是多个主机，多个IP，多个node就是多复制几个es的整个文件夹，改改配置即可。当时理解这个还懵逼了半天。新建索引的时候会提示是否备份，有备份会新建多个node，一个node可以不用新建备份，分片可以选多个，这个官方指南有介绍，也很好理解。   
   
ik配置，IK是[mdecl](https://github.com/medcl/elasticsearch-analysis-ik)大佬造出来的中文分词的轮子，很不错，我也没试过其他的，无法给出对比意见。IK配置config/IKAnalyzer.cfg.xml，[这位大佬](http://www.cnblogs.com/zlslch/p/6441315.html)主要是热更新动态字典用的，远程词典放在文件服务器上，配置以后可以热更新，我使用的版本经试验是该版本IK+es是支持远程词典热更新的，就是怎么实时更新这个远程词典我还没想到很好的方案，目前是手动改，然后放在本地的tomcat上访问。   
   
spring-data-elsticsearch配置，需要配置一个xml,百度有，读取properties文件中的集群地址端口和node名称，还有扫描dao层路径。完全类似数据库，大大降低了学习成本啊，虽然es可以通过官方的restClient来访问，但是好麻烦。
## 3.es学习
es搜索很强大，就是语句开始不是很好写，阅读以下[官方文档](https://elasticsearch.cn/book/elasticsearch_definitive_guide_2.x/_elasticsearch_version.html)练习一下会有所帮助，对于java后端开发，spring-data-elasticsearch集成简直就是福音啊。封装很好，直接可以用。

## 4.主要代码
[大佬](http://liuxi.name/blog/20160803/spring-data-es.html)在此      
我自己的代码无法分享，以下是用到的几个类和service，   
 @Autowired   
    private ElasticsearchTemplate elasticsearchTemplate;   
      -----------------------查询-----------------------   
 Criteria criteria = new Criteria("province").is("111111").   
                and(new Criteria("city").is("22222")).   
                and(new Criteria("district").is("33333")).   
                and(new Criteria("addressKeywords").contains("44444"));   
CriteriaQuery criteriaQuery = new CriteriaQuery(criteria);   
List<T> tList2 =  elasticsearchTemplate.queryForList(criteriaQuery, T.class);      
  -----------------------查询-----------------------   
  NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();   
        BoolQueryBuilder bqb = QueryBuilders.boolQuery();   
        bqb.must(QueryBuilders.termQuery("province","111"));   
        bqb.must(QueryBuilders.termQuery("city","222"));    
        bqb.must(QueryBuilders.termQuery("district","3333"));    
        bqb.must(QueryBuilders.matchQuery("addressKeywords","44444444"));   
        searchQueryBuilder.withIndices("index").withTypes("type").withQuery(bqb);   
        NativeSearchQuery nativeSearchQuery = searchQueryBuilder.build();    
        List<T> tList =  elasticsearchTemplate.queryForList(nativeSearchQuery,AreaSearchRequest.class);    
  -----------------------插入-----------------------   
T t = new T();   
        t.setProvince("111");   
        t.setCity("222");   
        t.setDistrict("333");   
        t.setAddressKeywords("444444");      
        T model = tDao.save(t);   
-----------------------Dao-----------------------   
public interface tDao  extends ElasticsearchCrudRepository<AreaSearchRequest,String> {

}
