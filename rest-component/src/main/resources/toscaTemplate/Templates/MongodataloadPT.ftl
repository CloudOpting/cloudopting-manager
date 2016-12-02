package{"wget":
}->
exec {<#if mongodl_datafile?has_content>'${mongodl_datafile}':</#if>
command => "wget --verbose <#if is_internal == "true"><#if co_jack_user?has_content>--user=${co_jack_user}</#if> <#if co_jack_password?has_content>--password=${co_jack_password}</#if></#if> --output-document=\"/tmp/<#if mongodl_datafile?has_content>${mongodl_datafile}</#if>\" \"<#if mongodl_datafile?has_content>${mongodl_datafile}</#if>\"",
path => '/bin:/usr/bin:/usr/sbin',
<#if require?has_content>require => ${require},</#if>
<#if before?has_content>before => ${before},</#if>
}->
<#if mongoshell == "true">
exec {'mongoshell_<#if mongodl_datafile?has_content>${mongodl_datafile}</#if>':
command = > "mongo -u <#if mongodl_user?has_content>${mongodl_user}</#if> -p <#if mongodl_password?has_content>${mongodl_password}</#if> <#if mongodl_dbname?has_content>${mongodl_dbname}</#if> /tmp/<#if mongodl_datafile?has_content>${mongodl_datafile}</#if>",
path => '/bin:/usr/bin:/usr/sbin',
}
<#else>
exec {'mongoimport_<#if mongodl_datafile?has_content>${mongodl_datafile}</#if>':
command = > "mongoimport -u <#if mongodl_user?has_content>${mongodl_user}</#if> -p <#if mongodl_password?has_content>${mongodl_password}</#if> --db <#if mongodl_dbname?has_content>${mongodl_dbname}</#if> --file /tmp/<#if mongodl_datafile?has_content>${mongodl_datafile}</#if>",
path => '/bin:/usr/bin:/usr/sbin',
}
</#if>
