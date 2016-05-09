$packages = [ 'java-1.7.0-openjdk', 'unzip', 'wget', 'lsof' ]
$solr_version = "<#if solr_version?has_content>${solr_version}</#if>"


package { $packages: 
	before => User['solr'],
}

## create a solr user
user {'solr':
    ensure     => present,
    home       => '/opt/solr',
    managehome => false,
    shell      => '/bin/bash',
}

  # directory to store downloaded solr versions
file {'/opt/solr':
    ensure  => directory,
    owner   => 'solr',
    group   => 'solr',
    require => User['solr'],
}

exec {"http://archive.apache.org/dist/lucene/solr/$solr_version/apache-solr-$solr_version.zip":
command => "wget --verbose --output-document=\"/tmp/apache-solr-$solr_version.zip\" \"http://archive.apache.org/dist/lucene/solr/$solr_version/apache-solr-$solr_version.zip\"",
path => '/bin:/usr/bin:/usr/sbin',
require => Package['wget'],
}->
archive::extract{'apache-solr-$solr_version':
target => '/opt/solr',
src_target => '/tmp',
extension => 'zip',
user => 'solr',
root_dir => '',
require => File['/opt/solr'],
}
->
file{'/opt/solr/solr.in.sh':
content => "SOLR_JAVA_MEM=\"-Xms512m -Xmx512m\"

# Enable verbose GC logging
GC_LOG_OPTS=\"-verbose:gc -XX:+PrintHeapAtGC -XX:+PrintGCDetails \
-XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+PrintTenuringDistribution -XX:+PrintGCApplicationStoppedTime\"

# These GC settings have shown to work well for a number of common Solr workloads
GC_TUNE=\"-XX:NewRatio=3 \
-XX:SurvivorRatio=4 \
-XX:TargetSurvivorRatio=90 \
-XX:MaxTenuringThreshold=8 \
-XX:+UseConcMarkSweepGC \
-XX:+UseParNewGC \
-XX:ConcGCThreads=4 -XX:ParallelGCThreads=4 \
-XX:+CMSScavengeBeforeRemark \
-XX:PretenureSizeThreshold=64m \
-XX:+UseCMSInitiatingOccupancyOnly \
-XX:CMSInitiatingOccupancyFraction=50 \
-XX:CMSMaxAbortablePrecleanTime=6000 \
-XX:+CMSParallelRemarkEnabled \
-XX:+ParallelRefProcEnabled\"
ENABLE_REMOTE_JMX_OPTS=\"true\"
SOLR_HOST=\"0.0.0.0\"
SOLR_SERVER_DIR=\"/opt/solr/apache-solr-$solr_version\"
",
mode => 700,
owner => 'solr',
group => 'solr',
}





#################
#class{'solr':
#<#if url?has_content>url => '${url}',</#if>
#<#if version?has_content>version => '${version}',</#if>
#<#if solr_host?has_content>dbhost => '${solr_host}',</#if>
#<#if solr_port?has_content>dbname => ${solr_port},</#if>
#}


##### in the file we need to read the file from GIT
#file {'/tmp/schema.xml':
#  ensure => file,
#  content => inline_template('....'),
#}

#solr::core{'ckan':
#  schema_src_file => '/tmp/schema.xml',
#  require         => File ['/tmp/schema.xml'],
#}
