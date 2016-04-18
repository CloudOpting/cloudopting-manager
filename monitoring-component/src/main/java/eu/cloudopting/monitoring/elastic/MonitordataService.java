package eu.cloudopting.monitoring.elastic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.cloudopting.domain.Customizations;
import eu.cloudopting.domain.MonitoringInfoElastic;
import eu.cloudopting.monitoring.elastic.data.ElasticData;
import eu.cloudopting.monitoring.elastic.data.ElasticGraphData;
import eu.cloudopting.monitoring.elastic.data.Monitordata;
import eu.cloudopting.service.CustomizationService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.AndFilterBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryFilterBuilder;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.InternalFilter;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.elasticsearch.action.search.SearchType.COUNT;

@Transactional
@Service
public class MonitordataService {
	private final Logger log = LoggerFactory.getLogger(MonitordataService.class);
	@Autowired
	CustomizationService customizationService;

	@Autowired
	private MonitordataRepository monitordataRepository;

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	private static final Logger logger = LoggerFactory.getLogger(MonitordataService.class);

	public Monitordata findOne(String id) {
		logger.debug("in findone: " + id);
		return monitordataRepository.findOne(id);
	}

	public Iterable<Monitordata> findAll() {
		// TODO Auto-generated method stub
		return monitordataRepository.findAll();
	}

	public List<Monitordata> findCustom(String container) {
		return monitordataRepository.findCustom(container,
				new PageRequest(0, 20, new Sort(new Sort.Order(Sort.Direction.ASC, "@timestamp"))));
	}

	public List<Monitordata> getMonitorData(String container, String condition, String fields, String type,
			Long pagination) {
		log.debug("pagination:" + pagination.toString());
		log.debug("condit" + condition);
		return monitordataRepository.findMonitorData(container, condition, fields,
				new PageRequest(0, pagination.intValue(), new Sort(new Sort.Order(Sort.Direction.ASC, "@timestamp"))));
	}

	public ElasticData[] getAggregatedMonitorData(String container, String condition, String fields, String type,
			Long pagination, String startDate, String endDate) {
		log.debug("pagination:" + pagination.toString());
		log.debug("condit:" + condition);
		ElasticData graphData[] = null;
		// we need a match all query to start with
		MatchAllQueryBuilder all = QueryBuilders.matchAllQuery();
		log.debug(all.toString());

		// we need a date histogram aggregation
		DateHistogramBuilder dayly = AggregationBuilders.dateHistogram("dayly").field("@timestamp")
				.interval(DateHistogram.Interval.DAY).format("yyyy-MM-dd hh:mm:ss");
		log.debug(dayly.toString());
		// we need to filter the data for time range AND data match
		RangeFilterBuilder rangeFilter = FilterBuilders.rangeFilter("@timestamp").gte(startDate);
		log.debug(rangeFilter.toString());
		// we need to match on the path AND containername
		MatchQueryBuilder pathMatch = QueryBuilders.matchQuery(fields, condition)
				.operator(MatchQueryBuilder.Operator.AND);
		MatchQueryBuilder contMatch = QueryBuilders.matchQuery("container_name", container)
				.operator(MatchQueryBuilder.Operator.AND);

		// place the match in bool AND
		BoolQueryBuilder boolQ = QueryBuilders.boolQuery().must(pathMatch);
		boolQ.must(contMatch);
		// assign the bool the the filter
		QueryFilterBuilder queryF = FilterBuilders.queryFilter(boolQ);

		// place the match in AND with the range
		AndFilterBuilder filter = FilterBuilders.andFilter(rangeFilter, queryF);

		// we need the filter aggregation
		FilterAggregationBuilder containerFilter = AggregationBuilders.filter("containers").filter(filter)
				.subAggregation(dayly);
		log.debug(containerFilter.toString());
		// build the aggregation native search query
		SearchQuery searchQ = new NativeSearchQueryBuilder().withQuery(all).withSearchType(COUNT).withIndices("coidx-")
				.withTypes("fluentd").addAggregation(containerFilter).build();

		// do the query
		Aggregations aggregations = elasticsearchTemplate.query(searchQ, new ResultsExtractor<Aggregations>() {
			@Override
			public Aggregations extract(SearchResponse response) {
				// TODO Auto-generated method stub
				// response.
				log.debug(response.toString());
				log.debug(response.getHits().toString());
				return response.getAggregations();
			}

		});
		log.debug("PARSING---------------");
		for (Aggregation aAgg : aggregations.asList()) {
			InternalFilter cnt = (InternalFilter) aAgg;
			log.debug("aggregation name:" + cnt.getName());
			log.debug(cnt.toString());

			for (Aggregation theA : cnt.getAggregations().asList()) {
				InternalDateHistogram idh = (InternalDateHistogram) theA;
				graphData = new ElasticData[idh.getBuckets().size()];
				int gdi = 0;
				for (Histogram.Bucket entry : idh.getBuckets()) {
					// logger.debug("entry:" + entry.getKey() + "aggre count:" +
					// new Long(entry.getDocCount()).toString());
					// logger.debug(new Long(entry.getDocCount()).toString());
					ElasticData ed = new ElasticData(entry.getKey(), new Long(entry.getDocCount()).toString());
					log.debug(ed.toString());
					graphData[gdi] = ed;
					gdi++;
				}
				ElasticGraphData egd = new ElasticGraphData();
				// log.debug(new Integer(graphData.size()).toString());
				// if (!graphData.isEmpty()) {
				egd.setData(graphData);
			}
			// logger.debug(cnt.;
			// logger.debug(new Long(cnt.getValue()).toString());
		}

		return graphData;
	}

