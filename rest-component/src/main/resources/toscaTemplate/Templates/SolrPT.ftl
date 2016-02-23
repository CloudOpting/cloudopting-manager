class{'solr':
<#if url?has_content>url => '${url}',</#if>
<#if version?has_content>version => '${version}',</#if>
<#if solr_host?has_content>dbhost => '${solr_host}',</#if>
<#if solr_port?has_content>dbname => ${solr_port},</#if>
}


##### in the file we need to read the file from GIT
file {'/tmp/schema.xml':
  ensure => file,
  content => inline_template('....'),
}

solr::core{'ckan':
  schema_src_file => '/tmp/schema.xml',
  require         => File ['/tmp/schema.xml'],
}