	public ArrayList<ElasticGraphData> getAllAggregatedMonitorData(Long customizationId, String startDate, String endDate) {
		// recover the customization
		Customizations cust = customizationService.findOne(customizationId);
		// get info on elastic form Db
		Set<MonitoringInfoElastic> elastinfo = cust.getElasticInfos();
		log.debug(new Integer(elastinfo.size()).toString());

		ArrayList<ElasticGraphData> retData = new ArrayList<ElasticGraphData>();
		// with info I get data to pass to the get monitor data
		for (MonitoringInfoElastic ei : elastinfo) {
			log.debug(ei.getContainer());
			ElasticData[] graphData = this.getAggregatedMonitorData(ei.getContainer(), ei.getCondition(),
					ei.getFields(), ei.getType(), ei.getPagination(), startDate, endDate);
			ElasticGraphData egd = new ElasticGraphData();
			// log.debug(new Integer(graphData.size()).toString());
			// if (!graphData.isEmpty()) {
			egd.setData(graphData);
			// }
			egd.setTitle(ei.getTitle());
			egd.setDescription(ei.getDescription());
			egd.setType(ei.getType());
			egd.setXkey("time");
			egd.setYkeys(new String[] { "value" });
			egd.setLabels(new String[] { "Value" });
			egd.setLineColors(new String[] { "blue" });

			retData.add(egd);
		}
		return retData;
	}

	public ArrayList<ElasticGraphData> getAllMonitorData(Long customizationId) {
		// recover the customization
		Customizations cust = customizationService.findOne(customizationId);
		// get info on elastic form Db
		Set<MonitoringInfoElastic> elastinfo = cust.getElasticInfos();
		log.debug(new Integer(elastinfo.size()).toString());

		ArrayList<ElasticGraphData> retData = new ArrayList<ElasticGraphData>();
		// with info I get data to pass to the get monitor data
		for (MonitoringInfoElastic ei : elastinfo) {
			log.debug(ei.getContainer());
			List<Monitordata> eData = this.getMonitorData(ei.getContainer(), ei.getCondition(), ei.getFields(),
					ei.getType(), ei.getPagination());

			ElasticData graphData[] = new ElasticData[eData.size()];
			for (int k = 0; k < eData.size(); k++) {
				ElasticData ed = new ElasticData(
						new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(eData.get(k).getTimestamp()), "1");
				log.debug(ed.toString());
				graphData[k] = ed;
			}
			ElasticGraphData egd = new ElasticGraphData();
			// log.debug(new Integer(graphData.size()).toString());
			// if (!graphData.isEmpty()) {
			egd.setData(graphData);
			// }
			egd.setTitle(ei.getTitle());
			egd.setDescription(ei.getDescription());
			egd.setType(ei.getType());
			egd.setXkey("time");
			egd.setYkeys(new String[] { "value" });
			egd.setLabels(new String[] { "Value" });
			egd.setLineColors(new String[] { "blue" });

			retData.add(egd);
		}
		// will return a list of list
		return retData;
	}

}
